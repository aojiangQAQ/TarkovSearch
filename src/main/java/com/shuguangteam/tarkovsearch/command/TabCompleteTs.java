package com.shuguangteam.tarkovsearch.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompleteTs implements TabCompleter {
    private static final List<String> SUBS = Arrays.asList("create", "edit", "delete", "reload");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], SUBS, new ArrayList<>());
        }
        return new ArrayList<>();
    }
}
