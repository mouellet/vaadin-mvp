package org.vaadin.mvp.test.tools;

import java.util.EventObject;

import org.vaadin.mvp.test.tools.EventBindingTestPresenter.IEventBindingTestView;
import org.vaadin.mvp.view.annotation.Callback;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ReadOnlyStatusChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class EventBindingTestView extends VerticalLayout implements IEventBindingTestView {

    @Callback(event = Component.ErrorEvent.class, to = "componentError")
    @Callback(event = BlurEvent.class, to = "componentBlur")
    @Callback(event = FocusEvent.class, to = "componentFocus")
    @Callback(event = ReadOnlyStatusChangeEvent.class, to = "componentReadOnlyStatusChange")
    @Callback(event = TextChangeEvent.class, to = "componentTextChange")
    private AccessibleComponent component = new AccessibleComponent();

    @Callback(event = ComponentAttachEvent.class, to = "componentAttachedToContainer")
    @Callback(event = ComponentDetachEvent.class, to = "componentDetachedFromContainer")
    @Callback(event = LayoutClickEvent.class, to = "layoutClick")
    private AccessibleComponentContainer componentContainer = new AccessibleComponentContainer();

    @Callback(event = SelectedTabChangeEvent.class, to = "selectedTabChange")
    private AccessibleTabSheet tabSheet = new AccessibleTabSheet();

    @Callback(event = MouseEvents.ClickEvent.class, to = "mouseClick")
    private AccessiblePanel field = new AccessiblePanel();

    @Callback(event = Button.ClickEvent.class, to = "buttonClick")
    private AccessibleButton button = new AccessibleButton();

    @Callback(event = ItemClickEvent.class, to = "tableItemClick")
    @Callback(event = Table.HeaderClickEvent.class, to = "tableHeaderClick")
    @Callback(event = Table.FooterClickEvent.class, to = "tableFooterClick")
    @Callback(event = Table.ColumnReorderEvent.class, to = "tableColumnReorder")
    @Callback(event = Table.ColumnResizeEvent.class, to = "tableColumnResize")
    private AccessibleTable table = new AccessibleTable();

    @Callback(event = Tree.CollapseEvent.class, to = "treeNodeCollapse")
    @Callback(event = Tree.ExpandEvent.class, to = "treeNodeExpand")
    private AccessibleTree tree = new AccessibleTree();

    @Callback(event = Window.CloseEvent.class, to = "windowClose")
    @Callback(event = Window.WindowModeChangeEvent.class, to = "windowModeChange")
    @Callback(event = Window.ResizeEvent.class, to = "windowResize")
    private AccessibleWindow window = new AccessibleWindow();

    @Override
    public AccessibleComponentContainer getComponentContainer() {
        return componentContainer;
    }

    @Override
    public AccessibleTabSheet getTabSheet() {
        return tabSheet;
    }

    @Override
    public AccessibleComponent getComponent() {
        return component;
    }

    @Override
    public AccessiblePanel getField() {
        return field;
    }

    @Override
    public AccessibleButton getButton() {
        return button;
    }

    @Override
    public AccessibleTable getTable() {
        return table;
    }

    @Override
    public AccessibleTree getTree() {
        return tree;
    }

    @Override
    public AccessibleWindow getWindow() {
        return window;
    }

    public static class AccessibleButton extends Button {

        @Override
        public void fireClick() {
            super.fireClick();
        }

        @Override
        public void fireEvent(EventObject event) {
            super.fireEvent(event);
        }

    }

    public static class AccessibleComponent extends TextField {

        @Override
        public void fireValueChange(boolean repaintIsNotNeeded) {
            super.fireValueChange(repaintIsNotNeeded);
        }

        @Override
        public void fireReadOnlyStatusChange() {
            super.fireReadOnlyStatusChange();
        }

        @Override
        public void fireComponentEvent() {
            super.fireComponentEvent();
        }

        @Override
        public void fireComponentErrorEvent() {
            super.fireComponentErrorEvent();
        }

        @Override
        public void fireEvent(EventObject event) {
            super.fireEvent(event);
        }

    }

    public static class AccessiblePanel extends Panel {

        @Override
        public void fireEvent(EventObject event) {
            super.fireEvent(event);
        }

    }

    public static class AccessibleTable extends Table {

        public User itemId = new User();
        public BeanItem<User> item = new BeanItem<>(itemId);
        @SuppressWarnings("rawtypes")
        public Property propertyId = item.getItemProperty("userName");
        public BeanItemContainer<User> container = new BeanItemContainer<>(User.class);

        public AccessibleTable() {
            container.addItem(item);
            setContainerDataSource(container);
        }

        @Override
        public void fireEvent(EventObject event) {
            super.fireEvent(event);
        }

    }

    public static class AccessibleTree extends Tree {

        public User parent = new User("parent");
        public User firstChild = new User("firstChild");
        public User secondChild = new User("secondChild");

        public AccessibleTree() {
            addItems(parent);
            setChildrenAllowed(parent, true);
            setParent(firstChild, parent);
            setParent(secondChild, parent);
        }

        @Override
        public void fireExpandEvent(Object itemId) {
            super.fireExpandEvent(itemId);
        }

        @Override
        public void fireCollapseEvent(Object itemId) {
            super.fireCollapseEvent(itemId);
        }

    }

    public static class AccessibleTabSheet extends TabSheet {

        public Tab firstTab;
        public Tab secondTab;

        public AccessibleTabSheet() {
            firstTab = addTab(new Label("firstTab"));
            secondTab = addTab(new Label("secondTab"));
        }

        @Override
        public void fireSelectedTabChange() {
            super.fireSelectedTabChange();
        }

    }

    public static class AccessibleWindow extends Window {

        @Override
        public void fireEvent(EventObject event) {
            super.fireEvent(event);
        }

        @Override
        public void fireClose() {
            super.fireClose();
        }

        @Override
        public void fireWindowWindowModeChange() {
            super.fireWindowWindowModeChange();
        }

        @Override
        public void fireResize() {
            super.fireResize();
        }

    }

    public static class AccessibleComponentContainer extends VerticalLayout {

        @Override
        public void fireComponentAttachEvent(Component component) {
            super.fireComponentAttachEvent(component);
        }

        @Override
        public void fireComponentDetachEvent(Component component) {
            super.fireComponentDetachEvent(component);
        }

        @Override
        public void fireEvent(EventObject event) {
            super.fireEvent(event);
        }

    }
}
