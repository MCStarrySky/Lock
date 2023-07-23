package org.sct.lock.data;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.bukkit.OfflinePlayer;
//import org.sct.easylib.api.DataBaseManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author icestar
 * @date 2020/2/15 20:11
 */

public class LibData {

   // @Getter
    private static Map<String, String> newestversion;

    public static Map<String, String> getNewestversion() {
        return newestversion;
    }

    /*事件抑制器*/
   // @Getter
    private static Map<OfflinePlayer, Boolean> inhibition;

    public static Map<OfflinePlayer, Boolean> getInhibition() {
        return inhibition;
    }

    /*插件专用计划线程池*/
   // @Getter
    private static ScheduledThreadPoolExecutor scheduledpool;

    public static ScheduledThreadPoolExecutor getScheduledpool() {
        return scheduledpool;
    }

    /*SQLite*/
 ////   @Getter
 //   @Setter
   // private static DataBaseManager dataBaseManager;

    /*自动更新 shezhi*/
 //   @Getter
 //   @Setter
    private static Boolean autoupdate;

    public static Boolean getAutoupdate() {
        return autoupdate;
    }

    public static void setAutoupdate(Boolean autoupdate) {
        LibData.autoupdate = autoupdate;
    }

    /*
    @Getter
    @Setter
    private static ObjectMapper objectMapper;*/

    static {
        newestversion = Maps.newHashMap();
        inhibition = new HashMap<>();
        scheduledpool = new ScheduledThreadPoolExecutor(1);
    }
}