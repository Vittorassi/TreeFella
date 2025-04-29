package me.otavio.treefella;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import me.otavio.treefella.files.PlacedBlocks;
import me.otavio.treefella.listeners.BlockBreakEvent;
import me.otavio.treefella.listeners.BlockPlaceEvent;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.ExperienceOrb;
import java.util.Objects;

public final class TreeFella extends JavaPlugin {

    public static final ImmutableList<Material> LOGS = ImmutableList.of(
            Material.ACACIA_LOG,
            Material.BIRCH_LOG,
            Material.JUNGLE_LOG,
            Material.OAK_LOG,
            Material.MANGROVE_LOG,
            Material.SPRUCE_LOG,
            Material.DARK_OAK_LOG,
            Material.CHERRY_LOG,
            Material.WARPED_STEM,
            Material.CRIMSON_STEM,
            Material.PALE_OAK_LOG
    );

    public static final ImmutableList<Material> ORES = ImmutableList.of(
            Material.COAL_ORE,
            Material.COPPER_ORE,
            Material.IRON_ORE,
            Material.GOLD_ORE,
            Material.EMERALD_ORE,
            Material.DIAMOND_ORE,
            Material.LAPIS_ORE,
            Material.REDSTONE_ORE,
            Material.DEEPSLATE_COAL_ORE,
            Material.DEEPSLATE_COPPER_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.DEEPSLATE_LAPIS_ORE,
            Material.DEEPSLATE_REDSTONE_ORE,
            Material.NETHER_QUARTZ_ORE,
            Material.NETHER_GOLD_ORE,
            Material.ANCIENT_DEBRIS
    );

    private static final ImmutableMap.Builder<Material, OreXP> ORES_XP_raw = ImmutableMap.<Material, OreXP>builder()
            .put(Material.COAL_ORE, new OreXP(0, 2))
            .put(Material.DEEPSLATE_COAL_ORE, new OreXP(0, 2))
            .put(Material.NETHER_GOLD_ORE, new OreXP(0,1))
            .put(Material.DIAMOND_ORE, new OreXP(3,7))
            .put(Material.DEEPSLATE_DIAMOND_ORE, new OreXP(3,7))
            .put(Material.EMERALD_ORE, new OreXP(3,7))
            .put(Material.DEEPSLATE_EMERALD_ORE, new OreXP(3,7))
            .put(Material.LAPIS_ORE, new OreXP(2,5))
            .put(Material.DEEPSLATE_LAPIS_ORE, new OreXP(2,5))
            .put(Material.NETHER_QUARTZ_ORE, new OreXP(2,5))
            .put(Material.REDSTONE_ORE, new OreXP(1,5))
            .put(Material.DEEPSLATE_REDSTONE_ORE, new OreXP(1,5));

    public static final ImmutableMap<Material, OreXP> ORES_XP = ORES_XP_raw.build();

    public static class OreXP {
        private final int minXP;
        private final int maxXP;

        public OreXP(int minXP, int maxXP) {
            this.minXP = minXP;
            this.maxXP = maxXP;
        }

        public int getMinXP() {
            return minXP;
        }

        public int getMaxXP() {
            return maxXP;
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        PlacedBlocks.setup();
        PlacedBlocks.get().options().copyDefaults(true);
        PlacedBlocks.save();

        this.getServer().getPluginManager().registerEvents(new BlockPlaceEvent(), this);
        this.getServer().getPluginManager().registerEvents(new BlockBreakEvent(), this);
    }
}
