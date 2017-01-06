package org.vaadin.mvp.presenter;

import org.vaadin.mvp.eventbus.EventBus;

/**
 * Interface that defines an event handler.
 *
 * @param <V>
 *            View type
 * @param <E>
 *            Event bus type
 */
public interface EventHandler<V, E extends EventBus> {

    void bind();

    void bind(Object args);

    void bindIfNeeded(Object args);

    boolean isBound();

    V getView();

    void setView(V view);

    E getEventBus();

    void setEventBus(E eventBus);
}
