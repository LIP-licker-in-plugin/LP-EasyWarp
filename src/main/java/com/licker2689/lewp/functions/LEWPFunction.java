package com.licker2689.lewp.functions;

import com.licker2689.lewp.EasyWarp;
import com.licker2689.lpc.utils.ColorUtils;
import com.licker2689.lpc.utils.ConfigUtils;
import com.licker2689.lpc.utils.Triple;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@SuppressWarnings("all")
public class LEWPFunction {
    private static final EasyWarp plugin = EasyWarp.getInstance();

    public static void createWarp(Player p, String name) {
        if (plugin.warps.containsKey(name)) {
            p.sendMessage(plugin.prefix + "이미 존재하는 워프입니다.");
            return;
        }
        plugin.warps.put(name, new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ()));
        p.sendMessage(plugin.prefix + "워프를 생성했습니다.");
        plugin.config.set("Settings.warps." + name + ".world", p.getWorld().getName());
        plugin.config.set("Settings.warps." + name + ".x", p.getLocation().getBlockX());
        plugin.config.set("Settings.warps." + name + ".y", p.getLocation().getBlockY());
        plugin.config.set("Settings.warps." + name + ".z", p.getLocation().getBlockZ());
    }

    public static void linkWarp(Player p, String from, String to) {
        if (!plugin.warps.containsKey(from) || !plugin.warps.containsKey(to)) {
            p.sendMessage(plugin.prefix + "존재하지 않는 워프입니다.");
            return;
        }
        plugin.linked.put(from, Triple.of(plugin.warps.get(from), plugin.warps.get(to), to));
        p.sendMessage(plugin.prefix + "워프를 연결했습니다.");
        plugin.config.set("Settings.linked." + from + ".from", from);
        plugin.config.set("Settings.linked." + from + ".to", to);
    }

    public static void unLink(Player p, String name) {
        if (!plugin.linked.containsKey(name)) {
            p.sendMessage(plugin.prefix + "존재하지 않는 워프입니다.");
            return;
        }
        plugin.linked.remove(name);
        p.sendMessage(plugin.prefix + "워프를 연결 해제했습니다.");
        plugin.config.set("Settings.linked." + name, null);
    }

    public static void deleteWarp(Player p, String name) {
        if (!plugin.warps.containsKey(name)) {
            p.sendMessage(plugin.prefix + "존재하지 않는 워프입니다.");
            return;
        }
        plugin.warps.remove(name);
        p.sendMessage(plugin.prefix + "워프를 삭제했습니다.");
        plugin.config.set("Settings.warps." + name, null);
        plugin.config.set("Settings.linked." + name, null);
        unLink(p, name);
    }

    public static void teleportToWarp(Player p, String name) {
        if (!plugin.warps.containsKey(name)) {
            p.sendMessage(plugin.prefix + "존재하지 않는 워프입니다.");
            return;
        }
        p.teleport(plugin.warps.get(name));
        p.sendMessage(plugin.prefix + "워프로 이동했습니다.");
    }

    public static void saveConfig() {
        ConfigUtils.savePluginConfig(plugin, plugin.config);
    }

    public static void loadAllWarps() {
        plugin.warps.clear();
        plugin.linked.clear();
        if (plugin.config.getConfigurationSection("Settings.warps") != null) {
            for (String name : plugin.config.getConfigurationSection("Settings.warps").getKeys(false)) {
                Location loc = new Location(plugin.getServer().getWorld(plugin.config.getString("Settings.warps." + name + ".world")), plugin.config.getInt("Settings.warps." + name + ".x"), plugin.config.getInt("Settings.warps." + name + ".y"), plugin.config.getInt("Settings.warps." + name + ".z"));
                plugin.warps.put(name, loc);
            }
        }
        if (plugin.config.getConfigurationSection("Settings.linked") != null) {
            for (String name : plugin.config.getConfigurationSection("Settings.linked").getKeys(false)) {
                plugin.linked.put(name, Triple.of(plugin.warps.get(plugin.config.getString("Settings.linked." + name + ".from")), plugin.warps.get(plugin.config.getString("Settings.linked." + name + ".to")), plugin.config.getString("Settings.linked." + name + ".to")));
            }
        }
        initTask();
    }

    public static void initTask() {
        if (plugin.warpTask != null) {
            plugin.warpTask.cancel();
        }
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    for (Triple<Location, Location, String> tpl : plugin.linked.values()) {
                        if (tpl.getA().getBlockX() == p.getLocation().getBlockX() && tpl.getA().getBlockY() == p.getLocation().getBlockY() && tpl.getA().getBlockZ() == p.getLocation().getBlockZ()) {
                            if (tpl.getA().getWorld().getName().equals(p.getWorld().getName())) {
                                p.teleport(tpl.getB());
                            }
                        }
                    }
                });
            }
        }, 0L, 5L);
        plugin.warpTask = task;
    }

    public static void listWarp(Player p) {
        p.sendMessage(plugin.prefix + "워프 목록입니다.");
        for (String name : plugin.warps.keySet()) {
            if (plugin.linked.containsKey(name)) {
                p.sendMessage(plugin.prefix + "링크됨 : " + name + " to " + plugin.linked.get(name).getC());
            }else{
                p.sendMessage(plugin.prefix + "링크되지 않음 : " + name);
            }
        }
    }

    public static void reloadConfig() {
        plugin.config = ConfigUtils.reloadPluginConfig(plugin, plugin.config);
        plugin.prefix = ColorUtils.applyColor(plugin.config.getString("Settings.prefix"));
        loadAllWarps();
        initTask();
    }
}
