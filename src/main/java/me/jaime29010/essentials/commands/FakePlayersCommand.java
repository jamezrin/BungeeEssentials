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

public class FakePlayersCommand extends Command {
    private final Main main;
    public FakePlayersCommand(Main main) {
        super("fakeplayers");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (player.hasPermission(main.getConfig().getString("permissions.broadcast-send"))) {
                if (args.length != 0) {
                    switch (args[0].toLowerCase()) {
                        case "online": {
                            String string = args[1];
                            try {
                                int number = Integer.parseInt(string);
                                try (Jedis jedis = RedisHook.getJedisPool().getResource()) {
                                    jedis.set("rce:fpo", String.valueOf(number));
                                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                            main.getConfig().getString("messages.fakeplayers-message")
                                    )));
                                }
                            } catch (NumberFormatException e) {
                                player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                        main.getConfig().getString("messages.error-fakeplayers-input")
                                )));
                                player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                        main.getConfig().getString("messages.fakeplayers-usage")
                                        .replace("%command%", this.getName())
                                )));
                            }
                            break;
                        }

                        case "max": {
                            if (args.length == 2) {
                                String string = args[1];
                                try {
                                    int number = Integer.parseInt(string);
                                    try (Jedis jedis = RedisHook.getJedisPool().getResource()) {
                                        jedis.set("rce:fpm", String.valueOf(number));
                                        player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                                main.getConfig().getString("messages.fakeplayers-message")
                                        )));
                                    }
                                } catch (NumberFormatException e) {
                                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                            main.getConfig().getString("messages.error-fakeplayers-input")
                                    )));
                                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                            main.getConfig().getString("messages.fakeplayers-usage")
                                            .replace("%command%", this.getName())
                                    )));
                                }
                            } else {
                                player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                        main.getConfig().getString("messages.fakeplayers-usage")
                                        .replace("%command%", this.getName())
                                )));
                            }
                            break;
                        }

                        case "toggle": {
                            try (Jedis jedis = RedisHook.getJedisPool().getResource()) {
                                String status = jedis.get("rce:fps");
                                if (status != null && status.equals("on")) {
                                    jedis.set("rce:fps", "off");
                                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                            main.getConfig().getString("messages.fakeplayers-toggled")
                                            .replace("%status%", ChatColor.RED + "off")
                                    )));
                                } else {
                                    jedis.set("rce:fps", "on");
                                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                            main.getConfig().getString("messages.fakeplayers-toggled")
                                            .replace("%status%", ChatColor.GREEN + "on")
                                    )));
                                }
                            }
                            break;
                        }
                        default: {
                            player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                    main.getConfig().getString("messages.fakeplayers-usage")
                                    .replace("%command%", this.getName())
                            )));
                        }
                    }
                } else {
                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                            main.getConfig().getString("messages.fakeplayers-usage")
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
