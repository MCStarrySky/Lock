package org.sct.lock.util.function;

import java.util.concurrent.ThreadFactory;

/**
 * @author LovesAsuna
 * @date 2020/4/21 23:48
 */

public class LockThreadFactory implements ThreadFactory {
    private final String threadName;

    public LockThreadFactory(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(threadName);
        return thread;
    }
}
