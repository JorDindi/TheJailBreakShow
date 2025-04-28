package com.thejailbreakshow.economy;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyManager {

    private static final Map<UUID, Integer> balances = new HashMap<>();
    private static final int STARTING_GOLD = 300;
    private static final int GANG_CREATION_COST = 150;

    public static void initPlayer(Player player) {
        balances.putIfAbsent(player.getUniqueId(), STARTING_GOLD);
    }

    public static int getBalance(Player player) {
        return balances.getOrDefault(player.getUniqueId(), 0);
    }

    public static void addGold(Player player, int amount) {
        balances.put(player.getUniqueId(), getBalance(player) + amount);
    }

    public static boolean subtractGold(Player player, int amount) {
        int current = getBalance(player);
        if (current < amount) return false;
        balances.put(player.getUniqueId(), current - amount);
        return true;
    }

    public static int getGangCreationCost() {
        return GANG_CREATION_COST;
    }
}
