package com.thejailbreakshow.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class LeaderChallengeManager {

    private static final String[] phrases = {
            "JailBreak is fun!",
            "First one to type this wins!",
            "Type quickly to become leader!",
            "Speed matters!",
            "Freedom or prison!"
    };

    private static ChallengeState state = ChallengeState.INACTIVE;
    private static String currentPhrase;

    public static void startChallenge() {
        if (state == ChallengeState.RUNNING) return;

        Random random = new Random();
        currentPhrase = phrases[random.nextInt(phrases.length)];
        state = ChallengeState.RUNNING;

        Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<gold><bold>LEADER CHALLENGE:</bold> First to type: <yellow>\"" + currentPhrase + "\"</yellow>"));

        // Optional: timeout after 30 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                if (state == ChallengeState.RUNNING) {
                    state = ChallengeState.INACTIVE;
                    Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<red>No one typed the phrase in time! Challenge ended."));
                }
            }
        }.runTaskLater(com.thejailbreakshow.TheJailBreakShow.getInstance(), 30 * 20); // 30 seconds
    }

    public static void handleChat(Player player, String message) {
        if (state != ChallengeState.RUNNING) return;

        if (message.equalsIgnoreCase(currentPhrase)) {
            state = ChallengeState.INACTIVE;
            Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<green><bold>" + player.getName() + "</bold> won the Leader Challenge!"));
            // TODO: promote player to Guard or Leader role here.
        }
    }

    public static boolean isRunning() {
        return state == ChallengeState.RUNNING;
    }
}
