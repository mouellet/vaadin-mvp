package org.vaadin.mvp.view.binder;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.vaadin.mvp.view.event.EventBinder;
import org.vaadin.mvp.view.handler.AnnotationHandler;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.SingleComponentContainer;

/**
 * @author Mathieu Ouellet
 *
 */
public class UiBinder implements InitializingBean {

    private static final Log logger = LogFactory.getLog(UiBinder.class);

    @Lazy
    @Autowired
    private List<AnnotationHandler> annotationHandlers;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (logger.isTraceEnabled()) {
            logger.trace("Scanning for AnnotationHandlers");
            annotationHandlers.forEach(annotationHandler -> {
                logger.trace("Bean [" + annotationHandler.getClass().getName() + "] declares a AnnotationHandlers");
            });
        }
    }

    /**
     * Checks if a view class given is bindable
     *
     * @param viewClass
     * @return
     */
    public boolean isBindable(Class<?> viewClass) {
        boolean bindable = AbstractComponent.class.isAssignableFrom(viewClass)
                || ComponentContainer.class.isAssignableFrom(viewClass)
                || SingleComponentContainer.class.isAssignableFrom(viewClass)
                || CustomComponent.class.isAssignableFrom(viewClass)
                || CustomField.class.isAssignableFrom(viewClass);
        if (logger.isTraceEnabled()) {
            logger.trace("isBindable: Class<" + viewClass.getName() + "> = " + bindable);
        }
        return bindable;
    }

    /**
     * Create a view instance binding a UI components and callbacks
     *
     * @param <T>
     *            Type of the view component
     * @param viewClass
     *            Class of the view component
     * @param eventBinder
     *            Event binder
     * @return
     * @throws UiBinderException
     */
    public <T> T createAndBind(Class<T> viewClass, EventBinder eventBinder) throws UiBinderException {
        T view = null;
        try {
            view = viewClass.newInstance();
            view = createAndBind(view, eventBinder);

        } catch (InstantiationException | IllegalAccessException e) {
            throw new UiBinderException("Failed to instantiate component type " + viewClass.getName(), e);
        }
        return view;
    }

    public <T> T createAndBind(T view, EventBinder eventBinder) throws UiBinderException {
        if (logger.isTraceEnabled()) {
            logger.trace("Binding view " + view.getClass().getName());
        }
        bind(view, eventBinder);
        if (view instanceof UiInitializable) {
            if (logger.isTraceEnabled()) {
                logger.trace("Calling init method for UiInitializable view " + view.getClass().getName());
            }
            ((UiInitializable) view).init();
        }
        return view;
    }

    /**
     * Binds components of the view to fields marked with @UiField and other annotations managed by AnnotationHandlers.
     *
     * @param view
     * @param uiHandler
     * @throws UiBinderException
     */
    public <T> void bind(T view, EventBinder eventBinder) throws UiBinderException {
        for (Field field : view.getClass().getDeclaredFields()) {
            boolean accessible = field.isAccessible();
            if (!accessible) {
                field.setAccessible(true);
            }
            try {
                Object component = field.get(view);
                for (AnnotationHandler handler : annotationHandlers) {
                    Object result = handler.handle(field, component, view, eventBinder);
                    component = result;
                }

            } catch (IllegalArgumentException | IllegalAccessException | UiBinderException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Failed to fully bind " + view.getClass().getName(), e);
                }
                throw new UiBinderException(e);
            } finally {
                field.setAccessible(accessible);
            }
        }
    }

}
