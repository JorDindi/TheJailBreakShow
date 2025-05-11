package com.thejailbreakshow.regions;

import com.thejailbreakshow.managers.PlayerManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;

public class WeaponRoomManager implements Listener {
    private static final Set<String> weaponRooms = new HashSet<>();

    public static void init() {
        // Initialize weapon rooms from config/saved data
    }

    public static void registerWeaponRoom(String regionName) {
        weaponRooms.add(regionName);
    }

    public static void unregisterWeaponRoom(String regionName) {
        weaponRooms.remove(regionName);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        
        if (to == null) return;

        // Check if player is entering a weapon room
        Region region = RegionManager.getRegionAt(to);
        if (region != null && weaponRooms.contains(region.getName())) {
            if (!PlayerManager.isGuard(player.getUniqueId())) {
                // Prevent prisoner from entering
                event.setCancelled(true);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Only guards can enter the weapon room!"));
            }
        }
    }
} 