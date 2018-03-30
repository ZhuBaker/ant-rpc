package com.antrpc.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-30
 * Time: 9:29
 */
public class NamedThreadFactory implements ThreadFactory{

    private static final Logger logger = LoggerFactory.getLogger(NamedThreadFactory.class);

    private static final AtomicInteger poolId = new AtomicInteger();

    private static final AtomicInteger nextId = new AtomicInteger();

    private final String prefix;
    private final boolean daemon;
    private final ThreadGroup group;

    public NamedThreadFactory(String prefix) {
        this(prefix,false);
    }

    public NamedThreadFactory(String prefix , boolean daemon) {
        this.prefix = prefix + " #";
        this.daemon = daemon;
        SecurityManager s = System.getSecurityManager();
        group = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        String name = prefix + nextId.getAndIncrement();
        Thread t = new Thread(group , r , name , 0);
        try{
            if (t.isDaemon()) {
                if (!daemon) {
                    t.setDaemon(false);
                }
            } else {
                if (daemon) {
                    t.setDaemon(true);
                }
            }
        }catch (Exception ignored){ /* Doesn't matter even if failed to set. */}

        logger.debug("Creates new {}.", t);
        return t;
    }

    public ThreadGroup getGroup() {
        return group;
    }
}
