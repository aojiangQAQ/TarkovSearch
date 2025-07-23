package com.shuguangteam.tarkovsearch.listener;

import com.shuguangteam.tarkovsearch.TarkovSearch;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class BlockListener implements Listener {

    private TarkovSearch plugin = TarkovSearch.getInstance();

    public BlockListener(TarkovSearch plugin) {
        this.plugin = plugin;
    }

    private boolean protect(Block b) {
        return plugin.getBoxManager().isBox(b.getLocation());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        if (!e.getPlayer().hasPermission("tarkovsearch.admin.delete") && protect(e.getBlock())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent e) {
        e.blockList().removeIf(this::protect);
    }

    @EventHandler
    public void onPiston(BlockPistonExtendEvent e) {
        e.getBlocks().removeIf(this::protect);
    }
    @EventHandler
    public void onPiston(BlockPistonRetractEvent e) {
        e.getBlocks().removeIf(this::protect);
    }
}
