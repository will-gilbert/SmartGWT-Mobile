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
package com.smartgwt.mobile.client.widgets.tab.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.client.widgets.tab.Tab;

public class TabSelectedEvent extends GwtEvent<TabSelectedHandler> {

    private static Type<TabSelectedHandler> TYPE = null;

    public static Type<TabSelectedHandler> getType() {
        if (TYPE == null) TYPE = new Type<TabSelectedHandler>();
        return TYPE;
    }

    public static <S extends HasTabSelectedHandlers> void _fire(S source, Tab tab, int tabNum) {
        if (TYPE != null) {
            final TabSelectedEvent event = new TabSelectedEvent(tab, tabNum);
            source.fireEvent(event);
        }
    }

    private Tab tab;
    private int tabNum;

    private TabSelectedEvent(Tab tab, int tabNum) {
        this.tab = tab;
        this.tabNum = tabNum;
    }

    public final Tab getTab() {
        return tab;
    }

    public final int getTabIndex() {
        return this.tabNum;
    }

    @Override
    public final Type<TabSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TabSelectedHandler handler) {
        handler.onTabSelected(this);
    }
}
