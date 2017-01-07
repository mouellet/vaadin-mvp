package org.vaadin.mvp.test.tools;

import org.vaadin.mvp.presenter.AbstractPresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;
import org.vaadin.mvp.test.tools.EventBindingTestView.AccessibleButton;
import org.vaadin.mvp.test.tools.EventBindingTestView.AccessibleComponent;
import org.vaadin.mvp.test.tools.EventBindingTestView.AccessibleComponentContainer;
import org.vaadin.mvp.test.tools.EventBindingTestView.AccessiblePanel;
import org.vaadin.mvp.test.tools.EventBindingTestView.AccessibleTabSheet;
import org.vaadin.mvp.test.tools.EventBindingTestView.AccessibleTable;
import org.vaadin.mvp.test.tools.EventBindingTestView.AccessibleTree;
import org.vaadin.mvp.test.tools.EventBindingTestView.AccessibleWindow;

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

@Presenter(view = EventBindingTestView.class)
public class EventBindingTestPresenter extends AbstractPresenter<EventBindingTestPresenter.IEventBindingTestView, EventBindingTestEventBus> {

    public interface IEventBindingTestView {

        AccessibleComponentContainer getComponentContainer();

        AccessibleTabSheet getTabSheet();

        AccessibleComponent getComponent();

        AccessiblePanel getField();

        AccessibleButton getButton();

        AccessibleTable getTable();

        AccessibleTree getTree();

        AccessibleWindow getWindow();
    }

    public void onComponentError(Component.ErrorEvent event) {
        view.getComponent().setData(ComponentEvents.ERROR);
    }

    public void onComponentBlur(BlurEvent event) {
        view.getComponent().setData(ComponentEvents.BLUR);
    }

    public void onComponentFocus(FocusEvent event) {
        view.getComponent().setData(ComponentEvents.FOCUS);
    }

    public void onComponentReadOnlyStatusChange(ReadOnlyStatusChangeEvent event) {
        view.getComponent().setData(ComponentEvents.READ_ONLY_STATUS_CHANGE);
    }

    public void onComponentTextChange(TextChangeEvent event) {
        view.getComponent().setData(ComponentEvents.TEXT_CHANGE);
    }

    public void onComponentAttachedToContainer(HasComponents.ComponentAttachEvent event) {
        view.getComponentContainer().setData(ComponentEvents.COMPONENT_ATTACHED_TO_CONTAINER);
    }

    public void onComponentDetachedFromContainer(HasComponents.ComponentDetachEvent event) {
        view.getComponentContainer().setData(ComponentEvents.COMPONENT_DETACHED_TO_CONTAINER);
    }

    public void onLayoutClick(LayoutEvents.LayoutClickEvent event) {
        view.getComponentContainer().setData(ComponentEvents.LAYOUT_CLICK);
    }

    public void onButtonClick(Button.ClickEvent event) {
        view.getButton().setData(ComponentEvents.BUTTON_CLICK);
    }

    public void onMouseClick(MouseEvents.ClickEvent event) {
        view.getField().setData(ComponentEvents.MOUSE_CLICK);
    }

    public void onMouseDoubleClick(MouseEvents.DoubleClickEvent event) {
        view.getField().setData(ComponentEvents.MOUSE_DOUBLE_CLICK);
    }

    public void onTableItemClick(ItemClickEvent event) {
        view.getTable().setData(ComponentEvents.ITEM_CLICK);
    }

    public void onTableHeaderClick(Table.HeaderClickEvent event) {
        view.getTable().setData(ComponentEvents.TABLE_HEADER_CLICK);
    }

    public void onTableFooterClick(Table.FooterClickEvent event) {
        view.getTable().setData(ComponentEvents.TABLE_FOOTER_CLICK);
    }

    public void onTableColumnReorder(Table.ColumnReorderEvent event) {
        view.getTable().setData(ComponentEvents.TABLE_COLUMN_REORDER);
    }

    public void onTableColumnResize(Table.ColumnResizeEvent event) {
        view.getTable().setData(ComponentEvents.TABLE_COLUMN_RESIZE);
    }

    public void onTreeNodeCollapse(Tree.CollapseEvent event) {
        view.getTree().setData(ComponentEvents.TREE_NODE_COLLAPSE);
    }

    public void onTreeNodeExpand(Tree.ExpandEvent event) {
        view.getTree().setData(ComponentEvents.TREE_NODE_EXPAND);
    }

    public void onSelectedTabChange(TabSheet.SelectedTabChangeEvent event) {
        view.getTabSheet().setData(ComponentEvents.SELECTED_TAB_CHANGE);
    }

    public void onWindowClose(Window.CloseEvent event) {
        view.getWindow().setData(ComponentEvents.WINDOW_CLOSE);
    }

    public void onWindowModeChange(Window.WindowModeChangeEvent event) {
        view.getWindow().setData(ComponentEvents.WINDOW_MODE_CHANGE);
    }

    public void onWindowResize(Window.ResizeEvent event) {
        view.getWindow().setData(ComponentEvents.WINDOW_RESIZE);
    }

    public void onNavigatorBeforeViewChange(ViewChangeEvent event) {

    }

    public void onNavigatorAfterViewChange(ViewChangeEvent event) {

    }
}
