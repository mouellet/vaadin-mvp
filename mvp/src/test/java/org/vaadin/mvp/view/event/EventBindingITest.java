package org.vaadin.mvp.view.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.eventbus.EventHandlerRegistry;
import org.vaadin.mvp.presenter.factory.DefaultPresenterFactory;
import org.vaadin.mvp.presenter.factory.PresenterFactory;
import org.vaadin.mvp.test.tools.ComponentEvents;
import org.vaadin.mvp.test.tools.EventBindingTestPresenter;
import org.vaadin.mvp.test.tools.EventBindingTestPresenter.IEventBindingTestView;
import org.vaadin.mvp.view.binder.UiBinder;
import org.vaadin.mvp.view.factory.UiBinderViewFactory;
import org.vaadin.mvp.view.factory.ViewFactory;
import org.vaadin.mvp.view.handler.CallbackAnnotationHandler;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.MouseEvents;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Table;

@RunWith(MockitoJUnitRunner.class)
public class EventBindingITest {

    @Mock
    private ApplicationContext applicationContext;

    @Spy
    private CallbackAnnotationHandler callbackAnnotationHandler;

    @Spy
    private UiBinder uiBinder;

    private EventBusManager eventBusManager;

    private ViewFactory viewFactory;

    private PresenterFactory presenterFactory;

    private EventBindingTestPresenter presenter;
    private IEventBindingTestView view;

    @Before
    public void setUp() throws Exception {

        setInternalState(uiBinder, "annotationHandlers", Arrays.asList(callbackAnnotationHandler));
        when(applicationContext.containsBean(anyString())).thenReturn(true);

        viewFactory = new UiBinderViewFactory();
        setInternalState(viewFactory, "uiBinder", uiBinder);

        eventBusManager = new EventBusManager();
        setInternalState(eventBusManager, "applicationContext", applicationContext);
        setInternalState(eventBusManager, "eventHandlerRegistry", new EventHandlerRegistry());

        presenterFactory = new DefaultPresenterFactory();
        setInternalState(presenterFactory, "applicationContext", applicationContext);

        presenter = new EventBindingTestPresenter();
        setInternalState(presenter, "applicationContext", applicationContext);
        setInternalState(presenter, "beanName", "eventBindingTestPresenter");
        setInternalState(presenter, "eventBusManager", eventBusManager);
        setInternalState(presenter, "viewFactory", viewFactory);

        ReflectionTestUtils.invokeMethod(presenter, "init");

        view = presenter.getView();
    }

    @Test
    public void component_error_successful() {
        assertNull(view.getComponent().getData());

        view.getComponent().fireComponentErrorEvent();

        assertNotNull(view.getComponent().getData());
        assertEquals(ComponentEvents.ERROR, view.getComponent().getData());
    }

    @Test
    public void component_blur_successful() {
        assertNull(view.getComponent().getData());

        view.getComponent().fireEvent(new FieldEvents.BlurEvent(view.getComponent()));

        assertNotNull(view.getComponent().getData());
        assertEquals(ComponentEvents.BLUR, view.getComponent().getData());
    }

    @Test
    public void component_focus_successful() {
        assertNull(view.getComponent().getData());

        view.getComponent().fireEvent(new FieldEvents.FocusEvent(view.getComponent()));

        assertNotNull(view.getComponent().getData());
        assertEquals(ComponentEvents.FOCUS, view.getComponent().getData());
    }

    @Test
    public void component_readOnlyStatusChange_successful() {
        assertNull(view.getComponent().getData());

        view.getComponent().fireReadOnlyStatusChange();

        assertNotNull(view.getComponent().getData());
        assertEquals(ComponentEvents.READ_ONLY_STATUS_CHANGE, view.getComponent().getData());
    }

    @Test
    public void component_textChange_successful() {
        assertNull(view.getComponent().getData());

        view.getComponent().fireEvent(new FieldEvents.TextChangeEvent(view.getComponent()) {

            @Override
            public String getText() {
                return null;
            }

            @Override
            public int getCursorPosition() {
                return 0;
            }
        });

        assertNotNull(view.getComponent().getData());
        assertEquals(ComponentEvents.TEXT_CHANGE, view.getComponent().getData());
    }

    @Test
    public void hasComponent_AttachedDetachedToContainer_successful() {
        assertNull(view.getComponentContainer().getData());

        view.getComponentContainer().fireComponentAttachEvent(view.getComponent());

        assertNotNull(view.getComponentContainer().getData());
        assertEquals(ComponentEvents.COMPONENT_ATTACHED_TO_CONTAINER, view.getComponentContainer().getData());

        view.getComponentContainer().fireComponentDetachEvent(view.getComponent());

        assertNotNull(view.getComponentContainer().getData());
        assertEquals(ComponentEvents.COMPONENT_DETACHED_TO_CONTAINER, view.getComponentContainer().getData());
    }

    @Test
    public void button_click_successful() {
        assertNull(view.getButton().getData());

        view.getButton().fireClick();

        assertNotNull(view.getButton().getData());
        assertEquals(ComponentEvents.BUTTON_CLICK, view.getButton().getData());
    }

    @Test
    public void layout_click_successful() {
        assertNull(view.getComponentContainer().getData());

        view.getComponentContainer()
                .fireEvent(new LayoutEvents.LayoutClickEvent(view.getComponentContainer(), new MouseEventDetails(), view.getComponentContainer(), null));

        assertNotNull(view.getComponentContainer().getData());
        assertEquals(ComponentEvents.LAYOUT_CLICK, view.getComponentContainer().getData());
    }

    @Test
    public void mouse_click_successful() {
        assertNull(view.getField().getData());

        view.getField().fireEvent(new MouseEvents.ClickEvent(view.getField(), new MouseEventDetails()));

        assertNotNull(view.getField().getData());
        assertEquals(ComponentEvents.MOUSE_CLICK, view.getField().getData());
    }

    @Test
    public void table_itemClick_successful() {
        assertNull(view.getTable().getData());

        view.getTable().fireEvent(
            new ItemClickEvent(view.getTable(),
                    view.getTable().item,
                    view.getTable().itemId,
                    view.getTable().item.getItemProperty("userName"),
                    new MouseEventDetails()));

        assertNotNull(view.getTable().getData());
        assertEquals(ComponentEvents.ITEM_CLICK, view.getTable().getData());
    }

    @Test
    public void table_headerClick_successful() {
        assertNull(view.getTable().getData());

        view.getTable().fireEvent(new Table.HeaderClickEvent(view.getTable(), view.getTable().propertyId, new MouseEventDetails()));

        assertNotNull(view.getTable().getData());
        assertEquals(ComponentEvents.TABLE_HEADER_CLICK, view.getTable().getData());
    }

    @Test
    public void table_footerClick_successful() {
        assertNull(view.getTable().getData());

        view.getTable().fireEvent(new Table.FooterClickEvent(view.getTable(), view.getTable().propertyId, new MouseEventDetails()));

        assertNotNull(view.getTable().getData());
        assertEquals(ComponentEvents.TABLE_FOOTER_CLICK, view.getTable().getData());
    }

    @Test
    public void table_columnReorder_successful() {
        assertNull(view.getTable().getData());

        view.getTable().fireEvent(new Table.ColumnReorderEvent(view.getTable()));

        assertNotNull(view.getTable().getData());
        assertEquals(ComponentEvents.TABLE_COLUMN_REORDER, view.getTable().getData());
    }

    @Test
    public void table_columnResize_successful() {
        assertNull(view.getTable().getData());

        view.getTable().fireEvent(new Table.ColumnResizeEvent(view.getTable(), view.getTable().propertyId, 10, 20));

        assertNotNull(view.getTable().getData());
        assertEquals(ComponentEvents.TABLE_COLUMN_RESIZE, view.getTable().getData());
    }

    @Test
    public void tree_nodeExpand_successful() {
        assertNull(view.getTree().getData());

        view.getTree().fireExpandEvent(view.getTree().parent);

        assertNotNull(view.getTree().getData());
        assertEquals(ComponentEvents.TREE_NODE_EXPAND, view.getTree().getData());
    }

    @Test
    public void tree_nodeCollapse_successful() {
        assertNull(view.getTree().getData());

        view.getTree().fireCollapseEvent(view.getTree().parent);

        assertNotNull(view.getTree().getData());
        assertEquals(ComponentEvents.TREE_NODE_COLLAPSE, view.getTree().getData());
    }

    @Test
    public void tabSheet_selectedTabChange_successful() {
        assertNull(view.getTabSheet().getData());

        view.getTabSheet().fireSelectedTabChange();

        assertNotNull(view.getTabSheet().getData());
        assertEquals(ComponentEvents.SELECTED_TAB_CHANGE, view.getTabSheet().getData());
    }

    @Test
    public void window_close_successful() {
        assertNull(view.getWindow().getData());

        view.getWindow().fireClose();

        assertNotNull(view.getWindow().getData());
        assertEquals(ComponentEvents.WINDOW_CLOSE, view.getWindow().getData());
    }

    @Test
    public void window_modeChange_successful() {
        assertNull(view.getWindow().getData());

        view.getWindow().fireWindowWindowModeChange();

        assertNotNull(view.getWindow().getData());
        assertEquals(ComponentEvents.WINDOW_MODE_CHANGE, view.getWindow().getData());
    }

    @Test
    public void window_resize_successful() {
        assertNull(view.getWindow().getData());

        view.getWindow().fireResize();

        assertNotNull(view.getWindow().getData());
        assertEquals(ComponentEvents.WINDOW_RESIZE, view.getWindow().getData());
    }

}
