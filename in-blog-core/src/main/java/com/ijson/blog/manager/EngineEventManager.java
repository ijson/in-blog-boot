

package com.ijson.blog.manager;

//import com.ijson.framework.bus.EventBusListener;
//import com.ijson.framework.bus.IEventBus;

import com.ijson.blog.bus.EventBusListener;
import com.ijson.blog.bus.IEventBus;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/5 4:03 PM
 */
@Component
public class EngineEventManager extends ApplicationObjectSupport {

    @PostConstruct
    public void init() {
        Map<String, EventBusListener> eventBusListener = getApplicationContext().getBeansOfType(EventBusListener.class);
        if (MapUtils.isNotEmpty(eventBusListener)) {
            IEventBus.init(eventBusListener.values());
        }
    }
}
