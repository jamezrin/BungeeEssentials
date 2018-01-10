package me.jaime29010.essentials.utils;

import net.md_5.bungee.api.ChatColor;

public class PluginUtils {
    public static char SEPARATOR = ' ';
    public static String joinArray(String[] array, int start, int end) {
        final StringBuilder builder = new StringBuilder();
        int index = start;
        while (index < end) {
            builder.append(array [index++]);
            builder.append(SEPARATOR);
        }
        builder.append(array [index]);
        return builder.toString();
    }

    public static String joinArray(String[] array, int start) {
        return joinArray(array, start, array.length - 1);
    }

    public static String joinArray(String[] array) {
        return joinArray(array, 0, array.length - 1);
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String decolor(String message) {
        return ChatColor.stripColor(message);
    }
}
