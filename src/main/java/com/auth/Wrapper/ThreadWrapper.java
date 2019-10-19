package com.auth.Wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadWrapper {
    private static Logger logger = LoggerFactory.getLogger(ThreadWrapper.class);

    public static void setTimeoutAync(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }).start();
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
