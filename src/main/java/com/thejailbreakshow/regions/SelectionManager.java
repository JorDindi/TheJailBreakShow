package com.thejailbreakshow.regions;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SelectionManager {

    private static final Map<Player, Location> pos1 = new HashMap<>();
    private static final Map<Player, Location> pos2 = new HashMap<>();

    public static void setPos1(Player player, Location loc) {
        pos1.put(player, loc);
    }

    public static void setPos2(Player player, Location loc) {
        pos2.put(player, loc);
    }

    public static Location getPos1(Player player) {
        return pos1.get(player);
    }

    public static Location getPos2(Player player) {
        return pos2.get(player);
    }

    public static boolean hasBothPositions(Player player) {
        return pos1.containsKey(player) && pos2.containsKey(player);
    }

    public static void clear(Player player) {
        pos1.remove(player);
        pos2.remove(player);
    }
}
