package org.vaadin.mvp.eventbus;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.vaadin.mvp.eventbus.annotation.Events;
import org.vaadin.mvp.presenter.AbstractPresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

@RunWith(MockitoJUnitRunner.class)
public class EventBusManagerTest {

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private EventHandlerRegistry eventHandlerRegistry;

    @Mock
    private EventBusInvocationHandler invocationHandler;

    @Spy
    @InjectMocks
    private EventBusManager eventBusManager;

    @Test
    public void create_whenEventBusIsValid_thenEventBusSuccessfullyRegistered() {

        when(eventBusManager.getEventBusInvocationHandler()).thenReturn(invocationHandler);

        SimpleEventBus eventBus = eventBusManager.create(new SimpleEventBus.EventHandler());

        assertNotNull(eventBus);

        verify(eventBusManager).getEventBusInvocationHandler();
        verify(eventBusManager).createEventBus(SimpleEventBus.class, invocationHandler);

        verify(eventBusManager).registerEventHandler(any(), any(Events.class), eq(invocationHandler));
        verify(eventHandlerRegistry).register(any());
        verify(invocationHandler, times(0)).setPrivateHandlerInstance(any());
        verify(invocationHandler).setEventHandlerRegistry(eventHandlerRegistry);

        verify(eventBusManager).registerEventFilters(any(Events.class), eq(invocationHandler));
        verify(applicationContext).getBeansOfType(EventFilter.class);
        verify(invocationHandler, times(0)).setFilters(anySetOf(EventFilter.class));

        verify(eventBusManager).registerParentEventBus(any(Events.class), eq(invocationHandler));
        verify(invocationHandler, times(0)).setParent(any());
    }

    @Test
    public void create_whenEventBusIsPrivate_thenEventBusSuccessfullyRegisteredWithPrivateHandler() {

        when(eventBusManager.getEventBusInvocationHandler()).thenReturn(invocationHandler);

        EventBus eventBus = eventBusManager.create(new PrivateEventBus.EventHandler());

        assertNotNull(eventBus);

        verify(invocationHandler).setPrivateHandlerInstance(any());
    }

    @Test
    public void create_whenEventBusHasEventFilters_thenEventBusSuccessfullyRegisteredWithEventFilters() {

        when(eventBusManager.getEventBusInvocationHandler()).thenReturn(invocationHandler);

        when(applicationContext.getBeansOfType(EventFilter.class)).thenReturn(Collections.singletonMap("eventFilter", new FiltersEventBus.EventFilterClass()));

        EventBus eventBus = eventBusManager.create(new FiltersEventBus.EventHandler());

        assertNotNull(eventBus);

        verify(invocationHandler).setFilters(anySetOf(EventFilter.class));
    }

    @Test
    public void create_whenEventBusHasParent_thenEventBusSuccessfullyRegisteredWithParent() {

        when(eventBusManager.getEventBusInvocationHandler()).thenReturn(invocationHandler);

        EventBus eventBus = eventBusManager.create(new ChildEventBus.EventHandler());

        assertNotNull(eventBus);

        verify(invocationHandler).setParent(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_whenEventHandlerArguementIsNull_thenThrowsIllegalArgumentException() {
        eventBusManager.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_whenEventsAnnotationMissingFromEventBus_thenThrowsIllegalArgumentException() {
        eventBusManager.create(new EventsAnnotationMissingEventBus.EventHandler());
    }

    @Test
    public void unregister_whenEventBusIsRegistered_thenEventBusSuccessfullyUnregistered() {
        SimpleEventBus bus = mock(SimpleEventBus.class);
        SimpleEventBus.EventHandler handler = mock(SimpleEventBus.EventHandler.class);
        when(handler.getEventBus()).thenReturn(bus);

        eventBusManager.unregister(handler);

        verify(eventHandlerRegistry).unregister(handler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void unregister_whenEventHandlerArguementIsNull_thenThrowsIllegalArgumentException() {
        eventBusManager.unregister(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void unregister_whenEventBusNotRegistered_thenThrowsIllegalArgumentException() {
        eventBusManager.unregister(new SimpleEventBus.EventHandler());
    }

    @Events
    static interface SimpleEventBus extends EventBus {

        @Presenter
        static class EventHandler extends AbstractPresenter<Presenter.NoView, SimpleEventBus> {
        }
    }

    @Events(multiple = true)
    static interface PrivateEventBus extends EventBus {

        @Presenter
        static class EventHandler extends AbstractPresenter<Presenter.NoView, PrivateEventBus> {
        }
    }

    @Events(filterClasses = FiltersEventBus.EventFilterClass.class)
    static interface FiltersEventBus extends EventBus {

        @Presenter
        static class EventHandler extends AbstractPresenter<Presenter.NoView, FiltersEventBus> {
        }

        static class EventFilterClass implements EventFilter {

            @Override
            public boolean filterEvent(String eventName, Object[] params, Object eventBus) {
                return false;
            }
        }
    }

    @Events(parent = EventBus.class)
    static interface ChildEventBus extends EventBus {

        @Presenter
        static class EventHandler extends AbstractPresenter<Presenter.NoView, ChildEventBus> {
        }
    }

    static interface EventsAnnotationMissingEventBus extends EventBus {

        @Presenter
        static class EventHandler extends AbstractPresenter<Presenter.NoView, EventsAnnotationMissingEventBus> {
        }
    }

}
