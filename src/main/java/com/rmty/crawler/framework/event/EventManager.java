package com.rmty.crawler.framework.event;

import com.rmty.crawler.framework.config.Config;

import java.util.*;
import java.util.function.Consumer;

public class EventManager {

    private static final Map<Event, List<Consumer<Config>>> EventConsumerMap = new HashMap<>();

    public static void registerEvent(Event Event, Consumer<Config> consumer) {
        List<Consumer<Config>> consumers = EventConsumerMap.get(Event);
        if (null == consumers) {
            consumers = new ArrayList<>();
        }
        consumers.add(consumer);
        EventConsumerMap.put(Event, consumers);
    }

    public static void fireEvent(Event Event, Config config) {
        Optional.ofNullable(EventConsumerMap.get(Event)).ifPresent(consumers -> consumers.forEach(consumer -> consumer.accept(config)));
    }

}