package com.thejailbreakshow.cells;

import com.thejailbreakshow.regions.Region;
import com.thejailbreakshow.regions.RegionManager;
import com.thejailbreakshow.regions.RegionType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class CellManager {

    public static void openAllCells() {
        List<Region> cellRegions = RegionManager.getAllRegions().stream()
                .filter(region -> region.getType() == RegionType.CELL)
                .toList();

        for (Region cell : cellRegions) {
            // For now just clear doors (you can define better later)
            openRegion(cell);
        }
    }

    private static void openRegion(Region region) {
        int minX = region.getMinX();
        int minY = region.getMinY();
        int minZ = region.getMinZ();
        int maxX = region.getMaxX();
        int maxY = region.getMaxY();
        int maxZ = region.getMaxZ();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = region.getWorld().getBlockAt(x, y, z);
                    if (block.getType() == Material.IRON_DOOR || block.getType() == Material.OAK_DOOR) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }
}
