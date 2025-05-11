package com.thejailbreakshow.lastrequest.games;

import com.thejailbreakshow.TheJailBreakShow;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class BoxingMatch implements Listener {
    private static final int COUNTDOWN_TIME = 5; // seconds
    private static final int ROUND_TIME = 120; // seconds
    private static final int ROUNDS_TO_WIN = 2; // Best of 3 rounds
    private static final double SMALL_HIT_HEAD_DAMAGE = 20.0;
    private static final double SMALL_HIT_BODY_DAMAGE = 5.0;
    private static final double BIG_HIT_HEAD_DAMAGE = 80.0;
    private static final double BIG_HIT_BODY_DAMAGE = 50.0;
    private static final int DIZZY_DURATION = 40; // 2 seconds (20 ticks per second)
    private static Player prisoner;
    private static Player guard;
    private static int roundTimeLeft;
    private static int prisonerScore = 0;
    private static int guardScore = 0;
    private static int currentRound = 1;
    private static BukkitTask countdownTask;
    private static BukkitTask roundTask;
    private static boolean isActive = false;

    public static void start(Player prisonerPlayer, Player guardPlayer) {
        prisoner = prisonerPlayer;
        guard = guardPlayer;
        isActive = true;
        roundTimeLeft = ROUND_TIME;
        prisonerScore = 0;
        guardScore = 0;
        currentRound = 1;

        // Register events
        TheJailBreakShow.getInstance().getServer().getPluginManager().registerEvents(new BoxingMatch(), TheJailBreakShow.getInstance());

        // Teleport players to arena
        Location arena = prisoner.getWorld().getSpawnLocation();
        prisoner.teleport(arena.clone().add(5, 0, 0));
        guard.teleport(arena.clone().add(-5, 0, 0));

        // Give boxing gloves
        ItemStack gloves = new ItemStack(Material.LEATHER_CHESTPLATE);
        prisoner.getInventory().clear();
        guard.getInventory().clear();
        prisoner.getInventory().setChestplate(gloves);
        guard.getInventory().setChestplate(gloves);

        // Start countdown
        countdownTask = TheJailBreakShow.getInstance().getServer().getScheduler().runTaskTimer(TheJailBreakShow.getInstance(), () -> {
            if (COUNTDOWN_TIME - roundTimeLeft > 0) {
                sendMessage("<yellow>Boxing Match starting in " + (COUNTDOWN_TIME - roundTimeLeft) + " seconds!");
            } else {
                countdownTask.cancel();
                startRound();
            }
            roundTimeLeft--;
        }, 0L, 20L);
    }

    private static void startRound() {
        roundTimeLeft = ROUND_TIME;
        sendMessage("<green>Round " + currentRound + " has begun! First to knock out their opponent wins the round!");

        roundTask = TheJailBreakShow.getInstance().getServer().getScheduler().runTaskTimer(TheJailBreakShow.getInstance(), () -> {
            if (roundTimeLeft > 0) {
                roundTimeLeft--;
            } else {
                endRound(null); // Time's up, no winner for this round
            }
        }, 0L, 20L);
    }

    private static void endRound(Player winner) {
        if (!isActive) return;

        if (winner == null) {
            sendMessage("<yellow>Time's up! No winner for this round!");
        } else {
            if (winner == prisoner) {
                prisonerScore++;
                sendMessage("<green>" + prisoner.getName() + " wins round " + currentRound + "!");
            } else {
                guardScore++;
                sendMessage("<green>" + guard.getName() + " wins round " + currentRound + "!");
            }
        }

        currentRound++;
        
        // Check if someone has won the match
        if (prisonerScore >= ROUNDS_TO_WIN || guardScore >= ROUNDS_TO_WIN) {
            end(prisonerScore > guardScore ? prisoner : guard);
        } else {
            // Start next round after a short delay
            TheJailBreakShow.getInstance().getServer().getScheduler().runTaskLater(TheJailBreakShow.getInstance(), () -> {
                // Reset player positions and health
                Location arena = prisoner.getWorld().getSpawnLocation();
                prisoner.teleport(arena.clone().add(5, 0, 0));
                guard.teleport(arena.clone().add(-5, 0, 0));
                prisoner.setHealth(20);
                guard.setHealth(20);
                startRound();
            }, 60L); // 3 second delay
        }
    }

    private static void end(Player winner) {
        if (!isActive) return;

        cleanup();
        HandlerList.unregisterAll(new BoxingMatch());

        if (winner == null) {
            sendMessage("<red>Match ended in a draw!");
        } else {
            sendMessage("<green>" + winner.getName() + " has won the Boxing Match " + 
                       (winner == prisoner ? prisonerScore : guardScore) + "-" + 
                       (winner == prisoner ? guardScore : prisonerScore) + "!");
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
            endRound(winner);
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
                    
                    // Apply dizzy effect for big headshots
                    if (isHeadshot) {
                        applyDizzyEffect(victim);
                    }
                } else {
                    event.setDamage(isHeadshot ? SMALL_HIT_HEAD_DAMAGE : SMALL_HIT_BODY_DAMAGE);
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    private static void applyDizzyEffect(Player player) {
        // Tilt player's vision up
        player.setVelocity(new Vector(0, 0.1, 0));
        
        // Create dizzy effect
        BukkitTask dizzyTask = TheJailBreakShow.getInstance().getServer().getScheduler().runTaskTimer(TheJailBreakShow.getInstance(), () -> {
            if (!isActive || player == null) return;
            
            // Randomly tilt player's view
            float yaw = player.getLocation().getYaw() + (float) (Math.random() * 10 - 5);
            float pitch = player.getLocation().getPitch() + (float) (Math.random() * 10 - 5);
            player.teleport(player.getLocation().setDirection(new Vector(
                -Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)),
                -Math.sin(Math.toRadians(pitch)),
                Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch))
            )));
        }, 0L, 1L);
        
        // Cancel dizzy effect after duration
        TheJailBreakShow.getInstance().getServer().getScheduler().runTaskLater(TheJailBreakShow.getInstance(), () -> {
            dizzyTask.cancel();
        }, DIZZY_DURATION);
    }
} 