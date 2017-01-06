package org.vaadin.mvp.eventbus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.util.Assert;
import org.vaadin.mvp.presenter.EventHandler;
import org.vaadin.mvp.presenter.annotation.Presenter;

@SuppressWarnings("rawtypes")
public class EventHandlerRegistry {

    private final Map<Class<?>, EventHandler> registry = new ConcurrentHashMap<>();

    public void register(EventHandler eventHandler) {

        Assert.notNull(eventHandler, "eventHandler must not be null");

        registry.put(eventHandler.getClass(), eventHandler);
    }

    public <T extends EventHandler> T lookup(Class<T> type) {
        return registry.entrySet()
                .stream()
                .filter(entry -> type.isAssignableFrom(entry.getKey()))
                .map(entry -> entry.getValue())
                .map(handlerType -> type.cast(handlerType))
                .findFirst()
                .orElse((T) null);
    }

    public <T extends EventHandler> List<T> findAll(Class<T> type) {
        return registry.entrySet()
                .stream()
                .filter(entry -> type.isAssignableFrom(entry.getKey()))
                .map(entry -> entry.getValue())
                .map(handlerType -> type.cast(handlerType))
                .collect(Collectors.toList());
    }

    public <E extends EventBus> E findEventBusByType(final Class<E> eventBusType) {
        return registry.values()
                .stream()
                .map(handler -> handler.getEventBus())
                .filter(eventBus -> eventBusType.isAssignableFrom(eventBus.getClass()))
                .map(type -> eventBusType.cast(type))
                .findFirst()
                .orElse(null);
    }

    public Optional<EventHandler> findByViewType(final Class<?> viewType) {
        return registry.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(Presenter.class))
                .filter(entry -> !entry.getKey().getAnnotation(Presenter.class).equals(Presenter.NoView.class))
                .filter(entry -> viewType.equals(entry.getKey().getAnnotation(Presenter.class).view()))
                .map(entry -> entry.getValue())
                .findFirst();
    }

    public void unregister(EventHandler eventHandler) {

        Assert.notNull(eventHandler, "eventHandler must not be null");

        registry.remove(eventHandler.getClass());
    }

}
