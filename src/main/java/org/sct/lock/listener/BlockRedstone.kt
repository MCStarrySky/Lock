package org.sct.lock.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockRedstoneEvent
import org.sct.lock.Lock
import org.sct.lock.enumeration.ConfigType
import org.sct.lock.util.player.CheckUtil

class BlockRedstone : Listener {
    @EventHandler
    fun onBlockRedstone(e: BlockRedstoneEvent) {
        var isDoor = false
        if (!Lock.getInstance().config.getBoolean(ConfigType.SETTING_BANREDSTONEACTIVE.path)) {
            return
        }
        for (door in Lock.getInstance().config.getStringList(ConfigType.SETTING_DOORTYPE.path)) {
            if (e.block.type.toString().equals(door, ignoreCase = true)) {
                isDoor = true
            }
        }
        if (!isDoor) {
            return
        }
        if (CheckUtil.CheckSign(null, e.block)) {
            e.newCurrent = e.oldCurrent
        }
    }
}