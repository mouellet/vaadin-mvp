package org.vaadin.mvp.eventbus;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;
import org.vaadin.mvp.eventbus.annotation.Events;
import org.vaadin.mvp.presenter.EventHandler;

/**
 * Create new instances of a typed event bus.
 *
 */
public class EventBusManager {

    private static final Log logger = LogFactory.getLog(EventBusManager.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private EventHandlerRegistry eventHandlerRegistry;

    /**
     * Utility method to create an event bus.
     *
     * @param eventHandler
     * @return the created EventBus
     */
    @SuppressWarnings("unchecked")
    public <E extends EventBus, T extends EventHandler<?, E>> E create(T eventHandler) {

        Assert.notNull(eventHandler, "EventHandler cannot be null");

        Class<E> eventBusType = (Class<E>) GenericTypeResolver.resolveTypeArguments(eventHandler.getClass(), EventHandler.class)[1];

        Events eventsDef = eventBusType.getAnnotation(Events.class);
        Assert.notNull(eventsDef, "@Events annotation missing from EventBus");

        EventBusInvocationHandler invocationHandler = getEventBusInvocationHandler();
        E eventBus = createEventBus(eventBusType, invocationHandler);

        registerEventHandler(eventHandler, eventsDef, invocationHandler);
        registerEventFilters(eventsDef, invocationHandler);
        registerParentEventBus(eventsDef, invocationHandler);

        return eventBus;
    }

    public <E extends EventBus, T extends EventHandler<?, E>> void unregister(T eventHandler) {

        Assert.notNull(eventHandler, "EventHandler cannot be null");
        Assert.notNull(eventHandler.getEventBus(), "No EventBus assigned to EventHandler");

        eventHandlerRegistry.unregister(eventHandler);
        if (logger.isDebugEnabled()) {
            logger.debug("Unregistering EventHandler [" + eventHandler + "] from EventHandlerRegistry [" + eventHandlerRegistry + "]");
        }
    }

    protected EventBusInvocationHandler getEventBusInvocationHandler() {
        return new EventBusInvocationHandler();
    }

    protected <E extends EventBus> E createEventBus(Class<E> eventBusType, EventBusInvocationHandler invocationHandler) {
        E eventBus = eventBusType.cast(
                Proxy.newProxyInstance(
                        eventBusType.getClassLoader(),
                        Stream.of(eventBusType)
                                .distinct()
                                .toArray(Class<?>[]::new),
                        invocationHandler));
        return eventBus;
    }

    protected void registerEventHandler(EventHandler<?, ? extends EventBus> eventHandler, Events eventsDef, EventBusInvocationHandler invocationHandler) {
        eventHandlerRegistry.register(eventHandler);
        if (eventsDef.multiple()) {
            invocationHandler.setPrivateHandlerInstance(eventHandler);
        }

        invocationHandler.setEventHandlerRegistry(eventHandlerRegistry);
    }

    protected void registerEventFilters(Events eventsDef, EventBusInvocationHandler invocationHandler) {
        Set<EventFilter> eventFilters = applicationContext.getBeansOfType(EventFilter.class)
                .values()
                .stream()
                .filter(eventFilter -> Arrays.asList(eventsDef.filterClasses()).contains(eventFilter.getClass()))
                .collect(Collectors.toSet());
        if (!eventFilters.isEmpty()) {
            invocationHandler.setFilters(eventFilters);
        }
    }

    protected void registerParentEventBus(Events eventsDef, EventBusInvocationHandler invocationHandler) {
        if (eventsDef.parent() != Events.NoParent.class) {
            invocationHandler.setParent(eventsDef.parent());
        }
    }

}
