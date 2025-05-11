package com.thejailbreakshow.lastrequest.games;

import com.thejailbreakshow.TheJailBreakShow;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GrenadeToss implements Listener {
    private static final int COUNTDOWN_TIME = 5; // seconds
    private static final int ROUND_TIME = 60; // seconds
    private static final double BASE_DAMAGE = 50.0;
    private static final double MAX_RADIUS = 6.0; // blocks
    private static final long GRENADE_COOLDOWN = 40L; // 2 seconds (ticks)
    private static Player prisoner;
    private static Player guard;
    private static int roundTimeLeft;
    private static BukkitTask countdownTask;
    private static BukkitTask roundTask;
    private static boolean isActive = false;
    private static final Map<UUID, Long> lastGrenadeThrow = new HashMap<>();
    private static final Map<UUID, BukkitTask> grenadeTimers = new HashMap<>();
    private static Plugin plugin = TheJailBreakShow.getInstance();

    public static void start(Player prisonerPlayer, Player guardPlayer) {
        prisoner = prisonerPlayer;
        guard = guardPlayer;
        isActive = true;
        roundTimeLeft = ROUND_TIME;
        lastGrenadeThrow.clear();
        grenadeTimers.clear();

        // Register events
        plugin.getServer().getPluginManager().registerEvents(new GrenadeToss(), plugin);

        // Teleport players to arena
        Location arena = prisoner.getWorld().getSpawnLocation();
        prisoner.teleport(arena.clone().add(5, 0, 0));
        guard.teleport(arena.clone().add(-5, 0, 0));

        // Give infinite grenades (snowballs)
        giveGrenade(prisoner);
        giveGrenade(guard);

        // Start countdown
        countdownTask = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            int time = COUNTDOWN_TIME;
            @Override
            public void run() {
                if (time > 0) {
                    sendMessage("<yellow>Grenade Toss starting in " + time + " seconds!");
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
        sendMessage("<green>Grenade Toss has begun! First to kill the other wins!");

        roundTask = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
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
        HandlerList.unregisterAll(new GrenadeToss());

        if (winner == null) {
            sendMessage("<red>Time's up! No winner!");
        } else {
            sendMessage("<green>" + winner.getName() + " has won the Grenade Toss!");
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
        lastGrenadeThrow.clear();
        grenadeTimers.clear();
    }

    private static void cleanup() {
        if (countdownTask != null) countdownTask.cancel();
        if (roundTask != null) roundTask.cancel();
        countdownTask = null;
        roundTask = null;
        for (BukkitTask task : grenadeTimers.values()) {
            task.cancel();
        }
        grenadeTimers.clear();
    }

    private static void sendMessage(String message) {
        if (prisoner != null) prisoner.sendMessage(MiniMessage.miniMessage().deserialize(message));
        if (guard != null) guard.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    private static void giveGrenade(Player player) {
        player.getInventory().clear();
        player.getInventory().addItem(new ItemStack(Material.SNOWBALL, 16));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!isActive) return;
        Player player = event.getPlayer();
        if ((player != prisoner && player != guard) || event.getItem() == null || event.getItem().getType() != Material.SNOWBALL) return;
        long now = System.currentTimeMillis();
        long last = lastGrenadeThrow.getOrDefault(player.getUniqueId(), 0L);
        if (now - last < GRENADE_COOLDOWN * 50) { // cooldown in ms
            event.setCancelled(true);
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Grenade is on cooldown!"));
            return;
        }
        lastGrenadeThrow.put(player.getUniqueId(), now);
        // Replenish grenade after throw
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline() && isActive) {
                player.getInventory().addItem(new ItemStack(Material.SNOWBALL, 1));
            }
        }, 5L);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!isActive) return;
        Player player = event.getPlayer();
        if ((player != prisoner && player != guard) || event.getItemDrop().getItemStack().getType() != Material.SNOWBALL) return;
        event.setCancelled(true); // Prevent dropping grenades
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!isActive) return;
        if (!(event.getDamager() instanceof Snowball)) return;
        Snowball snowball = (Snowball) event.getDamager();
        if (!(snowball.getShooter() instanceof Player)) return;
        Player shooter = (Player) snowball.getShooter();
        if (shooter != prisoner && shooter != guard) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();
        if (victim != prisoner && victim != guard) return;

        // Calculate distance for damage reduction
        double distance = snowball.getLocation().distance(victim.getLocation());
        double damage = BASE_DAMAGE;
        if (distance > MAX_RADIUS / 2) {
            damage = BASE_DAMAGE * 0.5; // Reduce damage at longer range
        }
        event.setDamage(damage);

        // Simulate explosion effect
        victim.getWorld().createExplosion(snowball.getLocation(), 0.0F, false, false, shooter);
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