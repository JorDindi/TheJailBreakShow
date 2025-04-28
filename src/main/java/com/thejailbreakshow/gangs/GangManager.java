package com.thejailbreakshow.gangs;

import com.thejailbreakshow.economy.EconomyManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GangManager {

    private static final Map<String, Gang> gangs = new HashMap<>();
    private static final Map<UUID, String> playerGangMap = new HashMap<>();

    public static boolean isInGang(Player player) {
        return playerGangMap.containsKey(player.getUniqueId());
    }

    public static Gang getGang(Player player) {
        String gangName = playerGangMap.get(player.getUniqueId());
        if (gangName == null) return null;
        return gangs.get(gangName);
    }

    private static void applyGlow(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, false, false));
    }

    public static void removeGlow(Player player) {
        player.removePotionEffect(PotionEffectType.GLOWING);
    }

    public static void resetAll() {
        gangs.clear();
        playerGangMap.clear();
    }
    public static void createGang(Player leader, String name) {
        if (EconomyManager.getBalance(leader) < EconomyManager.getGangCreationCost()) {
            leader.sendMessage(MiniMessage.miniMessage().deserialize("<red>Not enough Gold to create a gang! You need <gold>" + EconomyManager.getGangCreationCost() + "</gold> Gold."));
            return;
        }

        EconomyManager.subtractGold(leader, EconomyManager.getGangCreationCost());

        Gang gang = new Gang(name, leader);
        gangs.put(name.toLowerCase(), gang);
        playerGangMap.put(leader.getUniqueId(), name.toLowerCase());
        applyGlow(leader);

        leader.sendMessage(MiniMessage.miniMessage().deserialize("<green>You created the gang <white>" + name + "</white>! (-" + EconomyManager.getGangCreationCost() + " Gold)"));
    }
}
