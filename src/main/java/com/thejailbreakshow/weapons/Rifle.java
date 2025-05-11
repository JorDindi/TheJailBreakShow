package com.thejailbreakshow.weapons;

import com.thejailbreakshow.TheJailBreakShow;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Rifle extends Weapon {
    private static final int MAX_AMMO = 30;
    private static final int RELOAD_TIME = 60; // 3 seconds
    private static final int FIRE_RATE = 5; // Ticks between shots
    private final Map<UUID, BukkitTask> autoFireTasks = new HashMap<>();

    public Rifle() {
        super("Rifle", 15.0, 2.5, 5); // 15 damage, 2.5x headshot, 0.25s cooldown
    }

    @Override
    public ItemStack createWeaponItem() {
        ItemStack item = new ItemStack(Material.IRON_HOE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize("<gold>Rifle"));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void shoot(Player player) {
        if (!canShoot(player)) return;

        // Start auto-fire task
        UUID playerId = player.getUniqueId();
        if (!autoFireTasks.containsKey(playerId)) {
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline() || !player.isSneaking()) {
                        stopAutoFire(player);
                        return;
                    }
                    fireBullet(player);
                }
            }.runTaskTimer(TheJailBreakShow.getInstance(), 0L, FIRE_RATE);
            autoFireTasks.put(playerId, task);
        }
    }

    private void fireBullet(Player player) {
        // Get player's look direction
        Vector direction = player.getLocation().getDirection();
        
        // Create ray trace for bullet
        RayTraceResult hit = player.getWorld().rayTrace(
            player.getEyeLocation(),
            direction,
            100.0, // Longer range than pistol
            org.bukkit.FluidCollisionMode.NEVER,
            true,
            0.1,
            entity -> entity != player && entity instanceof Player
        );

        if (hit != null && hit.getHitEntity() instanceof Player target) {
            // Calculate damage based on hit location
            double finalDamage = damage;
            if (hit.getHitPosition().getY() > target.getLocation().getY() + 1.5) {
                finalDamage *= headshotMultiplier;
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gold>Headshot!"));
            }
            
            // Apply damage
            target.damage(finalDamage, player);
        }

        // Play sound and particle effects
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.8f, 1.2f);
        player.getWorld().spawnParticle(Particle.SMOKE, 
            player.getEyeLocation().add(direction), 3, 0.1, 0.1, 0.1, 0);
    }

    public void stopAutoFire(Player player) {
        UUID playerId = player.getUniqueId();
        BukkitTask task = autoFireTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public void reload(Player player) {
        stopAutoFire(player);
        player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Reloading..."));
        startReload(player, RELOAD_TIME);
    }
} 