package org.vaadin.mvp.view.event;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.lang.reflect.Proxy;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.vaadin.mvp.eventbus.EventBusInvocationHandler;
import org.vaadin.mvp.test.tools.EventBindingTestEventBus;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

@RunWith(MockitoJUnitRunner.class)
public class EventBusEventBinderTest {

    private EventBindingTestEventBus eventBus = (EventBindingTestEventBus) Proxy.newProxyInstance(
            EventBindingTestEventBus.class.getClassLoader(),
            Stream.of(EventBindingTestEventBus.class)
                    .distinct()
                    .toArray(Class<?>[]::new),
            new EventBusInvocationHandler());

    private EventBinder eventBinder = new EventBusEventBinder(eventBus);

    @Test
    public void bindListener_supportedListeners_eventBinderExceptionNotThrown() {

        try {
            Button component = new Button();
            eventBinder.bindEvent(component, Button.ClickEvent.class, "buttonClick");

            assertFalse(component.getListeners(Button.ClickEvent.class).isEmpty());

        } catch (EventBinderException e) {
            e.printStackTrace();
            fail("Component does support listener but an exception was thrown");
        }
    }

    // TODO find a way to get the listener of an event to check if it's supported in EventBinder
    @Ignore
    @Test(expected = EventBinderException.class)
    public void bindListener_unsupportedListener_eventBinderExceptionThrown() throws EventBinderException {
        eventBinder.bindEvent(new Label(), Button.ClickEvent.class, "buttonClick");
    }

    @Test(expected = EventBinderException.class)
    public void bindListener_bindToMethodMissingFromEventBus_eventBinderExceptionThrown() throws EventBinderException {
        eventBinder.bindEvent(new Button(), Button.ClickEvent.class, "bindToMethodMissingFromEventBus");
    }

}
