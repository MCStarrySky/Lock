package org.sct.lock.listener

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent
import org.sct.lock.data.LockData
import org.sct.lock.enumeration.ConfigType
import org.sct.lock.file.Config
import org.sct.lock.util.function.SignProcessUtil
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author LovesAsuna
 * @since 2019/12/4 23:03
 */
object SignChangeListener {
    @SubscribeEvent
    fun onSignChange(e: SignChangeEvent) {
        val location = e.block.location
        var cancel = true
        if (LockData.PlayerDoorLocation[e.player] == null) {
            return
        }
        for (doors in Config.getStringList(ConfigType.SETTING_DOORTYPE.path)) {
            if (LockData.PlayerDoorLocation[e.player]!!.block.type == Material.getMaterial(doors)) {
                cancel = false
                break
            }
        }
        if (cancel) {
            return
        }
        if (e.player == LockData.PlayerSignLocation.inverse()?.get(location)) {
            SignProcessUtil.processSign(e)
        }
    }
}