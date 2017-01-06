package org.vaadin.mvp.presenter.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.EventHandler;

/**
 * Abstract base class for presenter factories.
 *
 * <p>
 * A Presenter Factory is stateful since it hold a reference to the
 * {@link EventBusManager}, an instance is required for each application (i.e.
 * session).
 *
 * @author tam
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DefaultPresenterFactory implements PresenterFactory {

    private static final Log logger = LogFactory.getLog(DefaultPresenterFactory.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public EventHandler<?, ? extends EventBus> create(Class<?> presenterClass) {
        try {

            return (EventHandler) applicationContext.getBean(presenterClass);

        } catch (BeansException e) {
            logger.error("Failed to create presenter", e);
            throw new IllegalArgumentException("No bean is defined for presenter named: " + presenterClass, e);
        }
    }

    @Override
    public EventHandler<?, ? extends EventBus> createAndBind(Class<?> presenterClass) {
        EventHandler presenter = this.create(presenterClass);
        presenter.bind();
        return presenter;
    }

    @Override
    public EventHandler<?, ? extends EventBus> createAndBind(Class<?> presenterClass, Object args) {
        EventHandler presenter = this.create(presenterClass);
        presenter.bind(args);
        return presenter;
    }

}
