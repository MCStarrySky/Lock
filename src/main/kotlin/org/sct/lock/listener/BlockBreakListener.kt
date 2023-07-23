package org.sct.lock.listener

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.persistence.PersistentDataType
import org.sct.lock.LockAPI
import org.sct.lock.data.LockData
import org.sct.lock.enumeration.ConfigType
import org.sct.lock.enumeration.LangType
import org.sct.lock.file.Config
import org.sct.lock.parseToString
import org.sct.lock.util.function.LockUtil
import org.sct.lock.util.player.CheckUtil
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.sendLang
import java.util.UUID

object BlockBreakListener {
    @SubscribeEvent
    fun onBlockBreak(e: BlockBreakEvent) {
        if (e.player.isOp) {
            return
        }
        val block = e.block
        val BlockX = block.location.blockX
        val BlockY = block.location.blockY
        val BlockZ = block.location.blockZ
        val doorAbove = Location(e.player.world, BlockX.toDouble(), (BlockY - 1).toDouble(), BlockZ.toDouble()).block
        val doorBelow = Location(e.player.world, BlockX.toDouble(), (BlockY + 1).toDouble(), BlockZ.toDouble()).block
        if (checkDoor(e, block) || checkDoor(e, doorAbove) || checkDoor(e, doorBelow) || checkSign(e, e.block)) {
            e.isCancelled = true
            e.player.sendLang("DenyBreak")
        } else {
            e.block.chunk.persistentDataContainer.set(
                    LockAPI.LOCK,
                    PersistentDataType.TAG_CONTAINER_ARRAY,
                    e.block.chunk.persistentDataContainer
                            .getOrDefault(LockAPI.LOCK, PersistentDataType.TAG_CONTAINER_ARRAY, emptyArray())
                            .toMutableList()
                            .also { it.removeIf { it.getOrDefault(LockAPI.LOCATION, PersistentDataType.STRING, "") == block.location.parseToString() } }
                            .toTypedArray()
            )
        }
    }

    private fun checkDoor(e: BlockBreakEvent, doorBlock: Block): Boolean {
        val doorList = Config.getStringList(ConfigType.SETTING_DOORTYPE.path)

        /*判断是否符合门类型*/for (door in doorList) {
            /*如果破坏的门符合类型*/
            if (doorBlock.type == Material.getMaterial(door)) {
                /*如果是自动收费门 */
                if (CheckUtil.checkSign(e.player, doorBlock)) {
                    val block = LockData.PlayerSignLocation[e.player]?.block
                    // 防止正版玩家更改用户名导致的不认主问题 by mical
                    val owner = UUID.fromString(((block?.chunk ?: return false)
                            .persistentDataContainer.getOrDefault(LockAPI.LOCK, PersistentDataType.TAG_CONTAINER_ARRAY, emptyArray())
                            .firstOrNull { it.getOrDefault(LockAPI.LOCATION, PersistentDataType.STRING, "") == block.location.parseToString() }
                            ?: return false)
                            .get(LockAPI.LOCKUSER, PersistentDataType.STRING) ?: return false)

                    if (owner != e.player.uniqueId) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun checkSign(e: BlockBreakEvent, signBlock: Block): Boolean {
        /*如果是自动收费门 */
        if (CheckUtil.findSign(e.block)) {
            val owner = LockUtil.getOwner(signBlock)
            if (owner.name != e.player.name) {
                return true
            }
        }
        return false
    }
}