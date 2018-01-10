package me.jaime29010.essentials.manager.broadcast;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;

public class DefinedBroadcast {
    private final List<UUID> receivers;
    private final TextComponent[] components;
    public DefinedBroadcast(List<UUID> receivers, TextComponent... components) {
        this.receivers = receivers;
        this.components = components;
    }

    public List<UUID> getReceivers() {
        return receivers;
    }

    public TextComponent[] getComponents() {
        return components;
    }

    void send() {
        receivers.forEach(receiver -> {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(receiver);
            if (player != null) {
                for (TextComponent component : components) {
                    if (component != null) {
                        player.sendMessage(component);
                    }
                }
            }
        });
    }
}