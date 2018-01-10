package me.jaime29010.essentials.commands;

import me.jaime29010.essentials.Main;
import me.jaime29010.essentials.manager.RedisHook;
import me.jaime29010.essentials.manager.broadcast.BroadcastManager;
import me.jaime29010.essentials.manager.broadcast.DefinedBroadcast;
import me.jaime29010.essentials.utils.PluginUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;

public class MessageCommand extends Command {
    private final Main main;

    public MessageCommand(Main main) {
        super("msg", null, "tell", "pm");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (player.hasPermission(main.getConfig().getString("permissions.message-send"))) {
                if (args.length >= 2) {
                    String name = args[0];
                    UUID uuid = RedisHook.getRedisBungee().getUuidFromName(name);
                    if (uuid != null && RedisHook.getRedisBungee().isPlayerOnline(uuid)) {
                        String message = PluginUtils.color(PluginUtils.joinArray(args, 1));
                        if (!player.hasPermission(main.getConfig().getString("permissions.message-send-color"))) {
                            message = PluginUtils.decolor(message);
                        }

                        player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                main.getConfig().getString("messages.message-send-format")
                                .replace("%player%", name)
                                .replace("%message%", message)
                        )));

                        TextComponent component = new TextComponent(TextComponent.fromLegacyText(PluginUtils.color(
                                main.getConfig().getString("messages.message-receive-format")
                                .replace("%sender%", player.getName())
                                .replace("%message%", message)
                        )));

                        DefinedBroadcast object = new DefinedBroadcast(Collections.singletonList(uuid), component);
                        BroadcastManager.sendBroadcast(object);

                        try (Jedis jedis = RedisHook.getJedisPool().getResource()) {
                            jedis.hset("rce:lmp", player.getUniqueId().toString(), uuid.toString());
                            jedis.hset("rce:lmp", uuid.toString(), player.getUniqueId().toString());
                        }
                    } else {
                        player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                                main.getConfig().getString("messages.error-player-offline")
                        )));
                    }
                } else {
                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                            main.getConfig().getString("messages.message-usage")
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
