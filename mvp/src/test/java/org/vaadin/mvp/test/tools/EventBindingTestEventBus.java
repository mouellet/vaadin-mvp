package org.vaadin.mvp.test.tools;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;
import org.vaadin.mvp.eventbus.annotation.Events;

import com.vaadin.data.Container;
import com.vaadin.data.Property.ReadOnlyStatusChangeEvent;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.MouseEvents;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window;

@Events
public interface EventBindingTestEventBus extends EventBus {

    @Event(handlers = EventBindingTestPresenter.class)
    void componentError(Component.ErrorEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void componentBlur(BlurEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void componentFocus(FocusEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void componentReadOnlyStatusChange(ReadOnlyStatusChangeEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void componentTextChange(TextChangeEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void componentAttachedToContainer(HasComponents.ComponentAttachEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void componentDetachedFromContainer(HasComponents.ComponentDetachEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void layoutClick(LayoutEvents.LayoutClickEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void buttonClick(Button.ClickEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void containerPropertySetChange(Container.PropertySetChangeEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void containerItemSetChange(Container.ItemSetChangeEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void mouseClick(MouseEvents.ClickEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void mouseDoubleClick(MouseEvents.DoubleClickEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void tableItemClick(ItemClickEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void tableHeaderClick(Table.HeaderClickEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void tableFooterClick(Table.FooterClickEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void tableColumnReorder(Table.ColumnReorderEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void tableColumnResize(Table.ColumnResizeEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void treeNodeCollapse(Tree.CollapseEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void treeNodeExpand(Tree.ExpandEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void selectedTabChange(TabSheet.SelectedTabChangeEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void windowClose(Window.CloseEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void windowModeChange(Window.WindowModeChangeEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void windowResize(Window.ResizeEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void beforeViewChange(ViewChangeEvent event);

    @Event(handlers = EventBindingTestPresenter.class)
    void afterViewChange(ViewChangeEvent event);
}
