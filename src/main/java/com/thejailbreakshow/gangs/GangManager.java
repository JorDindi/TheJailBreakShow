package com.thejailbreakshow.gangs;

import com.thejailbreakshow.economy.EconomyManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class GangManager {
    private static final Map<String, Gang> gangs = new HashMap<>();
    private static final Map<UUID, String> playerGangMap = new HashMap<>();
    private static final Random random = new Random();

    public static boolean isInGang(Player player) {
        return playerGangMap.containsKey(player.getUniqueId());
    }

    public static Gang getGang(Player player) {
        String gangName = playerGangMap.get(player.getUniqueId());
        if (gangName == null) return null;
        return gangs.get(gangName);
    }

    public static void applyGlow(Player player, Color color) {
        // Remove any existing glow effect
        player.removePotionEffect(PotionEffectType.GLOWING);
        
        // Apply new glow effect with custom color
        PotionEffect glow = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, false, false);
        player.addPotionEffect(glow);
        
        // Set player's glowing color (requires ProtocolLib or similar)
        // This is a placeholder - actual implementation depends on the server version and available APIs
        player.setGlowing(true);
    }

    public static void removeGlow(Player player) {
        player.removePotionEffect(PotionEffectType.GLOWING);
        player.setGlowing(false);
    }

    public static void resetAll() {
        // Remove glow effects from all players
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeGlow(player);
        }
        
        gangs.clear();
        playerGangMap.clear();
    }

    public static void createGang(Player leader, String name) {
        if (EconomyManager.getBalance(leader) < EconomyManager.getGangCreationCost()) {
            leader.sendMessage(MiniMessage.miniMessage().deserialize("<red>Not enough Gold to create a gang! You need <gold>" + EconomyManager.getGangCreationCost() + "</gold> Gold."));
            return;
        }

        EconomyManager.subtractGold(leader, EconomyManager.getGangCreationCost());

        // Generate a random color for the gang
        Color color = Color.fromRGB(
            random.nextInt(256),
            random.nextInt(256),
            random.nextInt(256)
        );

        Gang gang = new Gang(name, leader, color);
        gangs.put(name.toLowerCase(), gang);
        playerGangMap.put(leader.getUniqueId(), name.toLowerCase());
        applyGlow(leader, color);

        leader.sendMessage(MiniMessage.miniMessage().deserialize("<green>You created the gang <white>" + name + "</white>! (-" + EconomyManager.getGangCreationCost() + " Gold)"));
    }

    public static void joinGang(Player player, String gangName) {
        Gang gang = gangs.get(gangName.toLowerCase());
        if (gang == null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Gang not found!"));
            return;
        }

        if (isInGang(player)) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You are already in a gang!"));
            return;
        }

        gang.addMember(player);
        playerGangMap.put(player.getUniqueId(), gangName.toLowerCase());
        applyGlow(player, gang.getColor());

        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You joined the gang <white>" + gang.getName() + "</white>!"));
    }

    public static void leaveGang(Player player) {
        Gang gang = getGang(player);
        if (gang == null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You are not in a gang!"));
            return;
        }

        gang.removeMember(player);
        playerGangMap.remove(player.getUniqueId());
        removeGlow(player);

        player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>You left the gang <white>" + gang.getName() + "</white>!"));
    }

    public static void handleKill(Player killer, Player victim) {
        Gang killerGang = getGang(killer);
        Gang victimGang = getGang(victim);

        if (killerGang != null) {
            killerGang.addKill();
        }
        if (victimGang != null) {
            victimGang.addDeath();
        }
    }

    public static void handleGameEnd(Player winner, Player loser) {
        Gang winnerGang = getGang(winner);
        Gang loserGang = getGang(loser);

        if (winnerGang != null) {
            winnerGang.addWin();
        }
        if (loserGang != null) {
            loserGang.addLoss();
        }
    }

    public static String getGangStats(String gangName) {
        Gang gang = gangs.get(gangName.toLowerCase());
        if (gang == null) return "Gang not found!";

        return String.format(
            "Gang: %s\n" +
            "Members: %d\n" +
            "Kills: %d\n" +
            "Deaths: %d\n" +
            "KDR: %.2f\n" +
            "Wins: %d\n" +
            "Losses: %d\n" +
            "Win Rate: %.2f%%",
            gang.getName(),
            gang.getMembers().size(),
            gang.getKills(),
            gang.getDeaths(),
            gang.getKDR(),
            gang.getWins(),
            gang.getLosses(),
            gang.getWinRate() * 100
        );
    }
}
