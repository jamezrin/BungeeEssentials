package me.jaime29010.essentials.manager;

import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import me.jaime29010.essentials.Main;
import net.md_5.bungee.config.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisHook {
    private static RedisBungeeAPI redisBungee;
    private static JedisPool pool;
    public static boolean hook(Main main) {
        redisBungee = RedisBungee.getApi();

        Configuration section = main.getConfig().getSection("redis");
        String host = section.getString("host");
        int port = section.getInt("port");
        int timeout = section.getInt("timeout");
        String password = section.getString("password");
        if (!password.matches(".*\\w.*")) {
            password = null;
        }

        try {
            pool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password);
            try (Jedis jedis = pool.getResource()) {
                //Clear last messaged players
                jedis.del("rce:lmp");
            }
            main.getLogger().info("Successfully connected to redis");
        } catch (Exception e) {
            main.getLogger().severe("Unable to connect to redis");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static JedisPool getJedisPool() {
        return pool;
    }

    public static RedisBungeeAPI getRedisBungee() {
        return redisBungee;
    }
}
