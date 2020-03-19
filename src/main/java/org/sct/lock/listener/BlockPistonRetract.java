package org.sct.lock.listener;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class BlockPistonRetract implements Listener {
    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent e) {
        for (Block block :e.getBlocks()) {

        }
    }
}
