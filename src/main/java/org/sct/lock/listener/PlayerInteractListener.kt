package org.sct.lock.listener

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.sct.lock.Lock
import org.sct.lock.data.LockData
import org.sct.lock.enumeration.ConfigType
import org.sct.lock.enumeration.LangType
import org.sct.lock.event.PlayerAccessLockDoorEvent
import org.sct.lock.file.Config
import org.sct.lock.file.Lang
import org.sct.lock.util.function.InteractInhit
import org.sct.lock.util.function.LockUtil
import org.sct.lock.util.function.SIgnProcessUtil
import org.sct.lock.util.player.CheckUtil
import org.sct.lock.util.player.TeleportAPI
import org.sct.lock.util.player.TeleportAPI.status
import org.sct.plugincore.util.BasicUtil
import org.sct.plugincore.util.function.Inhibition
import java.util.concurrent.TimeUnit

/**
 * @author LovesAsuna
 * @since 2019/12/4 23:01
 */
class PlayerInteractListener : Listener {
    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        val player = e.player
        if (!InteractInhit.getInhibitStatus(player, 50)) {
            return
        }
        val doorList = Config.getStringList(ConfigType.SETTING_DOORTYPE)
        if (LockUtil.addStatus(e)) {
            return
        }


        /*如果玩家右键方块*/
        if (e.action == Action.RIGHT_CLICK_BLOCK) {

            /*如果玩家手持物品*/
            if (e.hasItem() && e.item.type.name.contains("SIGN")) {
                LockUtil.setLocation(e)
            }
            for (door in doorList) {
                // 如果玩家正在潜行
                if (LockData.PlayerisSneak?.get(player) == null || !LockData.PlayerisSneak?.get(player)!!) {
                    return
                }
                if (e.clickedBlock.location.block.type == Material.getMaterial(door)) {

                    /*如果门的上方有自动收费门的牌子,在CheckUtil内存入牌子和方块的位置*/
                    if (CheckUtil.CheckSign(player, e.clickedBlock)) {
                        val teleportAPI = TeleportAPI()

                        /*设置状态数据*/
                        teleportAPI.getData(player)

                        /*如果执行传送并返回进出状态，以此来进行扣费操作*/
                        val s = teleportAPI.getPlayerFace(player)
                        if (s == status.LEAVE) {
                            if (!InteractInhit.getInhibitStatus(player.name + "temp", 5)) {
                                return
                            }
                            callEvent(player, teleportAPI)
                            return
                        }

                        /*事件抑制确认*/
                        val orignDelay = Config.getInteger(ConfigType.SETTING_ENTERDELAY)
                        val delay = (orignDelay.toDouble() / 50).toLong()
                        Inhibition.getInhibitStatus(player, Config.getInteger(ConfigType.SETTING_ENTERDELAY), TimeUnit.MILLISECONDS)
                        val inhit = Inhibition.getInhibitStatus(player, Config.getInteger(ConfigType.SETTING_ENTERDELAY), TimeUnit.MILLISECONDS)
                        Bukkit.getScheduler().runTaskLaterAsynchronously(Lock.getInstance(), { LockData.ensure?.set(player, false) }, delay)
                        LockData.ensure?.putIfAbsent(player, false)
                        val ensure = LockData.ensure?.get(player)!!
                        if (!inhit && !ensure) {
                            /*提示*/
                            showDoorDetail(e, delay / 20)
                            LockData.ensure?.set(player, true)
                            return
                        }
                        if (!inhit && ensure) {
                            if (!InteractInhit.getInhibitStatus(player.name + "temp", 10)) {
                                return
                            }
                            callEvent(player, teleportAPI)
                        }
                    }
                }
            }
        }
    }

    private fun showDoorDetail(e: PlayerInteractEvent, delay: Long) {
        val sign = LockData.PlayerSign?.get(e.player)
        val player = e.player
        val conditions = SIgnProcessUtil.getHoverTextAPI().getText(sign!!.location)
        val details = BasicUtil.convert(Lang.getStringList(LangType.LANG_DoorDetail))
        for (detail in details) {
            var editDetail = detail
            editDetail = BasicUtil.replace(editDetail, "%conditons%", conditions)
            editDetail = BasicUtil.replace(editDetail, "%second%", delay.toString())
            player.sendMessage(editDetail)
        }
    }

    private fun callEvent(player: Player, teleportAPI: TeleportAPI) {
        Bukkit.getPluginManager().callEvent(PlayerAccessLockDoorEvent(player,
                LockUtil.getOwner(LockData.PlayerSign?.get(player)),
                teleportAPI,
                LockData.PlayerSign?.get(player)))
    }
}