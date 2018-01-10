package me.jaime29010.essentials.listeners;

import me.jaime29010.essentials.Main;
import me.jaime29010.essentials.manager.broadcast.BroadcastManager;
import me.jaime29010.essentials.manager.broadcast.PermissiveBroadcast;
import me.jaime29010.essentials.utils.PluginUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {
    private final Main main;
    public ChatListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        String message = event.getMessage();
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String character = main.getConfig().getString("messages.adminchat-char");
        if (message.startsWith(character)) {
            if (player.hasPermission(main.getConfig().getString("permissions.adminchat-send"))) {
                event.setCancelled(true);
                message = message.substring(character.length());
                TextComponent component = new TextComponent(TextComponent.fromLegacyText(PluginUtils.color(
                        main.getConfig().getString("messages.adminchat-format")
                        .replace("%sender%", player.getName())
                        .replace("%message%", message)
                )));
                PermissiveBroadcast object = new PermissiveBroadcast(main.getConfig().getString("permissions.adminchat-view"), component);
                BroadcastManager.sendBroadcast(object);
            }
        }
    }
}
