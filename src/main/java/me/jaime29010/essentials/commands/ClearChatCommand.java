package me.jaime29010.essentials.commands;

import me.jaime29010.essentials.Main;
import me.jaime29010.essentials.manager.broadcast.BroadcastManager;
import me.jaime29010.essentials.manager.broadcast.PermissiveBroadcast;
import me.jaime29010.essentials.manager.broadcast.ServerBroadcast;
import me.jaime29010.essentials.utils.PluginUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ClearChatCommand extends Command {
    private final Main main;
    private final TextComponent component = new TextComponent(new String(new char[100]).replace("\0", "\n"));
    public ClearChatCommand(Main main) {
        super("clearchat");
        this.main = main;
        component.addExtra(new TextComponent(TextComponent.fromLegacyText(PluginUtils.color(
                main.getConfig().getString("messages.clearchat-message")
        ))));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (player.hasPermission(main.getConfig().getString("permissions.clearchat"))) {
                if (args.length != 0) {
                    String name = args[0];
                    if (main.getProxy().getServers().containsKey(name)) {
                        ServerBroadcast object = new ServerBroadcast(name, component);
                        BroadcastManager.sendBroadcast(object);
                    } else {
                        player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                main.getConfig().getString("messages.error-clearchat")
                        )));

                        player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                main.getConfig().getString("messages.clearchat-usage")
                                .replace("%command%", this.getName())
                        )));
                    }
                } else {
                    PermissiveBroadcast object = new PermissiveBroadcast(null, component);
                    BroadcastManager.sendBroadcast(object);
                }
            } else {
                player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                        main.getConfig().getString("messages.error-noperms")
                )));
            }
        } else {
            sender.sendMessage(new ComponentBuilder("This command can only be executed by a player").color(ChatColor.RED).create());
        }
    }
}
