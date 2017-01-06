package org.vaadin.mvp.navigation.history;

import org.vaadin.mvp.eventbus.EventBus;

/**
 * Interface that defines methods to convert a token from/to an event.<br>
 *
 * @author plcoirier
 */
public interface HistoryConverter {

    Object convertFromToken(String token, EventBus eventBus);

    String convertToToken(Object args, EventBus eventBus);
}
