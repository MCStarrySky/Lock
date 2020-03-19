package org.sct.lock.listener;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.sct.lock.Lock;
import org.sct.lock.enumeration.ConfigType;
import org.sct.lock.util.player.CheckUtil;

public class BlockRedstone implements Listener {

    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent e) {

        boolean isDoor = false;

        if (!Lock.getInstance().getConfig().getBoolean(ConfigType.SETTING_BANREDSTONEACTIVE.getPath())) {
            return;
        }

        for (String door : Lock.getInstance().getConfig().getStringList(ConfigType.SETTING_DOORTYPE.getPath())) {
            if (e.getBlock().getType().toString().equalsIgnoreCase(door)) {
                isDoor = true;
            }
        }

        if (!isDoor) {
            return;
        }

        if (CheckUtil.CheckSign(null, e.getBlock())) {
            e.setNewCurrent(e.getOldCurrent());
        }

    }
}
