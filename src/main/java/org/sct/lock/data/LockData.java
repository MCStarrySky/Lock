package org.sct.lock.data;

import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author icestar
 */
public class LockData {

    static {
        LocationPlayer = new HashMap<>();
        PlayerLocation = new HashMap<>();
        PlayerisSneak = new HashMap<>();
        PlayerSign = new HashMap<>();
        PlayerBlock = new HashMap<>();
        inhibition = new HashMap<>();
        pool = new ThreadPoolExecutor(10, 25, 5, TimeUnit.MINUTES, new ArrayBlockingQueue<>(100));
        scheduledpool = new ScheduledThreadPoolExecutor(1);

        addStatus = Maps.newHashMap();
        LockData.addStatus.put("sign", false);
        LockData.addStatus.put("door", false);
    }

    /*牌子坐标-玩家*/
    @Getter private static Map<Location, Player> LocationPlayer;

    /*玩家-门坐标*/
    @Getter private static Map<Player, Location> PlayerLocation;

    /*玩家潜行的状态*/
    @Getter private static Map<Player, Boolean> PlayerisSneak;

    /*玩家交互的门上方的牌子*/
    @Getter private static Map<Player, Block> PlayerSign;

    /*玩家交互的门(忽视高度)*/
    @Getter private static Map<Player, Block> PlayerBlock;

    /*插件专用线程池*/
    @Getter private static ThreadPoolExecutor pool;

    /*交互事件抑制器*/
    @Getter private static Map<Player, Boolean> inhibition;

    /*插件专用计划线程池*/
    @Getter private static ScheduledThreadPoolExecutor scheduledpool;

    /*添加配置状态*/
    @Getter private static Map<String, Boolean> addStatus;
}
