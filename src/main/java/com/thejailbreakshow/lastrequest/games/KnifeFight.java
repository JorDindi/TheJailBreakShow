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
import org.bukkit.scheduler.BukkitTask;

public class KnifeFight implements Listener {
    private static final int COUNTDOWN_TIME = 5; // seconds
    private static final int ROUND_TIME = 60; // seconds
    private static final double SMALL_HIT_HEAD_DAMAGE = 33.0;
    private static final double SMALL_HIT_BODY_DAMAGE = 10.0;
    private static final double BIG_HIT_HEAD_DAMAGE = 100.0;
    private static final double BIG_HIT_BODY_DAMAGE = 60.0;
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
        TheJailBreakShow.getInstance().getServer().getPluginManager().registerEvents(new KnifeFight(), TheJailBreakShow.getInstance());

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
        countdownTask = TheJailBreakShow.getInstance().getServer().getScheduler().runTaskTimer(TheJailBreakShow.getInstance(), () -> {
            if (COUNTDOWN_TIME - roundTimeLeft > 0) {
                sendMessage("<yellow>Knife Fight starting in " + (COUNTDOWN_TIME - roundTimeLeft) + " seconds!");
            } else {
                countdownTask.cancel();
                startRound();
            }
            roundTimeLeft--;
        }, 0L, 20L);
    }

    private static void startRound() {
        roundTimeLeft = ROUND_TIME;
        sendMessage("<green>Knife Fight has begun! First to die loses!");

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
        HandlerList.unregisterAll(new KnifeFight());

        if (winner == null) {
            sendMessage("<red>Time's up! No winner!");
        } else {
            Player loser = winner == prisoner ? guard : prisoner;
            sendMessage("<green>" + winner.getName() + " has won the Knife Fight!");
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
        prisoner.sendMessage(MiniMessage.miniMessage().deserialize(message));
        guard.sendMessage(MiniMessage.miniMessage().deserialize(message));
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

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!isActive) return;
        
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            
            // Only allow damage between the two players
            if ((attacker == prisoner && victim == guard) || (attacker == guard && victim == prisoner)) {
                // Determine if it's a big hit (sprinting) or small hit
                boolean isBigHit = attacker.isSprinting();
                
                // Determine if it's a headshot
                boolean isHeadshot = event.getCause() == EntityDamageByEntityEvent.DamageCause.ENTITY_ATTACK && 
                                   attacker.getLocation().getY() > victim.getLocation().getY() + 1.5;
                
                // Set damage based on hit type and location
                if (isBigHit) {
                    event.setDamage(isHeadshot ? BIG_HIT_HEAD_DAMAGE : BIG_HIT_BODY_DAMAGE);
                } else {
                    event.setDamage(isHeadshot ? SMALL_HIT_HEAD_DAMAGE : SMALL_HIT_BODY_DAMAGE);
                }
            } else {
                event.setCancelled(true);
            }
        }
    }
} 