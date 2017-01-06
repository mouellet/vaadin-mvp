package org.vaadin.mvp.eventbus;

/**
 * This interface can be implemented in order to apply filters to events from an
 * EventBus. <br/>
 * <br/>
 * Classes that implement this interface are associated with an EventBus class
 * by adding the filter class literal to the filterClasses list in the EventBus' @Filters
 * annotation <br/>
 *
 * @author Mathieu Ouellet
 */
public interface EventFilter {

    /**
     * Filter an event
     *
     * @param eventName
     *            name of the event to filter
     * @param params
     *            objects sent with the event
     * @param eventBus
     *            event bus used to fire the event
     * @return false if event should be stopped, true otherwise
     */
    boolean filterEvent(String eventName, Object[] params, Object eventBus);
}
