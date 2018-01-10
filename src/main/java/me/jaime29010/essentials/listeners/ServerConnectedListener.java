package me.jaime29010.essentials.listeners;

import me.jaime29010.essentials.Main;
import me.jaime29010.essentials.utils.PluginUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectedListener implements Listener {
    private final Main main;
    public ServerConnectedListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onConnect(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ServerInfo server = event.getServer().getInfo();
        Configuration section = main.getConfig().getSection("server-motds");
        if (player.hasPermission(main.getConfig().getString("permissions.motd-receive"))) {
            if (section.getKeys().contains(server.getName())) {
                section.getStringList(server.getName()).forEach(message -> {
                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(message)));
                });
            }
        }
    }
}