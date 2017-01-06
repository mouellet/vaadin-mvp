package org.vaadin.mvp.navigation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.vaadin.mvp.navigation.history.HistoryConverter;
import org.vaadin.mvp.presenter.EventHandler;

@SuppressWarnings("rawtypes")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {

    Class<? extends EventHandler> startPresenter();

    Class<? extends HistoryConverter> historyConverter() default NoHistoryConverter.class;

    interface NoHistoryConverter extends HistoryConverter {
    }
}
