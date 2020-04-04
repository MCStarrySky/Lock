package org.sct.lock.listener;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.sct.lock.enumeration.ConfigType;
import org.sct.lock.enumeration.LangType;
import org.sct.lock.event.PlayerAccessLockDoorEvent;
import org.sct.lock.file.Config;
import org.sct.lock.file.Lang;
import org.sct.lock.util.function.LockUtil;
import org.sct.lock.util.player.InventoryUtil;
import org.sct.lock.util.player.TeleportAPI;
import org.sct.plugincore.util.BasicUtil;
import org.sct.plugincore.util.player.EcoUtil;

import java.util.Map;

/**
 * @author alchemy
 * @date 2019-12-09 19:24
 */

public class LockDoorAccessListener implements Listener {

    @EventHandler
    public void onAccess(PlayerAccessLockDoorEvent e) {
        Sign sign = (Sign) e.getBlock().getState();
        int charge = BasicUtil.ExtraceInt(sign.getLine(1).trim());

        TeleportAPI teleportAPI = e.getTeleportAPI();

        TeleportAPI.status s = teleportAPI.getPlayerFace(e.getPayer());

        /*收费门指定允许的方向*/
        TeleportAPI.status direction = LockUtil.getDirection(e.getBlock());

        if (!"double".equals(direction) && !e.getPayer().getName().equals(e.getOwner().getName())) {
            if (s != direction) {
                e.getPayer().sendMessage(BasicUtil.convert(Lang.getString(LangType.LANG_DENYDIRECTION)));
                return;
            }
        }

        String conditons = LockUtil.getConditons(e.getBlock());

        if (s == TeleportAPI.status.ENTER) {
            if (e.getPayer().getName().equals(e.getOwner().getName())) {
                teleportAPI.Tp(TeleportAPI.status.ENTER, e.getPayer());
                return;
            }

            if (!conditons.isEmpty()) {
                if (conditons.contains("1")) {
                    if (!InventoryUtil.isInvEmpty(e.getPayer())) {
                        e.getPayer().sendMessage(BasicUtil.convert(Lang.getString(LangType.LANG_DENYNOTEMPTYINV)));
                        return;
                    }
                }

                if (conditons.contains("2")) {
                    String line = BasicUtil.remove(((Sign) e.getBlock().getState()).getLine(2));
                    int money = BasicUtil.ExtraceInt(line);
                    int currentMoney = (int) EcoUtil.get(e.getPayer());
                    Map<String, Boolean> moneyDetail = LockUtil.getMoneydetail(line, currentMoney, money);
                    String symbol = moneyDetail.keySet().iterator().next();
                    boolean access = moneyDetail.get(symbol);
                    if (!access && !symbol.isEmpty()) {
                        e.getPayer().sendMessage(BasicUtil.replace(Lang.getString(LangType.LANG_DENYMONEY), "%needmoney", symbol + money));
                        return;
                    }
                }

                if (conditons.contains("3")) {
                    if (!e.getPayer().getActivePotionEffects().isEmpty()) {
                        e.getPayer().sendMessage(BasicUtil.convert(Lang.getString(LangType.LANG_DENYHAVEEFFECT)));
                        return;
                    }
                }
            }

            /*如果余额不足*/
            if (!EcoUtil.has(e.getPayer(), charge)) {
                e.getPayer().sendMessage(Lang.getString(LangType.LANG_NOTENOUGHMONEY));
                return;
            }

            teleportAPI.Tp(TeleportAPI.status.ENTER, e.getPayer());
            /*payer付钱部分*/

            /*如果owner是vip或权限未设置*/
            if (!"".equalsIgnoreCase(Config.getString(ConfigType.SETTING_VIPALLOWED)) || ((Player) LockUtil.getOwner(e.getBlock())).hasPermission(Config.getString(ConfigType.SETTING_VIPALLOWED))) {
                EcoUtil.take(e.getPayer(), charge);
                EcoUtil.give(LockUtil.getOwner(e.getBlock()), charge);
            } else {
                /*owner不是vip,扣税*/
                EcoUtil.take(e.getPayer(), charge);
                charge = (1 - Config.getInteger(ConfigType.SETTING_TAXPERCENT)) * charge;
                EcoUtil.give(LockUtil.getOwner(e.getBlock()), charge);
            }

            e.getPayer().sendMessage(BasicUtil.replace(Lang.getString(LangType.LANG_ENTER),"%charge", charge));
        } else {
            teleportAPI.Tp(TeleportAPI.status.LEAVE, e.getPayer());
        }

    }

}
