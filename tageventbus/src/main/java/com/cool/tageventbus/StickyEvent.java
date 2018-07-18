package com.cool.tageventbus;

/**
 * Created by cool on 2018/7/12.
 */
public class StickyEvent {
    public String tag;
    public Class<?>[] eventTypes;
    public Object[] events;

    public StickyEvent(String tag, Class<?>[] eventTypes,Object[] events) {
        this.tag = tag;
        this.eventTypes = eventTypes;
        this.events = events;
    }
}
