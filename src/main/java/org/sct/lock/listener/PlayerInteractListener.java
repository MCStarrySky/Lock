package org.sct.lock.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.sct.lock.Lock;
import org.sct.lock.data.LockData;
import org.sct.lock.enumeration.ConfigType;
import org.sct.lock.enumeration.LangType;
import org.sct.lock.event.PlayerAccessLockDoorEvent;
import org.sct.lock.file.Config;
import org.sct.lock.file.Lang;
import org.sct.lock.util.function.LockUtil;
import org.sct.lock.util.function.SIgnProcessUtil;
import org.sct.lock.util.player.CheckUtil;
import org.sct.plugincore.util.BasicUtil;
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

                    /*如果门的上方有自动收费门的牌子,在CheckUtil内存入牌子和方块的位置*/
                    if (CheckUtil.CheckSign(e.getPlayer(), e.getClickedBlock())) {

                        /*事件抑制*/
                        int orignDelay = Config.getInteger(ConfigType.SETTING_ENTERDELAY);
                        long delay = (long) ((double) orignDelay / 50);
                        boolean inhit = Inhibition.getInhibitStatus(e.getPlayer(), Config.getInteger(ConfigType.SETTING_ENTERDELAY), TimeUnit.MILLISECONDS);
                        Bukkit.getScheduler().runTaskLaterAsynchronously(Lock.getInstance(), () -> {
                            LockData.getEnsure().put(e.getPlayer(), false);
                        }, delay);
                        LockData.getEnsure().putIfAbsent(e.getPlayer(), false);

                        boolean ensure = LockData.getEnsure().get(e.getPlayer());
                        if (!inhit && !ensure) {
                            /*提示*/
                            showDoorDetail(e, delay / 20);
                            LockData.getEnsure().put(e.getPlayer(), true);
                            return;
                        }

                        if (!inhit && ensure) {
                            Bukkit.getPluginManager().callEvent(new PlayerAccessLockDoorEvent(e.getPlayer(), LockUtil.getOwner(LockData.getPlayerSign().get(e.getPlayer())), LockData.getPlayerSign().get(e.getPlayer())));
                        }

                    }
                }
            }
        }
    }

    private void showDoorDetail(PlayerInteractEvent e, long delay) {
        Block sign = LockData.getPlayerSign().get(e.getPlayer());
        Player player = e.getPlayer();
        String conditions = SIgnProcessUtil.getHoverTextAPI().getText(sign.getLocation());
        List<String> details = BasicUtil.convert(Lang.getStringList(LangType.LANG_DoorDetail));
        for (String detail : details) {
            detail = BasicUtil.replace(detail, "%conditons%", conditions);
            detail = BasicUtil.replace(detail, "%second%", String.valueOf(delay));
            player.sendMessage(detail);
        }
    }
}
