package com.cratesystem.location;

import com.cratesystem.CratePlugin;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Fiziksel kasa konumlarini (blok -> kasa id) locations.yml uzerinden yonetir.
 */
public class LocationManager {

    private final CratePlugin plugin;
    private final File file;
    private final Map<String, String> locations = new HashMap<>();

    public LocationManager(CratePlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "locations.yml");
    }

    private String key(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
    }

    public void load() {
        locations.clear();
        if (!file.exists()) return;
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        for (String k : cfg.getKeys(false)) {
            locations.put(k, cfg.getString(k));
        }
    }

    public void save() {
        YamlConfiguration cfg = new YamlConfiguration();
        for (var e : locations.entrySet()) cfg.set(e.getKey(), e.getValue());
        try {
            cfg.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("locations.yml kaydedilemedi: " + e.getMessage());
        }
    }

    public void addLocation(Location loc, String crateId) {
        locations.put(key(loc), crateId);
        save();
    }

    public void removeLocation(Location loc) {
        locations.remove(key(loc));
        save();
    }

    public String getCrateId(Location loc) {
        return locations.get(key(loc));
    }

    public int count(String crateId) {
        return (int) locations.values().stream().filter(v -> v.equalsIgnoreCase(crateId)).count();
    }
}
