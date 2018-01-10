package me.jaime29010.essentials.commands;

import me.jaime29010.essentials.Main;
import me.jaime29010.essentials.manager.RedisHook;
import me.jaime29010.essentials.utils.PluginUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import redis.clients.jedis.Jedis;

public class MaintenanceCommand extends Command {
    private final Main main;
    public MaintenanceCommand(Main main) {
        super("maintenance");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (player.hasPermission(main.getConfig().getString("permissions.maintenance"))) {
                if (args.length == 1) {
                    switch (args[0].toLowerCase()) {
                        case "off": {
                            try (Jedis jedis = RedisHook.getJedisPool().getResource()) {
                                String status = jedis.get("rce:ms");
                                if (status != null && status.equals("on")) {
                                    jedis.set("rce:ms", "off");
                                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                            main.getConfig().getString("messages.maintenance-toogled")
                                            .replace("%status%", ChatColor.RED + "off")
                                    )));
                                } else {
                                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                            main.getConfig().getString("messages.error-maintenance-toggle")
                                            .replace("%status%", ChatColor.RED + "off")
                                    )));
                                }

                            }
                            break;
                        }

                        case "on": {
                            try (Jedis jedis = RedisHook.getJedisPool().getResource()) {
                                String status = jedis.get("rce:ms");
                                if (status == null || status.equals("off")) {
                                    jedis.set("rce:ms", "on");
                                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                            main.getConfig().getString("messages.maintenance-toogled")
                                            .replace("%status%", ChatColor.GREEN + "on")
                                    )));
                                } else {
                                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                            main.getConfig().getString("messages.error-maintenance-toggle")
                                            .replace("%status%", ChatColor.GREEN + "on")
                                    )));
                                }
                            }
                            break;
                        }

                        default: {
                            player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                    main.getConfig().getString("messages.maintenance-usage")
                                    .replace("%command%", this.getName())
                            )));
                        }
                    }
                } else {
                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                            main.getConfig().getString("messages.maintenance-usage")
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
