package me.jaime29010.essentials.manager.broadcast;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

public class PermissiveBroadcast {
    private final String permission;
    private final TextComponent[] components;
    public PermissiveBroadcast(String permission, TextComponent... components) {
        this.permission = permission;
        this.components = components;
    }

    public String getPermission() {
        return permission;
    }

    public TextComponent[] getComponents() {
        return components;
    }

    void send() {
        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (permission == null || player.hasPermission(permission)) {
                for (TextComponent component : components) {
                    player.sendMessage(component);
                }
            }
        });
    }
}
