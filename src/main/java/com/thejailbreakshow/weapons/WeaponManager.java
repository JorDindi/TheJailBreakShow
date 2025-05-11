package com.thejailbreakshow.weapons;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class WeaponManager implements Listener {
    private static final Map<String, Weapon> registeredWeapons = new HashMap<>();

    public static void init() {
        // Register default weapons
        registerWeapon(new Pistol());
        registerWeapon(new Rifle());
        registerWeapon(new Knife());
    }

    public static void registerWeapon(Weapon weapon) {
        registeredWeapons.put(weapon.getName(), weapon);
    }

    public static Weapon getWeapon(String name) {
        return registeredWeapons.get(name);
    }

    public static Weapon getWeaponFromItem(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return null;
        }

        String name = item.getItemMeta().getDisplayName();
        return registeredWeapons.values().stream()
                .filter(weapon -> weapon.createWeaponItem().getItemMeta().getDisplayName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) return;

        Weapon weapon = getWeaponFromItem(item);
        if (weapon == null) return;

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            weapon.shoot(player);
        } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            weapon.reload(player);
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null) return;

        Weapon weapon = getWeaponFromItem(item);
        if (weapon instanceof Rifle rifle) {
            if (!event.isSneaking()) {
                rifle.stopAutoFire(player);
            }
        }
    }
} 