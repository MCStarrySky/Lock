package org.sct.lock.listener

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPistonExtendEvent
import org.sct.lock.Lock
import org.sct.lock.enumeration.ConfigType
import org.sct.lock.util.player.CheckUtil

/**
 * @author LovesAsuna
 * @date 2020/10/19 23:14
 **/
class BlockPistonExtendListener : Listener {
    @EventHandler
    fun onBlockPistonExtend(e: BlockPistonExtendEvent) {
        var door: Block? = null
        for (doorType in Lock.instance.config.getStringList(ConfigType.SETTING_DOORTYPE.path)) {
            e.blocks.stream().filter {
                it.type.name.equals(doorType, ignoreCase = true) ||
                        it.getRelative(BlockFace.UP).type.name.equals(doorType, ignoreCase = true)
            }.findFirst().ifPresent {
                door = it
            }
        }
        if (door != null) {
            if (!door!!.type.name.contains("DOOR")) {
                door = door!!.getRelative(BlockFace.UP)
            }

            if (CheckUtil.checkSign(null, door)) {
                e.isCancelled = true
                TODO("缺少提示")
            }
        }


    }
}