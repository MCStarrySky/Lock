package org.sct.lock.util.function;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.player.PlayerInteractEvent;
import org.sct.easylib.util.BasicUtil;
import org.sct.lock.Lock;
import org.sct.lock.data.LockData;
import org.sct.lock.enumeration.ConfigType;
import org.sct.lock.enumeration.Direction;
import org.sct.lock.enumeration.LangType;
import org.sct.lock.file.Config;
import org.sct.lock.file.Lang;

import java.util.List;
import java.util.Map;

/**
 * @author LovesAsuna
 */
public class LockUtil {
    /**
     * 存入与触发此事件的玩家相关的牌子与门坐标
     *
     * @param e 玩家交互事件
     **/
    public static void setLocation(PlayerInteractEvent e) {
        Location location = null;
        World world = e.getPlayer().getWorld();
        if (e.getClickedBlock() != null) {
            double x = e.getClickedBlock().getX();
            double y = e.getClickedBlock().getY();
            double z = e.getClickedBlock().getZ();
            double lowY = e.getClickedBlock().getY() - 1;
            switch (e.getBlockFace()) {
                case NORTH:
                    location = new Location(world, x, y, z - 1);
                    break;
                case SOUTH:
                    location = new Location(world, x, y, z + 1);
                    break;
                case WEST:
                    location = new Location(world, x - 1, y, z);
                    break;
                case EAST:
                    location = new Location(world, x + 1, y, z);
                    break;
                default:
            }
            // 存入玩家-牌子坐标
            LockData.INSTANCE.getPlayerSignLocation().putIfAbsent(e.getPlayer(), location);
            // 存入玩家-门坐标
            LockData.INSTANCE.getPlayerDoorLocation().putIfAbsent(e.getPlayer(), new Location(world, x, lowY, z));
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
    public static Direction getDirection(Block block) {
        Sign sign = (Sign) block.getState();
        String orign = sign.getLine(2);
        String enter = BasicUtil.convert(Config.getString(ConfigType.SETTING_ENTERREPLACE.getPath()));
        String leave = BasicUtil.convert(Config.getString(ConfigType.SETTING_LEAVEREPLACE.getPath()));
        if (orign.contains(enter) && orign.contains(leave)) {
            return Direction.DOUBLE;
        } else if (orign.contains(enter)) {
            return Direction.ENTER;
        } else if (orign.contains(leave)) {
            return Direction.LEAVE;
        } else {
            return null;
        }
    }

    /**
     * 获得条件
     *
     * @param block 牌子
     * @return 条件
     **/
    public static String getConditions(Block block) {
        String orign = SignProcessUtil.getHoverTextAPI().getText(block.getLocation());
        String empty = BasicUtil.convert(Config.getString(ConfigType.SETTING_EMPTYREPLACE.getPath()));
        String money = BasicUtil.convert(Config.getString(ConfigType.SETTING_FLAGMONEY.getPath()));
        String effect = BasicUtil.convert(Config.getString(ConfigType.SETTING_EFFECTREPLACE.getPath()));
        StringBuilder restriction = new StringBuilder();
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
     *
     * @param line         内容
     * @param currentMoney 现有余额
     * @param money        需要的钱
     * @return java.util.Map<java.lang.String, java.lang.Boolean>
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

    /**
     * @param e 交互事件
     * @return 正在添加配置返回true, 否则返回false
     **/
    public static boolean addStatus(PlayerInteractEvent e) {
        List<String> doorList = Config.getStringList(ConfigType.SETTING_DOORTYPE.getPath());

        if (LockData.INSTANCE.getAddStatus().get("door")) {
            doorList.add(e.getClickedBlock().getLocation().getBlock().getType().toString());
            Config.setStringList(ConfigType.SETTING_DOORTYPE.getPath(), doorList);
            Lock.getInstance().saveConfig();
            LockData.INSTANCE.getAddStatus().put("door", false);
            e.getPlayer().sendMessage(BasicUtil.convert(BasicUtil.replace(Lang.getString(LangType.LANG_ADDTYPESUCCESS.getPath()), "%type", "DOOR")));
            ;
            e.setCancelled(true);
            return true;
        }
        return false;
    }

}
