package com.thejailbreakshow.cells;

import com.thejailbreakshow.TheJailBreakShow;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CellButtonSaveManager {

    private static File file;
    private static FileConfiguration config;

    public static void init() {
        file = new File(TheJailBreakShow.getInstance().getDataFolder(), "cell_button.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        load();
    }

    public static void save() {
        if (CellButtonManager.getCellButtonLocation() == null) return;

        Location loc = CellButtonManager.getCellButtonLocation();
        config.set("world", loc.getWorld().getName());
        config.set("x", loc.getBlockX());
        config.set("y", loc.getBlockY());
        config.set("z", loc.getBlockZ());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        if (config.getString("world") == null) return;

        String world = config.getString("world");
        int x = config.getInt("x");
        int y = config.getInt("y");
        int z = config.getInt("z");

        Location loc = new Location(Bukkit.getWorld(world), x, y, z);
        CellButtonManager.setCellButtonLocation(loc);
    }
}
