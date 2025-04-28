package com.thejailbreakshow;

import com.thejailbreakshow.cells.CellButtonSaveManager;
import com.thejailbreakshow.regions.RegionSaveManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TheJailBreakShow extends JavaPlugin {

    private static TheJailBreakShow instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("The JailBreak Show plugin enabled.");

        ManagerLoader.init(this);
        RegionSaveManager.init();
        RegionSaveManager.loadAllRegions();
        CellButtonSaveManager.init();
    }

    @Override
    public void onDisable() {
        RegionSaveManager.saveAllRegions();
        getLogger().info("The JailBreak Show plugin disabled.");
    }

    public static TheJailBreakShow getInstance() {
        return instance;
    }
}
