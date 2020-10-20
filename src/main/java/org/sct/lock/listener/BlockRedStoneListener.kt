package org.sct.lock.listener

import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockRedstoneEvent
import org.sct.lock.Lock
import org.sct.lock.enumeration.ConfigType
import org.sct.lock.util.player.CheckUtil

class BlockRedStoneListener : Listener {
    @EventHandler
    fun onBlockRedstone(e: BlockRedstoneEvent) {
        var door: Block? = null
        if (!Lock.instance.config.getBoolean(ConfigType.SETTING_BANREDSTONEACTIVE.path)) {
            return
        }
        for (doorType in Lock.instance.config.getStringList(ConfigType.SETTING_DOORTYPE.path)) {
            if (e.block.type.name.equals(doorType, ignoreCase = true)) {
                door = e.block
            }
        }
        if (door != null) {
            if (CheckUtil.checkSign(null, e.block)) {
                e.newCurrent = e.oldCurrent
            }
        }

    }
}