package com.thejailbreakshow.weapons;

import com.thejailbreakshow.TheJailBreakShow;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Knife extends Weapon {
    private static final int ATTACK_RANGE = 3; // Blocks
    private static final int ATTACK_COOLDOWN = 10; // 0.5 seconds
    private final Map<UUID, BukkitTask> attackTasks = new HashMap<>();

    public Knife() {
        super("Knife", 20.0, 2.0, 10); // 20 damage, 2x headshot, 0.5s cooldown
    }

    @Override
    public ItemStack createWeaponItem() {
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize("<gray>Knife"));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void shoot(Player player) {
        if (!canShoot(player)) return;

        // Get player's look direction
        Vector direction = player.getLocation().getDirection();
        
        // Check for players in range
        for (Player target : player.getWorld().getPlayers()) {
            if (target == player) continue;
            
            double distance = player.getLocation().distance(target.getLocation());
            if (distance <= ATTACK_RANGE) {
                // Check if target is in front of player
                Vector toTarget = target.getLocation().toVector().subtract(player.getLocation().toVector());
                double dot = direction.dot(toTarget.normalize());
                
                if (dot > 0.7) { // About 45 degrees
                    // Calculate damage based on hit location
                    double finalDamage = damage;
                    if (target.getLocation().getY() > player.getLocation().getY() + 1.5) {
                        finalDamage *= headshotMultiplier;
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<gold>Headshot!"));
                    }
                    
                    // Apply damage
                    target.damage(finalDamage, player);
                    
                    // Play sound and particle effects
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);
                    player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, 
                        target.getLocation().add(0, 1, 0), 5, 0.3, 0.3, 0.3, 0);
                    
                    // Add attack cooldown
                    lastShotTime.put(player.getUniqueId(), System.currentTimeMillis());
                    return;
                }
            }
        }
        
        // Play miss sound
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_WEAK, 0.5f, 1.0f);
    }

    @Override
    public void reload(Player player) {
        // Knives don't need to reload
        player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Knives don't need to reload!"));
    }
} 