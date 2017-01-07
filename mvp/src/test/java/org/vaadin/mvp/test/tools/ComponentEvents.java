package org.vaadin.mvp.test.tools;

public abstract class ComponentEvents {

    protected ComponentEvents() {
    }

    /**
     * @see com.vaadin.event.FieldEvents.BlurEvent
     */
    public static final String BLUR = "blur";

    /**
     * @see com.vaadin.ui.Button.ClickEvent
     */
    public static final String BUTTON_CLICK = "buttonClick";

    /**
     * @see com.vaadin.ui.HasComponents.ComponentAttachEvent
     */
    public static final String COMPONENT_ATTACHED_TO_CONTAINER = "componentAttachedToContainer";

    /**
     * @see com.vaadin.ui.HasComponents.ComponentDetachEvent
     */
    public static final String COMPONENT_DETACHED_TO_CONTAINER = "componentDetachedFromContainer";

    /**
     * @see com.vaadin.data.Container.ItemSetChangeEvent
     */
    public static final String CONTAINER_ITEM_SET_CHANGE = "containerItemSetChange";

    /**
     * @see com.vaadin.data.Container.PropertySetChangeEvent
     */
    public static final String CONTAINER_PROPERTY_SET_CHANGE = "containerPropertySetChange";

    /**
     * @see com.vaadin.ui.Component.ErrorEvent
     */
    public static final String ERROR = "error";

    /**
     * @see com.vaadin.event.FieldEvents.FocusEvent
     */
    public static final String FOCUS = "focus";

    /**
     * @see com.vaadin.event.LayoutEvents.LayoutClickEvent
     */
    public static final String LAYOUT_CLICK = "layoutClick";

    /**
     * @see com.vaadin.event.ItemClickEvent
     */
    public static final String ITEM_CLICK = "itemClick";

    /**
     * @see com.vaadin.data.Item.PropertySetChangeEvent
     */
    public static final String ITEM_PROPERTY_SET_CHANGE = "itemPropertySetChange";

    /**
     * @see com.vaadin.event.MouseEvents.DoubleClickEvent
     */
    public static final String MOUSE_DOUBLE_CLICK = "doubleClick";

    /**
     * @see com.vaadin.event.MouseEvents.ClickEvent
     */
    public static final String MOUSE_CLICK = "click";

    /**
     * @see com.vaadin.data.Property.ReadOnlyStatusChangeEvent
     */
    public static final String READ_ONLY_STATUS_CHANGE = "readOnlyStatusChange";

    /**
     * @see com.vaadin.ui.TabSheet.SelectedTabChangeEvent
     */
    public static final String SELECTED_TAB_CHANGE = "selectedTabChange";

    /**
     * @see com.vaadin.ui.AbstractSplitPanel.SplitterClickEvent
     */
    public static final String SPLITTER_CLICK = "splitterClick";

    /**
     * @see com.vaadin.ui.Table.ColumnResizeEvent
     */
    public static final String TABLE_COLUMN_RESIZE = "columnResize";

    /**
     * @see com.vaadin.ui.Table.ColumnReorderEvent
     */
    public static final String TABLE_COLUMN_REORDER = "columnReorder";

    /**
     * @see com.vaadin.ui.Table.HeaderClickEvent
     */
    public static final String TABLE_HEADER_CLICK = "headerClick";

    /**
     * @see com.vaadin.ui.Table.FooterClickEvent
     */
    public static final String TABLE_FOOTER_CLICK = "footerClick";

    /**
     * @see com.vaadin.event.FieldEvents.TextChangeEvent
     */
    public static final String TEXT_CHANGE = "textChange";

    /**
     * @see com.vaadin.ui.Tree.CollapseEvent
     */
    public static final String TREE_NODE_COLLAPSE = "nodeCollapse";

    /**
     * @see com.vaadin.ui.Tree.ExpandEvent
     */
    public static final String TREE_NODE_EXPAND = "nodeExpand";

    /**
     * @see com.vaadin.ui.Upload.FailedEvent
     */
    public static final String UPLOAD_FAILED = "uploadFailed";

    /**
     * @see com.vaadin.ui.Upload.FinishedEvent
     */
    public static final String UPLOAD_FINISHED = "uploadFinished";

    /**
     * @see com.vaadin.ui.Upload.ProgressListener
     */
    public static final String UPLOAD_PROGRESS = "updateProgress";

    /**
     * @see com.vaadin.ui.Upload.StartedEvent
     */
    public static final String UPLOAD_STARTED = "uploadStarted";

    /**
     * @see com.vaadin.ui.Upload.SucceededEvent
     */
    public static final String UPLOAD_SUCCEEDED = "uploadSucceeded";

    /**
     * @see com.vaadin.data.Property.ValueChangeEvent
     */
    public static final String VALUE_CHANGE = "valueChange";

    /**
     * @see com.vaadin.ui.Window.CloseEvent
     */
    public static final String WINDOW_CLOSE = "windowClose";

    /**
     * @see com.vaadin.ui.Window.ResizeEvent
     */
    public static final String WINDOW_RESIZE = "windowResize";

    /**
     * @see com.vaadin.ui.Window.WindowModeChangeEvent
     */
    public static final String WINDOW_MODE_CHANGE = "windowModeChange";
}
