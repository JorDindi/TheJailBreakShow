package com.thejailbreakshow.weapons;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Pistol extends Weapon {
    private static final int MAX_AMMO = 8;
    private static final int RELOAD_TIME = 40; // 2 seconds

    public Pistol() {
        super("Pistol", 20.0, 2.0, 10); // 20 damage, 2x headshot, 0.5s cooldown
    }

    @Override
    public ItemStack createWeaponItem() {
        ItemStack item = new ItemStack(Material.WOODEN_HOE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize("<gold>Pistol"));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void shoot(Player player) {
        if (!canShoot(player)) return;

        // Get player's look direction
        Vector direction = player.getLocation().getDirection();
        
        // Create ray trace for bullet
        RayTraceResult hit = player.getWorld().rayTrace(
            player.getEyeLocation(),
            direction,
            50.0, // Max distance
            org.bukkit.FluidCollisionMode.NEVER,
            true,
            0.1, // Ray size
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
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.SMOKE, 
            player.getEyeLocation().add(direction), 5, 0.1, 0.1, 0.1, 0);
    }

    @Override
    public void reload(Player player) {
        player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Reloading..."));
        startReload(player, RELOAD_TIME);
    }
} 