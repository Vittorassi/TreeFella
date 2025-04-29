package me.otavio.treefella.listeners;

import com.google.common.collect.ImmutableList;
import me.otavio.treefella.TreeFella;
import me.otavio.treefella.files.PlacedBlocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.Random;

public class BlockBreakEvent implements Listener {

    private final ImmutableList<Material> AXES = ImmutableList.of(
            Material.DIAMOND_AXE,
            Material.GOLDEN_AXE,
            Material.IRON_AXE,
            Material.STONE_AXE,
            Material.NETHERITE_AXE,
            Material.WOODEN_AXE
    );

    private final ImmutableList<Material> PICKAXES = ImmutableList.of(
            Material.DIAMOND_PICKAXE,
            Material.GOLDEN_PICKAXE,
            Material.IRON_PICKAXE,
            Material.STONE_PICKAXE,
            Material.NETHERITE_PICKAXE,
            Material.WOODEN_PICKAXE
    );

    @EventHandler
    public void onBlockBreakEvent(org.bukkit.event.block.BlockBreakEvent e) {
        Player p = e.getPlayer();
        ItemStack itemInHand = p.getInventory().getItemInMainHand();
        Block b = e.getBlock();

        int blockX = b.getLocation().getBlockX();
        int blockY = b.getLocation().getBlockY();
        int blockZ = b.getLocation().getBlockZ();

        String path = blockX + "," + blockY + "," + blockZ;

        boolean isPlayerPlacedBlock = PlacedBlocks.get().isInt(path);

        if (
            (
                (
                    AXES.contains(itemInHand.getType()) && TreeFella.LOGS.contains(b.getType())
                ) || (
                    PICKAXES.contains(itemInHand.getType()) && TreeFella.ORES.contains(b.getType())
                )
            ) && !isPlayerPlacedBlock && p.isSneaking()
        ) {
            this.checkNearbyBlocks(b, p, e);

        } else if (isPlayerPlacedBlock && TreeFella.LOGS.contains(b.getType())) {
            PlacedBlocks.get().set(path, null);
        }
    }

    public void checkNearbyBlocks(Block block, Player player, org.bukkit.event.block.BlockBreakEvent e) {
        int x = -1;
        int y = -1;
        int z = -1;

        outerLoop:
        for (int k = 0; k < 3; k++) {

            for (int i = 0; i < 3; i++) {

                for (int j = 0; j < 3; j++) {
                    Location center = block.getLocation().clone();

                    center.add(x, y, z);

                    Block nearbyBlock = center.getBlock();

                    if (TreeFella.LOGS.contains(nearbyBlock.getType()) || TreeFella.ORES.contains(nearbyBlock.getType())) {
                        ItemStack tool = player.getInventory().getItemInMainHand();

                        if (TreeFella.ORES_XP.containsKey(nearbyBlock.getType())) {
                            int minXP = Objects.requireNonNull(TreeFella.ORES_XP.get(nearbyBlock.getType())).getMinXP();
                            int maxXP = Objects.requireNonNull(TreeFella.ORES_XP.get(nearbyBlock.getType())).getMaxXP();

                            int xp = (int) (Math.random() * (maxXP - minXP + 1)) + minXP;

                            ExperienceOrb orb = e.getPlayer().getWorld().spawn(nearbyBlock.getLocation(), ExperienceOrb.class);
                            orb.setExperience(xp);
                        }
                        
						nearbyBlock.breakNaturally(tool);

                        int toolDurability = tool.getType().getMaxDurability();

                        ItemMeta meta = tool.getItemMeta();

                        Damageable dmg = (Damageable) meta;

                        if (dmg != null && dmg.getDamage() <= toolDurability) {
                            // Apply Unbreaking enchantment
                            int unbreakingLevel = tool.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.DURABILITY);
                            if (unbreakingLevel == 0 || new Random().nextInt(unbreakingLevel + 1) == 0) {
                                dmg.setDamage(dmg.getDamage() + 1);
                            }

                            tool.setItemMeta(dmg);

                            player.getInventory().setItemInMainHand(tool);
                        } else {
                            player.getInventory().setItemInMainHand(null);
                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 1F);
                            break outerLoop;
                        }

                        this.checkNearbyBlocks(nearbyBlock, player, e);

                    }

                    z++;
                }

                x++;
                z = -1;
            }

            y++;
            x = -1;
            z = -1;
        }
    }
}
