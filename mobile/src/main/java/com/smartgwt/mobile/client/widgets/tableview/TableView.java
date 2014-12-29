/*
 * SmartGWT Mobile
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * SmartGWT Mobile is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT Mobile is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package com.smartgwt.mobile.client.widgets.tableview;

import static com.smartgwt.mobile.client.internal.util.ElementUtil.getChildElementHavingClass;
import static com.smartgwt.mobile.client.internal.util.ElementUtil.hasClassName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.core.Function;
import com.smartgwt.mobile.client.core.Rectangle;
import com.smartgwt.mobile.client.data.Criteria;
import com.smartgwt.mobile.client.data.DSCallback;
import com.smartgwt.mobile.client.data.DSRequest;
import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.data.ResultSet;
import com.smartgwt.mobile.client.data.SortSpecifier;
import com.smartgwt.mobile.client.data.events.DataArrivedEvent;
import com.smartgwt.mobile.client.data.events.DataArrivedHandler;
import com.smartgwt.mobile.client.data.events.DataChangedEvent;
import com.smartgwt.mobile.client.data.events.DataChangedHandler;
import com.smartgwt.mobile.client.data.events.HasDataArrivedHandlers;
import com.smartgwt.mobile.client.data.events.HasDataChangedHandlers;
import com.smartgwt.mobile.client.i18n.SmartGwtMessages;
import com.smartgwt.mobile.client.internal.EventHandler;
import com.smartgwt.mobile.client.internal.EventUtil;
import com.smartgwt.mobile.client.internal.SelectedSetEvent;
import com.smartgwt.mobile.client.internal.SelectedSetHandler;
import com.smartgwt.mobile.client.internal.data.CanUnsort;
import com.smartgwt.mobile.client.internal.data.HasRecordCanSelectPropertyAttribute;
import com.smartgwt.mobile.client.internal.data.HasSortSpecifiers;
import com.smartgwt.mobile.client.internal.test.AutoTest;
import com.smartgwt.mobile.client.internal.test.AutoTestLocatable;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.util.AnimationUtil;
import com.smartgwt.mobile.client.internal.util.ElementUtil;
import com.smartgwt.mobile.client.internal.util.NativeIntMap;
import com.smartgwt.mobile.client.internal.util.Pair;
import com.smartgwt.mobile.client.internal.widgets.events.BeforePanelShownEvent;
import com.smartgwt.mobile.client.internal.widgets.events.BeforePanelShownHandler;
import com.smartgwt.mobile.client.internal.widgets.menu.events.BeforeMenuHiddenEvent;
import com.smartgwt.mobile.client.internal.widgets.menu.events.BeforeMenuHiddenHandler;
import com.smartgwt.mobile.client.internal.widgets.tableview.TableViewImpl;
import com.smartgwt.mobile.client.theme.TableViewCssResource;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.types.FetchMode;
import com.smartgwt.mobile.client.types.NavigationMode;
import com.smartgwt.mobile.client.types.RecordLayout;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.Label;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.grid.GroupNode;
import com.smartgwt.mobile.client.widgets.grid.GroupTitleRenderer;
import com.smartgwt.mobile.client.widgets.grid.GroupValueFunction;
import com.smartgwt.mobile.client.widgets.grid.ListGrid;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;
import com.smartgwt.mobile.client.widgets.grid.events.HasRowContextClickHandlers;
import com.smartgwt.mobile.client.widgets.grid.events.HasSelectionChangedHandlers;
import com.smartgwt.mobile.client.widgets.grid.events.HasSelectionUpdatedHandlers;
import com.smartgwt.mobile.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.mobile.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.mobile.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.mobile.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.menu.Menu;
import com.smartgwt.mobile.client.widgets.tableview.events.DetailsSelectedEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.DetailsSelectedHandler;
import com.smartgwt.mobile.client.widgets.tableview.events.HasDetailsSelectedHandlers;
import com.smartgwt.mobile.client.widgets.tableview.events.HasImageClickHandlers;
import com.smartgwt.mobile.client.widgets.tableview.events.HasRecordNavigationClickHandlers;
import com.smartgwt.mobile.client.widgets.tableview.events.ImageClickEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.ImageClickHandler;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickHandler;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperElement;

/**
 * Shows a listing of records with one or more fields from each record shown, with
 * built-in support for navigation and editing of lists of records.
 * <p/>
 * Records can be fetched from a {@link DataSource}, or, a local dataset can be provided as a
 * {@link RecordList}.
 * <p/>
 * The TableView provides built-in controls such as +link{navigation arrows,showNavigation} and
 * shows fields from the provided records in one of several built-in {@link RecordLayout}s or in a
 * {@link #setRecordFormatter custom format}.
 */
public class TableView extends ListGrid implements HasDetailsSelectedHandlers, HasImageClickHandlers,
HasRecordCanSelectPropertyAttribute, HasRecordNavigationClickHandlers, HasRowContextClickHandlers,
HasSelectionChangedHandlers, HasSelectionUpdatedHandlers, SelectedSetHandler {

    public static final TableViewCssResource _CSS = ThemeResources.INSTANCE.tableViewCSS();

    // HTML5 data- attributes
    // http://ejohn.org/blog/html-5-data-attributes/

    @SGWTInternal
    public static final String IS_LOAD_MORE_RECORDS_ATTRIBUTE_NAME = "data-sc-isLoadMoreRecords";

    private static final String RECORD_INDEX_ATTRIBUTE_NAME = "data-sc-recordindex";

    /**
     * <code>data-sc-groupValueString</code>, the HTML5 data attribute that is set on the element
     * holding the table group for the records of a {@link com.smartgwt.mobile.client.widgets.grid.GroupNode}.
     * 
     * <p>Using CSS attribute selectors, you can style the table rows differently by group.
     * For example, when using group values 0, 1, and 2:
     * 
     * <pre>
     * .sc-table > [data-sc-groupValueString="0"] > .sc-table-group > .sc-row {
     *     &#x2f;* Group 0 row styles *&#x2f;
     * }
     * .sc-table > [data-sc-groupValueString="1"] > .sc-table-group > .sc-row {
     *     &#x2f;* Group 1 row styles *&#x2f;
     * }
     * .sc-table > [data-sc-groupValueString="2"] > .sc-table-group > .sc-row {
     *     &#x2f;* Group 2 row styles *&#x2f;
     * }
     * </pre>
     */
    public static final String GROUP_VALUE_STRING_ATTRIBUTE_NAME = "data-sc-groupValueString";

    // Class name constants

    /**
     * <code>sc-table</code>, the CSS class name that is added to the <code>TableView</code>
     * {@link com.google.gwt.user.client.ui.UIObject#getElement() element}.
     */
    public static final String COMPONENT_CLASS_NAME = _CSS.tableViewClass();

    /**
     * <code>sc-table-group</code>
     */
    public static final String TABLE_GROUP_CLASS_NAME = _CSS.tableViewGroupClass();

    /**
     * <code>sc-row</code>, the CSS class name that is added to each table row element.
     */
    public static final String ROW_CLASS_NAME = _CSS.tableViewRowClass();

    /**
     * <code>sc-record-icon</code>, the CSS class name added to the icon element of a row.
     */
    public static final String RECORD_ICON_CLASS_NAME = _CSS.recordIconClass();

    /**
     * <code>sc-record-counter</code>, the CSS class name added to the counter element of a row.
     */
    public static final String RECORD_COUNTER_CLASS_NAME = _CSS.recordCounterClass();

    /**
     * <code>sc-record-title</code>, the CSS class name added to the title element of a row.
     */
    public static final String RECORD_TITLE_CLASS_NAME = _CSS.recordTitleClass();

    /**
     * <code>sc-record-info</code>, the CSS class name added to the info element of a row.
     */
    public static final String RECORD_INFO_CLASS_NAME = _CSS.recordInfoClass();

    /**
     * <code>sc-record-description</code>, the CSS class name added to the description element of a row.
     */
    public static final String RECORD_DESCRIPTION_CLASS_NAME = _CSS.recordDescriptionClass();

    /**
     * <code>sc-record-component</code>, the CSS class name added to the {@link com.google.gwt.user.client.ui.UIObject#getElement() element}
     * of each record component.
     */
    public static final String RECORD_COMPONENT_CLASS_NAME = _CSS.recordComponentClass();

    private class DetailsRow extends Canvas implements ClickHandler {

        private transient HandlerRegistration clickRegistration;
        private transient Image image;

        public DetailsRow(ImageResource img) {
            setElement(Document.get().createDivElement());

            // If no image resource is provided, then use a CSS-based icon.
            if (img == null) {
                getElement().addClassName(TableView._CSS.recordDetailDisclosureClass());
                getElement().setInnerHTML("&#x203a;");

            } else {
                getElement().addClassName(TableView._CSS.recordDetailDisclosureNavIconClass());
                image = new Image(img);
                add(image, getElement());
            }
        }

        @Override
        protected void onLoad() {
            super.onLoad();
            clickRegistration = addClickHandler(this);
        }

        protected void onUnload() {
            clickRegistration.removeHandler();
            super.onUnload();
        }

        @Override
        public void onClick(ClickEvent event) {
            if (navigationMode == NavigationMode.NAVICON_ONLY) {
                TableView.this.fireEvent(new DetailsSelectedEvent(_findRecord(getElement())));
            }
        }
    }

    private class MoveIndicator extends Canvas {

        private Element li;
        private UListElement ul;
        private int fromTop, lastY = 0, totalY, height, totalRows, activeRow, targetRow;
        private boolean lastLIHadLastClass;
        private int[] rowOffsets;
        private boolean active = false;
        private AnimationUtil.AnimationRegistration animationRegistration;

        public MoveIndicator(Element li) {
            this.li = li;
            setElement(Document.get().createDivElement());
            getElement().addClassName(TableView._CSS.recordMoveIndicatorClass());
            sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEMOVE | Event.ONMOUSEUP | Event.TOUCHEVENTS | Event.ONLOSECAPTURE);
        }

        @Override
        public void onBrowserEvent(Event event) {
            int type = event.getTypeInt();
            switch (type) {
                case Event.ONMOUSEDOWN:
                case Event.ONTOUCHSTART:
                    if (!active) {
                        event.stopPropagation();
                        ul = li.getParentElement().cast();
                        DOM.setCapture(getElement());
                        active = true;
                        onStart(event);
                    }
                    break;
                case Event.ONMOUSEMOVE:
                case Event.ONTOUCHMOVE:
                    if (active) {
                        onMove(event);
                    }
                    break;
                case Event.ONMOUSEUP:
                case Event.ONTOUCHEND:
                case Event.ONTOUCHCANCEL:
                    if (active) {
                        onEnd(event);
                        active = false;
                        DOM.releaseCapture(getElement());
                    }
                    break;
                case Event.ONLOSECAPTURE:
                    if (active) {
                        active = false;
                        onEnd(event);
                    }
                    break;
            }
            super.onBrowserEvent(event);
        }

        private void onStart(Event event) {
            int y = (event.getTouches() != null && event.getTouches().length() > 0) ? event.getTouches().get(0).getClientY(): event.getClientY();
            fromTop = y - li.getAbsoluteTop();
            //SC.logWarn("fromTop = " + fromTop);
            assert fromTop >= 0;
            lastY = y;
            totalY = 0;
            height = li.getOffsetHeight();
            //SC.logWarn("(height - fromTop) = " + (height - fromTop));
            totalRows = ul.getChildCount();
            assert totalRows >= 1;
            if (Element.as(ul.getChild(totalRows - 1)).hasAttribute(IS_LOAD_MORE_RECORDS_ATTRIBUTE_NAME)) {
                totalRows -= 1;
            }
            activeRow = DOM.getChildIndex(ul.<com.google.gwt.user.client.Element>cast(), li.<com.google.gwt.user.client.Element>cast());
            targetRow = activeRow;
            if (rowOffsets == null || rowOffsets.length != totalRows) {
                rowOffsets = new int[totalRows];
            }
            for (int i = 0; i < rowOffsets.length; ++i) {
                rowOffsets[i] = 0;
            }
            lastLIHadLastClass = ElementUtil.hasClassName(Element.as(ul.getChild(totalRows - 1)), TableView._CSS.lastTableViewRowClass());
            li.getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none 0s");
            li.getStyle().setProperty(DOMConstants.INSTANCE.getBoxShadowPropertyName(), "2px 2px 8px grey");
            li.getStyle().setZIndex(5);

            // Prevent dragging / text selection on some browsers.
            event.preventDefault();
        }

        private void onMove(Event event) {
            int y = (event.getTouches() != null && event.getTouches().length() > 0) ? event.getTouches().get(0).getClientY(): event.getClientY();
            int deltaY = y - lastY;
            lastY = y;
            totalY += deltaY;

            int targetRow = activeRow;
            if (totalY < -fromTop) {
                assert totalY <= 0;
                // Dragging up.
                int accumulator = 0;
                while (targetRow > 0) {
                    Element otherLi = Element.as(ul.getChild(targetRow - 1));
                    int otherHeight = otherLi.getOffsetHeight();
                    accumulator += otherHeight;
                    if (totalY < -fromTop - accumulator + otherHeight / 2) {
                        if (rowOffsets[targetRow - 1] != 1) {
                            otherLi.getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + " 200ms linear");
                            otherLi.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, " + Element.as(ul.getChild(targetRow)).getOffsetHeight() + "px)");
                            rowOffsets[targetRow - 1] = 1;
                        }
                        --targetRow;
                    } else {
                        break;
                    }
                }
                assert targetRow <= activeRow;
                for (int i = targetRow; i > 0; --i) {
                    if (rowOffsets[i - 1] != 0) {
                        Element otherLi = Element.as(ul.getChild(i - 1));
                        otherLi.getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + " 200ms linear");
                        otherLi.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, 0px)");
                        rowOffsets[i - 1] = 0;
                    }
                }
            } else if ((height - fromTop) < totalY) {
                // Dragging down.
                int accumulator = 0;
                while (targetRow < totalRows - 1) {
                    Element otherLi = Element.as(ul.getChild(targetRow + 1));
                    int otherHeight = otherLi.getOffsetHeight();
                    accumulator += otherHeight;
                    if ((height - fromTop) < totalY - accumulator + otherHeight / 2) {
                        if (rowOffsets[targetRow + 1] != -1) {
                            otherLi.getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + " 200ms linear");
                            otherLi.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, -" + Element.as(ul.getChild(targetRow)).getOffsetHeight() + "px)");
                            rowOffsets[targetRow + 1] = -1;
                        }
                        ++targetRow;
                    } else {
                        break;
                    }
                }
                assert activeRow <= targetRow;
                for (int i = targetRow; i < totalRows - 1; ++i) {
                    if (rowOffsets[i + 1] != 0) {
                        Element otherLi = Element.as(ul.getChild(i + 1));
                        otherLi.getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + " 200ms linear");
                        otherLi.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, 0px)");
                        rowOffsets[i + 1] = 0;
                    }
                }
            }
            if (this.targetRow != targetRow) {
                //SC.logWarn("totalY = " + totalY + ", targetRow = " + targetRow);
            }
            this.targetRow = targetRow;

            if (totalRows > 1) {
                // Correct the two rows that have rounded corners if necessary.

                if (targetRow == 0) {
                    li.addClassName(TableView._CSS.firstTableViewRowClass());
                    Element.as(ul.getChild(activeRow == 0 ? 1 : 0)).removeClassName(TableView._CSS.firstTableViewRowClass());
                } else {
                    li.removeClassName(TableView._CSS.firstTableViewRowClass());
                    Element.as(ul.getChild(activeRow == 0 ? 1 : 0)).addClassName(TableView._CSS.firstTableViewRowClass());
                }

                if (lastLIHadLastClass) {
                    if (targetRow == totalRows - 1) {
                        li.addClassName(TableView._CSS.lastTableViewRowClass());
                        Element.as(ul.getChild(activeRow == totalRows - 1 ? totalRows - 2 : totalRows - 1)).removeClassName(TableView._CSS.lastTableViewRowClass());
                    } else {
                        li.removeClassName(TableView._CSS.lastTableViewRowClass());
                        Element.as(ul.getChild(activeRow == totalRows - 1 ? totalRows - 2 : totalRows - 1)).addClassName(TableView._CSS.lastTableViewRowClass());
                    }
                }
            } else {
                li.addClassName(TableView._CSS.firstTableViewRowClass());
                if (lastLIHadLastClass) li.addClassName(TableView._CSS.lastTableViewRowClass());
            }

            li.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(0px, " + totalY + "px)");

            // Prevent dragging / text selection on some browsers.
            event.preventDefault();
        }

        private void onEnd(Event event) {
            int accumulator = 0;
            if (targetRow < activeRow) {
                // Dragged up.
                for (int i = targetRow; i < activeRow; ++i) {
                    accumulator -= Element.as(ul.getChild(i)).getOffsetHeight();
                }
            } else if (activeRow < targetRow) {
                // Dragged down.
                for (int i = activeRow; i < targetRow; ++i) {
                    accumulator += Element.as(ul.getChild(i)).getOffsetHeight();
                }
            }
            li.getStyle().setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), "none 0s");
            animationRegistration = AnimationUtil.animate(li, accumulator > height / 2 ? 200 : 100, AnimationUtil.LINEAR, new Function<Void>() {
                @Override
                public Void execute() {
                    animationRegistration = null;
                    li.getStyle().setProperty(DOMConstants.INSTANCE.getBoxShadowPropertyName(), "none");
                    li.getStyle().clearProperty(DOMConstants.INSTANCE.getBoxShadowPropertyName());
                    li.getStyle().setZIndex(0);
                    if (activeRow != targetRow) {
                        final Record activeRecord = _findRecord(ul.getChild(activeRow).<Element>cast());
                        final int activeRecordIndex = activeRecord.getAttributeAsInt(recordIndexProperty).intValue();
                        final Record targetRecord = _findRecord(ul.getChild(targetRow).<Element>cast());
                        final int targetRecordIndex = targetRecord.getAttributeAsInt(recordIndexProperty).intValue();
                        final Record record = _getData().remove(activeRecordIndex);
                        assert activeRecord == record;
                        int offset = activeRow <= targetRow
                                     ? (activeRecordIndex <= targetRecordIndex ? 0 : 1)
                                     : (activeRecordIndex <= targetRecordIndex ? -1 : 0);
                        _getData().add(targetRecordIndex + offset, record);
                        _refreshRows();
                        showMoveableRecords();
                    }
                    return null;
                }
            }, new AnimationUtil.AnimationProperty(DOMConstants.INSTANCE.getTransformPropertyName(), DOMConstants.INSTANCE.getTransformPropertyNameForCSSText(), null, "translate(0px, " + accumulator + "px)"));
        }
    }

    private TableViewImpl impl = GWT.create(TableViewImpl.class);

    private String defaultPrimaryKeyFieldName = "_id";
    private HandlerRegistration dataArrivedRegistration,
            dataChangedRegistration;

    /**
     * The default table style is PLAIN
     */
    private TableMode tableMode = TableMode.PLAIN;
    private RecordLayout recordLayout = RecordLayout.AUTOMATIC;
    private RecordFormatter recordFormatter;
    private Map<Object, Element> elementMap;
    private List<Canvas> recordComponents = null;
    private List<Record> markedForRemoval = new ArrayList<Record>();

    @SGWTInternal
    protected UListElement _ul;

    // Selection
    private NavStack parentNavStack = null;
    private HandlerRegistration beforePanelShownRegistration = null;
    private HandlerRegistration selectedSetRegistration;
    private boolean showSelectedIcon = false;

    private boolean touchActive = false;
    private Timer startTimer;
    private Element activeElement;
    private Integer touchIdentifier;
    private boolean contextClickFired = false;
    private int touchPointX, touchPointY;

    // Navigation icon
    private ImageResource navIcon;
    private ImageResource wholeRecordNavIcon;

    // SelectedIcon
    private ImageResource selectedIcon;

    // Sorting
    private SortSpecifier[] initialSort;
    private Boolean sortDirection;

    private int currentPage = 0;

    private NavigationMode navigationMode = NavigationMode.WHOLE_RECORD;
    private boolean showNavigation = false;
    private boolean showIcons = false;
    private boolean deferRemoval = true;
    private String canRemoveProperty = "canRemove";
    private String recordRemovedProperty = "$899";
    private String recordCanSelectProperty = "canSelect";
    private String recordNavigationProperty = "_navigate";
    private String detailCountProperty = "detailCount";
    private String recordIndexProperty = "$76w";
    private String groupByFieldName = null;

    // Appearance
    private int cellHeight = 30;
    private boolean hiliteOnTouch = true;
    private Panel panel = new Panel();

    private boolean canReorderRecords = false;
    private boolean canRemoveRecords = false;
    private boolean showDetailCount = false;

    private String emptyMessage = SmartGwtMessages.INSTANCE.listGrid_emptyMessage();
    private String loadingMessage = SmartGwtMessages.INSTANCE.listGrid_loadingDataMessage();

    private boolean fireContentChangedOnLoad = false;

    public TableView() {
        this.setWidth("100%");
        this.addMember(panel);
        this._setClassName(_CSS.tableViewWrapperClass(), false);
        setTableMode(tableMode);
        sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEOUT | Event.TOUCHEVENTS | Event.ONCLICK | Event.ONCONTEXTMENU);
    }

    public TableView(RecordList records) {
        this();
        setData(records);
    }

    @Override
    public void destroy() {
        if (startTimer != null) {
            startTimer.cancel();
            startTimer = null;
        }
        destroyRecordComponents();
        if (beforePanelShownRegistration != null) {
            beforePanelShownRegistration.removeHandler();
            beforePanelShownRegistration = null;
        }
        if (dataChangedRegistration != null) {
            dataChangedRegistration.removeHandler();
            dataChangedRegistration = null;
        }
        if (dataArrivedRegistration != null) {
            dataArrivedRegistration.removeHandler();
            dataArrivedRegistration = null;
        }
        super.destroy();
    }

    private void destroyRecordComponents() {
        if (recordComponents != null) {
            for (Canvas component : recordComponents) {
                component.destroy();
            }
            recordComponents = null;
        }
    }

    private Element findElementByRowFieldConfigObj(Map<String, String> configObj) {
        final String title = configObj.get("title");
        if (title != null && elementMap != null) {
            for (final Element rowElem : elementMap.values()) {
                final NodeList<Node> children = rowElem.getChildNodes();
                final int children_length = children.getLength();
                for (int i = 0; i < children_length; ++i) {
                    final Element titleElem = getChildElementHavingClass(rowElem, RECORD_TITLE_CLASS_NAME);
                    if (titleElem != null && titleElem.getInnerText().equals(title)) {
                        return rowElem;
                    }
                }
            }
        }
        final String valueOnly = configObj.get(AutoTest.FALLBACK_VALUE_ONLY_FIELD);
        if (valueOnly != null) {
            int i = -1;
            try {
                i = Integer.parseInt(valueOnly, 10);
            } catch (NumberFormatException ex) {}
            if (i >= 0) {
                final RecordList data = _getData();
                if (data != null && i < data.size()) {
                    final Record record = data.get(i);
                    return elementMap.get(getRecordId(record));
                }
            }
        }
        return null;
    }

    private Element findLoadMoreRecordsElement() {
        if (_ul != null) {
            final NodeList<Node> children = _ul.getChildNodes();
            for (int ri = children.getLength(); ri > 0; --ri) {
                final Node child = children.getItem(ri - 1);
                if (child.getNodeType() != Node.ELEMENT_NODE) continue;
                final Element childElem = (Element)child;
                if (childElem.hasAttribute(IS_LOAD_MORE_RECORDS_ATTRIBUTE_NAME)) {
                    return childElem;
                }
            }
        }
        return null;
    }

    @Override
    public AutoTestLocatable _getChildFromLocatorSubstring(String substring, int index, List<String> locatorArray, GetAttributeConfiguration configuration) {
        if ("body".equals(substring)) return this;
        return super._getChildFromLocatorSubstring(substring, index, locatorArray, configuration);
    }

    @Override
    public Object _getInnerAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration) {
        switch (configuration.getAttribute()) {
            case ELEMENT:
                if (locatorArray.size() == 1 || locatorArray.size() == 2) {
                    String substring = locatorArray.get(0);
                    if (substring.startsWith("row[")) {
                        final Pair<String, Map<String, String>> p = AutoTest.parseLocatorFallbackPath(substring);
                        if (p != null) {
                            assert "row".equals(p.getFirst());
                            final Element rowElem = findElementByRowFieldConfigObj(p.getSecond());

                            if (locatorArray.size() == 1) return rowElem;
                            else if (locatorArray.size() == 2) {
                                substring = locatorArray.get(1);
                                if ("title".equals(substring)) {
                                    return getChildElementHavingClass(rowElem, RECORD_TITLE_CLASS_NAME);
                                }
                            }
                        }
                    } else if ("loadMoreRecordsRow".equals(substring) && locatorArray.size() == 1) {
                        return findLoadMoreRecordsElement();
                    }
                }
                break;
            case VALUE:
                if (locatorArray.size() == 2) {
                    String substring = locatorArray.get(0);
                    if (substring.startsWith("row[")) {
                        final Pair<String, Map<String, String>> p = AutoTest.parseLocatorFallbackPath(substring);
                        if (p != null) {
                            assert "row".equals(p.getFirst());
                            final Element rowElem = findElementByRowFieldConfigObj(p.getSecond());
                            if (rowElem != null && rowElem.hasAttribute(RECORD_INDEX_ATTRIBUTE_NAME)) {
                                int i = Integer.parseInt(rowElem.getAttribute(RECORD_INDEX_ATTRIBUTE_NAME), 10);
                                final RecordList data = _getData();
                                if (data != null && i >= 0 && i < data.size()) {
                                    final Record record = data.get(i);
                                    substring = locatorArray.get(1);
                                    if ("isSelected".equals(substring)) return isSelected(record);
                                    else if ("title".equals(substring)) {
                                        return record.getAttributeAsObject(getTitleField());
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        return super._getInnerAttributeFromSplitLocator(locatorArray, configuration);
    }

    @Override
    public void setBaseStyle(String baseStyle) {
        super.setBaseStyle(baseStyle);
        _refreshRows();
    }

    @SGWTInternal
    protected static Element _getDeleteDisclosureElem(Element recordElem, Element targetElem) {
        for (Element element = targetElem; element != recordElem; element = element.getParentElement()) {
            if (hasClassName(element, _CSS.recordDeleteDisclosureClass()) && element.getParentElement() == recordElem) {
                return element;
            }
        }
        return null;
    }

    @SGWTInternal
    protected static Element _getSelectionDisclosureElem(Element recordElem, Element targetElem) {
        for (Element element = targetElem; element != recordElem; element = element.getParentElement()) {
            if (hasClassName(element, _CSS.recordSelectionDisclosureClass()) && element.getParentElement() == recordElem) {
                return element;
            }
        }
        return null;
    }

    private String getPrimaryKeyFieldName() {
        DataSource ds = getDataSource();
        if (ds != null) {
            String ret = ds.getPrimaryKeyFieldName();
            if (ret != null) return ret;
        }
        return defaultPrimaryKeyFieldName;
    }

    /**
     * Returns the default primary key field name that is used when a {@link com.smartgwt.mobile.client.data.DataSource}
     * is not set on this <code>TableView</code>. (When a <code>DataSource</code> <em>is</em> set, the
     * primary key field name is the name of the {@link com.smartgwt.mobile.client.data.DataSourceField}
     * that has its {@link com.smartgwt.mobile.client.data.DataSourceField#isPrimaryKey() primaryKey} attribute
     * set to <code>true</code>.)
     * 
     * @return the default primary key field name. Default value: "_id"
     */
    public final String getDefaultPrimaryKeyFieldName() {
        return defaultPrimaryKeyFieldName;
    }

    /**
     * Sets {@link #getDefaultPrimaryKeyFieldName() TableView.defaultPrimaryKeyFieldName}.
     * 
     * @param defaultPrimaryKeyFieldName the new default primary key field name.
     */
    public void setDefaultPrimaryKeyFieldName(String defaultPrimaryKeyFieldName) {
        if (defaultPrimaryKeyFieldName == null) throw new NullPointerException("`defaultPrimaryKeyFieldName' cannot be null.");
        this.defaultPrimaryKeyFieldName = defaultPrimaryKeyFieldName;
    }

    private Object getRecordId(Record record) {
        return record.getAttributeAsObject(getPrimaryKeyFieldName());
    }

    @SGWTInternal
    protected Element _findElement(NativeEvent event) {
        Element element = EventUtil.getTargetElem(event);
        if (element == null) {
            return null;
        }
        for (Element parentElement = element.getParentElement(); parentElement != null; element = parentElement, parentElement = element.getParentElement()) {
            if ("UL".equals(parentElement.getTagName()) &&
                (ElementUtil.hasClassName(parentElement, TABLE_GROUP_CLASS_NAME) ||
                 (_ul == parentElement && ElementUtil.hasClassName(parentElement, COMPONENT_CLASS_NAME))))
            {
                break;
            }
        }
        return element;
    }

    @SGWTInternal
    protected Integer _findRecordIndex(Element element) {
        if (element == null) {
            return null;
        }
        while (!element.hasAttribute(RECORD_INDEX_ATTRIBUTE_NAME)) {
            element = element.getParentElement();
            if (element == null) {
                break;
            }
        }
        boolean found = false;
        if (element != null) {
            if(element.getTagName().equalsIgnoreCase("li")) {
                Element parent = element.getParentElement();
                Element thisElement = this.getElement();
                while (parent != null) {
                    if (parent == thisElement) {
                        found = true;
                        break;
                    }
                    parent = parent.getParentElement();
                }
            } else {
                NodeList<Node> nodes = element.getChildNodes();
                for (int i = 0; i < nodes.getLength(); ++i) {
                    Node node = nodes.getItem(i);
                    if (element == node) {
                        found = true;
                        break;
                    }
                }
            }
        }
        if (found) {
            assert element != null;
            return Integer.valueOf(element.getAttribute(RECORD_INDEX_ATTRIBUTE_NAME));
        }
        return null;
    }

    @SGWTInternal
    protected Record _findRecord(Element element) {
        final Integer recordIndex = _findRecordIndex(element);
        if (recordIndex != null) {
            final Record record = _getData().get(recordIndex.intValue());
            if (record != null) {
                elementMap.put(getRecordId(record), element);
            }
            return record;
        }
        return null;
    }

    @Override
    public void setDataSource(DataSource ds) {
        if (getDataSource() != ds) {
            super.setDataSource(ds);
            renumberFields();
        }
    }

    private void renumberFields() {
        fieldNums_ = NativeIntMap.create();
        final ListGridField[] fields = getFields();
        int nextFieldNum = fields == null ? 0 : fields.length;
        final DataSource ds = getDataSource();
        if (ds != null) {
            final DataSourceField[] dsFields = ds.getFields();
            if (dsFields != null) {
                for (DataSourceField dsField : dsFields) {
                    fieldNums_.put(dsField.getName(), nextFieldNum++);
                }
            }
        }
        nextFieldNum = 0;
        if (fields != null) {
            for (int i = 0; i < fields.length; ++i) {
                fieldNums_.put(fields[i].getName(), nextFieldNum++);
            }
        }
    }

    @Override
    public void setFields(ListGridField... fields) {
        if (getFields() != fields) {
            super.setFields(fields);
            renumberFields();

            // Need to refreshRows() so that any new ListGridFields will have an effect, such
            // as applying their implementation of formatCellValue() or handling escapeHTML.
            _refreshRows();
        }
    }

    public final String getGroupByField() {
        return groupByFieldName;
    }

    public void setGroupByField(String groupByField) {
        if ((this.groupByFieldName == null && groupByField != null) ||
                !this.groupByFieldName.equals(groupByField))
        {
            this.groupByFieldName = groupByField;
            _refreshRows();
        }
    }

    @Override
    public final String getFieldName(int fieldNum) {
        int f;
        final ListGridField[] fields = getFields();
        if (fields != null) {
            if (fieldNum < fields.length) return fields[fieldNum].getName();
            f = fieldNum - fields.length;
        } else f = 0;
        final DataSource ds = getDataSource();
        if (ds != null) {
            final DataSourceField[] dsFields = ds.getFields();
            if (dsFields != null && f < dsFields.length) return dsFields[f].getName();
        }
        return null;
    }

    /**
     * Set the style of the table. The default style is {@link com.smartgwt.mobile.client.types.TableMode#PLAIN}.
     *
     * @param tableMode the default table style (plain or grouped)
     */
    public void setTableMode(TableMode tableMode) {
        this.tableMode = tableMode;
    }

    /**
     * Return the table style. The default style is {@link com.smartgwt.mobile.client.types.TableMode#PLAIN}.
     *
     * @return the table style
     */
    public TableMode getTableMode() {
        return tableMode;
    }

    /**
     * return the table content panel
     */
    public void setInnerHTML(String html) {
        panel.getElement().setInnerHTML(html);
    }

    /**
     * Sets the arrangement of data fields from the record.
     * <p>
     * Several presets are available, or a {@link #setRecordFormatter(RecordFormatter) RecordFormatter} can be
     * defined to take over rendering of <code>TableView</code> rows entirely.
     * <p>
     * Note that controls supported by the TableView itself, such as navigation icons, are
     * implicitly added to the data fields described in the RecordLayout.
     *
     * @param layout the record layout
     */
    public void setRecordLayout(RecordLayout layout) {
        this.recordLayout = layout;
    }

    public RecordLayout getRecordLayout() {
        return this.recordLayout;
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        if (! isEnabled()) return;
        final Element targetElem = EventUtil.getTargetElem(event);
        if (targetElem != null) {
            final Element element;
            final JsArray<Touch> touches;
            final int clientX, clientY;
            final boolean wasContextClickFired;
            switch (event.getTypeInt()) {
                case Event.ONMOUSEDOWN:
                    onStart(event, null);
                    break;
                case Event.ONTOUCHSTART:
                    touches = event.getTouches();
                    if (touches.length() == 1 && touchIdentifier == null) {
                        onStart(event, touches.get(0));
                    } else {
                        // Another finger is touching the screen.
                        onEnd(event);
                    }
                    break;
                case Event.ONMOUSEMOVE:
                    if (touchActive) {
                        clientX = event.getClientX();
                        clientY = event.getClientY();
                        if (Math.abs(touchPointX - clientX) >= 10 ||
                                Math.abs(touchPointY - clientY) >= 10)
                        {
                            onEnd(event);
                        }
                    }
                    break;
                case Event.ONTOUCHMOVE:
                    if (touchActive) {
                        touches = event.getTouches();
                        if (touches.length() == 1 && touchIdentifier != null) {
                            final Touch touch = touches.get(0);
                            if (touch.getIdentifier() == touchIdentifier.intValue()) {
                                clientX = touch.getClientX();
                                clientY = touch.getClientY();
                                if (Math.abs(touchPointX - clientX) >= 10 ||
                                        Math.abs(touchPointY - clientY) >= 10)
                                {
                                    onEnd(event);
                                }
                            }
                        }
                    }
                    break;
                case Event.ONMOUSEUP:
                case Event.ONMOUSEOUT:
                case Event.ONTOUCHEND:
                case Event.ONTOUCHCANCEL:
                    element = activeElement;
                    wasContextClickFired = contextClickFired;
                    onEnd(event);
                    if (element != null && wasContextClickFired) {
                        onClick(element, null);
                    }
                    break;

                case Event.ONCLICK:
                    if (!isEnabled()) return;

                    element = _findElement(event);
                    onClick(element, targetElem);
                    break;

                case Event.ONCONTEXTMENU:
                    if (!isEnabled()) return;

                    element = _findElement(event);
                    if (element != null) {
                        // Find the "context clickable element".
                        // The context clickable element is the title element, unless there
                        // is no title element, in which case it is the <li>.
                        Element contextClickableElement = element;
                        final NodeList<Node> children = element.getChildNodes();
                        final int children_length = children.getLength();
                        for (int i = 0; i < children_length; ++i) {
                            final Node child = children.getItem(i);
                            if (child.getNodeType() != Node.ELEMENT_NODE) continue;
                            final Element childElem = (Element)child;
                            if (ElementUtil.hasClassName(childElem, TableView.RECORD_TITLE_CLASS_NAME)) {
                                contextClickableElement = childElem;
                                if (touchActive) contextClickFired = true;
                                break;
                            }
                        }

                        if (contextClickableElement.isOrHasChild(targetElem)) {
                            final Integer recordIndex = _findRecordIndex(element);
                            if (recordIndex != null) {
                                final Record record = _getData().get(recordIndex.intValue());
                                final boolean cancelled = RowContextClickEvent._fire(this, -1, record, recordIndex.intValue());
                                if (!cancelled) {
                                    final Menu contextMenu = getContextMenu();
                                    if (contextMenu != null) {
                                        contextClickableElement.addClassName(_CSS.contextClickedElementClass());
                                        final Object recordID = getRecordId(record);
                                        final Element li = elementMap.get(recordID);
                                        assert li != null;
                                        final Element finalContextClickableElement = contextClickableElement;
                                        new BeforeMenuHiddenHandler() {

                                            private HandlerRegistration beforeMenuHiddenRegistration = contextMenu._addBeforeMenuHiddenHandler(this);

                                            @Override
                                            public void _onBeforeMenuHidden(BeforeMenuHiddenEvent event) {
                                                beforeMenuHiddenRegistration.removeHandler();
                                                finalContextClickableElement.removeClassName(_CSS.contextClickedElementClass());
                                            }
                                        };
                                        contextMenu._showAt(this, event.getClientX(), event.getClientY(),
                                                contextClickableElement.getAbsoluteLeft(), contextClickableElement.getAbsoluteRight(),
                                                contextClickableElement.getAbsoluteTop(), contextClickableElement.getAbsoluteBottom());
                                        if (touchActive) contextClickFired = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (fireContentChangedOnLoad) {
            fireContentChangedOnLoad = false;
            _fireContentChangedEvent();
        }
    }

    private void onStart(Event event, Touch touch) {
        touchActive = true;
        if (touch != null) {
            touchIdentifier = touch.getIdentifier();
            touchPointX = touch.getClientX();
            touchPointY = touch.getClientY();
        } else {
            touchPointX = event.getClientX();
            touchPointY = event.getClientY();
        }

        final Element element = _findElement(event);
        if (element != null) {
            startTimer = new Timer() {
                @Override
                public void run() {
                    if (startTimer != this) return;
                    try {
                        final Record record = _findRecord(element);
                        if (record == null || (!hasClassName(element, _CSS.recordDeleteDisclosureClass()) && !hasClassName(element.getParentElement(), _CSS.recordDeleteDisclosureClass()))) {
                            if ((record == null && !element.hasAttribute(IS_LOAD_MORE_RECORDS_ATTRIBUTE_NAME)) ||
                                (record != null && _canSelectRecord(record) && navigationMode == NavigationMode.WHOLE_RECORD))
                            {
                                activeElement = element;
                                element.removeClassName(_CSS.clearingTemporaryTableViewRowSelectionClass());
                                element.addClassName(_CSS.selectedTableViewRowClass());
                            }
                        }
                    } finally {
                        startTimer = null;
                    }
                }
            };
            // Delay the temporary selection effect in order to see if the user is going to
            // cancel it.
            startTimer.schedule(150);
        }
    }

    private void onEnd(Event event) {
        // Clear the temporary selection unless the row will be clicked momentarily, in which
        // case, we do not clear the selected styling so as to prevent a flicker.
        if (activeElement != null && (EventHandler.lastClickedElem == null || !activeElement.isOrHasChild(EventHandler.lastClickedElem))) {
            final Element element = activeElement;
            element.addClassName(_CSS.clearingTemporaryTableViewRowSelectionClass());
            element.removeClassName(_CSS.selectedTableViewRowClass());
        }

        activeElement = null;
        if (startTimer != null) {
            startTimer.cancel();
            startTimer = null;
        }
        touchIdentifier = null;
        contextClickFired = false;
        touchActive = false;
    }

    private void onClick(Element element, Element targetElem) {
        if (parentNavStack != null) {
            if (parentNavStack._isAnimating()) return;
        }
        if (element != null) {
            final Record record = _findRecord(element);
            if (record != null) {
                final Element deleteDisclosureElem = (targetElem == null ? null : _getDeleteDisclosureElem(element, targetElem));
                if (deleteDisclosureElem != null) {
                    if (_recordMarkedAsRemoved(record)) {
                        _unmarkRecordRemoved(record, deleteDisclosureElem);
                    } else {
                        _markRecordRemoved(record, deleteDisclosureElem, false);
                    }
                } else {
                    final Element selectionDisclosureElem = (targetElem == null ? null : _getSelectionDisclosureElem(element, targetElem));
                    if (_canSelectRecord(record)) {
                        if (getSelectionType() == SelectionStyle.MULTIPLE) {
                            if (selectionDisclosureElem != null) {
                                if (isSelected(record)) {
                                    deselectRecord(record);
                                } else {
                                    selectRecord(record);
                                }
                            }
                        } else {
                            selectSingleRecord(record);
                        }
                    }
                    if (selectionDisclosureElem == null && navigationMode == NavigationMode.WHOLE_RECORD) {
                        recordNavigationClick(record);
                    }
                }
            } else if (element.hasAttribute(IS_LOAD_MORE_RECORDS_ATTRIBUTE_NAME)) {
                currentPage++;
                if (_getData() instanceof ResultSet) {
                    element.setInnerText(getLoadingMessage());
                    // no double loading
                    element.removeAttribute(IS_LOAD_MORE_RECORDS_ATTRIBUTE_NAME);
                    int firstRow = getDataPageSize() * currentPage,
                            lastRow = firstRow + getDataPageSize();
                    ((ResultSet)_getData()).getRange(firstRow, lastRow);
                }
            }
        }
    }

    /**
     * Formatter for the record as a whole, aside from built-in controls such as navigation
     * controls. If this formatter is not supplied, a default formatter is chosen based on the
     * {@link #getRecordLayout() recordLayout}.
     *
     * <p>Writing a custom <code>RecordFormatter</code> is advanced usage. In many cases, where
     * it may seem that a custom formatter is needed, the same customization can be achieved
     * using a default formatter in conjunction with a {@link com.smartgwt.mobile.client.widgets.grid.CellFormatter}
     * set on a {@link com.smartgwt.mobile.client.widgets.grid.ListGridField}.
     *
     * @param recordFormatter the record formatter
     */
    public void setRecordFormatter(RecordFormatter recordFormatter) {
        this.recordFormatter = recordFormatter;
        if (_getData() != null && ! _getData().isEmpty()) {
            _refreshRows();
        }
    }

    public RecordFormatter getRecordFormatter() {
        return recordFormatter;
    }

    @Override
    public void setData(RecordList records) {
        if (_getData() != records) {
            if (dataChangedRegistration != null) {
                dataChangedRegistration.removeHandler();
                dataChangedRegistration = null;
            }
            if (dataArrivedRegistration != null) {
                dataArrivedRegistration.removeHandler();
                dataArrivedRegistration = null;
            }
            assert dataChangedRegistration == null;
            assert dataArrivedRegistration == null;
            super.setData(records);
            if (_getData() instanceof HasSortSpecifiers) {
                ((HasSortSpecifiers)_getData()).setSort(_sortSpecifiers);
            }
            setSelecteds();

            if (records instanceof ResultSet) {
                ((ResultSet)records).setFetchMode(getDataFetchMode());
            }
            if (records instanceof HasDataArrivedHandlers) {
                dataArrivedRegistration = ((HasDataArrivedHandlers)records).addDataArrivedHandler(new DataArrivedHandler() {
                    @Override
                    public void onDataArrived(DataArrivedEvent event) {
                        _dataArrived(event.getStartRow(), event.getEndRow());
                    }
                });
            }
            if (records instanceof HasDataChangedHandlers) {
                dataChangedRegistration = ((HasDataChangedHandlers)records).addDataChangedHandler(new DataChangedHandler() {
                    @Override
                    public void onDataChanged(DataChangedEvent event) {
                        _dataChanged(event);
                    }
                });
            }
        }

        _refreshRows();
    }

    @Override
    protected void _createSelectionModel() {
        super._createSelectionModel();
        // There is no GridRenderer class. Instead, the essential functionality from that
        // class is brought into the TableView class.

        selectedSetRegistration = _getSelectionObject()._addSelectedSetHandler(this);
    }

    @Override
    protected void _destroySelectionModel() {
        if (selectedSetRegistration != null) {
            selectedSetRegistration.removeHandler();
            selectedSetRegistration = null;
        }
        super._destroySelectionModel();
    }

    @Override
    public void _onSelectedSet(SelectedSetEvent event) {
        assert event.getSource() == _getSelectionObject();

        if (event.getPreviousState() != event.getState()) {
            final Object recordID = getRecordId((Record)event.getSelectionItem());
            final Element li = elementMap.get(recordID);
            if (event.getState()) {
                _setSelected(li);
            } else {
                _clearSelected(li);
            }
            SelectionEvent._fire(this, (Record)event.getSelectionItem(), event.getState(), event.getPreviousState());
        }
    }

    @SGWTInternal
    protected void _dataChanged(DataChangedEvent event) {
        if ("invalidateCache".equals(event._getType())) {
            currentPage = 0;
        } else {
            _refreshRows();
        }
    }

    @SGWTInternal
    protected void _dataArrived(int start, int end) {
        _refreshRows();
    }

    @Override
    public final SortSpecifier[] getSort() {
        if (_sortSpecifiers != null) {
            return SortSpecifier._shallowClone(_sortSpecifiers);
        } else if (initialSort != null) {
            return SortSpecifier._shallowClone(initialSort);
        } else {

            return null;
        }
    }

    @Override
    public void setSort(SortSpecifier... sortSpecifiers) {
        SortSpecifier[] newSpecifiers = SortSpecifier._shallowClone(sortSpecifiers);

        this._sortSpecifiers = newSpecifiers;

        if (_sortSpecifiers != null && _sortSpecifiers.length > 0) {
            if (_getData() != null && (_getData().size() > 0 || _getData() instanceof HasSortSpecifiers)) {
                // do the actual sorting
                if (_getData() instanceof HasSortSpecifiers) {
                    ((HasSortSpecifiers)_getData()).setSort(_sortSpecifiers);
                }
            }
        } else {
            if (_getData() != null) {
                if (_getData() instanceof HasSortSpecifiers) ((HasSortSpecifiers)_getData()).setSort(new SortSpecifier[0]);
                else if (_getData() instanceof CanUnsort) ((CanUnsort)_getData()).unsort();
            }
        }
    }

    public void clearSort() {
        setSort((SortSpecifier[])null);
    }

    @Override
    public RecordList _createDataModel(Criteria filterCriteria, Object operation, DSRequest context) {
        final RecordList dataModel = super._createDataModel(filterCriteria, operation, context);
        if (dataModel != null && sortDirection != null) {
            dataModel.sortByProperty(getDataField(), sortDirection.booleanValue());
        }
        return dataModel;
    }

    /**
     * Perform a DataSource "add" operation to add new records to this component's DataSource.
     *
     * @param record new record
     */
    public void addData(Record record) {
        DataSource ds = this.getDataSource();
        if (ds != null) {
            ds.addData(record);
        }
    }

    /**
     * Perform a DataSource "remove" operation to remove records from this component's DataSource.
     *
     * @param record primary key values of record to delete, (or complete record)
     */
    public void removeData(Record record) {
        DataSource ds = this.getDataSource();
        if (ds != null) {
            ds.removeData(record);
        }
    }

    public void removeData(Record record, DSCallback callback) {
        DataSource ds = this.getDataSource();
        if (ds != null) {
            ds.removeData(record, callback);
        }
    }

    public void removeData(Record record, DSCallback callback, DSRequest request) {
        DataSource ds = this.getDataSource();
        if (ds != null) {
            ds.removeData(record, callback, request);
        }
    }

    /**
     * Perform a DataSource "update" operation to update existing records in this component's DataSource.
     *
     * @param record updated record
     */
    public void updateData(Record record) {
        DataSource ds = this.getDataSource();
        if (ds != null) {
            ds.updateData(record);
        }
    }

    public void setSortDirection(Boolean up) {
        this.sortDirection = up;
    }

    public Boolean getSortDirection() {
        return this.sortDirection;
    }

    public Boolean hasChanges() {
        return !markedForRemoval.isEmpty();
    }

    // refreshRows will actually create the markup to represent each records. This occurs
    // lazily on draw, and is refreshed on setData(), dataChanged() [for RecordLists] and
    // dataArrived() [for ResultSets]

    @SGWTInternal
    protected void _refreshRows() {
        // Clear out any already-applied children, and rebuild from scratch
        this.clear();
        if(this.elementMap != null) {
            this.elementMap.clear();
        }
        if (_ul != null) {
            ElementUtil.removeAllChildren(_ul);
        }

        final RecordList data = _getData();
        if (data == null) {
            destroyRecordComponents();
            return;
        }

        int firstRow, lastRow;
        if (getDataFetchMode() == FetchMode.PAGED) {
            // We only want to render out dataPageSize records.
            // currentPage will indicate the start/end slice of the range
            firstRow = getDataPageSize() * currentPage;
            lastRow = firstRow + getDataPageSize();
        } else {
            firstRow = 0;
            lastRow = data.size();
        }

        // If the row isn't yet loaded, call 'get' to kick off a server fetch.
        // We'll be notified when new data arrives.
        // We could display a "loading" marker here too of course.
        // NOTE: ResultSet doesn't yet support partial caches so right now we'll load all records when we
        // request a range, which

        if (data instanceof ResultSet && !((ResultSet)data).lengthIsKnown()) {
            if (getDataFetchMode() != FetchMode.PAGED) lastRow = 0;
            ((ResultSet)data).getRange(firstRow, lastRow);
            loadRecords(null);
            return;
        }

        // our pagination logic will already ensure firstRow doesn't exceed the current dataSet -
        // ensure our calculated lastRow isn't off the end of the set of loaded records
        if (data.size() < lastRow) lastRow = data.size();
        if (data instanceof ResultSet && ((ResultSet)data).getFetchMode() == FetchMode.PAGED) {
            firstRow = 0;
        }
        loadRecords(data.subList(firstRow, lastRow));
    }

    /**
     * If <code>true</code>, this <code>TableView</code> should create and show a custom embedded
     * component in every row.  The returned component is used in place of all default table
     * row controls except for the reorder and deletion controls.
     *
     * @see #createRecordComponent(Record)
     * @return whether to create and show a custom widget in every row.  Default value:  <code>false</code>.
     */
    public boolean getShowRecordComponents() {
        return false;
    }

    /**
     * If {@link #getShowRecordComponents() showRecordComponents} is <code>true</code>, then this
     * method must be overridden to create the custom component that should be used in place of all
     * default table row controls except for the reorder and deletion controls.
     *
     * @param record the <code>Record</code> from this <code>TableView</code>'s data.
     * @return the record component for <code>record</code>.
     */
    protected Canvas createRecordComponent(Record record) {
        return null;
    }

    private String formatCellValue(Record record, int rowNum, String fieldName) {
        int fieldNum = getFieldNum(fieldName);
        if (fieldNum >= 0) {
            final ListGridField[] fields = getFields();
            if (fields != null && fieldNum < fields.length) {
                final ListGridField field = fields[fieldNum];
                String value = field._formatCellValue(record.getAttributeAsObject(fieldName), record, rowNum, fieldNum, this);
                if (field._getEscapeHTML()) {
                    value = SafeHtmlUtils.htmlEscape(value);
                }
                return value;
            }
        }
        return record.getAttribute(fieldName);
    }

    /**
     * Renders the records in <code>recordsToShow</code> on screen.
     *
     * @see #getShowRecordComponents()
     */
    public void loadRecords(List<Record> recordsToShow) {
        destroyRecordComponents();
        final boolean hadShowDeletableRecordsClassName,
                hadShowMoveableRecordsClassName;
        if (_ul != null) {
            hadShowDeletableRecordsClassName = ElementUtil.hasClassName(_ul, _CSS.tableViewShowDeleteDisclosuresClass());
            hadShowMoveableRecordsClassName = ElementUtil.hasClassName(_ul, _CSS.tableViewShowMoveIndicatorsClass());
            if (_ul.hasParentElement()) {
                _ul.removeFromParent();
            }
        } else {
            hadShowDeletableRecordsClassName = false;
            hadShowMoveableRecordsClassName = false;
        }
        final Document document = Document.get();
        _ul = document.createULElement();
        _ul.addClassName(COMPONENT_CLASS_NAME);
        if (parentNavStack != null) {
            _ul.addClassName(_CSS.tableViewHasParentNavStackClass());
        }
        if (hadShowDeletableRecordsClassName) {
            _ul.addClassName(_CSS.tableViewShowDeleteDisclosuresClass());
        }
        if (hadShowMoveableRecordsClassName) {
            _ul.addClassName(_CSS.tableViewShowMoveIndicatorsClass());
        }
        if (this.tableMode == TableMode.GROUPED) {
            _ul.addClassName(_CSS.groupedTableViewClass());
        }

        if (recordsToShow == null) recordsToShow = Collections.emptyList();
        for (int i = 0; i < recordsToShow.size(); ++i) {
            final Record record = recordsToShow.get(i);
            if (record == null) throw new NullPointerException("The Record at index " + i + " is null.");
            record.setAttribute(recordIndexProperty, Integer.valueOf(i));
        }

        ListGridField groupByField = null;
        GroupNode[] sortedGroupNodes = null;

        // Handle table grouping
        if (groupByFieldName != null) {
            final ListGridField[] fields = getFields();
            if (fields != null) {
                for (ListGridField field : fields) {
                    if (field != null && groupByFieldName.equals(field.getName())) {
                        groupByField = field;
                        break;
                    }
                }
            }

            if (groupByField == null) {
                SC.logWarn("Could not find groupByField '" + groupByFieldName + "'");
            } else if (groupByField.getGroupValueFunction() == null) {
                SC.logWarn("The groupByField '" + groupByFieldName + "' does not have a GroupByFunction.");
            } else {
                final GroupValueFunction groupByFunction = groupByField.getGroupValueFunction();
                final Map<Object, GroupNode> groupNodes = new LinkedHashMap<Object, GroupNode>();
                for (Record record : recordsToShow) {
                    final Object groupValue = groupByFunction.getGroupValue(record.getAttributeAsObject(groupByFieldName), record, groupByField, groupByFieldName, this);
                    GroupNode groupNode = groupNodes.get(groupValue);
                    if (groupNode == null) {
                        groupNode = new GroupNode(groupValue);
                        groupNodes.put(groupValue, groupNode);
                    }
                    groupNode._add(record);
                }

                sortedGroupNodes = groupNodes.values().toArray(new GroupNode[groupNodes.size()]);
                Arrays.sort(sortedGroupNodes, GroupNode._COMPARATOR);
            }
        }

        elementMap = new HashMap<Object, Element>();
        if (getShowRecordComponents()) recordComponents = new ArrayList<Canvas>();

        if (recordsToShow.isEmpty()) {
            if (_getData() instanceof ResultSet && !((ResultSet) _getData()).lengthIsKnown()) {
                _ul.setInnerText(this.getLoadingMessage());
            } else {
                if (emptyMessage != null) _ul.setInnerText(emptyMessage);
            }
            getElement().appendChild(_ul);
        } else {
            UListElement ul = _ul;
            LIElement lastLI;
            if (sortedGroupNodes == null) {
                lastLI = showGroup(recordsToShow, ul);
            } else {
                assert groupByField != null;
                assert sortedGroupNodes.length >= 1;

                final GroupTitleRenderer groupTitleRenderer = groupByField.getGroupTitleRenderer();

                int i = 0;
                LIElement li;
                do {
                    final GroupNode groupNode = sortedGroupNodes[i];
                    groupNode._setGroupTitle(groupTitleRenderer == null
                                             ? SafeHtmlUtils.htmlEscape(groupNode._getGroupValueString())
                                             : groupTitleRenderer.getGroupTitle(groupNode.getGroupValue(), groupNode, groupByField, groupByFieldName, this));

                    li = document.createLIElement();
                    li.setAttribute(GROUP_VALUE_STRING_ATTRIBUTE_NAME, groupNode._getGroupValueString());
                    if (ul == null || _ul.equals(ul)) li.addClassName(_CSS.firstTableViewGroupClass());
                    final String groupTitle = groupNode.getGroupTitle();
                    if (groupTitle == null) {
                        li.addClassName(_CSS.tableViewGroupWithoutGroupTitleClass());
                    } else {
                        final DivElement labelDiv = document.createDivElement();
                        labelDiv.setClassName(Label.COMPONENT_CLASS_NAME);
                        labelDiv.setInnerHTML(groupTitle);
                        li.appendChild(labelDiv);
                    }
                    ul = document.createULElement();
                    ul.setClassName(COMPONENT_CLASS_NAME);
                    ul.addClassName(TABLE_GROUP_CLASS_NAME);
                    lastLI = showGroup(groupNode._getGroupMembersList(), ul);
                    if (i != sortedGroupNodes.length - 1 && lastLI != null) {
                        lastLI.addClassName(_CSS.lastTableViewRowClass());
                    }
                    li.appendChild(ul);
                    _ul.appendChild(li);
                } while (++i < sortedGroupNodes.length);
                assert li != null;
                li.addClassName(_CSS.lastTableViewGroupClass());
            }

            if (_getData() instanceof ResultSet) {
                ResultSet rs = (ResultSet) _getData();
                if (rs.getFetchMode() == FetchMode.PAGED && !rs.allRowsCached()) {
                    LIElement li = document.createLIElement();
                    li.setClassName(ROW_CLASS_NAME);
                    com.google.gwt.user.client.Element loadMoreRecordsElement = (com.google.gwt.user.client.Element)li.cast();
                    DOM.setEventListener(loadMoreRecordsElement, this);
                    SpanElement span = document.createSpanElement();
                    span.addClassName(RECORD_TITLE_CLASS_NAME);
                    span.setInnerText(SmartGwtMessages.INSTANCE.listGrid_loadMoreRecords());
                    span.setAttribute(IS_LOAD_MORE_RECORDS_ATTRIBUTE_NAME, "true");
                    li.setAttribute(IS_LOAD_MORE_RECORDS_ATTRIBUTE_NAME, "true");
                    li.appendChild(span);
                    ul.appendChild(li);
                    lastLI = li;
                }
            }
            if (lastLI != null) {
                lastLI.addClassName(_CSS.lastTableViewRowClass());
            }

            getElement().appendChild(_ul);
            setSelecteds();
        }

        if (isAttached()) {
            _fireContentChangedEvent();

        // If this `TableView' is not currently attached, defer the firing of the content
        // changed event.
        } else {
            fireContentChangedOnLoad = true;
        }
    }

    private LIElement showGroup(List<Record> recordsToShow, UListElement ul) {
        final Document document = Document.get();
        final String primaryKeyField = getPrimaryKeyFieldName(),
                iconField = getIconField(),
                titleField = getTitleField(),
                infoField = getInfoField(),
                descriptionField = getDescriptionField();
        LIElement lastLI = null;
        for (final Record record : recordsToShow) {
            final Canvas recordComponent;
            if (getShowRecordComponents()) {
                recordComponent = createRecordComponent(record);
                if (recordComponent == null) {
                    continue;
                }
                else recordComponents.add(recordComponent);
            } else recordComponent = null;

            final LIElement li = document.createLIElement();
            li.addClassName(ROW_CLASS_NAME);
            com.google.gwt.user.client.Element element = li.cast();
            final Object recordID = record.getAttributeAsObject(primaryKeyField);
            elementMap.put(recordID, element);
            final Integer recordIndex = record.getAttributeAsInt(recordIndexProperty);
            li.setAttribute(RECORD_INDEX_ATTRIBUTE_NAME, recordIndex.toString());

            if (lastLI == null) {
                li.addClassName(_CSS.firstTableViewRowClass());
            }

            if (showSelectedIcon && selectedIcon == null && getSelectionType() == SelectionStyle.MULTIPLE) {
                DivElement selectionDisclosure = document.createDivElement();
                selectionDisclosure.setClassName(_CSS.recordSelectionDisclosureClass());
                if (!_canSelectRecord(record)) {
                    selectionDisclosure.addClassName(_CSS.nonselectableSelectionDisclosureClass());
                }
                SpanElement span = document.createSpanElement();
                selectionDisclosure.appendChild(span);
                li.appendChild(selectionDisclosure);
            }

            if (canRemoveRecords) {
                Boolean deletable = record.getAttributeAsBoolean(canRemoveProperty);
                if (deletable == null || (deletable != null && deletable.booleanValue())) {
                    DivElement div = document.createDivElement();
                    div.addClassName(_CSS.recordDeleteDisclosureClass());
                    SpanElement span = document.createSpanElement();
                    div.appendChild(span);
                    li.appendChild(div);

                    if (markedForRemoval != null && markedForRemoval.contains(record)) {
                        _markRecordRemoved(record, div, true);
                    }
                }
            }

            if (canReorderRecords) {
                MoveIndicator draggableRow = new MoveIndicator(element);
                add(draggableRow, element);
            }

            if (!getShowRecordComponents()) {
                if (recordFormatter != null) {
                    DivElement div = document.createDivElement();
                    div.setClassName("content");
                    div.setInnerHTML(recordFormatter.format(record));
                    li.appendChild(div);
                } else {
                    if (getShowNavigation(record)) {
                        final ImageResource navIcon = getNavigationIcon(record);
                        if (navigationMode == NavigationMode.NAVICON_ONLY) {
                            Boolean navigate = record.getAttributeAsBoolean(getRecordNavigationProperty());
                            if (navigate == null || (navigate != null && navigate.booleanValue())) {
                                final DetailsRow detailsRow = new DetailsRow(navIcon);
                                add(detailsRow, element);
                            }
                        } else if (navIcon != null) {
                            final Image image = new Image(navIcon);
                            image.getElement().addClassName(TableView._CSS.recordDetailDisclosureNavIconClass());
                            add(image, element);
                        } else {
                            li.addClassName(_CSS.tableViewRowHasNavigationDisclosureClass());
                        }
                    }
                    if (showIcons) {
                        Object icon = record.get(iconField);
                        if (!(icon instanceof ImageResource) &&
                            !(icon instanceof Image))
                        {
                            icon = formatCellValue(record, recordIndex.intValue(), iconField);
                        }
                        if (icon != null) {
                            SpanElement span = document.createSpanElement();
                            span.addClassName(RECORD_ICON_CLASS_NAME);
                            li.appendChild(span);
                            li.addClassName(_CSS.tableViewRowHasIconClass());
                            ImageElement img = document.createImageElement();
                            if(icon instanceof ImageResource) {
                                img.setSrc(((ImageResource)icon).getSafeUri().asString());
                            } else if(icon instanceof Image) {
                                img.setSrc(((Image)icon).getUrl());
                            } else {
                                img.setSrc(icon.toString());
                            }
                            span.appendChild(img);
                        }
                    }
                    if (showDetailCount) {
                        String count = formatCellValue(record, recordIndex.intValue(), detailCountProperty);
                        if(count != null) {
                            SpanElement span = document.createSpanElement();
                            span.addClassName(RECORD_COUNTER_CLASS_NAME);
                            span.setInnerHTML(count);
                            li.appendChild(span);
                        }
                    }
                    String title = formatCellValue(record, recordIndex.intValue(), titleField);
                    if (title != null) {
                        SpanElement span = document.createSpanElement();
                        final String baseStyle = getBaseStyle(record, recordIndex.intValue(), getFieldNum(titleField));
                        if (baseStyle != null) span.setClassName(baseStyle);
                        span.addClassName(RECORD_TITLE_CLASS_NAME);
                        span.setInnerHTML(title);
                        li.appendChild(span);
                    }
                    if (recordLayout == RecordLayout.AUTOMATIC || recordLayout == RecordLayout.SUMMARY_FULL || recordLayout == RecordLayout.SUMMARY_INFO) {
                        String info = formatCellValue(record, recordIndex.intValue(), infoField);
                        if (info != null) {
                            ul.addClassName(_CSS.stackedTableViewClass());
                            li.addClassName(_CSS.tableViewRowHasRecordInfoClass());
                            SpanElement span = document.createSpanElement();
                            final String baseStyle = getBaseStyle(record, recordIndex.intValue(), getFieldNum(infoField));
                            if (baseStyle != null) span.setClassName(baseStyle);
                            span.addClassName(RECORD_INFO_CLASS_NAME);
                            span.appendChild(document.createTextNode(info));
                            li.appendChild(span);
                        }
                    }
                    if (recordLayout == RecordLayout.AUTOMATIC || recordLayout == RecordLayout.TITLE_DESCRIPTION || recordLayout == RecordLayout.SUMMARY_DATA || recordLayout == RecordLayout.SUMMARY_FULL || recordLayout == RecordLayout.SUMMARY_INFO) {
                        String description = formatCellValue(record, recordIndex.intValue(), descriptionField);
                        if (description != null) {
                            SpanElement span = document.createSpanElement();
                            final String baseStyle = getBaseStyle(record, recordIndex.intValue(), getFieldNum(descriptionField));
                            if (baseStyle != null) span.setClassName(baseStyle);
                            span.addClassName(RECORD_DESCRIPTION_CLASS_NAME);
                            span.appendChild(document.createTextNode(description));
                            li.appendChild(span);
                        }
                    }
                }
            } else {
                assert recordComponent != null;
                recordComponent.getElement().addClassName(RECORD_COMPONENT_CLASS_NAME);
                add(recordComponent, element);
            }

            ul.appendChild(li);
            lastLI = li;
        }
        return lastLI;
    }

    /**
     * Calculates and returns the bounding client {@link com.smartgwt.mobile.client.core.Rectangle}
     * of the row at <code>rowNum</code>, or <code>null</code> if the row at <code>rowNum</code>
     * is not being displayed by this <code>TableView</code>.
     * 
     * <p>The bounding rectangle can be passed to {@link com.smartgwt.mobile.client.widgets.Popover#showForArea(Rectangle) Popover.showForArea()}.
     * to display a <code>Popover</code> for the row.
     * 
     * @param rowNum the index of the row to calculate the client bounds of.
     * @return a new <code>Rectangle</code> representing the bounds relative to the viewport
     * of the row at <code>rowNum</code>, or <code>null</code> if the row at <code>rowNum</code>
     * is not being displayed by this <code>TableView</code>.
     */
    public final Rectangle getRowClientBounds(int rowNum) {
        final Record record = _getData().get(rowNum);
        final Object recordID = getRecordId(record);
        final Element element = elementMap.get(recordID);
        if (element != null) {
            return element.<SuperElement>cast().getBoundingClientRect().asRectangle();
        }
        return null;
    }

    /**
     * Calculates and returns the bounding client {@link com.smartgwt.mobile.client.core.Rectangle}
     * of the row corresponding to <code>record</code>, or <code>null</code> if this <code>TableView</code>
     * is not displaying a row for <code>Record</code>.
     * 
     * <p>The bounding rectangle can be passed to {@link com.smartgwt.mobile.client.widgets.Popover#showForArea(Rectangle) Popover.showForArea()}.
     * to display a <code>Popover</code> for the row.
     * 
     * @param record the <code>Record</code> corresponding to the row to calculate the client bounds of.
     * @return a new <code>Rectangle</code> representing the bounds relative to the viewport
     * of the row corresponding to <code>record</code>, or <code>null</code> if this <code>TableView</code>
     * is not displaying a row for <code>Record</code>.
     */
    public final Rectangle getRowClientBounds(Record record) {
        final Object recordID = getRecordId(record);
        final Element element = elementMap.get(recordID);
        if (element != null) {
            return element.<SuperElement>cast().getBoundingClientRect().asRectangle();
        }
        return null;
    }

    /**
     * Calculates the Y coordinate of the row at <code>rowNum</code> relative to the top of
     * the <code>TableView</code>. Note that this method is reliable for all rows that
     * are being shown by this <code>TableView</code>.
     *
     * @param rowNum the index of the row to calculate the top offset for.
     * @return the number of pixels from the top of the <code>TableView</code> that the row
     * at <code>rowNum</code> lies, or an estimate based on {@link #getCellHeight() cellHeight/rowHeight}
     * if the row is not being shown by this <code>TableView</code>.
     */
    public final int getRowTop(int rowNum) {
        if (rowNum < 0) throw new IllegalArgumentException("`rowNum' cannot be negative.");
        if (_getData() != null && rowNum < _getData().size()) {
            final Record record = _getData().get(rowNum);
            final Object recordID = getRecordId(record);
            final Element element = elementMap.get(recordID);
            if (element != null) {
                return element.getAbsoluteTop() - getElement().getAbsoluteTop();
            }
        }
        return cellHeight * rowNum;
    }

    /**
     * Calculates the Y coordinate of the row corresponding to <code>record</code> relative to
     * the top of the <code>TableView</code>.
     *
     * @param record the <code>Record</code> corresponding to the row to calculate the top offset for.
     * @return the number of pixels from the top of the <code>TableView</code> that the row
     * corresponding to <code>record</code> lies, or <code>null</code> if the row is not being shown by this
     * <code>TableView</code>.
     */
    public final Integer getRowTop(Record record) {
        if (_getData() != null) {
            final Object recordID = getRecordId(record);
            final Element element = elementMap.get(recordID);
            if (element != null) {
                return element.getAbsoluteTop() - getElement().getAbsoluteTop();
            }
        }
        return null;
    }

    @SGWTInternal
    public final String _getRecordRemovedProperty() {
        return recordRemovedProperty;
    }

    @SGWTInternal
    public void _setRecordRemovedProperty(String recordRemovedProperty) {
        if (recordRemovedProperty == null) throw new NullPointerException();
        if (!this.recordRemovedProperty.equals(recordRemovedProperty)) {
            if (markedForRemoval != null) {
                for (Record record : markedForRemoval) {
                    record.setAttribute(recordRemovedProperty, record.getAttributeAsObject(this.recordRemovedProperty));
                    record.setAttribute(this.recordRemovedProperty, null);
                }
            }
            this.recordRemovedProperty = recordRemovedProperty;
        }
    }

    public void markRecordRemoved(int rowNum) {
        if (_getData() == null) {
            return;
        }
        Record record = _getData().get(rowNum);  
        _markRecordRemoved(record);
    }

    @SGWTInternal
    public void _markRecordRemoved(Record record) {
        final Object recordID = getRecordId(record);
        final Element li = elementMap.get(recordID);
        final Element deleteDisclosureElem = getChildElementHavingClass(li, _CSS.recordDeleteDisclosureClass());
        _markRecordRemoved(record, deleteDisclosureElem, false);
    }

    @SGWTInternal
    protected void _markRecordRemoved(Record record, Element deleteDisclosureElem, boolean suppressEvent) {
        if (deleteDisclosureElem != null) {
            assert hasClassName(deleteDisclosureElem, _CSS.recordDeleteDisclosureClass());
            if (!hasClassName(deleteDisclosureElem, _CSS.checkedSelectionOrDeleteDisclosureClass())) {
                deleteDisclosureElem.addClassName(_CSS.checkedSelectionOrDeleteDisclosureClass());
                deleteDisclosureElem.getParentElement().addClassName("deletable");
            }
        }
        if (!_recordMarkedAsRemoved(record)) {
            markedForRemoval.add(record);
            record.setAttribute(recordRemovedProperty, Boolean.TRUE);
            if (!suppressEvent) {
                // TODO Event for marking / unmarking records as removed. For now use DataChangedEvent.
                fireEvent(new com.smartgwt.mobile.client.widgets.events.DataChangedEvent(record));
            }
        }
    }

    @SGWTInternal
    public void _unmarkRecordRemoved(Record record) {
        final Object recordID = getRecordId(record);
        final Element li = elementMap.get(recordID);
        final Element deleteDisclosureElem = getChildElementHavingClass(li, _CSS.recordDeleteDisclosureClass());
        _unmarkRecordRemoved(record, deleteDisclosureElem);
    }

    @SGWTInternal
    protected void _unmarkRecordRemoved(Record record, Element deleteDisclosureElem) {
        if (deleteDisclosureElem != null) {
            assert hasClassName(deleteDisclosureElem, _CSS.recordDeleteDisclosureClass());
            if (hasClassName(deleteDisclosureElem, _CSS.checkedSelectionOrDeleteDisclosureClass())) {
                deleteDisclosureElem.removeClassName(_CSS.checkedSelectionOrDeleteDisclosureClass());
                deleteDisclosureElem.getParentElement().removeClassName("deletable");
            }
        }
        if (_recordMarkedAsRemoved(record)) {
            markedForRemoval.remove(record);
            record.setAttribute(recordRemovedProperty, null);
            // TODO Event for marking / unmarking records as removed. For now use DataChangedEvent.
            fireEvent(new com.smartgwt.mobile.client.widgets.events.DataChangedEvent(record));
        }
    }

    @SGWTInternal
    public final boolean _recordMarkedAsRemoved(Record record) {
        return markedForRemoval.contains(record);
    }

    public void discardAllEdits() {
        for (Record record : markedForRemoval) {
            final Object recordID = getRecordId(record);
            final Element li = elementMap.get(recordID);
            if (li != null) {
                final Element deleteDisclosureElem = getChildElementHavingClass(li, _CSS.recordDeleteDisclosureClass());
                if (deleteDisclosureElem != null) {
                    deleteDisclosureElem.removeClassName(_CSS.checkedSelectionOrDeleteDisclosureClass());
                }
                li.removeClassName("deletable");
            }
            record.setAttribute(recordRemovedProperty, null);
        }
        markedForRemoval.clear();
    }

    public boolean saveAllEdits() {
        boolean nothingToSave = true;
        for (Record record : markedForRemoval) {
            nothingToSave = false;
            _getData().remove(record);
        }
        markedForRemoval.clear();
        _refreshRows();
        return !nothingToSave;
    }

    public void refreshRow(int rowNum) {
        if (_getData() == null) {
            return;
        }
        Record record = _getData().get(rowNum);
        Element element = elementMap.get(getRecordId(record));
        if (element != null) {
            String html = element.getInnerHTML();
            element.setInnerHTML(html);
        }
    }

    // Highlighting records

    /**
     * Should we show highlight styling when the user touches a record in this grid.
     *
     * @param hilite true to enable record highlighting on selection
     */
    public void setHiliteOnTouch(boolean hilite) {
        this.hiliteOnTouch = hilite;
    }

    public boolean getHiliteOnTouch() {
        return this.hiliteOnTouch;
    }

    @SGWTInternal
    protected void _setSelected(Element element) {
        final SelectionStyle selectionType = getSelectionType();
        assert selectionType != null && selectionType != SelectionStyle.NONE;
        if (!hasClassName(element, _CSS.selectedTableViewRowClass()) && !hasClassName(element, _CSS.selectedTableViewRowHasIconClass())) {
            if (showSelectedIcon) {
                element.addClassName(_CSS.selectedTableViewRowHasIconClass());
                if (selectionType == SelectionStyle.SINGLE || selectedIcon != null) {
                    SpanElement span = Document.get().createSpanElement();
                    span.addClassName(_CSS.selectedClass());
                    Image image = selectedIcon != null ? new Image(selectedIcon) : impl.getDefaultSingleSelectionIcon();
                    image.getElement().addClassName(_CSS.selectedClass());
                    span.setInnerHTML(image.toString());
                    element.insertFirst(span);
                } else {
                    assert selectionType != SelectionStyle.SINGLE;
                    assert selectionType == SelectionStyle.MULTIPLE;

                    // Find the .selection-disclosure element and add class `CSS.checkedSelectionOrDeleteDisclosureClass()'.
                    NodeList<Node> children = element.getChildNodes();
                    final int children_length = children.getLength();
                    int i = 0;
                    for (; i < children_length; ++i) {
                        final Node n = children.getItem(i);
                        if (n.getNodeType() != Node.ELEMENT_NODE) {
                            continue;
                        }
                        final Element child = Element.as(n);
                        if (hasClassName(child, _CSS.recordSelectionDisclosureClass())) {
                            child.addClassName(_CSS.checkedSelectionOrDeleteDisclosureClass());
                            break;
                        }
                    }
                    assert i < children_length;
                }
            } else {
                element.removeClassName(_CSS.clearingTemporaryTableViewRowSelectionClass());
                element.addClassName(_CSS.selectedTableViewRowClass());
            }
        }
    }

    @SGWTInternal
    protected void _clearSelected(Element element) {
        if (element == null) return;
        element.removeClassName(_CSS.selectedTableViewRowClass());
        element.removeClassName(_CSS.selectedTableViewRowHasIconClass());
        if (showSelectedIcon) {
            final SelectionStyle selectionType = getSelectionType();
            if (selectionType == SelectionStyle.SINGLE) {
                NodeList<Node> children = element.getChildNodes();
                final int children_length = children.getLength();
                for (int i = 0; i < children_length; ++i) {
                    final Node n = children.getItem(i);
                    if (n.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    final Element child = Element.as(n);
                    if (hasClassName(child, _CSS.selectedClass())) {
                        element.removeChild(child);
                        break;
                    }
                }
            } else if (selectionType == SelectionStyle.MULTIPLE) {
                NodeList<Node> children = element.getChildNodes();
                final int children_length = children.getLength();
                for (int i = 0; i < children_length; ++i) {
                    final Node n = children.getItem(i);
                    if (n.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    final Element child = Element.as(n);
                    if (hasClassName(child, _CSS.recordSelectionDisclosureClass())) {
                        child.removeClassName(_CSS.checkedSelectionOrDeleteDisclosureClass());
                    }
                }
            }
        }
    }


    /**
     * Set navigation style for this TableView. Default behavior will be {@link NavigationMode#WHOLE_RECORD}.
     *
     * @param navigationMode
     */
    public void setNavigationMode(NavigationMode navigationMode) {
        this.navigationMode = navigationMode;
    }

    public final NavigationMode getNavigationMode() {
        return navigationMode;
    }

    /**
     * Whether to show navigation controls by default on all records.  Can also be configured
     * per-record with {@link #setRecordNavigationProperty(String)}.
     *
     * @param showNavigation
     */
    public void setShowNavigation(boolean showNavigation) {
        this.showNavigation = showNavigation;
    }

    public final boolean getShowNavigation() {
        return showNavigation;
    }

    /**
     * Whether to show navigation controls for some specific record. If the {@link #setRecordNavigationProperty(String) record navigation property} is set
     * on the record in question, this will be respected, otherwise will return the result of the value set via {@link #setShowNavigation(boolean)}
     *
     * @param record
     * @return true if navigation controls should be shown for this record
     */
    public boolean getShowNavigation(Record record) {
        if (record != null) {
            Object shouldShow = record.get(this.getRecordNavigationProperty());
            if (shouldShow != null) {
                return (Boolean) shouldShow;
            }
        }
        return this.getShowNavigation();
    }

    /**
     * Boolean property on each record that controls whether navigation controls are shown for
     * that record. Defaults to <code>"_navigate"</code>.
     *
     * @param recordNavigationProperty
     */
    public void setRecordNavigationProperty(String recordNavigationProperty) {
        this.recordNavigationProperty = recordNavigationProperty;
    }

    public String getRecordNavigationProperty() {
        return recordNavigationProperty;
    }

    /**
     * The parent {@link com.smartgwt.mobile.client.widgets.layout.NavStack}.
     * 
     * <p>If the {@link #getNavigationMode() navigationMode} of this <code>TableView</code> is
     * {@link com.smartgwt.mobile.client.types.NavigationMode#WHOLE_RECORD}, you can set the
     * parentNavStack to the <code>NavStack</code> instance that contains this <code>TableView</code>
     * in its child widget hierarchy. Once set, this <code>TableView</code> will automatically
     * fade out and deselect the selected row when returning to the page that contains this
     * <code>TableView</code>, just like the standard iOS Settings app.
     * 
     * @return the parent <code>NavStack</code> if one has been set; otherwise <code>null</code>.
     */
    public final NavStack getParentNavStack() {
        return parentNavStack;
    }

    public void setParentNavStack(final NavStack parentNavStack) {
        if (beforePanelShownRegistration != null) {
            beforePanelShownRegistration.removeHandler();
            beforePanelShownRegistration = null;
        }
        this.parentNavStack = parentNavStack;
        if (parentNavStack == null) {
            if (_ul != null) _ul.removeClassName(_CSS.tableViewHasParentNavStackClass());
        } else {
            beforePanelShownRegistration = parentNavStack._addBeforePanelShownHandler(new BeforePanelShownHandler() {
                @Override
                public void _onBeforePanelShown(BeforePanelShownEvent event) {
                    if (navigationMode != NavigationMode.WHOLE_RECORD) return;

                    if (TableView.this.parentNavStack != parentNavStack) return;
                    if (!parentNavStack.equals(event.getSource())) return;

                    if (TableView.this.getSelectionType() == SelectionStyle.SINGLE) {
                        final Panel panel = event.getPanel();
                        if (panel == null) return;
                        Widget w = TableView.this;
                        do {
                            if (w.equals(panel)) {
                                final Record selectedRecord = getSelectedRecord();
                                if (selectedRecord != null) deselectRecord(selectedRecord);
                                break;
                            }
                        } while ((w = w.getParent()) != null);
                    }
                }
            });
        }
    }

    @Override
    public void setSelectionType(SelectionStyle selectionType) {
        if (getSelectionType() == selectionType) {
            return;
        }
        super.setSelectionType(selectionType);
        _refreshRows();
    }

    @Override
    public void setRecordCanSelectProperty(String recordCanSelectProperty) {
        this.recordCanSelectProperty = recordCanSelectProperty;
        _getSelectionObject().setCanSelectProperty(recordCanSelectProperty);
    }

    @Override
    public final String getRecordCanSelectProperty() {
        return recordCanSelectProperty;
    }


    @SGWTInternal
    protected boolean _canSelectRecord(Record record) {
        final SelectionStyle selectionType = getSelectionType();
        if (selectionType == null || selectionType == SelectionStyle.NONE) {
            return false;
        }
        final Boolean canSelect = record.getAttributeAsBoolean(recordCanSelectProperty);
        if (canSelect != null) {
            return canSelect.booleanValue();
        }
        return true;
    }

    public final boolean getShowSelectedIcon() {
        return showSelectedIcon;
    }

    public void setShowSelectedIcon(boolean showSelectedIcon) {
        this.showSelectedIcon = showSelectedIcon;
    }

    public final String getDetailCountProperty() {
        return detailCountProperty;
    }

    public void setDetailCountProperty(String detailCountProperty) {
        this.detailCountProperty = detailCountProperty;
    }

    public final String getCanRemoveProperty() {
        return canRemoveProperty;
    }

    public void setCanRemoveProperty(String canRemoveProperty) {
        this.canRemoveProperty = canRemoveProperty;
    }

    public boolean isDeferRemoval() {
        return deferRemoval;
    }

    public void setDeferRemoval(boolean deferRemoval) {
        this.deferRemoval = deferRemoval;
    }

    private void setSelecteds() {
        if (_getSelectionObject() == null) {
            return;
        }

        final Record[] selection = getSelectedRecords();
        if (selection != null) {
            final String primaryKeyFieldName = getPrimaryKeyFieldName();
            for (Record record : selection) {
                final Object recordID = record.getAttributeAsObject(primaryKeyFieldName);
                final Element li = elementMap.get(recordID);
                if (li == null) {
                    continue;
                }
                _setSelected(li);
            }
        }
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
        _refreshRows();
    }

    public final int getCellHeight() {
        return cellHeight;
    }

    public boolean getShowDetailCount() {
        return showDetailCount;
    }

    public void setShowDetailCount(Boolean showDetailCount) {
        this.showDetailCount = showDetailCount == null ? false : showDetailCount.booleanValue();
    }
    
    public boolean getCanRemoveRecords() {
        return canRemoveRecords;
    }

    public void setCanRemoveRecords(Boolean canRemoveRecords) {
        this.canRemoveRecords = canRemoveRecords == null ? false : canRemoveRecords.booleanValue();
        _refreshRows();
    }

    public boolean getCanReorderRecords() {
        return canReorderRecords;
    }

    public void setCanReorderRecords(Boolean canReorderRecords) {
        this.canReorderRecords = canReorderRecords == null ? false : canReorderRecords.booleanValue();
        _refreshRows();
    }
    
    public boolean getShowIcons() {
        return showIcons;
    }

    public void setShowIcons(Boolean showIcons) {
        this.showIcons = showIcons == null ? false : showIcons.booleanValue();
        _refreshRows();
    }

    public String getLoadingMessage() {
        return loadingMessage;
    }

    public void setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
    }

    public final String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public void setSelectedIcon(ImageResource selectedIcon) {
        this.selectedIcon = selectedIcon;
        _refreshRows();
    }

    public final ImageResource getSelectedIcon() {
        return selectedIcon;
    }

    /**
     * Setter to override the navigation icon shown next to records when {@link #getShowNavigation()}
     * returns true and {@link #getNavigationMode()} is set to {@link NavigationMode#NAVICON_ONLY}
     *
     * @param img
     * @see #getNavigationIcon(Record)
     */
    public void setNavIcon(ImageResource img) {
        navIcon = img;
    }

    /**
     * Navigation icon shown next to records when {@link #getShowNavigation()} returns true and
     * {@link #getNavigationMode()} is set to {@link NavigationMode#NAVICON_ONLY}. Default media
     * may be overridden via {@link #setNavIcon(ImageResource)}
     *
     * @return ImageResource
     * @see #getNavigationIcon(Record)
     */
    public final ImageResource getNavIcon() {
        return navIcon;
    }

    /**
     * Setter to override the navigation icon shown next to records when {@link #getShowNavigation()}
     * returns true and {@link #getNavigationMode()} is set to {@link NavigationMode#WHOLE_RECORD}
     *
     * @param img
     * @see #getNavigationIcon(Record)
     */
    public void setWholeRecordNavIcon(ImageResource img) {
        wholeRecordNavIcon = img;
    }

    /**
     * Navigation icon shown next to records when {@link #getShowNavigation()} returns true and
     * {@link #getNavigationMode()} is set to {@link NavigationMode#WHOLE_RECORD}. Default media
     * may be overridden via {@link #setWholeRecordNavIcon(ImageResource)}
     *
     * @return ImageResource
     * @see #getNavigationIcon(Record)
     */
    public final ImageResource getWholeRecordNavIcon() {
        return wholeRecordNavIcon;
    }

    /**
     * ImageResource to display as a NavigationIcon per record. Default behavior returns
     * {@link #getNavIcon()} or {@link #getWholeRecordNavIcon()} depending on
     * {@link #setNavigationMode(NavigationMode)} but could be overridden to customize this
     * icon on a per-record basis.
     *
     * @param record the record
     * @return ImageResource
     */
    public ImageResource getNavigationIcon(Record record) {
        if (navigationMode == NavigationMode.NAVICON_ONLY) {
            return getNavIcon();
        } else {
            return getWholeRecordNavIcon();
        }
    }

    public void showDeletableRecords() {
        _ul.addClassName(_CSS.tableViewShowDeleteDisclosuresClass());
    }

    public void hideDeletableRecords() {
        _ul.removeClassName(_CSS.tableViewShowDeleteDisclosuresClass());
        discardAllEdits();
    }

    public void showMoveableRecords() {
        if (_ul == null) {
            _ul = Document.get().createULElement();
        }
        _ul.addClassName(_CSS.tableViewShowMoveIndicatorsClass());
    }

    public void hideMoveableRecords() {
        if (_ul == null) {
            _ul = Document.get().createULElement();
        }
        _ul.removeClassName(_CSS.tableViewShowMoveIndicatorsClass());
    }

    private void recordNavigationClick(Record record) {
        RecordNavigationClickEvent._fire(this, record);
    }

    /**
     * Register a handler for when the data in the record changes - fired when the user marks the record for delete
     *
     * @param handler the handler
     * @return {@link HandlerRegistration}
     */
    public HandlerRegistration addDataChangedHandler(com.smartgwt.mobile.client.widgets.events.DataChangedHandler handler) {
        return this.addHandler(handler, com.smartgwt.mobile.client.widgets.events.DataChangedEvent.getType());
    }

    /**
     * Register a handler for a navigation click - fired when the user clicks on a record, or
     * on the navigate icon for a record depending on {@link #setNavigationMode(NavigationMode)}
     *
     * @param handler the handler
     * @return {@link HandlerRegistration}
     */
    public HandlerRegistration addRecordNavigationClickHandler(RecordNavigationClickHandler handler) {
        return this.addHandler(handler, RecordNavigationClickEvent.getType());
    }

    /**
     * Register a handler for a click on the image displayed in a record if {@link #setIconField(String) iconField}
     * has been specified
     *
     * @param handler the handler
     * @return {@link HandlerRegistration}
     */
    public HandlerRegistration addImageClickHandler(ImageClickHandler handler) {
        return this.addHandler(handler, ImageClickEvent.getType());
    }

    @Override
    public HandlerRegistration addRowContextClickHandler(RowContextClickHandler handler) {
        return addHandler(handler, RowContextClickEvent.getType());
    }

    /**
     * Register a handler for selection changed. Note that this method will fire on both selection and
     * deselection of records - check the {@link SelectionEvent#getState()} value for details
     * has been specified
     *
     * @param handler the handler
     * @return {@link HandlerRegistration}
     */
    @Override
    public HandlerRegistration addSelectionChangedHandler(SelectionChangedHandler handler) {
        return this.addHandler(handler, SelectionEvent.getType());
    }

    /**
     * Register a handler for details selected.
     * @param handler the handler
     * @return {@link HandlerRegistration}
     */
    public HandlerRegistration addDetailsSelectedHandler(DetailsSelectedHandler handler) {
        return this.addHandler(handler, DetailsSelectedEvent.getType());
    }
}
