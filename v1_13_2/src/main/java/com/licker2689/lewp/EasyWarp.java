package com.licker2689.lewp;

import com.licker2689.lewp.commands.LEWPCommand;
import com.licker2689.lewp.functions.LEWPFunction;
import com.licker2689.lpc.utils.ColorUtils;
import com.licker2689.lpc.utils.ConfigUtils;
import com.licker2689.lpc.utils.Triple;
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
        LEWPFunction.loadAllWarps();
        getCommand("워프").setExecutor(new LEWPCommand());
    }

    @Override
    public void onDisable() {
        LEWPFunction.saveConfig();
    }
}
