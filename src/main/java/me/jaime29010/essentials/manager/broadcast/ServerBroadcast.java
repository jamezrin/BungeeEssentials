package me.jaime29010.essentials.manager.broadcast;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

public class ServerBroadcast {
    private final String target;
    private final TextComponent[] components;
    public ServerBroadcast(String target, TextComponent... components) {
        this.target = target;
        this.components = components;
    }

    public String getTargetServer() {
        return target;
    }

    public TextComponent[] getComponents() {
        return components;
    }

    void send() {
        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (player.getServer().getInfo().getName().equals(target)) {
                for (TextComponent component : components) {
                    if (component != null) {
                        player.sendMessage(component);
                    }
                }
            }
        });
    }
}
