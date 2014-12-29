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

package com.smartgwt.mobile.client.widgets.tab.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.tab.Tab;

public class TabDeselectedEvent extends GwtEvent<TabDeselectedHandler> {

    private static Type<TabDeselectedHandler> TYPE = null;

    public static Type<TabDeselectedHandler> getType() {
        if (TYPE == null) TYPE = new Type<TabDeselectedHandler>();
        return TYPE;
    }

    @SGWTInternal
    public static <S extends HasTabDeselectedHandlers> void _fire(S source, Tab tab, int tabNum, Tab newTab) {
        if (TYPE != null) {
            final TabDeselectedEvent event = new TabDeselectedEvent(tab, tabNum, newTab);
            source.fireEvent(event);
        }
    }

    private Tab tab;
    private int tabNum;
    private Tab newTab;

    private TabDeselectedEvent(Tab tab, int tabNum, Tab newTab) {
        this.tab = tab;
        this.tabNum = tabNum;
        this.newTab = newTab;
    }

    public final String getID() {
        return tab == null ? null : tab.getID();
    }

    public final Tab getTab() {
        return tab;
    }

    public final int getTabNum() {
        return tabNum;
    }

    public final int getTabIndex() {
        return getTabNum();
    }

    public final Canvas getTabPane() {
        return tab == null ? null : tab.getPane();
    }

    public final Tab getNewTab() {
        return newTab;
    }

    @Override
    public final Type<TabDeselectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TabDeselectedHandler handler) {
        handler.onTabDeselected(this);
    }
}
