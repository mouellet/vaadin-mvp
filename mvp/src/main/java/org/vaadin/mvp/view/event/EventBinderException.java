package org.vaadin.mvp.view.event;

public class EventBinderException extends Exception {

    private static final long serialVersionUID = 1L;

    public EventBinderException() {
    }

    public EventBinderException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventBinderException(String message) {
        super(message);
    }

    public EventBinderException(Throwable cause) {
        super(cause);
    }

}
