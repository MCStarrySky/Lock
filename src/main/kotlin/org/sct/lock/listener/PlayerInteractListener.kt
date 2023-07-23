package org.sct.lock.listener

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType
import org.sct.lock.util.BasicUtil
import org.sct.lock.util.function.Inhibition
import org.sct.lock.Lock
import org.sct.lock.LockAPI
import org.sct.lock.data.LockData
import org.sct.lock.enumeration.ConfigType
import org.sct.lock.enumeration.Direction
import org.sct.lock.enumeration.LangType
import org.sct.lock.event.PlayerAccessLockDoorEvent
import org.sct.lock.file.Config
import org.sct.lock.parseToString
import org.sct.lock.toRoundedLocation
import org.sct.lock.util.function.InteractInhibit
import org.sct.lock.util.function.LockUtil
import org.sct.lock.util.function.SignProcessUtil
import org.sct.lock.util.player.CheckUtil
import org.sct.lock.util.player.TeleportHandler
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submitAsync
import taboolib.module.chat.uncolored
import taboolib.platform.util.asLangTextList
import taboolib.platform.util.bukkitPlugin
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author LovesAsuna
 * @date 2019/12/4 23:01
 */
object PlayerInteractListener {
    @SubscribeEvent
    fun onPlayerInteract(e: PlayerInteractEvent) {
        val player = e.player
        // 处理bukkit自身的神奇bug(交互事件触发连续触发两次)
        if (!InteractInhibit.getInhibitStatus(player, 50)) {
            return
        }
        // 获取可用门类型
        val doorList = Config.getStringList(ConfigType.SETTING_DOORTYPE.path)
        // 交互时是否是在添加门类型
        if (LockUtil.addStatus(e)) {
            return
        }

        // 如果玩家右键方块
        if (e.action == Action.RIGHT_CLICK_BLOCK) {

            // 如果玩家手持物品并且物品名中包含"SIGN"关键词
            if (e.hasItem() && e.item!!.type.name.contains("SIGN", true)) {
                // 此时玩家可视为正在安放牌子
                LockUtil.setLocation(e)
            }
            for (door in doorList) {
                // 如果玩家不在潜行
                if (LockData.PlayerisSneak[player] == null) {
                    return
                }
                val clickedBlockType = e.clickedBlock?.location?.block?.type
                if (clickedBlockType == Material.getMaterial(door)) {

                    // 如果门的上方有自动收费门的牌子,在CheckUtil内存入牌子和方块的位置
                    if (CheckUtil.checkSign(player, e.clickedBlock)) {
                        /*设置状态数据*/
                        val teleportAPI = TeleportHandler()
                        teleportAPI.setData(player)

                        /*如果执行传送并返回进出状态，以此来进行扣费操作*/
                        val direction = teleportAPI.getPlayerDirection(player)
                        if (direction == Direction.LEAVE) {
                            if (!Inhibition.getInhibitStatus(5, TimeUnit.MILLISECONDS)) {
                                return
                            }
                            callEvent(player, teleportAPI)
                            return
                        }

                        /*事件抑制确认*/
                        val originDelay = Config.getInt(ConfigType.SETTING_ENTERDELAY.path)
                        val delay = (originDelay.toDouble() / 50).toLong()
                        Inhibition.getInhibitStatus(player, Config.getInt(ConfigType.SETTING_ENTERDELAY.path), TimeUnit.MILLISECONDS)
                        val inhibit = Inhibition.getInhibitStatus(player, Config.getInt(ConfigType.SETTING_ENTERDELAY.path), TimeUnit.MILLISECONDS)
                  //      Bukkit.getScheduler().runTaskLaterAsynchronously(bukkitPlugin, { LockData.ensure[player] = false }, delay)
                        submitAsync(delay = delay) {
                            LockData.ensure[player] = false
                        }
                        LockData.ensure.putIfAbsent(player, false)
                        val ensure = LockData.ensure[player]!!
                        if (!inhibit && !ensure) {
                            /*提示*/
                            showDoorDetail(e, delay / 20)
                            LockData.ensure[player] = true
                            return
                        }
                        if (!inhibit && ensure) {
                            if (!InteractInhibit.getInhibitStatus(player.name + "temp", 10)) {
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
        val signLocation = LockData.PlayerSignLocation[e.player]
        val player = e.player
        val pdc =
                ((signLocation?.chunk) ?: return).persistentDataContainer.getOrDefault(LockAPI.LOCK, PersistentDataType.TAG_CONTAINER_ARRAY, emptyArray())
                        .firstOrNull { it.getOrDefault(LockAPI.LOCATION, PersistentDataType.STRING, "") == signLocation.toRoundedLocation().parseToString() }
                        ?: return
        val conditions = pdc.getOrDefault(LockAPI.LOCKCONDITION, PersistentDataType.STRING, "")
       // val details = BasicUtil.convert(Lang.getStringList(LangType.LANG_DoorDetail.path))
        val details = e.player.asLangTextList("DoorDetail")
        for (detail in details) {
            var editDetail = detail
            editDetail = BasicUtil.replace(editDetail, "{conditons}", conditions)
            editDetail = BasicUtil.replace(editDetail, "{second}", delay.toString())
            player.sendMessage(editDetail)
        }
        // 对正版玩家用户名刷新
        val sign = (signLocation.block.state as? Sign) ?: return
        val offlineUser = Bukkit.getOfflinePlayer(UUID.fromString(pdc.get(LockAPI.LOCKUSER, PersistentDataType.STRING) ?: return))
        val recordUsername = sign.getLine(3).uncolored()
        if (offlineUser.name != recordUsername) {
            sign.setLine(3, offlineUser.name ?: return)
        }
    }

    private fun callEvent(player: Player, teleportHandler: TeleportHandler) {
        val sign = LockData.PlayerSignLocation[player]?.block
        Bukkit.getPluginManager().callEvent(PlayerAccessLockDoorEvent(player,
                LockUtil.getOwner(sign),
                teleportHandler,
                sign))
    }
}