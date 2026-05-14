package me.otavio.treefella.listeners;

import me.otavio.treefella.TreeFella;
import me.otavio.treefella.files.PlacedBlocks;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockPlaceEvent implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(org.bukkit.event.block.BlockPlaceEvent e) {
        Block block = e.getBlock();
        if (TreeFella.LOGS.contains(block.getType()) || TreeFella.ORES.contains(block.getType())) {
            Location loc = block.getLocation();
            String path = block.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
            PlacedBlocks.get().set(path, 1);
            PlacedBlocks.save();
        }
    }
}
