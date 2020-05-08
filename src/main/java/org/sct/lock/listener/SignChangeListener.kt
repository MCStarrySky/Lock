package org.sct.lock.listener

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent
import org.sct.lock.data.LockData
import org.sct.lock.enumeration.ConfigType
import org.sct.lock.file.Config
import org.sct.lock.util.function.SIgnProcessUtil

/**
 * @author LovesAsuna
 * @since 2019/12/4 23:03
 */
class SignChangeListener : Listener {
    @EventHandler
    fun onSignChange(e: SignChangeEvent) {
        val location = e.block.location
        var cancel = true
        if (LockData.PlayerDoorLocation?.get(e.player) == null) {
            return
        }
        for (doors in Config.getStringList(ConfigType.SETTING_DOORTYPE)) {
            if (LockData.PlayerDoorLocation?.get(e.player)!!.block.type == Material.getMaterial(doors)) {
                cancel = false
                break
            }
        }
        if (cancel) {
            return
        }
        if (e.player == LockData.PlayerSignLocation?.inverse()?.get(location)) {
            SIgnProcessUtil.processSign(e)
        }
    }
}