package org.vaadin.mvp.eventbus;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.vaadin.mvp.eventbus.annotation.Events;
import org.vaadin.mvp.presenter.AbstractPresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

@SuppressWarnings("rawtypes")
@RunWith(MockitoJUnitRunner.class)
public class EventHandlerRegistryTest {

    @Test
    public void register_whenEventHandlerArgNotNull_thenSuccessfullyRegistered() {

        SimpleEventBus.EventHandler result = new SimpleEventBus.EventHandler();
        EventHandlerRegistry registry = new EventHandlerRegistry();
        registry.register(result);

        assertTrue(((Map) ReflectionTestUtils.getField(registry, "registry")).containsKey(SimpleEventBus.EventHandler.class));
        assertTrue(((Map) ReflectionTestUtils.getField(registry, "registry")).containsValue(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void register_whenEventHandlerArgIsNull_thenThrowsIllegalArgumentException() {

        new EventHandlerRegistry().register(null);
    }

    @Test
    public void lookup_whendEventHandlerIsRegistered_thenReturnsRegisteredInstance() {

        EventHandlerRegistry registry = new EventHandlerRegistry();
        registry.register(new SimpleEventBus.EventHandler());

        SimpleEventBus.EventHandler result = registry.lookup(SimpleEventBus.EventHandler.class);
        assertNotNull(result);
    }

    @Test
    public void lookup_whenEventHandlerNotRegistered_thenReturnsNull() {

        SimpleEventBus.EventHandler result = new EventHandlerRegistry().lookup(SimpleEventBus.EventHandler.class);
        assertNull(result);
    }

    @Test
    public void findAll_whenEventHandlersAreRegistered_thenReturnsAllRegisteredInstances() {

        SimpleEventBus.EventHandler simpleEventHandler = new SimpleEventBus.EventHandler();
        EventHandlerRegistry registry = new EventHandlerRegistry();
        registry.register(simpleEventHandler);

        OtherEventBus.EventHandler otherEventHandler = new OtherEventBus.EventHandler();
        registry.register(otherEventHandler);

        List<SimpleEventBus.EventHandler> result = registry.findAll(SimpleEventBus.EventHandler.class);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains(simpleEventHandler));
        assertFalse(result.contains(otherEventHandler));
    }

    @Test
    public void findAll_whenIsGenericOfRegisteredEventHandler_returnsAllRegisteredConcreteInstances() {

        SimpleEventBus.EventHandler simpleEventHandler = new SimpleEventBus.EventHandler();
        EventHandlerRegistry registry = new EventHandlerRegistry();
        registry.register(simpleEventHandler);

        ChildEventBus.EventHandler childEventHandler = new ChildEventBus.EventHandler();
        registry.register(childEventHandler);

        List<SimpleEventBus.EventHandler> result = registry.findAll(SimpleEventBus.EventHandler.class);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains(simpleEventHandler));
        assertTrue(result.contains(childEventHandler));
    }

    @Test
    public void findAll_whenEventHandlerNotRegistered_thenReturnsEmptyList() {

        SimpleEventBus.EventHandler simpleEventHandler = new SimpleEventBus.EventHandler();
        EventHandlerRegistry registry = new EventHandlerRegistry();
        registry.register(simpleEventHandler);

        ChildEventBus.EventHandler childEventHandler = new ChildEventBus.EventHandler();
        registry.register(childEventHandler);

        List<OtherEventBus.EventHandler> result = registry.findAll(OtherEventBus.EventHandler.class);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findEventBusByType_whenEventHandlerIsRegistered_thenReturnsEventBusOfRegisteredInstance() {

        SimpleEventBus eventBus = mock(SimpleEventBus.class);
        SimpleEventBus.EventHandler eventHandler = new SimpleEventBus.EventHandler();
        eventHandler.setEventBus(eventBus);

        EventHandlerRegistry registry = new EventHandlerRegistry();
        registry.register(eventHandler);

        SimpleEventBus result = registry.findEventBusByType(SimpleEventBus.class);
        assertNotNull(result);
        assertTrue(eventHandler.getEventBus().equals(result));
    }

    @Test
    public void findEventBusByType_whenEventHandlerNotRegistered_thenReturnsNull() {

        EventHandlerRegistry registry = new EventHandlerRegistry();
        SimpleEventBus result = registry.findEventBusByType(SimpleEventBus.class);
        assertNull(result);
    }

    @Test
    public void findEventBusByType_whenEventBusTypeArgIsNull_thenReturnsNull() {

        EventHandlerRegistry registry = new EventHandlerRegistry();
        SimpleEventBus result = registry.findEventBusByType(null);
        assertNull(result);
    }

    @Test
    public void unregister_whenEventHandlerIsRegistered_thenSuccessfullyUnregistered() {

        SimpleEventBus.EventHandler result = new SimpleEventBus.EventHandler();
        EventHandlerRegistry registry = new EventHandlerRegistry();
        registry.register(result);
        registry.unregister(result);

        assertFalse(((Map) ReflectionTestUtils.getField(registry, "registry")).containsKey(SimpleEventBus.EventHandler.class));
        assertFalse(((Map) ReflectionTestUtils.getField(registry, "registry")).containsValue(result));
    }

    @Test
    public void unregister_whenEventHandlerNotRegistered_thenDoesNothing() {

        SimpleEventBus.EventHandler result = new SimpleEventBus.EventHandler();
        EventHandlerRegistry registry = new EventHandlerRegistry();
        registry.unregister(result);

        assertFalse(((Map) ReflectionTestUtils.getField(registry, "registry")).containsKey(SimpleEventBus.EventHandler.class));
        assertFalse(((Map) ReflectionTestUtils.getField(registry, "registry")).containsValue(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void unregister_whenEventHandlerArgIsNull_thenThrowsIllegalArgumentException() {

        new EventHandlerRegistry().register(null);
    }

    @Events
    static interface SimpleEventBus extends EventBus {

        @Presenter
        static class EventHandler extends AbstractPresenter<Presenter.NoView, SimpleEventBus> {
        }
    }

    @Events
    static interface ChildEventBus extends SimpleEventBus {

        @Presenter
        static class EventHandler extends SimpleEventBus.EventHandler {
        }
    }

    @Events
    static interface OtherEventBus extends EventBus {

        @Presenter
        static class EventHandler extends AbstractPresenter<Presenter.NoView, OtherEventBus> {
        }
    }

}
