package org.sct.lock.listener

import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.sct.lock.util.BasicUtil
import org.sct.lock.Lock
import org.sct.lock.enumeration.ConfigType
import org.sct.lock.enumeration.Direction
import org.sct.lock.enumeration.LangType
import org.sct.lock.event.PlayerAccessLockDoorEvent
import org.sct.lock.file.Config
import org.sct.lock.util.function.LockUtil
import org.sct.lock.util.player.InventoryUtil
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.cdouble
import taboolib.common5.cint
import taboolib.platform.compat.depositBalance
import taboolib.platform.compat.getBalance
import taboolib.platform.compat.withdrawBalance
import taboolib.platform.util.sendLang

/**
 * @author alchemy
 * @date 2019-12-09 19:24
 */
object LockDoorAccessListener {
    @SubscribeEvent
    fun onAccess(e: PlayerAccessLockDoorEvent) {
        val sign = e.block.state as Sign
        var charge = BasicUtil.ExtraceInt(sign.getLine(1).trim { it <= ' ' })
        val teleportAPI = e.teleportAPI
        val direction = teleportAPI.getPlayerDirection(e.payer)

        /*收费门指定允许的方向*/
        val accessDirection = LockUtil.getDirection(e.block)
        if (accessDirection != Direction.DOUBLE && e.payer.name != e.owner.name) {
            if (direction != accessDirection) {
                e.payer.sendLang("DenyDirection")
                //e.payer.sendMessage(BasicUtil.convert(Lang.getString(LangType.LANG_DENYDIRECTION.path)))
                return
            }
        }
        val conditions = LockUtil.getConditions(e.block)
        if (direction == Direction.ENTER) {
            if (e.payer.name == e.owner.name) {
                teleportAPI.tp(Direction.ENTER, e.payer)
                return
            }
            if (conditions.isNotEmpty()) {
                if (conditions.contains("1")) {
                    if (!InventoryUtil.isInvEmpty(e.payer)) {
                        e.payer.sendLang("DenyNotEmptyInv")
                    //    e.payer.sendMessage(BasicUtil.convert(Lang.getString(LangType.LANG_DENYNOTEMPTYINV.path)))
                        return
                    }
                }
                if (conditions.contains("2")) {
                    val line = BasicUtil.remove((e.block.state as Sign).getLine(2))
                    val money = BasicUtil.ExtraceInt(line)
                 //   val currentMoney = Lock.easyLibAPI.ecoAPI[e.payer].toInt()
                    val currentMoney = e.payer.getBalance().cint
                    val moneyDetail = LockUtil.getMoneydetail(line, currentMoney, money)
                    val symbol = moneyDetail.keys.iterator().next()
                    val access = moneyDetail[symbol]!!
                    if (!access && !symbol.isEmpty()) {
                        e.payer.sendLang("DenyMoney", symbol + money to "needmoney")
                        //e.payer.sendMessage(BasicUtil.replace(Lang.getString(LangType.LANG_DENYMONEY.path), "%needmoney", symbol + money))
                        return
                    }
                }
                if (conditions.contains("3")) {
                    if (!e.payer.activePotionEffects.isEmpty()) {
                        e.payer.sendLang("DenyHaveEffect")
                     //   e.payer.sendMessage(BasicUtil.convert(Lang.getString(LangType.LANG_DENYHAVEEFFECT.path)))
                        return
                    }
                }
            }

            /*如果余额不足*/
            //if (!Lock.easyLibAPI.ecoAPI.has(e.payer, charge.toDouble())) {
            if (e.payer.getBalance() < charge.cdouble) {
                e.payer.sendLang("NotEnoughMoney")
              //  e.payer.sendMessage(Lang.getString(LangType.LANG_NOTENOUGHMONEY.path))
                return
            }
            teleportAPI.tp(Direction.ENTER, e.payer)
            /*payer付钱部分*/

            /*如果owner是vip或权限未设置*/
            if (!"".equals(Config.getString(ConfigType.SETTING_VIPALLOWED.path), ignoreCase = true) || (LockUtil.getOwner(e.block) as Player).hasPermission(Config.getString(ConfigType.SETTING_VIPALLOWED.path))) {
                //Lock.easyLibAPI.ecoAPI.take(e.payer, charge.toDouble())
                e.payer.withdrawBalance(charge.cdouble)
                LockUtil.getOwner(e.block).depositBalance(charge.cdouble)
               // Lock.easyLibAPI.ecoAPI.give(LockUtil.getOwner(e.block), charge.toDouble())
            } else {
                /*owner不是vip,扣税*/
             //   Lock.easyLibAPI.ecoAPI.take(e.payer, charge.toDouble())
                e.payer.withdrawBalance(charge.cdouble)
                charge *= (1 - Config.getInt(ConfigType.SETTING_TAXPERCENT.path))
              //  Lock.easyLibAPI.ecoAPI.give(LockUtil.getOwner(e.block), charge.toDouble())
                LockUtil.getOwner(e.block).depositBalance(charge.cdouble)
            }
            e.payer.sendLang("Enter", charge to "charge")
           // e.payer.sendMessage(BasicUtil.replace(Lang.getString(LangType.LANG_ENTER.path), "%charge", charge))
        } else {
            teleportAPI.tp(Direction.LEAVE, e.payer)
        }
    }
}