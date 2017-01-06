package org.vaadin.mvp.view.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.vaadin.mvp.presenter.EventHandler;

@SuppressWarnings("rawtypes")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UiChild {

    Class<? extends EventHandler> bind() default NoSlot.class;

    interface NoSlot extends EventHandler {

    }
}
