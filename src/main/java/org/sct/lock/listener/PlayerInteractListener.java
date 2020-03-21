package org.sct.lock.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.sct.lock.data.LockData;
import org.sct.lock.enumeration.ConfigType;
import org.sct.lock.event.PlayerAccessLockDoorEvent;
import org.sct.lock.file.Config;
import org.sct.lock.util.function.LockUtil;
import org.sct.lock.util.player.CheckUtil;
import org.sct.plugincore.util.function.Inhibition;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author LovesAsuna
 * @since 2019/12/4 23:01
 */
public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        List<String> signList = Config.getStringList(ConfigType.SETTING_SIGNTYPE);
        List<String> doorList = Config.getStringList(ConfigType.SETTING_DOORTYPE);

        if (LockUtil.addStatus(e)) {
            return;
        }


        // 如果玩家右键方块
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            // 如果玩家手持物品
            if (e.hasItem()) {
                for (String sign : signList) {
                    Material material = Material.getMaterial(sign);
                    if (e.getItem().getType() == material) {
                        LockUtil.getLocation(e);
                    }
                }
            }

            for (String door : doorList) {
                // 如果玩家正在潜行
                if (LockData.getPlayerisSneak().get(e.getPlayer()) == null || !LockData.getPlayerisSneak().get(e.getPlayer())) {
                    return;
                }

                if (e.getClickedBlock().getLocation().getBlock().getType() == Material.getMaterial(door)) {
                    // 如果门的上方有自动收费门的牌子,在CheckUtil内存入牌子和方块的位置
                    /*事件抑制*/
                    boolean inhit = Inhibition.getInhibitStatus(e.getPlayer(), Config.getInteger(ConfigType.SETTING_ENTERDELAY), TimeUnit.MILLISECONDS);

                    if (inhit) {
                        return;
                    }

                    if (CheckUtil.CheckSign(e.getPlayer(), e.getClickedBlock())) {
                        Bukkit.getPluginManager().callEvent(new PlayerAccessLockDoorEvent(e.getPlayer(), LockUtil.getOwner(LockData.getPlayerSign().get(e.getPlayer())), LockData.getPlayerSign().get(e.getPlayer())));
                    }
                }
            }
        }
    }
}
