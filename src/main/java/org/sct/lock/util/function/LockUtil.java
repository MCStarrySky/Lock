package org.sct.lock.util.function;

import com.google.common.collect.Maps;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.player.PlayerInteractEvent;
import org.sct.lock.data.LockData;
import org.sct.lock.enumeration.ConfigType;
import org.sct.lock.file.Config;
import org.sct.plugincore.util.BasicUtil;

import java.util.Map;

/**
 * @author icestar
 */
public class LockUtil {

    public static void getLocation(PlayerInteractEvent e) {
        Location lt = null;
        World world = e.getPlayer().getWorld();
        if (e.getClickedBlock() != null) {
            double X = e.getClickedBlock().getX();
            double Y = e.getClickedBlock().getY();
            double Z = e.getClickedBlock().getZ();
            double LowY = e.getClickedBlock().getY() - 1;
            //lt为牌子坐标
            if (e.getBlockFace() == BlockFace.NORTH) {
                lt = new Location(world, X, Y, Z - 1);
            }
            if (e.getBlockFace() == BlockFace.SOUTH) {
                lt = new Location(world, X, Y, Z + 1);
            }
            if (e.getBlockFace() == BlockFace.WEST) {
                lt = new Location(world, X - 1, Y, Z);
            }
            if (e.getBlockFace() == BlockFace.EAST) {
                lt = new Location(world, X + 1, Y, Z);
            }
            /*存入牌子坐标-玩家*/
            LockData.getLocationPlayer().put(lt, e.getPlayer());
            /*存入玩家-门坐标*/
            LockData.getPlayerLocation().put(e.getPlayer(), new Location(e.getPlayer().getWorld(), X, LowY, Z));
        }
    }

    /**
     * @param block 牌子
     * @return OfflinePlayer 牌子拥有者
     */
    @SuppressWarnings("deprecation")
    public static OfflinePlayer getOwner(Block block) {

        for (String materialString : Config.getStringList(ConfigType.SETTING_SIGNTYPE)) {
            Material material = Material.getMaterial(materialString);
            if (block.getType() == material) {
                Sign sign = (Sign) block.getState();
                return Bukkit.getOfflinePlayer(sign.getLine(3).replace("§l", ""));
            }
        }

        return null;
    }

    /**
     * @param block 牌子
     * @return String 收费门允许的方向
     */
    public static String getDirection(Block block) {
        Sign sign = (Sign) block.getState();
        String orign = sign.getLine(2);
        String enter = BasicUtil.convert(Config.getString(ConfigType.SETTING_ENTERREPLACE));
        String leave = BasicUtil.convert(Config.getString(ConfigType.SETTING_LEAVEREPLACE));
        if (orign.contains(enter) && orign.contains(leave)) {
            return "double";
        } else if (orign.contains(enter)) {
            return "enter";
        } else if (orign.contains(leave)) {
            return "leave";
        } else {
            return null;
        }
    }

    public static String getRestriction(Block block) {
        Sign sign = (Sign) block.getState();
        String orign = sign.getLine(2);
        String empty = BasicUtil.convert(Config.getString(ConfigType.SETTING_EMPTYREPLACE));
        String money = BasicUtil.convert(Config.getString(ConfigType.SETTING_FLAGMONEY));
        String effect = BasicUtil.convert(Config.getString(ConfigType.SETTING_EFFECTREPLACE));
        StringBuffer restriction = new StringBuffer();
        if (orign.contains(empty)) {
            restriction.append("1");
        }

        if (orign.contains(money)) {
            restriction.append("2");
        }
        if (orign.contains(effect)) {
            restriction.append("3");
        }
        return restriction.toString();
    }

    public static Map<String, Boolean> getMoneydetail(String line, int currentMoney, int money) {
        boolean access = false;
        String symbol = "";
        Map<String, Boolean> moneyDetail = Maps.newHashMap();
        if (line.contains(">=")) {
            symbol = ">=";
            if (currentMoney >= money) {
                access = true;
            }
        } else if (line.contains("<=")) {
            symbol = "<=";
            if (currentMoney <= money) {
                access = true;
            }
        } else if (line.contains("=")) {
            symbol = "=";
            if (currentMoney == money) {
                access = true;
            }
        } else if (line.contains(">")) {
            symbol = ">";
            if (currentMoney > money) {
                access = true;
            }
        } else if (line.contains("<")) {
            symbol = "<";
            if (currentMoney < money) {
                access = true;
            }
        }
        moneyDetail.put(symbol, access);
        return moneyDetail;
    }

}
