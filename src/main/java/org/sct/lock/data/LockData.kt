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
    private var PlayerSignLocation: BiMap<Player, Location>? = null

    /*玩家-门坐标*/
    private var PlayerDoorLocation: BiMap<Player, Location>? = null

    /*玩家潜行的状态*/
    private var PlayerisSneak: MutableMap<Player, Boolean>? = null

    /*玩家交互的门上方的牌子*/
    private var PlayerSign: MutableMap<Player, Block>? = null

    /*玩家交互的门(忽视高度)*/
    private var PlayerBlock: MutableMap<Player, Block>? = null

    /*插件专用线程池*/
    private var pool: ThreadPoolExecutor? = null

    /*交互事件抑制器*/
    private var inhibition: MutableMap<OfflinePlayer, Boolean>? = null

    /*插件专用计划线程池*/
    private var scheduledpool: ScheduledThreadPoolExecutor? = null

    /*添加配置状态*/
    private var addStatus: MutableMap<String, Boolean>? = null

    /*玩家进门确认状态*/
    private var ensure: MutableMap<Player, Boolean>? = null

    init {
        PlayerDoorLocation = HashBiMap.create()
        PlayerSignLocation = HashBiMap.create()
        addStatus = Maps.newHashMap()
        PlayerisSneak = Maps.newHashMap()
        PlayerSign = Maps.newHashMap()
        PlayerBlock = Maps.newHashMap()
        inhibition = Maps.newHashMap()
        ensure = Maps.newHashMap()
        pool = ThreadPoolExecutor(10, 25, 5, TimeUnit.MINUTES, ArrayBlockingQueue(100), LockThreadFactory("[Lock]"))
        scheduledpool = ScheduledThreadPoolExecutor(15)
        addStatus?.put("door", false)
    }

    fun getPlayerSign(): MutableMap<Player, Block>? {
        return PlayerSign
    }

    fun getPlayerisSneak(): MutableMap<Player, Boolean>? {
        return PlayerisSneak
    }

    fun getEnsure(): MutableMap<Player, Boolean>? {
        return ensure
    }

    fun getPlayerSignLocation(): BiMap<Player, Location>? {
        return PlayerSignLocation
    }

    fun getPlayerDoorLocation(): BiMap<Player, Location>? {
        return PlayerDoorLocation
    }

    fun getAddStatus(): MutableMap<String, Boolean>? {
        return addStatus
    }

    fun getPool(): ThreadPoolExecutor? {
        return pool
    }

    fun getScheduledpool(): ScheduledThreadPoolExecutor? {
        return scheduledpool
    }

    fun getPlayerBlock(): MutableMap<Player, Block>? {
        return PlayerBlock
    }

    fun getInhibition(): MutableMap<OfflinePlayer, Boolean>? {
        return inhibition
    }

}