package org.vaadin.mvp.view.handler;

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vaadin.mvp.view.annotation.UiField;
import org.vaadin.mvp.view.binder.UiBinderException;
import org.vaadin.mvp.view.event.EventBinder;

import com.vaadin.ui.Component;

public class UiFieldAnnotationHandler implements AnnotationHandler {

    private static final Log logger = LogFactory.getLog(UiFieldAnnotationHandler.class.getName());

    @Override
    public Object handle(Field field, Object component, Object view, EventBinder eventBinder) throws UiBinderException {
        UiField uiField = field.getAnnotation(UiField.class);
        if (uiField != null && !uiField.provided()) {
            component = this.newComponentInstance(field, view);
        }
        return component;
    }

    @SuppressWarnings("unchecked")
    private <T extends Component> T newComponentInstance(Field field, Object view) throws UiBinderException {
        Component component = null;
        if (view != null && field.isAnnotationPresent(UiField.class)) {
            try {
                Class<?> uiClass = field.getType();
                if (logger.isTraceEnabled()) {
                    logger.trace("Creating component with class: " + uiClass);
                }
                component = (Component) uiClass.newInstance();
                field.set(view, component);

            } catch (InstantiationException | IllegalAccessException e) {
                throw new UiBinderException("Cannot instantiate component type: " + view, e);
            }
        }
        return (T) component;
    }

}
