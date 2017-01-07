package org.vaadin.mvp.eventbus;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.vaadin.mvp.test.Util.ChildEventBus;
import org.vaadin.mvp.test.Util.FilterEventBus;
import org.vaadin.mvp.test.Util.OtherEventBus;
import org.vaadin.mvp.test.Util.ParentEventBus;
import org.vaadin.mvp.test.Util.SimpleEventBus;
import org.vaadin.mvp.test.Util.ChildEventBus.ChildPresenter;
import org.vaadin.mvp.test.Util.FilterEventBus.EventFiringFilter;
import org.vaadin.mvp.test.Util.FilterEventBus.EventNameBasedFilter;
import org.vaadin.mvp.test.Util.FilterEventBus.FilterPresenter;
import org.vaadin.mvp.test.Util.OtherEventBus.OtherPresenter;
import org.vaadin.mvp.test.Util.ParentEventBus.ParentPresenter;
import org.vaadin.mvp.test.Util.SimpleEventBus.SimplePresenter;

import com.vaadin.ui.Button;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class EventBusInvocationHandlerTest {

    private EventHandlerRegistry eventHandlerRegistry;

    private EventBusManager eventBusManager;

    @Before
    public void setUp() {
        eventHandlerRegistry = mock(EventHandlerRegistry.class);
        eventBusManager = spy(EventBusManager.class);
    }

    public class EventHandling {

        SimpleEventBus eventBus;
        SimplePresenter presenter;
        EventBusInvocationHandler invocationHandler;

        @Before
        public void setUp() {
            presenter = mock(SimplePresenter.class);
            when(eventHandlerRegistry.lookup(SimplePresenter.class)).thenReturn(presenter);

            invocationHandler = spy(EventBusInvocationHandler.class);
            invocationHandler.setEventHandlerRegistry(eventHandlerRegistry);

            eventBus = eventBusManager.createEventBus(SimpleEventBus.class, invocationHandler);
        }

        @Test
        public void isToStringMethod_printsEventBusName() throws Throwable {

            String result = eventBus.toString();

            verify(invocationHandler).invokeObjectMethod(any(), any(), any());

            assertTrue(result.contains("SimpleEventBus"));
        }

        @Test(expected = IllegalArgumentException.class)
        public void missingEventAnnotationOnMethod_throwsRuntimeException() {
            eventBus.eventMissingEventAnnotation();
        }

        @Test
        public void eventWithoutHandlers_doesNothing() throws Throwable {

            eventBus.eventWithoutHandlers();

            verifyZeroInteractions(presenter);
        }

        @Test
        public void eventWithSingleHandler_routedCorrectly() throws Throwable {

            eventBus.eventWithSingleHandler();

            verify(presenter).onEventWithSingleHandler();
        }

        @Test
        public void eventWithHandler_handlerMethodNotDefined_continuesAndDoesNothing() throws Throwable {

            eventBus.eventNotDefinedOnHandler();

            verifyZeroInteractions(presenter);
        }

        @Test
        public void eventWithHandler_handlerMethodDefinedWithMismatchingArguments_continuesAndDoesNothing() {

            eventBus.eventDefinedWithMismatchingArguments("", new Button.ClickEvent(new Button()));

            verifyZeroInteractions(presenter);
        }

        @Test
        public void eventWithHandler_handlerMethodReturningValue_returnsValue() {

            when(presenter.onEventWithReturningValue()).thenReturn("test");

            String returningValue = eventBus.eventWithReturningValue();

            verify(presenter).onEventWithReturningValue();

            assertNotNull(returningValue);
            assertTrue("test".equals(returningValue));
        }

        public class Multiple {

            OtherEventBus otherEventBus;
            OtherPresenter otherPresenter;
            EventBusInvocationHandler otherEventBusInvocationHandler;

            @Before
            public void setUp() {
                otherPresenter = mock(OtherPresenter.class);
                when(eventHandlerRegistry.lookup(OtherPresenter.class)).thenReturn(otherPresenter);

                otherEventBusInvocationHandler = spy(EventBusInvocationHandler.class);
                otherEventBusInvocationHandler.setEventHandlerRegistry(eventHandlerRegistry);

                otherEventBus = eventBusManager.createEventBus(OtherEventBus.class, otherEventBusInvocationHandler);
            }

            @Test
            public void eventWithHandlers_routedCorrectly() {

                eventBus.eventWithHandlers();

                verify(presenter).onEventWithHandlers();
                verify(otherPresenter).onEventWithHandlers();
            }

            @Test
            public void eventWithHandlers_callOrderIsRespected() {

                eventBus.eventWithHandlers();

                InOrder inOrder = inOrder(presenter, otherPresenter);
                inOrder.verify(presenter).onEventWithHandlers();
                inOrder.verify(otherPresenter).onEventWithHandlers();
            }

            @Test
            public void eventWithHandlers_someNotRegistered_continuesAndDoesNothing() {

                when(eventHandlerRegistry.lookup(OtherPresenter.class)).thenReturn(null);

                eventBus.eventWithHandlers();

                verify(presenter).onEventWithHandlers();
                verify(otherPresenter, never()).onEventWithHandlers();
            }
        }
    }

    public class Parent {

        ParentEventBus parentEventBus;
        ParentPresenter parentPresenter;
        EventBusInvocationHandler parentEventBusInvocationHandler;

        @Before
        public void setUp() {

            parentPresenter = mock(ParentPresenter.class);

            when(eventHandlerRegistry.lookup(ParentPresenter.class)).thenReturn(parentPresenter);

            parentEventBusInvocationHandler = spy(EventBusInvocationHandler.class);
            parentEventBusInvocationHandler.setEventHandlerRegistry(eventHandlerRegistry);

            parentEventBus = eventBusManager.createEventBus(ParentEventBus.class, parentEventBusInvocationHandler);
        }

        public class Child {

            ChildEventBus childEventBus;
            ChildPresenter childPresenter;
            EventBusInvocationHandler childEventBusInvocationHandler;

            @Before
            public void setUp() {

                childPresenter = mock(ChildPresenter.class);

                when(eventHandlerRegistry.lookup(ChildPresenter.class)).thenReturn(childPresenter);

                childEventBusInvocationHandler = spy(EventBusInvocationHandler.class);
                childEventBusInvocationHandler.setEventHandlerRegistry(eventHandlerRegistry);
                childEventBusInvocationHandler.setParent(ParentEventBus.class);

                childEventBus = eventBusManager.createEventBus(ChildEventBus.class, childEventBusInvocationHandler);
            }

            @Test
            public void eventWithHandler_forwardingToParent_successfullyForwarded() throws Throwable {

                when(eventHandlerRegistry.findEventBusByType(ParentEventBus.class)).thenReturn(parentEventBus);

                childEventBus.eventWithHandlerAndForwardingToParent();

                InOrder inOrder = inOrder(childPresenter, parentPresenter);
                inOrder.verify(childPresenter).onEventWithHandlerAndForwardingToParent();
                inOrder.verify(parentPresenter).onEventWithHandlerAndForwardingToParent();
            }

            // TODO invoke_eventForwardingToParent_withoutParentEventBus_

            @Test
            public void eventWithHandler_notForwardingToParentButHasParentEventBus_eventNotForwardedToParent() throws Throwable {

                childEventBus.eventWithHandlerNotForwardingToParent();

                verify(childPresenter).onEventWithHandlerNotForwardingToParent();

                verifyZeroInteractions(parentPresenter);
            }

            // TODO invoke_eventNotForwardingToParent_withoutParentEventBus_

            @Test
            public void eventWithHandler_methodReturningValueAndForwardingToParent_returnsParentValue() {

                when(eventHandlerRegistry.findEventBusByType(ParentEventBus.class)).thenReturn(parentEventBus);

                when(childPresenter.onEventWithHandlerMethodReturningValueAndForwardingToParent()).thenReturn("child");
                when(parentPresenter.onEventWithHandlerMethodReturningValueAndForwardingToParent()).thenReturn("parent");

                String returningValue = childEventBus.eventWithHandlerMethodReturningValueAndForwardingToParent();

                InOrder inOrder = inOrder(childPresenter, parentPresenter);
                inOrder.verify(childPresenter).onEventWithHandlerMethodReturningValueAndForwardingToParent();
                inOrder.verify(parentPresenter).onEventWithHandlerMethodReturningValueAndForwardingToParent();

                assertNotNull(returningValue);
                assertTrue("parent".equals(returningValue));
            }
        }
    }

    public class Filter {

        FilterEventBus filterEventBus;
        FilterPresenter filterPresenter;
        EventBusInvocationHandler filterEventBusInvocationHandler;

        @Before
        public void setUp() {
            filterPresenter = mock(FilterPresenter.class);
            when(eventHandlerRegistry.lookup(FilterPresenter.class)).thenReturn(filterPresenter);

            filterEventBusInvocationHandler = spy(EventBusInvocationHandler.class);
            filterEventBusInvocationHandler.setEventHandlerRegistry(eventHandlerRegistry);
            filterEventBusInvocationHandler.setFilters(Arrays.asList(new EventNameBasedFilter(), new EventFiringFilter()));

            filterEventBus = eventBusManager.createEventBus(FilterEventBus.class, filterEventBusInvocationHandler);
        }

        @Test
        public void eventWithoutFilters_eventPropagateSuccessfully() {

            filterEventBus.eventWithoutFilters();

            verify(filterPresenter).onEventWithoutFilters();
        }

        @Test
        public void eventWithFilters_eventNameBaseFilter_eventFilteredSuccessfully() {

            filterEventBus.eventWithEventNameBasedFilter();

            verify(filterPresenter, never()).onEventWithEventNameBasedFilter();
        }

        @Test
        public void eventWithFilters_filterFiringEventFromEventBus_otherEventSuccessfullyFired() {

            filterEventBus.eventWithEventBusFiringFilter();

            verify(filterPresenter, never()).onEventWithEventBusFiringFilter();
            verify(filterPresenter).onEventFiredFromFilter();
        }
    }

}
