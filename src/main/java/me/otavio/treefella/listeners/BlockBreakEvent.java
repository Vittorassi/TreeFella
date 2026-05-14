package me.otavio.treefella.listeners;

import me.otavio.treefella.TreeFella;
import me.otavio.treefella.files.PlacedBlocks;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BlockBreakEvent implements Listener {

    @EventHandler
    public void onBlockBreakEvent(org.bukkit.event.block.BlockBreakEvent e) {
        Player p = e.getPlayer();
        ItemStack itemInHand = p.getInventory().getItemInMainHand();
        Block b = e.getBlock();

        Location loc = b.getLocation();
        String path = b.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();

        boolean isPlayerPlacedBlock = PlacedBlocks.get().isInt(path);

        if (
            (
                (TreeFella.AXES.contains(itemInHand.getType()) && TreeFella.LOGS.contains(b.getType())) ||
                (TreeFella.PICKAXES.contains(itemInHand.getType()) && TreeFella.ORES.contains(b.getType()))
            ) && !isPlayerPlacedBlock && p.isSneaking()
        ) {
            checkNearbyBlocks(b, p);

        } else if (isPlayerPlacedBlock &&
                   (TreeFella.LOGS.contains(b.getType()) || TreeFella.ORES.contains(b.getType()))) {
            PlacedBlocks.get().set(path, null);
            PlacedBlocks.save();
        }
    }

    private void checkNearbyBlocks(Block origin, Player player) {
        Queue<Block> queue = new LinkedList<>();
        Set<Block> visited = new HashSet<>();
        queue.add(origin);
        visited.add(origin);

        while (!queue.isEmpty()) {
            Block block = queue.poll();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) continue;
                        Block nearby = block.getRelative(dx, dy, dz);
                        if (visited.contains(nearby)) continue;
                        if (!TreeFella.LOGS.contains(nearby.getType()) && !TreeFella.ORES.contains(nearby.getType())) continue;
                        visited.add(nearby);
                        ItemStack tool = player.getInventory().getItemInMainHand();
                        nearby.breakNaturally(tool);
                        if (!damageTool(player, tool)) return;
                        queue.add(nearby);
                    }
                }
            }
        }
    }

    private boolean damageTool(Player player, ItemStack tool) {
        int maxDurability = tool.getType().getMaxDurability();
        ItemMeta meta = tool.getItemMeta();
        if (!(meta instanceof Damageable dmg)) return true;
        if (dmg.getDamage() < maxDurability) {
            dmg.setDamage(dmg.getDamage() + 1);
            tool.setItemMeta(dmg);
            player.getInventory().setItemInMainHand(tool);
            return true;
        } else {
            player.getInventory().setItemInMainHand(null);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 1F);
            return false;
        }
    }
}
