package me.jaime29010.essentials.commands;

import me.jaime29010.essentials.Main;
import me.jaime29010.essentials.manager.RedisHook;
import me.jaime29010.essentials.manager.broadcast.BroadcastManager;
import me.jaime29010.essentials.manager.broadcast.PermissiveBroadcast;
import me.jaime29010.essentials.utils.PluginUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.UUID;

public class ReportCommand extends Command {
    private final Main main;
    public ReportCommand(Main main) {
        super("report");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (player.hasPermission(main.getConfig().getString("permissions.report-send"))) {
                if (args.length >= 2) {
                    String name = args[0];
                    UUID uuid = RedisHook.getRedisBungee().getUuidFromName(name);
                    if (uuid != null && RedisHook.getRedisBungee().isPlayerOnline(uuid)) {
                        String reason = PluginUtils.joinArray(args, 1);

                        player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(StringEscapeUtils.unescapeJava(
                                main.getConfig().getString("messages.report-message-reporter")
                                .replace("%reported%", name)
                                .replace("%report%", reason)
                        ))));

                        String string = PluginUtils.color(
                                main.getConfig().getString("messages.report-message-staff")
                                .replace("%reported%", name)
                                .replace("%reporter%", player.getName())
                                .replace("%report%", reason)
                        );

                        String marker = "%teleport%";
                        int index = string.lastIndexOf(marker);
                        final TextComponent component;
                        if (index != -1) {
                            String left = string.substring(0, index);
                            component = new TextComponent(TextComponent.fromLegacyText(left));

                            TextComponent extra = new TextComponent(PluginUtils.color(
                                    main.getConfig().getString("messages.report-teleport-button")
                            ));

                            extra.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                    "/server %name%"
                                    .replace("%name%", RedisHook.getRedisBungee().getServerFor(uuid).getName())
                            ));

                            extra.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(PluginUtils.color(
                                    main.getConfig().getString("messages.report-teleport-button-text")
                            ))));

                            component.addExtra(extra);

                            String right = string.substring(index + marker.length(), string.length());
                            if (right.matches(".*\\w.*")) {
                                component.addExtra(new TextComponent(TextComponent.fromLegacyText(right)));
                            }

                            PermissiveBroadcast object = new PermissiveBroadcast(main.getConfig().getString("permissions.report-staff"), component);
                            BroadcastManager.sendBroadcast(object);
                        } else {
                            component = new TextComponent(TextComponent.fromLegacyText(string));

                            PermissiveBroadcast object = new PermissiveBroadcast(main.getConfig().getString("permissions.report-staff"), component);
                            BroadcastManager.sendBroadcast(object);
                        }
                    } else {
                        player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                main.getConfig().getString("messages.error-player-offline")
                        )));
                    }
                } else {
                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                            main.getConfig().getString("messages.report-usage")
                            .replace("%command%", this.getName())
                    )));
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
