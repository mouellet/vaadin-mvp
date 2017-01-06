package org.vaadin.mvp.eventbus;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.vaadin.mvp.eventbus.annotation.Event;
import org.vaadin.mvp.eventbus.annotation.Events;
import org.vaadin.mvp.navigation.history.HistoryConverter;
import org.vaadin.mvp.presenter.EventHandler;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

/**
 * EventBus invocation handler; delegates events to the actual listeners.
 *
 * @author tam
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EventBusInvocationHandler implements InvocationHandler {

    private static final Log logger = LogFactory.getLog(EventBusInvocationHandler.class);

    private final Set<EventFilter> filters = new HashSet<>();
    private EventHandlerRegistry eventHandlerRegistry;
    private Class<? extends EventBus> parent;
    private EventHandler<?, ? extends EventBus> privateHandlerInstance;

    /**
     * Propagate the event to all handlers.
     *
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.isDefault()) {
            return invokeDefaultMethod(proxy, method, args);
        }

        if (ReflectionUtils.isObjectMethod(method)) {
            return invokeObjectMethod(proxy, method, args);
        }

        Event eventDef = method.getAnnotation(Event.class);
        Assert.notNull(eventDef, "@Event annotation is missing");
        if (logger.isTraceEnabled()) {
            logger.trace("Event received: " + method.getName() + ", annotation: " + eventDef);
        }

        Object result = null;
        if (isEventFiltered(proxy, method, args)) {
            return result;
        }

        invokeNavigationEvent(proxy, method, args);
        result = invokeEventHandlers(proxy, method, args);
        result = invokeForwardToParent(method, args, eventDef, result);

        return result;
    }

    public void setEventHandlerRegistry(EventHandlerRegistry eventHandlerRegistry) {
        this.eventHandlerRegistry = eventHandlerRegistry;
    }

    public void setPrivateHandlerInstance(EventHandler<?, ? extends EventBus> privateHandlerInstance) {
        this.privateHandlerInstance = privateHandlerInstance;
    }

    public void setFilters(Collection<EventFilter> eventFilters) {
        filters.addAll(eventFilters);
    }

    public void setParent(Class<? extends EventBus> parent) {
        this.parent = parent;
    }

    protected Object invokeObjectMethod(Object proxy, Method method, Object[] args) throws Throwable {
        if (ReflectionUtils.isEqualsMethod(method)) {
            return proxy == args[0];

        } else if (ReflectionUtils.isHashCodeMethod(method)) {
            return System.identityHashCode(proxy);

        } else if (ReflectionUtils.isToStringMethod(method)) {
            return proxy.getClass().getInterfaces()[0].getName() + "@" + Integer.toHexString(hashCode());
        }
        return null;
    }

    protected Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
        final Class<?> declaringClass = method.getDeclaringClass();
        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
        constructor.setAccessible(true);
        return constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
                .unreflectSpecial(method, declaringClass)
                .bindTo(proxy)
                .invokeWithArguments(args);
    }

    protected void invokeNavigationEvent(Object proxy, Method method, Object[] args) throws Throwable {
        Event eventDef = method.getAnnotation(Event.class);
        if (eventDef.navigationEvent() && !eventDef.navigateTo().equals(View.class)) {
            String token = "";
            if (!eventDef.historyConverter().equals(Event.NoHistoryConverter.class)) {
                HistoryConverter historyConverter = eventDef.historyConverter().newInstance();
                token = historyConverter.convertToToken(args, (EventBus) proxy);
            }

            SpringView navigationTarget = eventDef.navigateTo().getAnnotation(SpringView.class);

            // TODO inject Navigator
            UI.getCurrent().getNavigator().navigateTo(navigationTarget.name() + token);
        }
    }

    protected Object invokeEventHandlers(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        for (EventHandler eventHandler : buildEventReceiverList(proxy, method)) {
            try {
                // invoke handler; the actual method is the event name prefixed with "on..."
                Method handlerMethod = lookupHandlerMethod(method, method.getName(), eventHandler.getClass());
                if (handlerMethod != null) {
                    result = handlerMethod.invoke(eventHandler, args);
                }
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Failed to propagate event " + method.getName() + " to handler " + eventHandler.getClass().getName());
                }
                throw new RuntimeException("During the invocations of the handler method an exception has occurred", e);
            }
        }
        return result;
    }

    protected Object invokeForwardToParent(Method method, Object[] args, Event eventDef, Object result)
            throws IllegalAccessException, InvocationTargetException {
        if (parent != null && eventDef.forwardToParent()) {
            try {
                EventBus parentEventBus = eventHandlerRegistry.findEventBusByType(parent);
                if (parentEventBus != null) {
                    Method parentMethod = parentEventBus.getClass().getMethod(method.getName(), method.getParameterTypes());
                    result = parentMethod.invoke(parentEventBus, args);
                }
            } catch (NoSuchMethodException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Failed to propagate event [" + method.getName() + "] on parent EventBus [" + parent + "]");
                }
            }
        }
        return result;
    }

    protected boolean isEventFiltered(Object proxy, Method method, Object[] args) throws Throwable {
        for (EventFilter eventFilter : filters) {
            if (eventFilter.filterEvent(method.getName(), args, proxy)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Event " + method.getName() + " filtered by " + eventFilter.getClass().getSimpleName());
                }
                return true;
            }
        }
        return false;
    }

    protected List<EventHandler> buildEventReceiverList(Object proxy, Method method) throws Throwable {
        Event eventDef = method.getAnnotation(Event.class);
        List<EventHandler> eventReceivers = new ArrayList<>();
        Arrays.asList(eventDef.handlers()).forEach(handlerType -> {
            EventHandler handler = null;
            if (isPrivateEventBus(proxy, handlerType)) {
                handler = privateHandlerInstance;
            } else {
                handler = eventHandlerRegistry.lookup(handlerType);
            }
            if (handler == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Handler " + handlerType.getName() + " not registered");
                }
            } else {
                eventReceivers.add(handler);
            }
        });

        // buildBroadcastReceiverList
        if (!eventDef.broadcastTo().equals(Event.NoBroadcast.class)) {
            eventHandlerRegistry
                    .findAll((Class<EventHandler>) eventDef.broadcastTo())
                    .forEach(handler -> {
                        if (!eventReceivers.contains(handler)) {
                            eventReceivers.add(handler);
                        }
                    });
        }

        return eventReceivers;
    }

    protected String buildExpectedEventHandlerMethodName(String eventName) {
        return "on" + StringUtils.capitalize(eventName);
    }

    protected Method lookupHandlerMethod(Method method, String eventName, Class<?> handlerClass) {
        String eventHandlerName = buildExpectedEventHandlerMethodName(method.getName());
        try {
            return handlerClass.getMethod(eventHandlerName, method.getParameterTypes());

        } catch (NoSuchMethodException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(handlerClass.getName()
                        + " defined as a receiver for event "
                        + eventName
                        + " but no method "
                        + eventHandlerName
                        + " could be found with matching arguments");
            }
        }
        return null;
    }

    protected boolean isPrivateEventBus(Object proxy, Class<? extends EventHandler> handlerType) {
        return privateHandlerInstance != null
                && privateHandlerInstance.getClass().equals(handlerType)
                && proxy.getClass().getInterfaces()[0].getAnnotation(Events.class).multiple();
    }

}