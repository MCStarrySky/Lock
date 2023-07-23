package org.sct.lock.listener

import org.bukkit.block.Block
import org.bukkit.event.block.BlockRedstoneEvent
import org.sct.lock.Lock
import org.sct.lock.enumeration.ConfigType
import org.sct.lock.file.Config
import org.sct.lock.util.player.CheckUtil
import taboolib.common.platform.event.SubscribeEvent

object BlockRedStoneListener {
    @SubscribeEvent
    fun onBlockRedstone(e: BlockRedstoneEvent) {
        var door: Block? = null
        if (!Config.getBoolean(ConfigType.SETTING_BANREDSTONEACTIVE.path)) {
            return
        }
        for (doorType in Config.getStringList(ConfigType.SETTING_DOORTYPE.path)) {
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