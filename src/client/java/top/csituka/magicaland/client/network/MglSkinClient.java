package top.csituka.magicaland.client.network;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Util;
import top.csituka.magicaland.client.config.Config;
import top.csituka.magicaland.client.config.ModelConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MglSkinClient {
    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = LoggerFactory.getLogger("magicaland/mglskin");
    private static final HttpClient HTTP = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    private static final int MAX_RESPONSE = 2_000_000;

    public record RemoteSkin(long id, String name, String username, String data) {}

    private MglSkinClient() {}

    public static boolean isLoggedIn() {
        String token = Config.getInstance().mglSkinToken;
        return token != null && !token.isBlank();
    }

    public static String username() { return Config.getInstance().mglSkinUsername; }

    public static ModelConfig parseModel(RemoteSkin skin) {
        if (skin == null || skin.data() == null || skin.data().length() > 1_000_000) return null;
        try {
            ModelConfig model = GSON.fromJson(skin.data(), ModelConfig.class);
            if (model == null) return null;
            model.name = skin.name();
            return ModelConfig.sanitize(model);
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    public static void fetchSkins(Consumer<List<RemoteSkin>> success, Consumer<String> failure) {
        request("GET", "/api/skins", null, response -> {
            try {
                JsonObject root = JsonParser.parseString(response).getAsJsonObject();
                JsonArray items = root.getAsJsonArray("items");
                List<RemoteSkin> result = new ArrayList<>();
                if (items != null) for (JsonElement element : items) {
                    JsonObject item = element.getAsJsonObject();
                    result.add(new RemoteSkin(item.get("id").getAsLong(), item.get("name").getAsString(),
                            item.has("username") ? item.get("username").getAsString() : "", item.get("data").getAsString()));
                }
                onGameThread(() -> success.accept(result));
            } catch (RuntimeException error) {
                onGameThread(() -> failure.accept("服务返回了无效数据"));
            }
        }, failure);
    }

    public static void upload(ModelConfig model, Consumer<String> success, Consumer<String> failure) {
        if (!isLoggedIn()) { failure.accept("请先登录共享服务"); return; }
        JsonObject body = new JsonObject();
        body.addProperty("name", model.name);
        body.addProperty("data", GSON.toJson(model));
        request("POST", "/api/skins", GSON.toJson(body), response -> {
            try {
                String name = JsonParser.parseString(response).getAsJsonObject().get("name").getAsString();
                onGameThread(() -> success.accept(name));
            } catch (RuntimeException error) {
                onGameThread(() -> failure.accept("上传响应无效"));
            }
        }, failure);
    }

    public static void beginLogin(Consumer<String> success, Consumer<String> failure) {
        String state = UUID.randomUUID().toString();
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
            server.createContext("/callback", exchange -> handleCallback(exchange, server, state, success, failure));
            server.setExecutor(null);
            server.start();
            String callback = "http://127.0.0.1:" + server.getAddress().getPort() + "/callback";
            String url = baseUrl() + "/minecraft?callback=" + encode(callback) + "&state=" + encode(state);
            Util.getOperatingSystem().open(URI.create(url));
        } catch (Exception error) {
            failure.accept("无法打开登录页面");
        }
    }

    private static void handleCallback(HttpExchange exchange, HttpServer server, String expectedState,
            Consumer<String> success, Consumer<String> failure) {
        try {
            String query = exchange.getRequestURI().getRawQuery();
            String code = queryValue(query, "code");
            String state = queryValue(query, "state");
            if (code == null || !expectedState.equals(state)) {
                String message = "登录回调无效，请返回游戏重试。";
                onGameThread(() -> failure.accept(message));
                sendCallbackResponse(exchange, 400, message);
            } else {
                sendCallbackResponse(exchange, 200, "登录完成，可以返回游戏。 ");
                exchangeToken(code, success, failure);
            }
        } catch (IOException error) {
            onGameThread(() -> failure.accept("登录回调处理失败"));
        } finally {
            exchange.close();
            server.stop(0);
        }
    }

    private static void sendCallbackResponse(HttpExchange exchange, int status, String message) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        byte[] bytes = ("<html><meta charset='utf-8'><body>" + message + "</body></html>")
                .getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
    }

    private static void exchangeToken(String code, Consumer<String> success, Consumer<String> failure) {
        JsonObject body = new JsonObject();
        body.addProperty("code", code);
        request("POST", "/api/auth/minecraft/token", GSON.toJson(body), response -> {
            try {
                JsonObject root = JsonParser.parseString(response).getAsJsonObject();
                Config config = Config.getInstance();
                config.mglSkinToken = root.get("token").getAsString();
                config.mglSkinUsername = root.getAsJsonObject("user").get("username").getAsString();
                Config.save();
                onGameThread(() -> success.accept(config.mglSkinUsername));
            } catch (RuntimeException error) {
                onGameThread(() -> failure.accept("登录令牌无效"));
            }
        }, failure);
    }

    private static void request(String method, String path, String body, Consumer<String> success,
            Consumer<String> failure) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(baseUrl() + path))
                    .header("Accept", "application/json")
                    .header("User-Agent", "Magical-Land/" + modVersion());
            String token = Config.getInstance().mglSkinToken;
            if (token != null && !token.isBlank()) builder.header("Authorization", "Bearer " + token);
            if (body == null) builder.method(method, HttpRequest.BodyPublishers.noBody());
            else builder.method(method, HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                    .header("Content-Type", "application/json");
            HTTP.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                    .thenAccept(response -> {
                        if (response.body().length() > MAX_RESPONSE || response.statusCode() / 100 != 2) {
                            onGameThread(() -> failure.accept("服务请求失败（" + response.statusCode() + "）"));
                        } else success.accept(response.body());
            }).exceptionally(error -> {
                Throwable cause = error.getCause() == null ? error : error.getCause();
                LOGGER.warn("MGL Skin request failed: {}", cause.toString());
                onGameThread(() -> failure.accept("无法连接共享服务：" + cause.getClass().getSimpleName()));
                return null;
            });
        } catch (RuntimeException error) {
            onGameThread(() -> failure.accept("服务地址无效"));
        }
    }

    private static void onGameThread(Runnable action) { MinecraftClient.getInstance().execute(action); }

    private static String modVersion() {
        return "0.3.5";
    }

    private static String baseUrl() {
        String configured = Config.getInstance().mglSkinUrl;
        String value = configured == null || configured.isBlank()
                ? "http://127.0.0.1:4300" : configured.trim();
        if (value.startsWith("http://localhost")) value = "http://127.0.0.1" + value.substring("http://localhost".length());
        if (value.startsWith("https://localhost")) value = "https://127.0.0.1" + value.substring("https://localhost".length());
        while (value.endsWith("/")) value = value.substring(0, value.length() - 1);
        return value;
    }

    private static String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8); }

    private static String queryValue(String query, String key) {
        if (query == null) return null;
        for (String part : query.split("&")) {
            String[] pair = part.split("=", 2);
            if (pair.length == 2 && pair[0].equals(key))
                return java.net.URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
        }
        return null;
    }
}
