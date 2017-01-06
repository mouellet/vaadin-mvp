package org.vaadin.mvp.eventbus.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventFilter;

/**
 * This annotation indicates that the annotated interface should be used to define the event bus of
 * the specified module. This annotation can be used only on interfaces that extends <code>org.vaadin.mvp.eventbus.EventBus</code>.<br>
 *
 * @author Mathieu Ouellet
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Events {

    Class<? extends EventBus> parent() default NoParent.class;

    boolean multiple() default false;

    Class<? extends EventFilter>[] filterClasses() default { };

    interface NoParent extends EventBus {
    }

}
