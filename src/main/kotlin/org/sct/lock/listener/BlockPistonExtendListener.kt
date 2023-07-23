package org.sct.lock.listener

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPistonExtendEvent
//import org.sct.easylib.util.BasicUtil
import org.sct.lock.Lock
import org.sct.lock.enumeration.ConfigType
import org.sct.lock.enumeration.LangType
import org.sct.lock.file.Config
import org.sct.lock.util.function.LocationUtil
import org.sct.lock.util.player.CheckUtil
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.sendLang

/**
 * @author LovesAsuna
 * @date 2020/10/19 23:14
 **/
object BlockPistonExtendListener {
    @SubscribeEvent
    fun onBlockPistonExtend(e: BlockPistonExtendEvent) {
        var door: Block? = null
        for (doorType in Config.getStringList(ConfigType.SETTING_DOORTYPE.path)) {
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
                val list = LocationUtil.getNearbyEntities(e.block.location, 10)
                list.filterIsInstance<Player>().forEach {
                    it.sendLang("DenyBreak")
                   // it.sendMessage(BasicUtil.convert(Lang.getString(LangType.LANG_DENYBREAK.path)))
                }
            }
        }

    }
}