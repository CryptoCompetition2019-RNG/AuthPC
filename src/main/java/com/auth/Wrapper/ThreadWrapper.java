package com.auth.Wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadWrapper {
    private static Logger logger = LoggerFactory.getLogger(ThreadWrapper.class);

    public static Thread getTimeoutAyncThread(Runnable runnable, int delay) {
        return new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                logger.error(e.toString());
            }
        });
    }

    public static Thread setTimeoutAync(Runnable runnable, int delay){
        Thread newThread = new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                logger.error(e.toString());
            }
        });
        newThread.start();
        return newThread;
    }

    public static void setTimeoutSync(Runnable runnable, int delay){
        try {
            Thread.sleep(delay);
            runnable.run();
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
