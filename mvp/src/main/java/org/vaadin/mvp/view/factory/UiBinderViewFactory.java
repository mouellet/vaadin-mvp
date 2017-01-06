package org.vaadin.mvp.view.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.view.EventBusAwareView;
import org.vaadin.mvp.view.binder.UiBinder;
import org.vaadin.mvp.view.binder.UiBinderException;
import org.vaadin.mvp.view.event.EventBinder;
import org.vaadin.mvp.view.event.EventBusEventBinder;

public class UiBinderViewFactory implements ViewFactory {

    private static final Log logger = LogFactory.getLog(UiBinderViewFactory.class);

    @Autowired
    private UiBinder uiBinder;

    @SuppressWarnings("unchecked")
    @Override
    public <T, E extends EventBus> T create(Class<T> viewClass, E eventBus) throws ViewFactoryException {
        if (logger.isTraceEnabled()) {
            logger.trace("Creating view " + viewClass);
        }

        try {
            EventBinder eventBinder = new EventBusEventBinder(eventBus);
            T view = uiBinder.createAndBind(viewClass, eventBinder);
            if (EventBusAwareView.class.isAssignableFrom(viewClass)) {
                ((EventBusAwareView<E>) view).setEventBus(eventBus);
            }
            return view;

        } catch (UiBinderException e) {
            throw new ViewFactoryException("Failed to create view", e);
        }
    }

    @Override
    public boolean canCreateView(Class<?> viewType) {
        return uiBinder.isBindable(viewType);
    }

}
