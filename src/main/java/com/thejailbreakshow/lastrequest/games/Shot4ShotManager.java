package com.thejailbreakshow.lastrequest.games;

import com.thejailbreakshow.TheJailBreakShow;
import com.thejailbreakshow.weapons.Pistol;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayDeque;
import java.util.Deque;

public class Shot4ShotManager implements Listener {
    private static final int COUNTDOWN_TIME = 5; // seconds
    private static final int TURN_TIMEOUT = 30; // seconds
    private static Player prisoner;
    private static Deque<Player> guards = new ArrayDeque<>();
    private static Player currentShooter;
    private static boolean isActive = false;
    private static BukkitTask countdownTask;
    private static BukkitTask turnTimeoutTask;

    public static void start(Player prisonerPlayer, Deque<Player> guardsQueue) {
        prisoner = prisonerPlayer;
        guards = guardsQueue;
        isActive = true;

        // Register events
        TheJailBreakShow.getInstance().getServer().getPluginManager().registerEvents(new Shot4ShotManager(), TheJailBreakShow.getInstance());

        // Teleport players to arena
        Location arena = prisoner.getWorld().getSpawnLocation();
        prisoner.teleport(arena.clone().add(5, 0, 0));
        for (Player guard : guards) {
            guard.teleport(arena.clone().add(-5, 0, 0));
        }

        // Start countdown
        countdownTask = TheJailBreakShow.getInstance().getServer().getScheduler().runTaskTimer(TheJailBreakShow.getInstance(), new Runnable() {
            int time = COUNTDOWN_TIME;
            @Override
            public void run() {
                if (time > 0) {
                    sendMessage("<yellow>Shot4Shot starting in " + time + " seconds!");
                    time--;
                } else {
                    countdownTask.cancel();
                    startGame();
                }
            }
        }, 0L, 20L);
    }

    private static void startGame() {
        // Start with prisoner
        currentShooter = prisoner;
        givePistol(prisoner);
        sendMessage("<green>Shot4Shot has begun! " + prisoner.getName() + " shoots first!");
        startTurnTimeout();
    }

    private static void givePistol(Player player) {
        player.getInventory().clear();
        Pistol pistol = new Pistol();
        ItemStack pistolItem = pistol.createWeaponItem();
        player.getInventory().addItem(pistolItem);
    }

    public static void handleShot(Player shooter) {
        if (!isActive || !shooter.equals(currentShooter)) {
            shooter.sendMessage(MiniMessage.miniMessage().deserialize("<red>It's not your turn!"));
            return;
        }

        // Cancel any existing timeout task
        if (turnTimeoutTask != null) {
            turnTimeoutTask.cancel();
        }

        // Switch turns
        if (currentShooter.equals(prisoner)) {
            // Now guard shoots
            currentShooter = guards.peek();
            if (currentShooter != null) {
                givePistol(currentShooter);
                sendMessage("<yellow>" + currentShooter.getName() + "'s turn! Shoot back!");
                startTurnTimeout();
            }
        } else {
            // Prisoner shoots again
            currentShooter = prisoner;
            givePistol(prisoner);
            sendMessage("<yellow>" + prisoner.getName() + "'s turn!");
            startTurnTimeout();
        }
    }

    private static void startTurnTimeout() {
        // Give 30 seconds for each turn
        turnTimeoutTask = TheJailBreakShow.getInstance().getServer().getScheduler().runTaskLater(TheJailBreakShow.getInstance(), () -> {
            if (!isActive) return;
            
            // If current shooter hasn't shot, they lose
            Player winner = currentShooter.equals(prisoner) ? guards.peek() : prisoner;
            sendMessage("<red>" + currentShooter.getName() + " took too long! " + winner.getName() + " wins!");
            endGame(winner);
        }, TURN_TIMEOUT * 20L); // Convert seconds to ticks
    }

    public static void handleKill(Player killer, Player victim) {
        if (!isActive) return;

        if (victim.equals(currentShooter)) {
            sendMessage("<green><bold>" + killer.getName() + "</bold> wins the Shot4Shot duel!");
            endGame(killer);
        } else {
            sendMessage("<red>No kill yet. Next turn.");
        }
    }

    private static void endGame(Player winner) {
        if (!isActive) return;

        cleanup();
        HandlerList.unregisterAll(new Shot4ShotManager());

        // Reset players
        prisoner.getInventory().clear();
        prisoner.setGameMode(GameMode.SURVIVAL);
        for (Player guard : guards) {
            guard.getInventory().clear();
            guard.setGameMode(GameMode.SURVIVAL);
        }

        // Clear references
        prisoner = null;
        guards.clear();
        currentShooter = null;
        isActive = false;
    }

    private static void cleanup() {
        if (countdownTask != null) countdownTask.cancel();
        if (turnTimeoutTask != null) turnTimeoutTask.cancel();
        countdownTask = null;
        turnTimeoutTask = null;
    }

    private static void sendMessage(String message) {
        prisoner.sendMessage(MiniMessage.miniMessage().deserialize(message));
        for (Player guard : guards) {
            guard.sendMessage(MiniMessage.miniMessage().deserialize(message));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!isActive) return;
        
        Player dead = event.getEntity();
        if (dead == prisoner || guards.contains(dead)) {
            Player killer = dead.getKiller();
            if (killer != null) {
                handleKill(killer, dead);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!isActive) return;
        
        Player quitter = event.getPlayer();
        if (quitter == prisoner || guards.contains(quitter)) {
            Player winner = quitter == prisoner ? guards.peek() : prisoner;
            endGame(winner);
        }
    }
}
