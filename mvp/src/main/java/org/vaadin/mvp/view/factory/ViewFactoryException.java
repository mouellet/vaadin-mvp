package org.vaadin.mvp.view.factory;

public class ViewFactoryException extends Exception {

    private static final long serialVersionUID = 1L;

    public ViewFactoryException() {
    }

    public ViewFactoryException(String message) {
        super(message);
    }

    public ViewFactoryException(Throwable cause) {
        super(cause);
    }

    public ViewFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

}
