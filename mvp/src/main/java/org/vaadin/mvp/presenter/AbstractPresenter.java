package org.vaadin.mvp.presenter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.annotation.Presenter;
import org.vaadin.mvp.presenter.factory.PresenterFactory;
import org.vaadin.mvp.view.factory.ViewFactory;
import org.vaadin.mvp.view.factory.ViewFactoryException;

import com.vaadin.server.ClientConnector;

/**
 * Abstract base class for presenters.
 *
 * @author tam
 *
 * @param <V>
 *            Type of the view for this presenter
 * @param <E>
 *            Type of the event bus used by the presenter
 */
public abstract class AbstractPresenter<V, E extends EventBus> implements EventHandler<V, E>, BeanNameAware, ApplicationContextAware {

    private static final Log logger = LogFactory.getLog(AbstractPresenter.class);

    protected boolean bound;

    protected V view;

    protected E eventBus;

    @Autowired
    protected ViewFactory viewFactory;

    @Autowired
    protected EventBusManager eventBusManager;

    @Autowired
    protected PresenterFactory presenterFactory;

    private String beanName;

    private ApplicationContext applicationContext;

    @Override
    public void bind() {
        // override in implementation
    }

    @Override
    public void bind(Object args) {
        // override in implementation
    }

    @Override
    public void bindIfNeeded(Object args) {
        if (!bound) {
            this.bind(args);
            this.bound = true;
        }
    }

    @Override
    public boolean isBound() {
        return bound;
    }

    @Override
    public V getView() {
        return this.view;
    }

    @Override
    public void setView(V view) {
        this.view = view;
        if (this.view != null && isPrototypeScope()) {
            ((ClientConnector) view).addDetachListener(event -> destroy());
        }
    }

    @Override
    public E getEventBus() {
        return eventBus;
    }

    @Override
    public void setEventBus(E eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    protected void init() throws ViewFactoryException {
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing Presenter [" + this.getClass().getSimpleName() + "]");
        }

        Class<?> presenterClass = this.getClass();
        Presenter presenterDef = presenterClass.getAnnotation(Presenter.class);
        Assert.notNull(presenterDef, "@Presenter annotation missing for EventHandler [" + presenterClass.getSimpleName() + "]");

        this.eventBus = eventBusManager.create(this);

        if (presenterDef.view() != Presenter.NoView.class && viewFactory.canCreateView(presenterDef.view())) {
            setView((V) viewFactory.create(presenterDef.view(), this.eventBus));
        }
    }

    @PreDestroy
    protected void destroy() {
        if (logger.isDebugEnabled()) {
            logger.debug("Destroying Presenter [" + this.getClass().getName() + "]");
        }

        eventBusManager.unregister(this);
        this.view = null;
        this.eventBus = null;
    }

    protected boolean isPrototypeScope() {
        return applicationContext.isPrototype(this.beanName);
    }

}
