package org.vaadin.mvp.view.handler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vaadin.mvp.view.annotation.Callback;
import org.vaadin.mvp.view.annotation.Callbacks;
import org.vaadin.mvp.view.annotation.UiField;
import org.vaadin.mvp.view.binder.UiBinderException;
import org.vaadin.mvp.view.event.EventBinder;
import org.vaadin.mvp.view.event.EventBinderException;

/**
 * The {@link CallbackAnnotationHandler} is responsible for binding listeners to
 * components marked with @Callbacks or @Callback annotation.
 *
 * @author Mathieu Ouellet
 *
 */
public class CallbackAnnotationHandler implements AnnotationHandler {

    @Override
    public Object handle(Field field, Object component, Object view, EventBinder eventBinder) throws UiBinderException {

        List<Callback> callbacks = new ArrayList<>();
        Callback callbackDef = field.getAnnotation(Callback.class);
        if (callbackDef != null) {
            callbacks.add(callbackDef);
        }

        Callbacks callbacksDef = field.getAnnotation(Callbacks.class);
        if (callbacksDef != null) {
            callbacks.addAll(Arrays.asList(callbacksDef.value()));
        }

        UiField uiFieldDef = field.getAnnotation(UiField.class);
        if (uiFieldDef != null && uiFieldDef.callbacks().length > 0) {
            callbacks.addAll(Arrays.asList(uiFieldDef.callbacks()));
        }

        for (Callback callback : callbacks) {
            try {
                eventBinder.bindEvent(component, callback.event(), callback.to());
            } catch (EventBinderException e) {
                throw new UiBinderException(e);
            }
        }
        return component;
    }

}
