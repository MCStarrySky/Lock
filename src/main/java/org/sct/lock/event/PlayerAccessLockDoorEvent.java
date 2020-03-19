package org.sct.lock.event;

import lombok.Getter;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author alchemy
 * @since 2019/12/9/17:13:13
 */
public class PlayerAccessLockDoorEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    Player payer;
    @Getter
    OfflinePlayer owner;
    @Getter
    Block block;

    /**
     * 构造函数
     *
     * @param payer 支付费用使用收费门的玩家
     * @param owner 收费门的所有者
     * @param block 收费门上面的木牌，用于判断信息
     */
    public PlayerAccessLockDoorEvent(Player payer, OfflinePlayer owner, Block block) {
        this.payer = payer;
        this.owner = owner;
        this.block = block;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
