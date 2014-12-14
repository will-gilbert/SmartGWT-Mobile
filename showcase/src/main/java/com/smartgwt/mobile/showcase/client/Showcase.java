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
package com.smartgwt.mobile.showcase.client;

// GWT
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootLayoutPanel;

// SmartGWT Mobile
import com.smartgwt.mobile.client.cordova.CordovaEntryPoint;
import com.smartgwt.mobile.client.widgets.tab.Tab;
import com.smartgwt.mobile.client.widgets.tab.TabSet;

// Showcase Application Panes
import com.smartgwt.mobile.showcase.client.overview.About;
import com.smartgwt.mobile.showcase.client.overview.Overview;
import com.smartgwt.mobile.showcase.client.widgets.Widgets;

// Showcase icons
import com.smartgwt.mobile.showcase.client.resources.AppResources;

/**
 * Showcase entry point class.
 */
public class Showcase extends CordovaEntryPoint {

    // Each pane extends 'NavStack'
    private final Tab overviewTab, widgetsTab, aboutTab;
    private final TabSet tabSet;

    public Showcase() {

        // Builds a 'Tab' set along the bottom of the window
        tabSet = new TabSet();

        // Build 'Overview' tab; Load 'Overview' pane
        overviewTab = new Tab("Overview", AppResources.INSTANCE.home_prerendered_iOS());
        overviewTab.setIconPrerendered(true);
        overviewTab.setPane(new Overview());
        tabSet.addTab(overviewTab);

        // Build 'Widgets' tab; Load 'Widgets' pane
        widgetsTab = new Tab("Widgets", AppResources.INSTANCE.files_prerendered_iOS());
        widgetsTab.setIconPrerendered(true);
        widgetsTab.setPane(new Widgets());
        tabSet.addTab(widgetsTab);

        // Build 'About' tab; Load 'About' pane
        aboutTab = new Tab("About", AppResources.INSTANCE.info_prerendered_iOS());
        aboutTab.setIconPrerendered(true);
        aboutTab.setPane(new About());
        tabSet.addTab(aboutTab);

        // Select the 'Widgets' tab as the default
        tabSet.selectTab(widgetsTab);
    }

    @Override
    public void onDeviceReady() {

        RootLayoutPanel.get().add(tabSet);

        final Element appLoadingPanelElem = Document.get().getElementById("appLoadingPanel");

        appLoadingPanelElem.getStyle().setOpacity(0.0);

        new Timer() {
            @Override
            public void run() {
                appLoadingPanelElem.removeFromParent();
            }
        }.schedule(350);
    }
}
