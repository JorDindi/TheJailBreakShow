package com.thejailbreakshow.lastrequest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LastRequestMenu {

    public static void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9 * 3, "Choose Last Request");

        for (LastRequestType type : LastRequestType.values()) {
            ItemStack item = new ItemStack(type.getIcon());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(type.getDisplayName());
            item.setItemMeta(meta);

            menu.addItem(item);
        }

        player.openInventory(menu);
    }
}
