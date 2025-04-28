package com.thejailbreakshow.lastrequest;

import org.bukkit.Material;

public enum LastRequestType {
    SHOT4SHOT("Shot4Shot", Material.CROSSBOW),
    KNIFE_FIGHT("Knife Fight", Material.IRON_SWORD),
    BOXING_MATCH("Boxing Match", Material.LEATHER_HELMET),
    GRENADE_TOSS("Grenade Toss", Material.TNT),
    HEADSHOT_ONLY_AWP("Headshot Only AWP", Material.BOW),
    HEADSHOT_ONLY_KNIFE("Headshot Only Knife", Material.STONE_SWORD),
    RUSSIAN_ROULETTE("Russian Roulette", Material.TRIPWIRE_HOOK),
    ONE_HP_KNIFE("One HP Knife Fight", Material.GOLDEN_SWORD),
    TAG_YOURE_IT("Tag You're It!", Material.RED_WOOL),
    DODGEBALL("Dodgeball", Material.SNOWBALL);

    private final String displayName;
    private final Material icon;

    LastRequestType(String displayName, Material icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getIcon() {
        return icon;
    }
}
