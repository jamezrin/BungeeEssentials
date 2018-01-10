package me.jaime29010.essentials.manager;

import com.google.common.collect.Iterables;
import me.jaime29010.essentials.Main;
import net.md_5.bungee.api.Favicon;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FaviconManager {
    private static Favicon favicon;
    public static void init(Main main) {
        List<Favicon> list = new ArrayList<>();
        main.getConfig().getStringList("icons").forEach(name -> {
            File file = new File(main.getDataFolder(), name);
            try {
                list.add(Favicon.create(ImageIO.read(file)));
            } catch (IOException e) {
                main.getLogger().severe("An error occurred when loading the icon: " + name);
                e.printStackTrace();
            }
        });

        if (list.isEmpty()) return;

        //Cycling through the favicon list
        main.getLogger().info("Cycling through the favicons");
        final Iterator<Favicon> iterator = Iterables.cycle(list).iterator();
        main.getProxy().getScheduler().schedule(main, () -> {
            if (iterator.hasNext()) {
                favicon = iterator.next();
            }
        }, 0, main.getConfig().getInt("favicon-rotate"), TimeUnit.MINUTES);
    }

    public static Favicon getCurrentFavicon() {
        return favicon;
    }
}