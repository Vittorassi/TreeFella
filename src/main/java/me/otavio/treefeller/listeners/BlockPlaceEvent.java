package me.otavio.treefeller.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.otavio.treefeller.TreeFeller;
import me.otavio.treefeller.files.PlacedBlocks;

public class BlockPlaceEvent implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(org.bukkit.event.block.BlockPlaceEvent e) {
        Block block = e.getBlock();

        if (TreeFeller.LOGS.contains(block.getType()) || TreeFeller.ORES.contains(block.getType())) {
            int blockX = block.getLocation().getBlockX();
            int blockY = block.getLocation().getBlockY();
            int blockZ = block.getLocation().getBlockZ();

            String path = blockX + "," + blockY + "," + blockZ;

            PlacedBlocks.get().set(path, 1);
            PlacedBlocks.save();
        }
    }
}
