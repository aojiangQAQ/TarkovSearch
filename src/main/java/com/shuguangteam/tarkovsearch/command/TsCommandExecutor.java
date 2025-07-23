package com.shuguangteam.tarkovsearch.command;

import com.shuguangteam.tarkovsearch.TarkovSearch;
import com.shuguangteam.tarkovsearch.gui.SettingGUI;
import com.shuguangteam.tarkovsearch.manager.BoxManager;
import com.shuguangteam.tarkovsearch.util.Lang;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TsCommandExecutor implements CommandExecutor, TabCompleter {

    private final TarkovSearch plugin;
    private final BoxManager   boxManager;
    private final Lang         lang;
    private static final List<String> SUBS = Arrays.asList("create","edit","delete","reload");

    public TsCommandExecutor(TarkovSearch plugin) {
        this.plugin    = plugin;
        this.boxManager= plugin.getBoxManager();
        this.lang      = plugin.getLang();
    }

    /* ------------------ 指令执行 ------------------ */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only.");
            return true;
        }
        if (args.length == 0) { showHelp(p); return true; }

        switch (args[0].toLowerCase()) {
            case "create" -> doCreate(p);
            case "edit"   -> doEdit(p);
            case "delete" -> doDelete(p);
            case "reload" -> { plugin.reloadConfig(); lang.reload(); p.sendMessage(lang.msg("reload-success")); }
            default       -> showHelp(p);
        }
        return true;
    }

    /* ------------------ Tab 补全 ------------------ */
    @Override public List<String> onTabComplete(CommandSender s, Command c, String a, String[] args){
        return args.length==1 ? StringUtil.copyPartialMatches(args[0], SUBS, new ArrayList<>()) : List.of();
    }

    /* ------------------ 子命令实现 ------------------ */
    private void doCreate(Player p) {
        Block tgt = p.getTargetBlockExact(6);
        if (!(tgt != null && tgt.getState() instanceof Chest)) { p.sendMessage(lang.msg("not-looking-at-chest")); return; }
        Location loc = tgt.getLocation();
        if (boxManager.isBox(loc)) { p.sendMessage(lang.msg("box-exist")); return; }
        var bd = boxManager.createBox(loc);
        p.sendMessage(lang.msg("create-success"));
        SettingGUI.open(p, bd);
    }
    private void doEdit(Player p) {
        Block tgt = p.getTargetBlockExact(6);
        if (!(tgt != null && tgt.getState() instanceof Chest)) { p.sendMessage(lang.msg("not-looking-at-chest")); return; }
        Location loc = tgt.getLocation();
        if (!boxManager.isBox(loc)) { p.sendMessage(lang.msg("box-not-exist")); return; }
        p.sendMessage(lang.msg("edit-open"));
        SettingGUI.open(p, boxManager.getBox(loc));
    }
    private void doDelete(Player p) {
        Block tgt = p.getTargetBlockExact(6);
        if (!(tgt != null && tgt.getState() instanceof Chest)) { p.sendMessage(lang.msg("not-looking-at-chest")); return; }
        Location loc = tgt.getLocation();
        if (!boxManager.isBox(loc)) { p.sendMessage(lang.msg("box-not-exist")); return; }
        boxManager.deleteBox(loc);
        p.sendMessage(lang.msg("delete-success"));
    }

    /* ------------------ 美观帮助 ------------------ */
    private void showHelp(Player p) {
        String pre = lang.get("prefix");
        p.sendMessage(pre + "§6======== §eTarkovSearch 指令 §6========");
        p.sendMessage(pre + "§e/ts create §7- 创建物资箱 (准星对准箱子)");
        p.sendMessage(pre + "§e/ts edit   §7- 编辑物资箱");
        p.sendMessage(pre + "§e/ts delete §7- 删除物资箱");
        p.sendMessage(pre + "§e/ts reload §7- 重载配置/语言");
        p.sendMessage(pre + "§6================================");
    }
}
