package com.thejailbreakshow.regions;

import com.thejailbreakshow.TheJailBreakShow;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class RegionSaveManager {

    private static File file;
    private static FileConfiguration config;

    public static void init() {
        file = new File(TheJailBreakShow.getInstance().getDataFolder(), "regions.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void saveAllRegions() {
        config.set("regions", null); // Clear old entries

        for (Region region : RegionManager.getAllRegions()) {
            String path = "regions." + region.getName();
            config.set(path + ".type", region.getType().toString());
            config.set(path + ".world", region.getWorldName());
            config.set(path + ".minX", region.getMinX());
            config.set(path + ".minY", region.getMinY());
            config.set(path + ".minZ", region.getMinZ());
            config.set(path + ".maxX", region.getMaxX());
            config.set(path + ".maxY", region.getMaxY());
            config.set(path + ".maxZ", region.getMaxZ());
        }

        saveFile();
    }

    public static void loadAllRegions() {
        if (config.getConfigurationSection("regions") == null) return;

        for (String name : config.getConfigurationSection("regions").getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection("regions." + name);
            if (section == null) continue;

            String typeString = section.getString("type");
            String worldName = section.getString("world");
            int minX = section.getInt("minX");
            int minY = section.getInt("minY");
            int minZ = section.getInt("minZ");
            int maxX = section.getInt("maxX");
            int maxY = section.getInt("maxY");
            int maxZ = section.getInt("maxZ");

            RegionType type = RegionType.valueOf(typeString);
            Location loc1 = new Location(Bukkit.getWorld(worldName), minX, minY, minZ);
            Location loc2 = new Location(Bukkit.getWorld(worldName), maxX, maxY, maxZ);

            Region region = new Region(name, type, loc1, loc2);
            RegionManager.addRegion(region);
        }
    }

    private static void saveFile() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
