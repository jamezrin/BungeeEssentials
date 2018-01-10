package me.jaime29010.essentials.listeners;

import me.jaime29010.essentials.Main;
import me.jaime29010.essentials.manager.FaviconManager;
import me.jaime29010.essentials.manager.RedisHook;
import me.jaime29010.essentials.utils.PluginUtils;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.apache.commons.lang.StringEscapeUtils;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;

public class ProxyPingListener implements Listener {
    private final Main main;
    public ProxyPingListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        ServerPing response = event.getResponse();
        response.setFavicon(FaviconManager.getCurrentFavicon());

        List<String> list = main.getConfig().getStringList("playerlist-text");
        PlayerInfo[] sample = new PlayerInfo[list.size()];
        int offset = 0;
        for (String line : list) {
            PlayerInfo info = new PlayerInfo(PluginUtils.color(StringEscapeUtils.unescapeJava(line)), UUID.randomUUID());
            sample [offset++] = info;
        }
        response.getPlayers().setSample(sample);

        try (Jedis jedis = RedisHook.getJedisPool().getResource()) {
            String fps = jedis.get("rce:fps");
            if (fps != null && fps.equals("on")) {
                String online = jedis.get("rce:fpo");
                if (online != null) {
                    response.getPlayers().setOnline(Integer.valueOf(online));
                }
                String max = jedis.get("rce:fpm");
                if (max != null) {
                    response.getPlayers().setMax(Integer.valueOf(max));
                }
            }

            String ms = jedis.get("rce:ms");
            if (ms != null && ms.equals("on")) {
                response.setVersion(new Protocol(PluginUtils.color(
                        main.getConfig().getString("messages.maintenance-version-message")
                        .replace("%player%", event.getConnection().getName())
                ), 99999));
            }
        }
        event.setResponse(response);
    }
}