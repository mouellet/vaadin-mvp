package org.vaadin.mvp.navigation.module;

import org.springframework.util.Assert;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.EventHandler;

import com.vaadin.navigator.View;
import com.vaadin.ui.ComponentContainer;

public class ModuleViewDisplay implements NavigationTargetViewDisplay {

    private ComponentContainer container;

    @Override
    public void showView(View view) {
        Assert.notNull(container, "ViewDisplay ComponentContainer cannot be null");

        // TODO deal with View which are not AbstractModule (error views...)

        container.removeAllComponents();

        EventHandler<?, ? extends EventBus> startPresenter = ((AbstractModule) view).getStartPresenter();
        ComponentContainer moduleView = (ComponentContainer) startPresenter.getView();
        container.addComponent(moduleView);
    }

    @Override
    public void setNavigationTarget(ComponentContainer container) {
        this.container = container;
    }

}
