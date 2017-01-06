package org.vaadin.mvp.view.binder;

/**
 * Interface for UiBinder type views which should be additionally initialized.
 * Implicitly adds the UiBindable interface.
 *
 * @author silvan
 */
public interface UiInitializable {

    /**
     * Method will be called after the ui binding to perform additional
     * programmatic initialization.
     */
    void init();
}
