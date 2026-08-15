package com.cratesystem;

import com.cratesystem.animation.ChestAnimation;
import com.cratesystem.animation.CSGOAnimation;
import com.cratesystem.animation.CrateAnimation;
import com.cratesystem.animation.RouletteAnimation;
import com.cratesystem.command.CrateCommand;
import com.cratesystem.crate.CrateAnimationType;
import com.cratesystem.crate.CrateManager;
import com.cratesystem.key.KeyManager;
import com.cratesystem.listener.InventoryGuardListener;
import com.cratesystem.listener.PlayerInteractListener;
import com.cratesystem.location.LocationManager;
import com.cratesystem.util.ColorUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class CratePlugin extends JavaPlugin {

    private CrateManager crateManager;
    private KeyManager keyManager;
    private LocationManager locationManager;
    private final Map<CrateAnimationType, CrateAnimation> animations = new EnumMap<>(CrateAnimationType.class);
    private final Set<UUID> animatingPlayers = new HashSet<>();
    private FileConfiguration messages;
    private File messagesFile;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        setupMessages();

        this.crateManager = new CrateManager(this);
        this.keyManager = new KeyManager(this);
        this.locationManager = new LocationManager(this);

        animations.put(CrateAnimationType.CHEST, new ChestAnimation());
        animations.put(CrateAnimationType.CSGO, new CSGOAnimation());
        animations.put(CrateAnimationType.ROULETTE, new RouletteAnimation());

        crateManager.loadCrates();
        locationManager.load();

        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryGuardListener(), this);

        CrateCommand executor = new CrateCommand(this);
        getCommand("crate").setExecutor(executor);
        getCommand("crate").setTabCompleter(executor);

        getLogger().info("CrateSystem basariyla etkinlestirildi.");
    }

    @Override
    public void onDisable() {
        if (locationManager != null) locationManager.save();
        getLogger().info("CrateSystem devre disi birakildi.");
    }

    private void setupMessages() {
        messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) saveResource("messages.yml", false);
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void reloadAll() {
        reloadConfig();
        setupMessages();
        crateManager.loadCrates();
        locationManager.load();
    }

    public String raw(String path) {
        return messages.getString(path, path);
    }

    public void send(CommandSender sender, String path) {
        send(sender, path, Map.of());
    }

    public void send(CommandSender sender, String path, Map<String, String> placeholders) {
        String text = raw(path);
        for (var e : placeholders.entrySet()) text = text.replace(e.getKey(), e.getValue());
        String prefix = raw("prefix");
        sender.sendMessage(ColorUtils.color(prefix + text));
    }

    public CrateManager getCrateManager() { return crateManager; }
    public KeyManager getKeyManager() { return keyManager; }
    public LocationManager getLocationManager() { return locationManager; }
    public Map<CrateAnimationType, CrateAnimation> getAnimations() { return animations; }
    public Set<UUID> getAnimatingPlayers() { return animatingPlayers; }
}
