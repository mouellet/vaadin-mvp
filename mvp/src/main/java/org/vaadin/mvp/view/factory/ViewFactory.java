package org.vaadin.mvp.view.factory;

import org.vaadin.mvp.eventbus.EventBus;

public interface ViewFactory {

    <T, E extends EventBus> T create(Class<T> viewClass, E eventBus) throws ViewFactoryException;

    boolean canCreateView(Class<?> viewType);
}