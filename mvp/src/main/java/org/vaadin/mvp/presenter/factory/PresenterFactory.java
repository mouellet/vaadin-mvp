package org.vaadin.mvp.presenter.factory;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.EventHandler;

/**
 * Contract for presenter factories.
 *
 * @author tam
 *
 */
public interface PresenterFactory {

    /**
     * Create a new instance of a presenter with it's view and event bus setup.
     *
     * @param presenterClass
     *            class of the presenter to create
     * @return new instance of the presenter
     */
    EventHandler<?, ? extends EventBus> create(Class<?> presenterClass);

    // TODO Typed Args
    EventHandler<?, ? extends EventBus> createAndBind(Class<?> presenterClass);

    EventHandler<?, ? extends EventBus> createAndBind(Class<?> presenterClass, Object args);
}