package org.vaadin.mvp.test;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventFilter;
import org.vaadin.mvp.eventbus.annotation.Event;
import org.vaadin.mvp.eventbus.annotation.Events;
import org.vaadin.mvp.presenter.AbstractPresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

public abstract class Util {

    @Events
    public interface MissingAnnotationPresenterEventBus extends EventBus {

        public static class MissingAnnotationPresenter extends AbstractPresenter<MissingAnnotationPresenter.View, MissingAnnotationPresenterEventBus> {

            public static class View extends CssLayout {
            }
        }
    }

    @Events
    public interface SimpleEventBus extends EventBus {

        void eventMissingEventAnnotation();

        @Event
        void eventWithoutHandlers();

        @Event(handlers = SimplePresenter.class)
        void eventWithSingleHandler();

        @Event(handlers = SimplePresenter.class)
        void eventNotDefinedOnHandler();

        @Event(handlers = SimplePresenter.class)
        void eventDefinedWithMismatchingArguments(String name, Button.ClickEvent event);

        @Event(handlers = SimplePresenter.class)
        String eventWithReturningValue();

        @Event(handlers = { SimplePresenter.class, OtherEventBus.OtherPresenter.class })
        void eventWithHandlers();

        @Presenter(view = SimplePresenter.View.class)
        public static class SimplePresenter extends AbstractPresenter<SimplePresenter.View, SimpleEventBus> {

            public static class View extends VerticalLayout {
            }

            public void onEventWithSingleHandler() {
            }

            public void onEventDefinedWithMismatchingArguments(Button.ClickEvent event, String name) {
            }

            public String onEventWithReturningValue() {
                return "test";
            }

            public void onEventWithHandlers() {
            }
        }

        @Presenter
        public static class SimpleEventHandler extends AbstractPresenter<Presenter.NoView, SimpleEventBus> {
        }
    }

    @Events
    public interface OtherEventBus extends EventBus {

        @Presenter(view = OtherPresenter.View.class)
        public static class OtherPresenter extends AbstractPresenter<OtherPresenter.View, OtherEventBus> {

            public static class View extends VerticalLayout {
            }

            public void onEventWithHandlers() {
            }
        }
    }

    @Events
    public interface ParentEventBus extends EventBus {

        @Event(handlers = ParentPresenter.class)
        void eventWithHandlerAndForwardingToParent();

        @Event(handlers = ParentPresenter.class)
        void eventWithHandlerNotForwardingToParent();

        @Event(handlers = ParentPresenter.class)
        String eventWithHandlerMethodReturningValueAndForwardingToParent();

        @Presenter(view = ParentPresenter.View.class)
        public static class ParentPresenter extends AbstractPresenter<ParentPresenter.View, ParentEventBus> {

            public static class View extends VerticalLayout {
            }

            public void onEventWithHandlerAndForwardingToParent() {
            }

            public void onEventWithHandlerNotForwardingToParent() {
            }

            public String onEventWithHandlerMethodReturningValueAndForwardingToParent() {
                return "parent";
            }
        }
    }

    @Events(parent = ParentEventBus.class)
    public interface ChildEventBus extends EventBus {

        @Event(handlers = ChildPresenter.class, forwardToParent = true)
        void eventWithHandlerAndForwardingToParent();

        @Event(handlers = ChildPresenter.class)
        void eventWithHandlerNotForwardingToParent();

        @Event(handlers = ChildPresenter.class, forwardToParent = true)
        String eventWithHandlerMethodReturningValueAndForwardingToParent();

        @Presenter(view = ChildPresenter.View.class)
        public static class ChildPresenter extends AbstractPresenter<ChildPresenter.View, ChildEventBus> {

            public static class View extends VerticalLayout {
            }

            public void onEventWithHandlerAndForwardingToParent() {
            }

            public void onEventWithHandlerNotForwardingToParent() {
            }

            public String onEventWithHandlerMethodReturningValueAndForwardingToParent() {
                return "child";
            }
        }
    }

    @Events(multiple = true)
    public interface PrivateEventBus extends EventBus {

        @Presenter
        public static class PrivatePresenter extends AbstractPresenter<Presenter.NoView, PrivateEventBus> {
        }
    }

    @Events(filterClasses = {
            FilterEventBus.EventNameBasedFilter.class,
            FilterEventBus.EventFiringFilter.class })
    public interface FilterEventBus extends EventBus {

        @Event(handlers = FilterPresenter.class)
        void eventWithoutFilters();

        @Event(handlers = FilterPresenter.class)
        void eventWithEventNameBasedFilter();

        @Event(handlers = FilterPresenter.class)
        void eventWithEventBusFiringFilter();

        @Event(handlers = FilterPresenter.class)
        void eventFiredFromFilter();

        @Presenter(view = FilterPresenter.View.class)
        public static class FilterPresenter extends AbstractPresenter<FilterPresenter.View, FilterEventBus> {

            public static class View extends VerticalLayout {
            }

            public void onEventWithoutFilters() {
            }

            public void onEventWithEventNameBasedFilter() {
            }

            public void onEventWithEventBusFiringFilter() {
            }

            public void onEventFiredFromFilter() {
            }
        }

        public class EventNameBasedFilter implements EventFilter {

            @Override
            public boolean filterEvent(String eventName, Object[] params, Object eventBus) {
                return "eventWithEventNameBasedFilter".equals(eventName) ? true : false;
            }
        }

        public class EventFiringFilter implements EventFilter {

            @Override
            public boolean filterEvent(String eventName, Object[] params, Object eventBus) {
                if ("eventWithEventBusFiringFilter".equals(eventName) && eventBus instanceof FilterEventBus) {
                    ((FilterEventBus) eventBus).eventFiredFromFilter();
                    return true;
                }
                return false;
            }
        }
    }

}
