package org.vaadin.mvp.view.handler;

import java.lang.reflect.Field;

import org.vaadin.mvp.view.binder.UiBinderException;
import org.vaadin.mvp.view.event.EventBinder;

public interface AnnotationHandler {

    Object handle(Field field, Object component, Object view, EventBinder eventBinder) throws UiBinderException;
}
