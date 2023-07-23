package org.sct.lock.util.function;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ExampleProject
 * org.sct.lock.util.function.SignUtil
 *
 * @author Mical
 * @since 2023/7/23 18:20
 */
public class SignUtil {

    public static List<String> getText(final Location location) {
        final Block block = location.getBlock();
        // 不是牌子
        if (!block.getType().name().toUpperCase().contains("SIGN")) {
            return new ArrayList<>();
        }
        final Sign sign = (Sign) block.getState();
        return Arrays.asList(sign.getLines());
    }

    public static void setLines(final Location location, final List<String> lines) {
        final Block block = location.getBlock();
        // 不是牌子
        if (!block.getType().name().toUpperCase().contains("SIGN")) {
            return;
        }
        final Sign sign = (Sign) block.getState();
        for (int i = 0; i < 4; i++) {
            sign.setLine(i, "");
        }
        for (int i = 0; i < lines.size(); i++) {
            sign.setLine(i, lines.get(i));
        }
    }
}
