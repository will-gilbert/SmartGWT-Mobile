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

/**
 *
 */
package com.smartgwt.mobile.client.widgets.tab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.core.Function;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.internal.EventHandler;
import com.smartgwt.mobile.client.internal.theme.TabSetCssResourceBase;
import com.smartgwt.mobile.client.internal.util.AnimationUtil;
import com.smartgwt.mobile.client.internal.widgets.tab.TabSetItem;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.ContainerFeatures;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.PanelContainer;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.DropEvent;
import com.smartgwt.mobile.client.widgets.events.DropHandler;
import com.smartgwt.mobile.client.widgets.events.HasDropHandlers;
import com.smartgwt.mobile.client.widgets.events.HasPanelHideHandlers;
import com.smartgwt.mobile.client.widgets.events.HasPanelShowHandlers;
import com.smartgwt.mobile.client.widgets.events.PanelHideEvent;
import com.smartgwt.mobile.client.widgets.events.PanelHideHandler;
import com.smartgwt.mobile.client.widgets.events.PanelShowEvent;
import com.smartgwt.mobile.client.widgets.events.PanelShowHandler;
import com.smartgwt.mobile.client.widgets.grid.CellFormatter;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;
import com.smartgwt.mobile.client.widgets.icons.IconResources;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.layout.Layout;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.tab.events.HasTabDeselectedHandlers;
import com.smartgwt.mobile.client.widgets.tab.events.HasTabSelectedHandlers;
import com.smartgwt.mobile.client.widgets.tab.events.TabDeselectedEvent;
import com.smartgwt.mobile.client.widgets.tab.events.TabDeselectedHandler;
import com.smartgwt.mobile.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.mobile.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickHandler;

public class TabSet extends Layout implements PanelContainer, HasTabSelectedHandlers, HasTabDeselectedHandlers, HasPanelShowHandlers, HasPanelHideHandlers, HasDropHandlers {

    @SGWTInternal
    public static final TabSetCssResourceBase _CSS = ThemeResources.INSTANCE.tabsCSS();

    private static final int NO_TAB_SELECTED = -1,
            MORE_TAB_SELECTED = -2;

    private static class More extends NavStack {

        private static final String ID_PROPERTY = "_id",
                ICON_PROPERTY = "icon",
                TAB_PROPERTY = "tab";
        private static final ListGridField TITLE_FIELD = new ListGridField("-title") {{
            setCellFormatter(new CellFormatter() {
                @Override
                public String format(Object value, Record record, int rowNum, int fieldNum) {
                    final Tab tab = (Tab)record.getAttributeAsObject(TAB_PROPERTY);
                    if (tab.getTitle() != null) return tab.getTitle();
                    final Canvas pane = tab.getPane();
                    if (pane != null) return pane.getTitle();
                    return null;
                }
            });
        }};

        private static int nextMoreRecordID = 1;
        private static Record createMoreRecord(Tab tab) {
            assert tab != null;
            final Canvas pane = tab.getPane();
            final Record record = new Record();
            record.setAttribute(ID_PROPERTY, Integer.toString(nextMoreRecordID++));
            Object icon = tab.getIcon();
            if (icon == null && pane instanceof Panel) {
                icon = ((Panel)pane).getIcon();
            }
            record.setAttribute(ICON_PROPERTY, icon);
            record.setAttribute(TAB_PROPERTY, tab);
            return record;
        }

        private ScrollablePanel morePanel;
        private RecordList recordList;
        private TableView tableView;
        private HandlerRegistration recordNavigationClickRegistration;
        //TileLayout tileLayout = null;

        public More() {
            super("More", IconResources.INSTANCE.more());

            morePanel = new ScrollablePanel("More", IconResources.INSTANCE.more());
            morePanel.getElement().getStyle().setBackgroundColor("#f7f7f7");

            recordList = new RecordList();
            tableView = new TableView();
            tableView.setTitleField(TITLE_FIELD.getName());
            tableView.setShowNavigation(true);
            tableView.setSelectionType(SelectionStyle.SINGLE);
            tableView.setParentNavStack(this);
            tableView.setTableMode(TableMode.PLAIN);
            tableView.setShowIcons(true);
            recordNavigationClickRegistration = tableView.addRecordNavigationClickHandler(new RecordNavigationClickHandler() {
                @Override
                public void onRecordNavigationClick(RecordNavigationClickEvent event) {
                    final Record record = event.getRecord();
                    final Tab tab = (Tab)record.getAttributeAsObject(TAB_PROPERTY);
                    final TabSet tabSet = tab.getTabSet();
                    assert tabSet != null;
                    tabSet.swapTabs(recordList.indexOf(record) + tabSet.moreTabCount, tabSet.moreTabCount - 1);
                    tabSet.selectTab(tabSet.moreTabCount - 1, true);
                }
            });
            tableView.setFields(TITLE_FIELD);
            tableView.setData(recordList);
            morePanel.addMember(tableView);
            push(morePanel);
        }

        @Override
        public void destroy() {
            if (recordNavigationClickRegistration != null) {
                recordNavigationClickRegistration.removeHandler();
                recordNavigationClickRegistration = null;
            }
            super.destroy();
        }

        public final boolean containsNoTabs() {
            return recordList.isEmpty();
        }

        public void addTab(Tab tab, int position) {
            if (tab == null) throw new NullPointerException("`tab' cannot be null.");
            final Record moreRecord = createMoreRecord(tab);
            recordList.add(position, moreRecord);
        }

        public Tab removeTab(int position) {
            return (Tab)recordList.remove(position).getAttributeAsObject(TAB_PROPERTY);
        }

        public void setTab(int position, Tab tab) {
            if (tab == null) throw new NullPointerException("`tab' cannot be null.");
            recordList.set(position, createMoreRecord(tab));
        }

        public void swapTabs(int i, int j) {
            recordList.swap(i, j);
        }
    }

    private final List<Tab> tabs = new ArrayList<Tab>();

    // Tab bar to show tab widgets
    private HLayout tabBar = new HLayout();

    private int moreTabCount = 4;
    private boolean showMoreTab = true;
    private More more;
    private Tab moreTab;

    // paneContainer panel to hold tab panes
    private Layout paneContainer;
    //private final HashMap<Element, Tab> draggableMap = new HashMap<Element, Tab>();
    //private Canvas draggable, droppable;

    // Currently selected tab
    private int selectedTabIndex = NO_TAB_SELECTED;

    private transient Timer clearHideTabBarDuringFocusTimer;

    public TabSet() {
        getElement().addClassName(_CSS.tabSetClass());
        setWidth("100%");
        setHeight("100%");
        paneContainer = new Layout();
        paneContainer._setClassName(_CSS.tabPaneClass(), false);
        addMember(paneContainer);
        tabBar._setClassName(_CSS.tabBarClass(), false);
        tabBar._setClassName("sc-layout-box-pack-center", false);
        addMember(tabBar);
        //sinkEvents(Event.ONCLICK | Event.ONMOUSEWHEEL | Event.GESTUREEVENTS | Event.TOUCHEVENTS | Event.MOUSEEVENTS | Event.FOCUSEVENTS | Event.KEYEVENTS);
        _sinkFocusInEvent();
        _sinkFocusOutEvent();
    }

    @SGWTInternal
    public final Tab _getMoreTab() {
        return moreTab;
    }

    public final Tab[] getTabs() {
        return tabs.toArray(new Tab[tabs.size()]);
    }

    /**
     * Sets the pane of the given tab.
     * 
     * @param tab
     * @param pane
     */
    public void updateTab(Tab tab, Canvas pane) {
        if (tab == null) throw new NullPointerException("`tab' cannot be null.");
        final TabSet otherTabSet = tab.getTabSet();
        assert otherTabSet != null : "`tab' is not in any TabSet. In particular, it is not in this TabSet.";
        if (otherTabSet == null) {
            tab._setPane(pane);
        } else {
            assert this == otherTabSet : "`tab' is in a different TabSet.";
            if (this != otherTabSet) {
                assert otherTabSet != null;
                otherTabSet.updateTab(tab, pane);
            } else {
                final int tabPosition = getTabPosition(tab);
                if (tabPosition < 0) throw new IllegalArgumentException("`tab' is not in this TabSet.");
                updateTab(tabPosition, pane);
            }
        }
    }

    public void updateTab(int index, Canvas pane) throws IndexOutOfBoundsException {
        if (index < 0 || tabs.size() <= index) throw new IndexOutOfBoundsException("No tab exists at index " + index);
        final Tab tab = tabs.get(index);
        assert tab != null;
        final Canvas oldPane = tab.getPane();
        if (oldPane != pane) {
            if (oldPane != null) paneContainer.removeMember(oldPane);
            if (pane != null) {
                paneContainer.addMember(pane);
                if (selectedTabIndex != index) paneContainer.hideMember(pane);
            }
            tab._setPane(pane);
        }
    }

    /*******************************************************
     * Event handler registrations
     ******************************************************/

    /**
     * Add a tabSelected handler.
     * <p/>
     * Notification fired when a tab is selected.
     *
     * @param handler the tabSelectedHandler
     * @return {@link HandlerRegistration} used to remove this handler
     */
    @Override
    public HandlerRegistration addTabSelectedHandler(TabSelectedHandler handler) {
        return addHandler(handler, TabSelectedEvent.getType());
    }

    @Override
    public HandlerRegistration addDropHandler(DropHandler handler) {
        return addHandler(handler, DropEvent.getType());
    }

    /**
     * Add a tabDeselected handler.
     * <p/>
     * Notification fired when a tab is unselected.
     *
     * @param handler the tabDeselectedHandler
     * @return {@link HandlerRegistration} used to remove this handler
     */
    @Override
    public HandlerRegistration addTabDeselectedHandler(TabDeselectedHandler handler) {
        return addHandler(handler, TabDeselectedEvent.getType());
    }

    /**
     * ****************************************************
     * Settings
     * ****************************************************
     */

    @SuppressWarnings("unused")
    private Tab findTarget(Event event) {
        Tab tab = null;
        final int Y = (event.getTouches() != null && event.getTouches().length() > 0) ? event.getTouches().get(0).getClientY(): event.getClientY();
        final int X = (event.getTouches() != null && event.getTouches().length() > 0) ? event.getTouches().get(0).getClientX(): event.getClientX();
        if (Y >= tabBar.getAbsoluteTop() && !tabs.isEmpty()) {
            final int width = tabs.get(0)._getTabSetItem().getElement().getOffsetWidth();
            final int index = X / width;
            if (0 <= index &&
                ((showMoreTab && index < moreTabCount) ||
                 (!showMoreTab && index < tabs.size())))
            {
                tab = tabs.get(index);
            }
        }
        return tab;
    }

    @Override
    public void onBrowserEvent(Event event) {
        /*switch(event.getTypeInt()) {
            case Event.ONMOUSEOVER:
                if (!touched) {
                    if (draggable != null) {
                        final Tab tab = findTarget(event);
                        if (tab != null) {
                            droppable = tab._getTabSetItem();
                            droppable.fireEvent(new DropOverEvent());
                        } else if (droppable != null) {
                            droppable.fireEvent(new DropOutEvent());
                            droppable = null;
                        }
                    }
                }
                break;
            case Event.ONTOUCHMOVE:
            case Event.ONGESTURECHANGE:
                if (draggable != null) {
                    final Tab tab = findTarget(event);
                    if (tab != null) {
                        droppable = tab._getTabSetItem();
                        droppable.fireEvent(new DropOverEvent());
                    } else if (droppable != null) {
                        droppable.fireEvent(new DropOutEvent());
                        droppable = null;
                    }
                }
                super.onBrowserEvent(event);
                break;
            case Event.ONMOUSEUP:
                if (!touched) {
                    super.onBrowserEvent(event);
                    if (more != null && more.tileLayout != null) {
                        more.tileLayout.onEnd(event);
                    }
                    if (draggable != null && droppable != null) {
                        droppable.fireEvent(new DropEvent(draggable));
                        droppable = null;
                    }
                }
                break;
            case Event.ONTOUCHEND:
            case Event.ONGESTUREEND:
                super.onBrowserEvent(event);
                if (draggable != null && droppable != null) {
                    droppable.fireEvent(new DropEvent(draggable));
                    droppable = null;
                }
                break;
            default:
                super.onBrowserEvent(event);
                break;
        }*/

        if (Canvas._getHideTabBarDuringKeyboardFocus()) {
            // On Android devices, SGWT.mobile uses a technique similar to jQuery Mobile's hideDuringFocus
            // setting to fix the issue that the tabBar unnecessarily takes up a portion of the screen
            // when the soft keyboard is open. To work around this problem, when certain elements receive
            // keyboard focus, we add a special class to the TabSet element that hides the tabBar.
            // This special class is removed on focusout.
            //
            // One important consideration is that the user can move between input elements (such as
            // by using the Next/Previous buttons or by tapping on a different input) and the soft
            // keyboard will remain on screen. We don't want to show the tabBar only to hide it again.
            // See: JQM Issue 4724 - Moving through form in Mobile Safari with "Next" and "Previous"
            // system controls causes fixed position, tap-toggle false Header to reveal itself
            // https://github.com/jquery/jquery-mobile/issues/4724
            final String eventType = event.getType();
            if ("focusin".equals(eventType)) {
                if (EventHandler.couldShowSoftKeyboard(event)) {
                    if (clearHideTabBarDuringFocusTimer != null) {
                        clearHideTabBarDuringFocusTimer.cancel();
                        clearHideTabBarDuringFocusTimer = null;
                    }
                    getElement().addClassName(_CSS.hideTabBarDuringFocusClass());
                }
            } else if ("focusout".equals(eventType)) {
                // If the related event target cannot result in the soft keyboard showing, then
                // there is no need to wait; we can remove the hideTabBarDuringFocus class now.
                if (event.getRelatedEventTarget() != null && !EventHandler.couldShowSoftKeyboard(event)) {
                    if (clearHideTabBarDuringFocusTimer != null) {
                        clearHideTabBarDuringFocusTimer.cancel();
                        clearHideTabBarDuringFocusTimer = null;
                    }
                    getElement().removeClassName(_CSS.hideTabBarDuringFocusClass());

                // We use a timeout to clear the special CSS class because on Android 4.0.3 (possibly
                // affects other versions as well), there is an issue where tapping in the 48px above
                // the soft keyboard (where the tabBar would be) dismisses the soft keyboard.
                } else {
                    clearHideTabBarDuringFocusTimer = new Timer() {
                        @Override
                        public void run() {
                            clearHideTabBarDuringFocusTimer = null;
                            getElement().removeClassName(_CSS.hideTabBarDuringFocusClass());
                        }
                    };
                    clearHideTabBarDuringFocusTimer.schedule(1);
                }
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        // There is a very strange `TabSet' issue on iOS 6 Mobile Safari, but which does not appear
        // in iOS 5.0 or 5.1. For some reason, if the tabSelectedClass() is added to a `TabSetItem',
        // but the selected class is removed before the `TabSetItem' element is added to the document
        // later on, then the SGWT.mobile app will fail to load. However, if Web Inspector is attached,
        // then the app runs fine.
        //
        // This issue affects Showcase. In Showcase, the "Overview" tab is first selected when
        // it is added to the app's `TabSet' instance. It is then deselected when the "Widgets"
        // tab is programmatically selected.
        // To see the issue, comment out the check `if (Canvas._isIOSMin6_0() && !isAttached()) return;' in TabSetItem.setSelected(), then
        // compile & run Showcase in iOS 6 Mobile Safari.
        if (Canvas._isIOSMin6_0() && selectedTabIndex > 0) {
            tabs.get(selectedTabIndex)._getTabSetItem().setSelected(true);
        }
    }

    /*******************************************************
     * Add/Remove tabs
     ******************************************************/

    @SGWTInternal
    public void _onTabDrop(TabSetItem tabSetItem, DropEvent event) {
        /*Object source = event.getSource();
        if(source instanceof Tab) {
            tab.getElement().getStyle().setProperty("backgroundImage", "none");
            TileLayout.Tile draggable = (TileLayout.Tile)event.getData();
            Record record = TabSet.this.more.recordList.find("title",draggable.getTitle());
            if(record != null) {
                Panel newPanel = (Panel)record.get("panel");
                final int currentPosition = tabBar.getMemberNumber(tab);
                TabSet.this.replacePanel(currentPosition, newPanel);
                TileLayout tileLayout = (TileLayout)more.top();
                tileLayout.replaceTile(draggable, tab.getTitle(), tab.getIcon());
                more.recordList.remove(record);
//              final Panel oldPanel = tab.getPane() instanceof NavStack ? ((NavStack)tab.getPane()).top() : (Panel)tab.getPane();
                final Panel oldPanel = (Panel)tab.getPane();
                oldPanel.getElement().getStyle().clearDisplay();
                final Record finalRecord = record;
                more.recordList.add(0,new Record() {{put("_id", finalRecord.getAttribute("_id")); put("title", tab.getTitle()); put("icon", tab.getIcon());put("panel", oldPanel);}});
                more.tableView.setData(more.recordList);
            }
        }*/
    }

    private void swapTabs(int i, int j) {
        final int numTabs = tabs.size();
        assert 0 <= i && i < numTabs;
        assert 0 <= j && j < numTabs;
        if (i == j) return;
        // Make sure that i < j.
        if (!(i < j)) {
            final int tmp = i;
            i = j;
            j = tmp;
        }
        assert i < j;

        final Tab tabI = tabs.get(i), tabJ = tabs.get(j);
        assert tabI != null && tabJ != null;
        final TabSetItem tabSetItemI = tabI._getTabSetItem(),
                tabSetItemJ = tabJ._getTabSetItem();
        final Canvas paneI = tabI.getPane(), paneJ = tabJ.getPane();

        tabs.set(i, tabJ);
        tabs.set(j, tabI);

        if (showMoreTab && numTabs > moreTabCount) {
            assert more != null;
            if (j >= moreTabCount) {
                if (i >= moreTabCount) {
                    // Both tabs are in the More NavStack.
                    assert i != selectedTabIndex : "The tab at index " + i + " is supposed to be selected, but it is still in the More NavStack.";
                    assert j != selectedTabIndex : "The tab at index " + j + " is supposed to be selected, but it is still in the More NavStack.";
                    more.swapTabs(i - moreTabCount, j - moreTabCount);
                } else {
                    // The Tab at `j' is in the More NavStack, but the Tab at `i' is not.
                    assert j != selectedTabIndex : "The tab at index " + j + " is supposed to be selected, but it is still in the More NavStack.";

                    if (i == selectedTabIndex) {
                        tabSetItemI.setSelected(false);
                        if (paneI != null) paneContainer.hideMember(paneI);
                    }
                    tabBar.removeMember(tabSetItemI);
                    more.setTab(j - moreTabCount, tabI);

                    tabBar.addMember(tabSetItemJ, i);
                    if (i == selectedTabIndex) {
                        tabSetItemJ.setSelected(true);
                        if (paneJ != null) paneContainer.showMember(paneJ);

                        TabDeselectedEvent._fire(this, tabI, j, tabJ);
                        TabSelectedEvent._fire(this, tabJ, i);
                    } else {
                        if (paneJ != null) paneContainer.hideMember(paneJ);
                    }
                }
            } else {
                // Neither tab is in the More NavStack.
                assert selectedTabIndex != MORE_TAB_SELECTED;
                tabBar.removeMember(tabSetItemI);
                // TODO Don't remove the panes to save on calls to onLoad(), onUnload(), onAttach(), and onDetach().
                if (paneI != null && paneContainer.hasMember(paneI)) paneContainer.removeMember(paneI);
                tabBar.removeMember(tabSetItemJ);
                if (paneJ != null && paneContainer.hasMember(paneJ)) paneContainer.removeMember(paneJ);

                tabSetItemJ.setSelected(i == selectedTabIndex);
                tabBar.addMember(tabSetItemJ, i);
                if (paneJ != null) {
                    paneContainer.addMember(paneJ);
                    if (i != selectedTabIndex) paneContainer.hideMember(paneJ);
                }

                tabSetItemI.setSelected(j == selectedTabIndex);
                tabBar.addMember(tabSetItemI, j);
                if (paneI != null) {
                    paneContainer.addMember(paneI);
                    if (j != selectedTabIndex) paneContainer.hideMember(paneI);
                }

                if (i == selectedTabIndex) {
                    selectedTabIndex = j;

                    TabDeselectedEvent._fire(this, tabI, j, tabJ);
                    TabSelectedEvent._fire(this, tabJ, i);
                } else if (j == selectedTabIndex) {
                    selectedTabIndex = i;

                    TabDeselectedEvent._fire(this, tabJ, i, tabI);
                    TabSelectedEvent._fire(this, tabI, j);
                }
            }
        }
    }

    /**
     * Creates a {@link com.smartgwt.mobile.client.widgets.tab.Tab} and adds it to the end.
     * 
     * <p>This is equivalent to:
     * <pre>
     * final Tab tab = new Tab(panel.getTitle(), panel.getIcon());
     * tabSet.addTab(tab);
     * </pre>
     * 
     * @param panel the panel to add.
     * @return the automatically created <code>Tab</code>.
     */
    public Tab addPanel(Panel panel) {
        final Tab tab = new Tab(panel.getTitle(), panel.getIcon());
        tab.setPane(panel);
        addTab(tab);
        return tab;
    }

    /**
     * Add a tab to the end.
     *
     * @param tab the tab to add.
     */
    public void addTab(Tab tab) {
        addTab(tab, tabs.size());
    }

    /**
     * Add a tab at the given position.
     *
     * @param tab the tab to add.
     * @param position the index where the tab should be added. It is clamped within range
     * (0 through {@link #getNumTabs()}, inclusive) if out of bounds.
     */
    public void addTab(Tab tab, int position) {
        if (tab == null) throw new NullPointerException("`tab' cannot be null.");
        assert tab != moreTab;
        // Clamp `position' within range.
        position = Math.max(0, Math.min(position, tabs.size()));

        // Remove the tab if it exists.
        final int existingIndex = getTabPosition(tab);
        if (existingIndex >= 0) {
            // If the tab is being added in the exact same place, then return early.
            if (existingIndex == position) return;

            removeTab(existingIndex);
            if (existingIndex < position) --position;
        }

        final int currentNumTabs = tabs.size();

        tabs.add(position, tab);
        final TabSetItem tabSetItem = tab._ensureTabSetItem(this);
        assert tab.getTabSet() == this;

        // Add the tab's pane to `paneContainer'.
        final Canvas pane = tab.getPane();
        if (pane != null) {
            paneContainer.addMember(pane);
            paneContainer.hideMember(pane);
        }

        if (currentNumTabs + 1 > moreTabCount && showMoreTab) {
            if (more == null) {
                more = new More();
                assert moreTab == null;
                moreTab = new Tab(more.getTitle(), more.getIcon());
                moreTab.setPane(more);

                // The TabSetItem of `moreTab' and its pane (`more') are added to `tabBar' and
                // `paneContainer', respectively, but we don't want to add `moreTab' to `tabs';
                // we don't want to treat the More tab like a tab that was explicitly added.
                tabBar.addMember(moreTab._ensureTabSetItem(this), moreTabCount);
                paneContainer.addMember(more);
                paneContainer.hideMember(more);
            }

            if (position >= moreTabCount) {
                // Add to the More.
                more.addTab(tab, position - moreTabCount);
            } else {
                // Add the TabSetItem to `tabBar'.
                tabBar.addMember(tabSetItem, position);

                // That pushes one of the visible tabs into More.
                final Tab otherTab = tabs.get(moreTabCount);
                final TabSetItem otherTabSetItem = otherTab._getTabSetItem();
                if (moreTabCount == selectedTabIndex) {
                    otherTabSetItem.setSelected(false);
                    selectedTabIndex = NO_TAB_SELECTED;
                }
                tabBar.removeMember(otherTabSetItem);
                final Canvas otherPane = otherTab.getPane();
                if (otherPane != null) paneContainer.removeMember(otherPane);
                more.addTab(otherTab, 0);
            }
        } else {
            // Add the TabSetItem to `tabBar'.
            tabBar.addMember(tabSetItem, position);
        }

        if (selectedTabIndex == NO_TAB_SELECTED) {
            selectTab(position);
        }
    }

    /**
     * Remove a tab.
     *
     * @param tab to remove
     */
    public void removeTab(Tab tab) {
        assert tab == null || tab != moreTab;
        int index = tabs.indexOf(tab);
        if (index >= 0) {
            removeTab(index);
        }
    }

    /**
     * Remove a tab at the specified index.
     *
     * @param position the index of the tab to remove
     */
    public void removeTab(int position) {
        final int currentNumTabs = tabs.size();
        if (0 <= position && position < currentNumTabs) {
            final Tab tab = tabs.get(position);
            assert tab != null;
            assert tab != moreTab;

            tabs.remove(position);

            final TabSetItem tabSetItem = tab._getTabSetItem();
            if (!showMoreTab || position < moreTabCount) {
                tabBar.removeMember(tabSetItem);

                if (showMoreTab && currentNumTabs - 1 >= moreTabCount) {
                    // Move a tab from More to the tab bar.
                    final Tab firstMoreTab = more.removeTab(0);
                    final Tab otherTab = tabs.get(moreTabCount - 1);
                    assert firstMoreTab == otherTab;
                    final TabSetItem otherTabSetItem = otherTab._getTabSetItem();
                    tabBar.addMember(otherTabSetItem, moreTabCount - 1);
                }
            } else {
                assert more != null;
                more.removeTab(position - moreTabCount);
            }

            if (showMoreTab && currentNumTabs - 1 == moreTabCount) {
                // The More tab is no longer needed. Remove & destroy it.
                assert more.containsNoTabs();
                final TabSetItem moreTabSetItem = moreTab._getTabSetItem();
                tabBar.removeMember(moreTabSetItem);
                assert paneContainer.hasMember(more);
                paneContainer.removeMember(more);
                if (selectedTabIndex == MORE_TAB_SELECTED) {
                    assert more != null && moreTab.getPane() == more;
                    selectedTabIndex = NO_TAB_SELECTED;
                }
                moreTab._destroyTabSetItem();
                moreTab._setTabSet(null);
                moreTab.setPane(null);
                moreTab = null;
                more.destroy();
                more = null;
            }

            final Canvas pane = tab.getPane();
            if (pane != null) paneContainer.removeMember(pane);

            tab._setTabSet(null);

            if (position == selectedTabIndex) {
                selectedTabIndex = -1;
                tabSetItem.setSelected(false);
                TabDeselectedEvent._fire(this, tab, -1, null);
            }
        }
    }

    /**
     * Remove one or more tabs at the specified indexes.
     *
     * @param tabIndexes array of tab indexes
     */
    public void removeTabs(int[] tabIndexes) {
        if (tabIndexes == null) return;
        // Sort the indexes and remove the corresponding tabs in descending order.
        Arrays.sort(tabIndexes);
        for (int ri = tabIndexes.length; ri > 0; --ri) {
            removeTab(tabIndexes[ri - 1]);
        }
    }

    /*******************************************************
     * Update tabs
     ******************************************************/


    /*******************************************************
     * Selections
     ******************************************************/

    /**
     * Returns the index of the currently selected tab.
     *
     * @return -2 if the More tab is selected; otherwise, the index of the currently selected tab,
     * or -1 if no tab is selected.
     */
    public final int getSelectedTabNumber() {
        return selectedTabIndex;
    }

    /**
     * Returns the currently selected tab.
     *
     * @return the currently selected tab, or <code>null</code> if the More tab is selected
     * or no tab is selected.
     */
    public final Tab getSelectedTab() {
        final int index = selectedTabIndex;
        return (index >= 0 ? tabs.get(index) : null);
    }

    @SGWTInternal
    public void _selectMoreTab() {
        assert more != null && moreTab != null;
        if (selectedTabIndex != MORE_TAB_SELECTED) {
            if (selectedTabIndex != NO_TAB_SELECTED) {
                final Tab otherTab = tabs.get(selectedTabIndex);
                final TabSetItem otherTabSetItem = otherTab._getTabSetItem();
                final Canvas otherPane = otherTab.getPane();
                otherTabSetItem.setSelected(false);
                if (otherPane != null) paneContainer.hideMember(otherPane);

                // Even though the event's `newTab' could be considered `moreTab', we don't
                // pass `moreTab' as the third parameter to avoid exposing a reference to this
                // internal object.
                TabDeselectedEvent._fire(this, otherTab, selectedTabIndex, null);
            }

            final TabSetItem moreTabSetItem = moreTab._getTabSetItem();
            assert moreTab.getPane() == more;
            moreTabSetItem.setSelected(true);
            paneContainer.showMember(more);

            selectedTabIndex = MORE_TAB_SELECTED;
        }
    }

    /**
     * Select a tab by index
     *
     * @param index the tab index
     */
    public void selectTab(int index) throws IndexOutOfBoundsException {
        selectTab(index, false);
    }

    private void selectTab(int index, boolean animate) throws IndexOutOfBoundsException {
        if (tabs.size() <= index) throw new IndexOutOfBoundsException("No tab exists at index " + index);
        final Tab tab;
        if (index < 0) {
            // Allow a negative `index' to mean deselect the currently selected Tab (if any).
            tab = null;
            index = NO_TAB_SELECTED;
        } else {
            tab = tabs.get(index);
            if (tab.getDisabled()) return;
        }
        if (selectedTabIndex >= 0) {
            final Tab oldSelectedTab = tabs.get(selectedTabIndex);
            if (index == selectedTabIndex) return;

            final TabSetItem oldSelectedTabSetItem = oldSelectedTab._getTabSetItem();
            oldSelectedTabSetItem.setSelected(false);

            final Canvas oldSelectedPane = oldSelectedTab.getPane();
            if (oldSelectedPane != null) {
                paneContainer.hideMember(oldSelectedPane);
                if (oldSelectedPane instanceof Panel) PanelHideEvent.fire(this, (Panel)oldSelectedPane);
            }

            TabDeselectedEvent._fire(this, oldSelectedTab, selectedTabIndex, tab);
        } else if (selectedTabIndex == MORE_TAB_SELECTED) {
            assert more != null && moreTab != null;
            final TabSetItem moreTabSetItem = moreTab._getTabSetItem();
            moreTabSetItem.setSelected(false);
            assert moreTab.getPane() == more;
            if (!animate) paneContainer.hideMember(more);
        }
        if (index >= 0) {
            assert tab != null;
            final TabSetItem tabSetItem = tab._getTabSetItem();

            if (showMoreTab && index >= moreTabCount) {
                final Tab otherTab = tabs.get(moreTabCount - 1);
                tabs.set(moreTabCount - 1, tab);
                tabs.set(index, otherTab);
                tabBar.removeMember(otherTab._getTabSetItem());
                tabBar.addMember(tabSetItem, moreTabCount - 1);
                assert more != null;
                more.setTab(index - moreTabCount, otherTab);
                index = moreTabCount - 1;
            }

            tabSetItem.setSelected(true);
            final Canvas pane = tab.getPane();
            if (pane != null) {
                if (selectedTabIndex == MORE_TAB_SELECTED && animate) {
                    final Function<Void> callback;
                    if (!(pane instanceof Panel)) callback = null;
                    else {
                        callback = new Function<Void>() {
                            @Override
                            public Void execute() {
                                assert pane instanceof Panel;
                                PanelShowEvent.fire(TabSet.this, (Panel)pane);
                                return null;
                            }
                        };
                    }
                    AnimationUtil.slideTransition(more, pane, paneContainer, AnimationUtil.Direction.LEFT, null, callback, false);
                } else {
                    paneContainer.showMember(pane);
                    if (pane instanceof Panel) PanelShowEvent.fire(this, (Panel)pane);
                }
            }

            selectedTabIndex = index;
            TabSelectedEvent._fire(this, tab, index);
            if (_HISTORY_ENABLED) History.newItem(tab.getTitle());
        } else selectedTabIndex = NO_TAB_SELECTED;
    }

    /**
     * Select a tab.
     *
     * @param tab the canvas representing the tab
     */
    public void selectTab(Tab tab) {
        assert tab == null || tab != moreTab;
        int index = tab == null ? -1 : getTabPosition(tab);
        selectTab(index);
    }

    /*******************************************************
     * Enable/disable tabs
     ******************************************************/

    public void enableTab(String id) {
        enableTab(getTab(id));
    }

    public void enableTab(Tab tab) {
        final int position = getTabPosition(tab);
        if (position >= 0) {
            assert position < tabs.size();
            enableTab(position);
        }
    }

    /**
     * Enable a tab if it is currently disabled.
     *
     * @param index the tab index
     */
    public void enableTab(int index) throws IndexOutOfBoundsException {
        if (index < 0 || tabs.size() <= index) throw new IndexOutOfBoundsException("No tab exists at index " + index);
        final Tab tab = tabs.get(index);
        tab.setDisabled(false);
    }

    /**
     * Disable a tab if it is currently enabled. If the tab is selected, it is deselected.
     *
     * @param index the tab index
     */
    public void disableTab(int index) throws IndexOutOfBoundsException {
        if (index < 0 || tabs.size() <= index) throw new IndexOutOfBoundsException("No tab exists at index " + index);
        final Tab tab = tabs.get(index);
        tab.setDisabled(true);
        if (selectedTabIndex == index) {
            selectTab(-1);
        }
    }

    /*******************************************************
     * State and tab query methods
     ******************************************************/

    public final int getNumTabs() {
        return tabs.size();
    }

    public final int getTabCount() {
        return getNumTabs();
    }

    private final int getTabPosition(Tab other) {
        if (other == null) return -1;
        final int tabs_size = tabs.size();
        for (int i = 0; i < tabs_size; ++i) {
            if (tabs.get(i).equals(other)) return i;
        }
        return -1;
    }

    /**
     * Returns the canvas representing a tab.
     *
     * @param tabIndex index of tab
     * @return the tab widget or null if not found
     */
    public final Tab getTab(int tabIndex) {
        if (0 <= tabIndex && tabIndex < tabs.size()) {
            return tabs.get(tabIndex);
        }
        return null;
    }

    public final Tab getTab(String id) {
        if (id == null) return null;
        final int tabs_size = tabs.size();
        for (int i = 0; i < tabs_size; ++i) {
            final Tab tab = tabs.get(i);
            if (id.equals(tab.getID())) return tab;
        }
        return null;
    }

    /**
     * ****************************************************
     * Helper methods
     * ****************************************************
     */

    @Override
    public ContainerFeatures getContainerFeatures() {
        return new ContainerFeatures(this, true, false, false, true, 0);
    }

    @Override
    public HandlerRegistration addPanelHideHandler(PanelHideHandler handler) {
        return addHandler(handler, PanelHideEvent.getType());
    }

    @Override
    public HandlerRegistration addPanelShowHandler(PanelShowHandler handler) {
        return addHandler(handler, PanelShowEvent.getType());
    }

    public void setMoreTabCount(int moreTabCount) {
        if (this.moreTabCount == moreTabCount) return;
        // We need moreTabCount to be at least one so that we can swap out a non-More tab
        // with the selected tab.
        if (moreTabCount < 1) throw new IllegalArgumentException("`moreTabCount' must be at least 1.");
        if (more != null) throw new IllegalStateException("`moreTabCount' cannot be changed once the More tab has been created.");
        if (tabs.size() > moreTabCount && showMoreTab) throw new IllegalArgumentException("`moreTabCount` cannot be set to a number less than the current number of tabs.");
        this.moreTabCount = moreTabCount;
    }

    public final int getMoreTabCount() {
        return moreTabCount;
    }

    public void setShowMoreTab(boolean showMoreTab) {
        if (this.showMoreTab == showMoreTab) return;
        if (more != null) throw new IllegalStateException("`showMoreTab' cannot be changed once the More tab has been created.");
        if (tabs.size() > moreTabCount && showMoreTab) throw new IllegalArgumentException("The More tab cannot be enabled now that there are already more than `this.getMoreTabCount()' tabs in this TabSet.");
        this.showMoreTab = showMoreTab;
    }

    //public void setDraggable(Canvas draggable) {
    //    this.draggable = draggable;
    //}
}