package com.thejailbreakshow;

import com.thejailbreakshow.cells.CellButtonSaveManager;
import com.thejailbreakshow.regions.RegionSaveManager;
import com.thejailbreakshow.regions.WeaponRoomManager;
import com.thejailbreakshow.rules.RulesManager;
import com.thejailbreakshow.weapons.WeaponManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TheJailBreakShow extends JavaPlugin {

    private static TheJailBreakShow instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("The JailBreak Show plugin enabled.");

        // Initialize managers
        ManagerLoader.init(this);
        RegionSaveManager.init();
        RegionSaveManager.loadAllRegions();
        CellButtonSaveManager.init();
        
        // Initialize new components
        RulesManager.init();
        WeaponRoomManager.init();
        WeaponManager.init();
        
        // Register event listeners
        getServer().getPluginManager().registerEvents(new WeaponRoomManager(), this);
        getServer().getPluginManager().registerEvents(new WeaponManager(), this);
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
