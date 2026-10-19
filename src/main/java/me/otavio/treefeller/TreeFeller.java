package me.otavio.treefeller;

import com.google.common.collect.ImmutableList;
import me.otavio.treefeller.files.PlacedBlocks;
import me.otavio.treefeller.listeners.BlockBreakEvent;
import me.otavio.treefeller.listeners.BlockPlaceEvent;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public final class TreeFeller extends JavaPlugin {

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
            Material.POPLAR_LOG,
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
