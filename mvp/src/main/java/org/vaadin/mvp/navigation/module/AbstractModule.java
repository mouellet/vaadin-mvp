package org.vaadin.mvp.navigation.module;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.Assert;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.navigation.annotation.Module;
import org.vaadin.mvp.navigation.history.HistoryConverter;
import org.vaadin.mvp.presenter.EventHandler;
import org.vaadin.mvp.presenter.factory.PresenterFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 *
 * @author Mathieu Ouellet
 *
 */
public abstract class AbstractModule implements View {

    private static final Log logger = LogFactory.getLog(AbstractModule.class);

    @Lazy
    @Autowired
    private PresenterFactory presenterFactory;

    protected EventHandler<?, ? extends EventBus> startPresenter;

    @PostConstruct
    protected void init() {
        Module moduleDef = this.getClass().getAnnotation(Module.class);
        Assert.notNull(moduleDef, "Missing @Module annotation");

        this.startPresenter = presenterFactory.create(moduleDef.startPresenter());
    }

    @Override
    public void enter(ViewChangeEvent event) {

        Assert.notNull(startPresenter, "Module's startPresenter hasn't been initialized");

        Module moduleDef = this.getClass().getAnnotation(Module.class);
        Assert.notNull(moduleDef, "class did not have a @Module annotation");

        Object args = null;
        if (!moduleDef.historyConverter().equals(Module.NoHistoryConverter.class)) {
            try {
                HistoryConverter historyConverter = moduleDef.historyConverter().newInstance();
                args = historyConverter.convertFromToken(event.getParameters(), startPresenter.getEventBus());

            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("Error while extracting parameters from HistoryConverter", e);
            }
        }

        startPresenter.bind(args == null ? null : args);
    }

    public EventHandler<?, ? extends EventBus> getStartPresenter() {
        return startPresenter;
    }

    @PreDestroy
    protected void destroy() {
        startPresenter = null;
    }

}
