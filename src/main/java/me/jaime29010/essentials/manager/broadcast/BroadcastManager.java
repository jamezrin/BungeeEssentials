package me.jaime29010.essentials.manager.broadcast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import me.jaime29010.essentials.Main;
import me.jaime29010.essentials.manager.RedisHook;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.chat.TextComponentSerializer;
import net.md_5.bungee.chat.TranslatableComponentSerializer;
import net.md_5.bungee.event.EventHandler;

public class BroadcastManager implements Listener {
    private static Gson gson;
    public static void init(Main main) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(BaseComponent.class, new ComponentSerializer());
        builder.registerTypeAdapter(TextComponent.class, new TextComponentSerializer());
        builder.registerTypeAdapter(TranslatableComponent.class, new TranslatableComponentSerializer());
        gson = builder.create();

        RedisHook.getRedisBungee().registerPubSubChannels("DefinedBroadcast", "PermissiveBroadcast", "ServerBroadcast");
        main.getProxy().getPluginManager().registerListener(main, new BroadcastManager());
    }

    @EventHandler
    public void onMessage(PubSubMessageEvent event) {
        switch (event.getChannel()) {
            case "DefinedBroadcast": {
                String json = event.getMessage();
                DefinedBroadcast broadcast = gson.fromJson(json, DefinedBroadcast.class);
                broadcast.send();
                break;
            }
            case "PermissiveBroadcast": {
                String json = event.getMessage();
                PermissiveBroadcast broadcast = gson.fromJson(json, PermissiveBroadcast.class);
                broadcast.send();
                break;
            }
            case "ServerBroadcast": {
                String json = event.getMessage();
                ServerBroadcast broadcast = gson.fromJson(json, ServerBroadcast.class);
                broadcast.send();
                break;
            }
        }
    }

    public static String sendBroadcast(DefinedBroadcast broadcast) {
        String json = gson.toJson(broadcast);
        RedisHook.getRedisBungee().sendChannelMessage("DefinedBroadcast", json);
        return json;
    }

    public static String sendBroadcast(PermissiveBroadcast broadcast) {
        String json = gson.toJson(broadcast);
        RedisHook.getRedisBungee().sendChannelMessage("PermissiveBroadcast", json);
        return json;
    }

    public static String sendBroadcast(ServerBroadcast broadcast) {
        String json = gson.toJson(broadcast);
        RedisHook.getRedisBungee().sendChannelMessage("ServerBroadcast", json);
        return json;
    }

    public static Gson getGson() {
        return gson;
    }
}
