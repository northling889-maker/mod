package com.betier;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TierApiClient {
    public static final Logger LOGGER = LoggerFactory.getLogger("BETier API");
    private static final Gson GSON = new Gson();
    private static final ExecutorService THREAD_POOL = Executors.newSingleThreadExecutor();
    public static final Map<String, CachedPlayer> PLAYER_CACHE = new ConcurrentHashMap<>();
    public static List<PlayerTierData> ALL_PLAYERS_CACHE = new ArrayList<>();
    public static long allCacheTime = 0;

    public static void fetchSinglePlayer(String username, Runnable callback) {
        THREAD_POOL.submit(() -> {
            try {
                String urlStr = TierConstants.API_BASE + "/api?name=" + URLEncoder.encode(username, StandardCharsets.UTF_8);
                HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                if (conn.getResponseCode() == 200) {
                    PlayerTierData data = GSON.fromJson(new InputStreamReader(conn.getInputStream()), PlayerTierData.class);
                    PLAYER_CACHE.put(username, new CachedPlayer(data, System.currentTimeMillis()));
                    if (callback != null) callback.run();
                }
                conn.disconnect();
            } catch (Exception e) {
                LOGGER.error("请求玩家 {} 段位失败", username, e);
            }
        });
    }

    public static void fetchAllPlayers(Runnable callback) {
        THREAD_POOL.submit(() -> {
            try {
                URL url = new URL(TierConstants.API_BASE + "/api/all");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(6000);
                List<PlayerTierData> list = GSON.fromJson(new InputStreamReader(conn.getInputStream()),
                        com.google.gson.reflect.TypeToken.getParameterized(List.class, PlayerTierData.class).getType());
                ALL_PLAYERS_CACHE = list;
                allCacheTime = System.currentTimeMillis();
                if (callback != null) callback.run();
                conn.disconnect();
            } catch (Exception e) {
                LOGGER.error("拉取全榜单失败", e);
            }
        });
    }

    public static CachedPlayer getCachedPlayer(String name) {
        CachedPlayer cp = PLAYER_CACHE.get(name);
        if (cp == null || cp.isExpired()) {
            fetchSinglePlayer(name, null);
            return null;
        }
        return cp;
    }
}
