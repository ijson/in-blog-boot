package com.ijson.blog.bus;

import com.google.common.eventbus.AsyncEventBus;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * www.ijson.com
 */
@Slf4j
public class IEventBus {


    private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() * 2,
            Runtime.getRuntime().availableProcessors() * 2, 0,
            TimeUnit.SECONDS, new LinkedBlockingDeque<>(),
            r -> new Thread(r, "Like4kEventBus-" + Thread.currentThread().getName()));


    private static AsyncEventBus engineEventBus = new AsyncEventBus("IEventBus", executorService);

    public static void init(Collection<EventBusListener> listeners) {
        if (listeners != null && !listeners.isEmpty()) {
            for (EventBusListener listener : listeners) {
                engineEventBus.register(listener);
            }
        }
    }


    public static void post(BaseMessageEvent event) {
        engineEventBus.post(event);
    }

}
