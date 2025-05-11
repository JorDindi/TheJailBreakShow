package com.thejailbreakshow.weapons;

import com.thejailbreakshow.TheJailBreakShow;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Weapon {
    protected final String name;
    protected final double damage;
    protected final double headshotMultiplier;
    protected final int cooldown;
    protected final Map<UUID, Long> lastShotTime = new HashMap<>();
    protected final Map<UUID, Long> reloadTasks = new HashMap<>();

    protected Weapon(String name, double damage, double headshotMultiplier, int cooldown) {
        this.name = name;
        this.damage = damage;
        this.headshotMultiplier = headshotMultiplier;
        this.cooldown = cooldown;
    }

    public abstract ItemStack createWeaponItem();
    public abstract void shoot(Player player);
    public abstract void reload(Player player);

    protected boolean canShoot(Player player) {
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        
        if (lastShotTime.containsKey(playerId)) {
            long timeSinceLastShot = currentTime - lastShotTime.get(playerId);
            if (timeSinceLastShot < cooldown * 50) { // Convert ticks to milliseconds
                return false;
            }
        }
        
        return true;
    }

    protected void startReload(Player player, int reloadTime) {
        UUID playerId = player.getUniqueId();
        reloadTasks.put(playerId, System.currentTimeMillis() + (reloadTime * 50L));
    }

    public String getName() {
        return name;
    }

    public double getDamage() {
        return damage;
    }

    public double getHeadshotMultiplier() {
        return headshotMultiplier;
    }

    public int getCooldown() {
        return cooldown;
    }
} 