package me.jaime29010.essentials;

import me.jaime29010.essentials.commands.*;
import me.jaime29010.essentials.listeners.ChatListener;
import me.jaime29010.essentials.listeners.PostLoginListener;
import me.jaime29010.essentials.listeners.ProxyPingListener;
import me.jaime29010.essentials.listeners.ServerConnectedListener;
import me.jaime29010.essentials.manager.FaviconManager;
import me.jaime29010.essentials.manager.RedisHook;
import me.jaime29010.essentials.manager.broadcast.BroadcastManager;
import me.jaime29010.essentials.utils.ConfigurationManager;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import redis.clients.jedis.JedisPool;

public class Main extends Plugin {
    private Configuration config;
    @Override
    public void onEnable() {
        config = ConfigurationManager.loadConfig("config.yml", this);
        if (RedisHook.hook(this)) {
            FaviconManager.init(this);
            BroadcastManager.init(this);

            getProxy().getPluginManager().registerCommand(this, new BroadcastCommand(this));
            getProxy().getPluginManager().registerCommand(this, new ClearChatCommand(this));
            getProxy().getPluginManager().registerCommand(this, new FakePlayersCommand(this));
            getProxy().getPluginManager().registerCommand(this, new MaintenanceCommand(this));
            getProxy().getPluginManager().registerCommand(this, new MessageCommand(this));
            getProxy().getPluginManager().registerCommand(this, new ReplyCommand(this));
            getProxy().getPluginManager().registerCommand(this, new ReportCommand(this));

            getProxy().getPluginManager().registerListener(this, new ServerConnectedListener(this));
            getProxy().getPluginManager().registerListener(this, new ProxyPingListener(this));
            getProxy().getPluginManager().registerListener(this, new PostLoginListener(this));
            getProxy().getPluginManager().registerListener(this, new ChatListener(this));
        }
    }

    @Override
    public void onDisable() {
        JedisPool pool = RedisHook.getJedisPool();
        if (pool != null) {
            pool.destroy();
        }
    }

    public Configuration getConfig() {
        return config;
    }
}