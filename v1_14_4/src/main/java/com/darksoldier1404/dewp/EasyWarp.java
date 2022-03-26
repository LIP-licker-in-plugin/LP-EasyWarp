package com.darksoldier1404.dewp;

import com.darksoldier1404.dewp.commands.DEWPCommand;
import com.darksoldier1404.dewp.functions.DEWPFunction;
import com.darksoldier1404.dppc.utils.ColorUtils;
import com.darksoldier1404.dppc.utils.ConfigUtils;
import com.darksoldier1404.dppc.utils.Triple;
import com.darksoldier1404.dppc.utils.Tuple;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class EasyWarp extends JavaPlugin {
    private static EasyWarp plugin;
    public static YamlConfiguration config;
    public static String prefix;
    public static Map<String, Location> warps = new HashMap<>();
    public static Map<String, Triple<Location, Location, String>> linked = new HashMap<>();
    public static BukkitTask warpTask;

    public static EasyWarp getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        config = ConfigUtils.loadDefaultPluginConfig(plugin);
        prefix = ColorUtils.applyColor(config.getString("Settings.prefix"));
        DEWPFunction.loadAllWarps();
        getCommand("워프").setExecutor(new DEWPCommand());
    }

    @Override
    public void onDisable() {
        DEWPFunction.saveConfig();
    }
}
