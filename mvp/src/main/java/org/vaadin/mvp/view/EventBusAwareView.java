package org.vaadin.mvp.view;

import org.vaadin.mvp.eventbus.EventBus;

/**
 * Interface that defines a view that knows it's
 * {@link org.vaadin.mvp.eventbus.EventBus}.
 *
 * @author Mathieu Ouellet
 *
 */
public interface EventBusAwareView<E extends EventBus> {

    E getEventBus();

    void setEventBus(E eventBus);
}
