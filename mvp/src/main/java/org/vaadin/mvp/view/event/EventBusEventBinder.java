package org.vaadin.mvp.view.event;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vaadin.mvp.eventbus.EventBus;

import com.vaadin.event.MethodEventSource;

/**
 * Binds event listeners to EventBus.
 *
 * @author Mathieu Ouellet
 */
public class EventBusEventBinder implements EventBinder {

    private static final Log logger = LogFactory.getLog(EventBusEventBinder.class.getName());

    private final EventBus eventBus;

    public EventBusEventBinder(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * Adds an Listener to the component and binds it to a EventBus
     *
     * @param obj
     * @param eventType
     * @param bindTo
     *
     * @throws EventBinderException
     */
    @Override
    public void bindEvent(Object obj, Class<?> eventType, String bindTo) throws EventBinderException {
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("Binding callback to " + bindTo + " on " + obj.getClass().getName());
            }

            Method method = eventBus.getClass().getInterfaces()[0].getMethod(bindTo, eventType);
            ((MethodEventSource) obj).addListener(eventType, eventBus, method);

        } catch (NoSuchMethodException e) {
            throw new EventBinderException("Unable to find matching method [" + bindTo + "] on [" + eventBus + "] to bind event to", e);

        } catch (SecurityException e) {
            throw new EventBinderException("Method [" + bindTo + "] on [" + eventBus + "] must be public", e);
        }
    }

}
