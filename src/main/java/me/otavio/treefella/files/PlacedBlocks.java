package me.otavio.treefella.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlacedBlocks {

    private static File file;
    private static FileConfiguration customFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("TreeFella").getDataFolder(), "PlacedBlocks.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().warning("TreeFella: could not create PlacedBlocks.yml: " + e.getMessage());
            }
        }

        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static void migrateKeys(String defaultWorldName) {
        boolean changed = false;
        for (String key : customFile.getKeys(false)) {
            if (key.split(",").length == 3) {
                int value = customFile.getInt(key);
                customFile.set(key, null);
                customFile.set(defaultWorldName + "," + key, value);
                changed = true;
            }
        }
        if (changed) save();
    }

    public static FileConfiguration get() {
        return customFile;
    }

    public static void save() {
        try {
            customFile.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().warning("TreeFella: could not save PlacedBlocks.yml");
        }
    }

    public static void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }
}
