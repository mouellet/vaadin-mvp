package org.vaadin.mvp.view.event;

public interface EventBinder {

    void bindEvent(Object obj, Class<?> eventType, String bindTo) throws EventBinderException;
}