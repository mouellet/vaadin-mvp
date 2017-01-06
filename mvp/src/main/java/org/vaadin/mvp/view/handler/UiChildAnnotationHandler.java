package org.vaadin.mvp.view.handler;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.EventHandler;
import org.vaadin.mvp.presenter.factory.PresenterFactory;
import org.vaadin.mvp.view.annotation.UiChild;
import org.vaadin.mvp.view.binder.UiBinder;
import org.vaadin.mvp.view.binder.UiBinderException;
import org.vaadin.mvp.view.event.EventBinder;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.SingleComponentContainer;

/**
 * The {@link UiChildAnnotationHandler} is responsible for propagating binding
 * to fields marked with @UiChild annotation.
 *
 * @author Mathieu Ouellet
 *
 */
public class UiChildAnnotationHandler implements AnnotationHandler {

    @Lazy
    @Autowired
    private UiBinder uiBinder;

    @Lazy
    @Autowired
    private PresenterFactory presenterFactory;

    @Override
    public Object handle(Field field, Object component, Object view, EventBinder eventBinder) throws UiBinderException {
        UiChild uiChild = field.getAnnotation(UiChild.class);
        if (uiChild != null) {
            if (!uiChild.bind().equals(UiChild.NoSlot.class)) {
                component = handlePresenterSlot(field, component, view, uiChild);

            } else {
                component = handleUiChildView(field, component, view, eventBinder);
            }
        }
        return component;
    }

    public void setUiBinder(UiBinder uiBinder) {
        this.uiBinder = uiBinder;
    }

    private Object handlePresenterSlot(Field field, Object component, Object view, UiChild uiChild) throws UiBinderException {
        try {
            EventHandler<?, ? extends EventBus> presenterWidget = presenterFactory.createAndBind(uiChild.bind());
            component = presenterWidget.getView();
            field.set(view, component);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new UiBinderException(e);
        }
        return component;
    }

    private Object handleUiChildView(Field field, Object component, Object view, EventBinder eventBinder) throws UiBinderException {
        Object fieldValue = null;
        try {
            fieldValue = field.get(view);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new UiBinderException(e);
        }
        if (fieldValue != null) {
            if (isBindable(view, component)) {
                uiBinder.createAndBind((Component) component, eventBinder);
            }
        }
        return component;
    }

    private boolean isBindable(Object view, Object component) {
        return (view instanceof AbstractComponent
                && (view instanceof ComponentContainer
                        || view instanceof SingleComponentContainer
                        || view instanceof CustomComponent))
                && (component instanceof AbstractComponent
                        && (component instanceof ComponentContainer
                                || component instanceof SingleComponentContainer
                                || component instanceof CustomComponent));
    }

}
