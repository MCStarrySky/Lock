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
import org.sct.lock.util.player.TeleportAPI;
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
        Player player = e.getPlayer();

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
                if (LockData.getPlayerisSneak().get(player) == null || !LockData.getPlayerisSneak().get(player)) {
                    return;
                }

                if (e.getClickedBlock().getLocation().getBlock().getType() == Material.getMaterial(door)) {

                    /*如果门的上方有自动收费门的牌子,在CheckUtil内存入牌子和方块的位置*/
                    if (CheckUtil.CheckSign(player, e.getClickedBlock())) {

                        TeleportAPI teleportAPI = new TeleportAPI();


                        /*设置状态数据*/
                        teleportAPI.getData(player);

                        /*如果执行传送并返回进出状态，以此来进行扣费操作*/
                        String status = teleportAPI.getPlayerFace(player);

                        if (status.equals("leave")) {
                            callEvent(player, teleportAPI);
                            return;
                        }

                        /*事件抑制*/
                        int orignDelay = Config.getInteger(ConfigType.SETTING_ENTERDELAY);
                        long delay = (long) ((double) orignDelay / 50);
                        boolean inhit = Inhibition.getInhibitStatus(player, Config.getInteger(ConfigType.SETTING_ENTERDELAY), TimeUnit.MILLISECONDS);
                        Bukkit.getScheduler().runTaskLaterAsynchronously(Lock.getInstance(), () -> {
                            LockData.getEnsure().put(player, false);
                        }, delay);
                        LockData.getEnsure().putIfAbsent(player, false);

                        boolean ensure = LockData.getEnsure().get(player);
                        if (!inhit && !ensure) {
                            /*提示*/
                            showDoorDetail(e, delay / 20);
                            LockData.getEnsure().put(player, true);
                            return;
                        }

                        if (!inhit && ensure) {
                            callEvent(player, teleportAPI);
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

    private void callEvent(Player player, TeleportAPI teleportAPI) {
        Bukkit.getPluginManager().callEvent(new PlayerAccessLockDoorEvent(player,
                LockUtil.getOwner(LockData.getPlayerSign().get(player)),
                teleportAPI,
                LockData.getPlayerSign().get(player)));
    }
}
