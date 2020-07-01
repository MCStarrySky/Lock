package org.sct.lock.util.function;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.player.PlayerInteractEvent;
import org.sct.easylib.util.BasicUtil;
import org.sct.lock.Lock;
import org.sct.lock.data.LockData;
import org.sct.lock.enumeration.ConfigType;
import org.sct.lock.enumeration.LangType;
import org.sct.lock.file.Config;
import org.sct.lock.file.Lang;
import org.sct.lock.util.player.TeleportAPI;

import java.util.List;
import java.util.Map;

/**
 * @author LovesAsuna
 */
public class LockUtil {

    public static void setLocation(PlayerInteractEvent e) {
        Location location = null;
        World world = e.getPlayer().getWorld();
        if (e.getClickedBlock() != null) {
            double X = e.getClickedBlock().getX();
            double Y = e.getClickedBlock().getY();
            double Z = e.getClickedBlock().getZ();
            double LowY = e.getClickedBlock().getY() - 1;
            //lt为牌子坐标
            if (e.getBlockFace() == BlockFace.NORTH) {
                location = new Location(world, X, Y, Z - 1);
            }
            if (e.getBlockFace() == BlockFace.SOUTH) {
                location = new Location(world, X, Y, Z + 1);
            }
            if (e.getBlockFace() == BlockFace.WEST) {
                location = new Location(world, X - 1, Y, Z);
            }
            if (e.getBlockFace() == BlockFace.EAST) {
                location = new Location(world, X + 1, Y, Z);
            }
            /*存入玩家-牌子坐标*/
            LockData.INSTANCE.getPlayerSignLocation().putIfAbsent(e.getPlayer(),location);
            /*存入玩家-门坐标*/
            LockData.INSTANCE.getPlayerDoorLocation().putIfAbsent(e.getPlayer(), new Location(e.getPlayer().getWorld(), X, LowY, Z));
        }
    }

    /**
     * @param block 牌子
     * @return OfflinePlayer 牌子拥有者
     */
    @SuppressWarnings("deprecation")
    public static OfflinePlayer getOwner(Block block) {
        Sign sign = (Sign) block.getState();
        return Bukkit.getOfflinePlayer(sign.getLine(3).replace("§l", ""));
    }

    /**
     * @param block 牌子
     * @return String 收费门允许的方向
     */
    public static TeleportAPI.status getDirection(Block block) {
        Sign sign = (Sign) block.getState();
        String orign = sign.getLine(2);
        String enter = BasicUtil.convert(Config.getString(ConfigType.SETTING_ENTERREPLACE.getPath()));
        String leave = BasicUtil.convert(Config.getString(ConfigType.SETTING_LEAVEREPLACE.getPath()));
        if (orign.contains(enter) && orign.contains(leave)) {
            return TeleportAPI.status.DOUBLE;
        } else if (orign.contains(enter)) {
            return TeleportAPI.status.ENTER;
        } else if (orign.contains(leave)) {
            return TeleportAPI.status.LEAVE;
        } else {
            return null;
        }
    }

    /**
     * 获得条件
     * @param block 牌子
     * @return 条件
     **/
    public static String getConditions(Block block) {
        String orign = SIgnProcessUtil.getHoverTextAPI().getText(block.getLocation());
        String empty = BasicUtil.convert(Config.getString(ConfigType.SETTING_EMPTYREPLACE.getPath()));
        String money = BasicUtil.convert(Config.getString(ConfigType.SETTING_FLAGMONEY.getPath()));
        String effect = BasicUtil.convert(Config.getString(ConfigType.SETTING_EFFECTREPLACE.getPath()));
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

    /**
     * 返回详细的金钱消息
     * @param line 内容
     * @param currentMoney 现有余额
     * @param money 需要的钱
     * @return java.util.Map<java.lang.String,java.lang.Boolean>
     **/
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

    public static boolean addStatus(PlayerInteractEvent e) {
        List<String> doorList = Config.getStringList(ConfigType.SETTING_DOORTYPE.getPath());

        if (LockData.INSTANCE.getAddStatus().get("door")) {
            doorList.add(e.getClickedBlock().getLocation().getBlock().getType().toString());
            Config.setStringList(ConfigType.SETTING_DOORTYPE.getPath(), doorList);
            Lock.getInstance().saveConfig();
            LockData.INSTANCE.getAddStatus().put("door", false);
            e.getPlayer().sendMessage(BasicUtil.convert(BasicUtil.replace(Lang.getString(LangType.LANG_ADDTYPESUCCESS.getPath()), "%type", "DOOR")));;
            e.setCancelled(true);
            return true;
        }
        return false;
    }

}
