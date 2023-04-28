package com.licker2689.lewp.commands;

import com.licker2689.lewp.EasyWarp;
import com.licker2689.lewp.functions.LEWPFunction;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class LEWPCommand implements CommandExecutor, TabCompleter {
    private final EasyWarp plugin = EasyWarp.getInstance();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!sender.isOp()) {
            sender.sendMessage(plugin.prefix + "권한이 없습니다.");
            return false;
        }
        if(!(sender instanceof Player)) {
           sender.sendMessage(plugin.prefix + "플레이어만 사용 가능합니다.");
           return false;
        }
        Player p = (Player) sender;
        if(args.length == 0) {
            p.sendMessage(plugin.prefix + "/워프 생성 <이름> - 해당 위치에 워프를 생성합니다.");
            p.sendMessage(plugin.prefix + "/워프 링크 <FROM> <TO> - 워프를 링크합니다.");
            p.sendMessage(plugin.prefix + "/워프 삭제 <이름> - 해당 워프를 삭제합니다.");
            p.sendMessage(plugin.prefix + "/워프 목록 - 워프 목록을 확인합니다.");
            p.sendMessage(plugin.prefix + "/워프 티피 <이름> - 해당 워프로 티피합니다.");
            p.sendMessage(plugin.prefix + "/워프 리로드 - 콘피그 설정을 리로드 합니다.");
            return false;
        }
        if(args[0].equalsIgnoreCase("생성")) {
            if(args.length == 1) {
                p.sendMessage(plugin.prefix + "생성할 워프의 이름을 입력해주세요.");
                return false;
            }
            if(args.length == 2) {
                LEWPFunction.createWarp(p, args[1]);
                return false;
            }
        }
        if(args[0].equalsIgnoreCase("링크")) {
            if(args.length == 1) {
                p.sendMessage(plugin.prefix + "링크할 워프의 FROM 이름을 입력해주세요.");
                return false;
            }
            if(args.length == 2) {
                p.sendMessage(plugin.prefix + "링크할 워프의 TO 이름을 입력해주세요.");
                return false;
            }
            if(args.length == 3) {
                LEWPFunction.linkWarp(p, args[1], args[2]);
                return false;
            }
        }
        if(args[0].equalsIgnoreCase("삭제")) {
            if(args.length == 1) {
                p.sendMessage(plugin.prefix + "삭제할 워프의 이름을 입력해주세요.");
                return false;
            }
            if(args.length == 2) {
                LEWPFunction.deleteWarp(p, args[1]);
                return false;
            }
        }
        if(args[0].equalsIgnoreCase("목록")) {
            LEWPFunction.listWarp(p);
            return false;
        }
        if(args[0].equalsIgnoreCase("티피")) {
            if(args.length == 1) {
                p.sendMessage(plugin.prefix + "티피할 워프의 이름을 입력해주세요.");
                return false;
            }
            if(args.length == 2) {
                LEWPFunction.teleportToWarp(p, args[1]);
                return false;
            }
        }
        if(args[0].equalsIgnoreCase("리로드")) {
            LEWPFunction.reloadConfig();
            p.sendMessage(plugin.prefix + "설정을 리로드 하였습니다.");
            return false;
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender.isOp()) {
            if(args.length == 1) {
                return Arrays.asList("생성", "링크", "삭제", "목록", "티피", "리로드");
            }
            if(args.length == 2) {
                if(!args[0].equals("목록") || !args[0].equals("리로드")) {
                    return plugin.warps.keySet().stream().collect(Collectors.toList());
                }
            }
        }
        return null;
    }
}
