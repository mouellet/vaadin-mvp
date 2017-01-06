package org.vaadin.mvp.eventbus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.vaadin.mvp.navigation.history.HistoryConverter;
import org.vaadin.mvp.presenter.EventHandler;

import com.vaadin.navigator.View;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Event {

    @SuppressWarnings("rawtypes")
    Class<? extends EventHandler>[] handlers() default { };

    boolean forwardToParent() default false;

    boolean navigationEvent() default false;

    Class<? extends View> navigateTo() default View.class;

    Class<? extends HistoryConverter> historyConverter() default NoHistoryConverter.class;

    Class<?> broadcastTo() default NoBroadcast.class;

    class NoBroadcast {
    }

    interface NoHistoryConverter extends HistoryConverter {
    }
}
