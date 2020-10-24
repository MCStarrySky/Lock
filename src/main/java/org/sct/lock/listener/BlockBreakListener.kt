package org.sct.lock.listener

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.sct.easylib.util.BasicUtil
import org.sct.lock.data.LockData
import org.sct.lock.enumeration.ConfigType
import org.sct.lock.enumeration.LangType
import org.sct.lock.file.Config
import org.sct.lock.file.Lang
import org.sct.lock.util.function.LockUtil
import org.sct.lock.util.player.CheckUtil

class BlockBreakListener : Listener {
    @EventHandler
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
            e.player.sendMessage(BasicUtil.convert(Lang.getString(LangType.LANG_DENYBREAK.path)))
        }
    }

    private fun checkDoor(e: BlockBreakEvent, doorBlock: Block): Boolean {
        val doorList = Config.getStringList(ConfigType.SETTING_DOORTYPE.path)

        /*判断是否符合门类型*/for (door in doorList) {
            /*如果破坏的门符合类型*/
            if (doorBlock.type == Material.getMaterial(door)) {
                /*如果是自动收费门 */
                if (CheckUtil.checkSign(e.player, doorBlock)) {
                    val owner = LockUtil.getOwner(LockData.PlayerSignLocation[e.player]?.block)
                    if (owner.name != e.player.name) {
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