package org.sct.lock.listener

import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.sct.lock.Lock
import org.sct.lock.enumeration.ConfigType
import org.sct.lock.enumeration.LangType
import org.sct.lock.event.PlayerAccessLockDoorEvent
import org.sct.lock.file.Config
import org.sct.lock.file.Lang
import org.sct.lock.util.function.LockUtil
import org.sct.lock.util.player.InventoryUtil
import org.sct.lock.util.player.TeleportAPI.status
import org.sct.plugincore.util.BasicUtil

/**
 * @author alchemy
 * @date 2019-12-09 19:24
 */
class LockDoorAccessListener : Listener {
    @EventHandler
    fun onAccess(e: PlayerAccessLockDoorEvent) {
        val sign = e.block.state as Sign
        var charge = BasicUtil.ExtraceInt(sign.getLine(1).trim { it <= ' ' })
        val teleportAPI = e.teleportAPI
        val s = teleportAPI.getPlayerFace(e.payer)

        /*收费门指定允许的方向*/
        val direction = LockUtil.getDirection(e.block)
        if (direction != status.DOUBLE && e.payer.name != e.owner.name) {
            if (s != direction) {
                e.payer.sendMessage(BasicUtil.convert(Lang.getString(LangType.LANG_DENYDIRECTION)))
                return
            }
        }
        val conditons = LockUtil.getConditions(e.block)
        if (s == status.ENTER) {
            if (e.payer.name == e.owner.name) {
                teleportAPI.Tp(status.ENTER, e.payer)
                return
            }
            if (conditons.isNotEmpty()) {
                if (conditons.contains("1")) {
                    if (!InventoryUtil.isInvEmpty(e.payer)) {
                        e.payer.sendMessage(BasicUtil.convert(Lang.getString(LangType.LANG_DENYNOTEMPTYINV)))
                        return
                    }
                }
                if (conditons.contains("2")) {
                    val line = BasicUtil.remove((e.block.state as Sign).getLine(2))
                    val money = BasicUtil.ExtraceInt(line)
                    val currentMoney = Lock.pluginCoreAPI.ecoAPI[e.payer].toInt()
                    val moneyDetail = LockUtil.getMoneydetail(line, currentMoney, money)
                    val symbol = moneyDetail.keys.iterator().next()
                    val access = moneyDetail[symbol]!!
                    if (!access && !symbol.isEmpty()) {
                        e.payer.sendMessage(BasicUtil.replace(Lang.getString(LangType.LANG_DENYMONEY), "%needmoney", symbol + money))
                        return
                    }
                }
                if (conditons.contains("3")) {
                    if (!e.payer.activePotionEffects.isEmpty()) {
                        e.payer.sendMessage(BasicUtil.convert(Lang.getString(LangType.LANG_DENYHAVEEFFECT)))
                        return
                    }
                }
            }

            /*如果余额不足*/
            if (!Lock.pluginCoreAPI.ecoAPI.has(e.payer, charge.toDouble())) {
                e.payer.sendMessage(Lang.getString(LangType.LANG_NOTENOUGHMONEY))
                return
            }
            teleportAPI.Tp(status.ENTER, e.payer)
            /*payer付钱部分*/

            /*如果owner是vip或权限未设置*/
            if (!"".equals(Config.getString(ConfigType.SETTING_VIPALLOWED), ignoreCase = true) || (LockUtil.getOwner(e.block) as Player).hasPermission(Config.getString(ConfigType.SETTING_VIPALLOWED))) {
                Lock.pluginCoreAPI.ecoAPI.take(e.payer, charge.toDouble())
                Lock.pluginCoreAPI.ecoAPI.give(LockUtil.getOwner(e.block), charge.toDouble())
            } else {
                /*owner不是vip,扣税*/
                Lock.pluginCoreAPI.ecoAPI.take(e.payer, charge.toDouble())
                charge = (1 - Config.getInteger(ConfigType.SETTING_TAXPERCENT)) * charge
                Lock.pluginCoreAPI.ecoAPI.give(LockUtil.getOwner(e.block), charge.toDouble())
            }
            e.payer.sendMessage(BasicUtil.replace(Lang.getString(LangType.LANG_ENTER), "%charge", charge))
        } else {
            teleportAPI.Tp(status.LEAVE, e.payer)
        }
    }
}