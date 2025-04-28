package com.thejailbreakshow.regions;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class RegionManager {

    private static final List<Region> regions = new ArrayList<>();

    public static void addRegion(Region region) {
        regions.add(region);
    }

    public static void removeRegion(String name) {
        regions.removeIf(r -> r.getName().equalsIgnoreCase(name));
    }

    public static Region getRegionByName(String name) {
        return regions.stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static Region getRegionAt(Location location) {
        return regions.stream()
                .filter(r -> r.contains(location))
                .findFirst()
                .orElse(null);
    }

    public static List<Region> getAllRegions() {
        return new ArrayList<>(regions);
    }

    public static void clearRegions() {
        regions.clear();
    }
}
