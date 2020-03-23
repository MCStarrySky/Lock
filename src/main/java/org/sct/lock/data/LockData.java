package org.sct.lock.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author LovesAsuna
 */
public class LockData {

    static {
        PlayerDoorLocation = HashBiMap.create();
        PlayerSignLocation = HashBiMap.create();
        PlayerisSneak = Maps.newHashMap();
        PlayerSign = Maps.newHashMap();
        PlayerBlock = Maps.newHashMap();
        inhibition = Maps.newHashMap();
        ensure = Maps.newHashMap();
        pool = new ThreadPoolExecutor(10, 25, 5, TimeUnit.MINUTES, new ArrayBlockingQueue<>(100));
        scheduledpool = new ScheduledThreadPoolExecutor(1);

        addStatus = Maps.newHashMap();
        LockData.addStatus.put("sign", false);
        LockData.addStatus.put("door", false);
    }

    /*玩家-牌子坐标*/
    @Getter
    private static BiMap<Player, Location> PlayerSignLocation;

    /*玩家-门坐标*/
    @Getter
    private static BiMap<Player, Location> PlayerDoorLocation;

    /*玩家潜行的状态*/
    @Getter
    private static Map<Player, Boolean> PlayerisSneak;

    /*玩家交互的门上方的牌子*/
    @Getter
    private static Map<Player, Block> PlayerSign;

    /*玩家交互的门(忽视高度)*/
    @Getter
    private static Map<Player, Block> PlayerBlock;

    /*插件专用线程池*/
    @Getter
    private static ThreadPoolExecutor pool;

    /*交互事件抑制器*/
    @Getter
    private static Map<Player, Boolean> inhibition;

    /*插件专用计划线程池*/
    @Getter
    private static ScheduledThreadPoolExecutor scheduledpool;

    /*添加配置状态*/
    @Getter
    private static Map<String, Boolean> addStatus;

    /*玩家进门确认状态*/
    @Getter
    private static Map<Player, Boolean> ensure;
}
