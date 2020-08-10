package org.sct.lock.data

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.common.collect.Maps
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.sct.lock.util.function.LockThreadFactory
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @author LovesAsuna
 */
object LockData {
    /*玩家-牌子坐标*/
    var PlayerSignLocation: BiMap<Player, Location>? = null

    /*玩家-门坐标*/
    var PlayerDoorLocation: BiMap<Player, Location>? = null

    /*玩家潜行的状态*/
    var PlayerisSneak: MutableMap<Player, Boolean>? = null

    /*插件专用线程池*/
    var pool: ThreadPoolExecutor? = null

    /*交互事件抑制器*/
    var inhibition: MutableMap<OfflinePlayer, Boolean>? = null

    /*插件专用计划线程池*/
    var scheduledpool: ScheduledThreadPoolExecutor? = null

    /*添加配置状态*/
    var addStatus: MutableMap<String, Boolean>? = null

    /*玩家进门确认状态*/
    var ensure: MutableMap<Player, Boolean>? = null

    init {
        PlayerDoorLocation = HashBiMap.create()
        PlayerSignLocation = HashBiMap.create()
        addStatus = Maps.newHashMap()
        PlayerisSneak = Maps.newHashMap()
        inhibition = Maps.newHashMap()
        ensure = Maps.newHashMap()
        pool = ThreadPoolExecutor(10, 25, 5, TimeUnit.MINUTES, ArrayBlockingQueue(100), LockThreadFactory("[Lock]"))
        scheduledpool = ScheduledThreadPoolExecutor(15)
        addStatus?.put("door", false)
    }

}