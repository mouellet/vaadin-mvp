package org.vaadin.mvp.view.binder;

public class UiBinderException extends Exception {

    private static final long serialVersionUID = 1L;

    public UiBinderException() {
    }

    public UiBinderException(String message) {
        super(message);
    }

    public UiBinderException(Throwable cause) {
        super(cause);
    }

    public UiBinderException(String message, Throwable cause) {
        super(message, cause);
    }

}
