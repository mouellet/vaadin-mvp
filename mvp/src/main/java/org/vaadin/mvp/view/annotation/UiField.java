package org.vaadin.mvp.view.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vaadin.ui.Component;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UiField {

    boolean provided() default false;

    Class<? extends Component> implementation() default Component.class;

    Callback[] callbacks() default { };
}
