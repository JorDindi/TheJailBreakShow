package com.thejailbreakshow.lastrequest.games;

import com.thejailbreakshow.TheJailBreakShow;
import com.thejailbreakshow.weapons.Knife;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class HeadshotOnlyKnife implements Listener {
    private static final int COUNTDOWN_TIME = 5; // seconds
    private static final int ROUND_TIME = 60; // seconds
    private static final double HEADSHOT_DAMAGE = 100.0;
    private static Player prisoner;
    private static Player guard;
    private static int roundTimeLeft;
    private static BukkitTask countdownTask;
    private static BukkitTask roundTask;
    private static boolean isActive = false;

    public static void start(Player prisonerPlayer, Player guardPlayer) {
        prisoner = prisonerPlayer;
        guard = guardPlayer;
        isActive = true;
        roundTimeLeft = ROUND_TIME;

        // Register events
        TheJailBreakShow.getInstance().getServer().getPluginManager().registerEvents(new HeadshotOnlyKnife(), TheJailBreakShow.getInstance());

        // Teleport players to arena
        Location arena = prisoner.getWorld().getSpawnLocation();
        prisoner.teleport(arena.clone().add(5, 0, 0));
        guard.teleport(arena.clone().add(-5, 0, 0));

        // Give knives
        Knife knife = new Knife();
        prisoner.getInventory().clear();
        guard.getInventory().clear();
        prisoner.getInventory().addItem(knife.createWeaponItem());
        guard.getInventory().addItem(knife.createWeaponItem());

        // Start countdown
        countdownTask = TheJailBreakShow.getInstance().getServer().getScheduler().runTaskTimer(TheJailBreakShow.getInstance(), new Runnable() {
            int time = COUNTDOWN_TIME;
            @Override
            public void run() {
                if (time > 0) {
                    sendMessage("<yellow>Headshot Only Knife starting in " + time + " seconds!");
                    time--;
                } else {
                    countdownTask.cancel();
                    startRound();
                }
            }
        }, 0L, 20L);
    }

    private static void startRound() {
        roundTimeLeft = ROUND_TIME;
        sendMessage("<green>Headshot Only Knife has begun! Only headshots deal damage!");

        roundTask = TheJailBreakShow.getInstance().getServer().getScheduler().runTaskTimer(TheJailBreakShow.getInstance(), () -> {
            if (roundTimeLeft > 0) {
                roundTimeLeft--;
            } else {
                end(null); // Time's up, no winner
            }
        }, 0L, 20L);
    }

    private static void end(Player winner) {
        if (!isActive) return;
        cleanup();
        HandlerList.unregisterAll(new HeadshotOnlyKnife());

        if (winner == null) {
            sendMessage("<red>Time's up! No winner!");
        } else {
            sendMessage("<green>" + winner.getName() + " has won the Headshot Only Knife duel!");
        }

        // Reset players
        prisoner.getInventory().clear();
        guard.getInventory().clear();
        prisoner.setGameMode(GameMode.SURVIVAL);
        guard.setGameMode(GameMode.SURVIVAL);

        // Clear references
        prisoner = null;
        guard = null;
        isActive = false;
    }

    private static void cleanup() {
        if (countdownTask != null) countdownTask.cancel();
        if (roundTask != null) roundTask.cancel();
        countdownTask = null;
        roundTask = null;
    }

    private static void sendMessage(String message) {
        if (prisoner != null) prisoner.sendMessage(MiniMessage.miniMessage().deserialize(message));
        if (guard != null) guard.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!isActive) return;
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();
        if ((attacker != prisoner && attacker != guard) || (victim != prisoner && victim != guard)) return;

        // Check if headshot (attacker Y > victim eye Y - small margin)
        double attackerY = attacker.getLocation().getY();
        double headY = victim.getEyeLocation().getY();
        if (Math.abs(attackerY - headY) < 0.5) {
            event.setDamage(HEADSHOT_DAMAGE);
        } else {
            event.setDamage(0.0);
            attacker.sendMessage(MiniMessage.miniMessage().deserialize("<gray>Body shot! Only headshots count."));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!isActive) return;
        Player dead = event.getEntity();
        if (dead == prisoner || dead == guard) {
            Player winner = dead == prisoner ? guard : prisoner;
            end(winner);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!isActive) return;
        Player quitter = event.getPlayer();
        if (quitter == prisoner || quitter == guard) {
            Player winner = quitter == prisoner ? guard : prisoner;
            end(winner);
        }
    }
} 