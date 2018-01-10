package me.jaime29010.essentials.listeners;

import me.jaime29010.essentials.Main;
import me.jaime29010.essentials.manager.RedisHook;
import me.jaime29010.essentials.utils.PluginUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import redis.clients.jedis.Jedis;

public class PostLoginListener implements Listener {
    private final Main main;
    public PostLoginListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        try (Jedis jedis = RedisHook.getJedisPool().getResource()) {
            PendingConnection connection = player.getPendingConnection();
            String status = jedis.get("rce:ms");
            if (status != null && status.equals("on")) {
                if (player.hasPermission(main.getConfig().getString("permissions.maintenance-whitelist"))) return;
                connection.disconnect(TextComponent.fromLegacyText(PluginUtils.color(
                        main.getConfig().getString("messages.maintenance-message")
                )));
            } else if (RedisHook.getRedisBungee().getPlayerCount() >= connection.getListener().getMaxPlayers()) {
                if (player.hasPermission(main.getConfig().getString("permissions.vipslot"))) return;
                connection.disconnect(TextComponent.fromLegacyText(PluginUtils.color(
                        main.getConfig().getString("messages.server-full-message")
                )));
            }
        }
    }
}