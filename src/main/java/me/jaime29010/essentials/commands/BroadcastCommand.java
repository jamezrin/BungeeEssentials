package me.jaime29010.essentials.commands;

import com.google.gson.JsonSyntaxException;
import me.jaime29010.essentials.Main;
import me.jaime29010.essentials.manager.broadcast.BroadcastManager;
import me.jaime29010.essentials.manager.broadcast.PermissiveBroadcast;
import me.jaime29010.essentials.utils.PluginUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BroadcastCommand extends Command {
    private final Main main;

    public BroadcastCommand(Main main) {
        super("gb");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (player.hasPermission(main.getConfig().getString("permissions.broadcast-send"))) {
                if (args.length != 0) {
                    String text = PluginUtils.joinArray(args);

                    TextComponent component;
                    try {
                        component = BroadcastManager.getGson().fromJson(text, TextComponent.class);
                    } catch (JsonSyntaxException e) {
                        component = new TextComponent(TextComponent.fromLegacyText(PluginUtils.color(text)));
                    } catch (Exception e) {
                        player.sendMessage(new ComponentBuilder("An unexpected error occurred while parsing the input, check your input").color(ChatColor.RED).create());
                        return;
                    }

                    PermissiveBroadcast object = new PermissiveBroadcast(null, component);
                    BroadcastManager.sendBroadcast(object);
                } else {
                    player.sendMessage(TextComponent.fromLegacyText(PluginUtils.color(
                            main.getConfig().getString("messages.broadcast-usage")
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
