package com.thejailbreakshow.regions;

import org.bukkit.Location;
import org.bukkit.World;

public class Region {

    private final String name;
    private final RegionType type;
    private final World world;
    private final int minX, minY, minZ, maxX, maxY, maxZ;

    public Region(String name, RegionType type, Location loc1, Location loc2) {
        this.name = name;
        this.type = type;
        this.world = loc1.getWorld();

        this.minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        this.minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        this.minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        this.maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        this.maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        this.maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
    }

    public boolean contains(Location location) {
        if (!location.getWorld().equals(this.world)) {
            return false;
        }

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return x >= minX && x <= maxX
                && y >= minY && y <= maxY
                && z >= minZ && z <= maxZ;
    }

    public String getName() {
        return name;
    }

    public RegionType getType() {
        return type;
    }

    public World getWorld() {
        return world;
    }

    public String getWorldName() {
        return world.getName();
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }
}
