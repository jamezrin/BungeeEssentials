package me.jaime29010.essentials.utils;

import me.jaime29010.essentials.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigurationManager {
    public static Configuration loadConfig(String name, Main main) {
        File file = new File(main.getDataFolder(), name);
        Configuration config = null;
        if (!main.getDataFolder().exists())
            main.getDataFolder().mkdir();
        if (!file.exists()) {
            try {
                Files.copy(main.getResourceAsStream(file.getName()), file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public static boolean saveConfig(Configuration config, String name, Main main) {
        File file = new File(main.getDataFolder(), name);
        if (!main.getDataFolder().exists())
            main.getDataFolder().mkdir();
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
