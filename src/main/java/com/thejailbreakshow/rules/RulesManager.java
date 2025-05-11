package com.thejailbreakshow.rules;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;

public class RulesManager {
    private static final Map<UUID, Set<String>> declaredRules = new HashMap<>();
    private static final Set<String> globalRules = new HashSet<>();
    private static ItemStack rulesBook;

    public static void init() {
        // Initialize default rules
        globalRules.add("No griefing");
        globalRules.add("No exploiting");
        globalRules.add("No cheating");
        createRulesBook();
    }

    public static void declareRule(Player guard, String rule) {
        declaredRules.computeIfAbsent(guard.getUniqueId(), k -> new HashSet<>()).add(rule);
        guard.sendMessage(MiniMessage.miniMessage().deserialize("<green>Rule declared: " + rule));
    }

    public static boolean isRuleDeclared(Player guard, String rule) {
        return declaredRules.getOrDefault(guard.getUniqueId(), new HashSet<>()).contains(rule);
    }

    public static Set<String> getDeclaredRules(Player guard) {
        return declaredRules.getOrDefault(guard.getUniqueId(), new HashSet<>());
    }

    public static void clearRules(Player guard) {
        declaredRules.remove(guard.getUniqueId());
    }

    public static void giveRulesBook(Player player) {
        player.getInventory().addItem(rulesBook);
    }

    private static void createRulesBook() {
        rulesBook = new ItemStack(org.bukkit.Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) rulesBook.getItemMeta();
        
        List<Component> pages = new ArrayList<>();
        pages.add(MiniMessage.miniMessage().deserialize("<bold>Jail Break Rules</bold>\n\n" +
                "1. No griefing\n" +
                "2. No exploiting\n" +
                "3. No cheating\n\n" +
                "Additional rules will be declared by guards."));
        
        meta.pages(pages);
        meta.title(Component.text("Jail Break Rules"));
        meta.author(Component.text("The Jail Break Show"));
        
        rulesBook.setItemMeta(meta);
    }
} 