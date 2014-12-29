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

package com.smartgwt.mobile.client.theme;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;
import com.smartgwt.mobile.client.internal.theme.InternalThemeResources;
import com.smartgwt.mobile.client.internal.theme.ThemeResourcesBase;
import com.smartgwt.mobile.client.internal.theme.ThemeResourcesFactory;

public interface ThemeResources extends InternalThemeResources {

    public static final ThemeResourcesBase INSTANCE = GWT.<ThemeResourcesFactory>create(ThemeResourcesFactory.class).create();

    @Source("activityindicator.gwt.css")
    public ActivityIndicatorCssResource activityIndicatorCSS();

    @Source("base.gwt.css")
    public BaseCssResource baseCSS();
    @Source("iOS/base.css")
    public TextResource otherBaseCSS();

    @Source("base.gwt.css")
    public BaseCssResource baseCSSLandscape();

    @Source("buttons.gwt.css")
    public ButtonsCssResource buttonsCSS();

    @Source("dialog.gwt.css")
    public DialogCssResource dialogCSS();

    @Source("form.gwt.css")
    public FormCssResource formCSS();

    @Source("form.gwt.css")
    public FormCssResource formCSSLandscape();

    @Source("headings.gwt.css")
    public HeadingsCssResource headingsCSS();

    @Source("iOS/layout.css")
    public TextResource layoutCSS();

    @Source("navigationbar.gwt.css")
    public NavigationBarCssResource navigationBarCSS();

    @Source("iOS/panel.css")
    public TextResource panelCSS();

    @Source("popover.gwt.css")
    public PopoverCssResource popoverCSS();

    @Source("progressbar.gwt.css")
    public ProgressbarCssResource progressbarCSS();

    @Source("scrollable.gwt.css")
    public ScrollablePanelCssResource scrollableCSS();

    @Source("slider.gwt.css")
    public SliderCssResource sliderCSS();

    @Source("switchitem.gwt.css")
    public SwitchItemCssResource switchItemCSS();

    @Source("tableview.gwt.css")
    public TableViewCssResource tableViewCSS();

    @Source("tableview.gwt.css")
    public TableViewCssResource tableViewCSSLandscape();

    @Source("tabs.gwt.css")
    public TabSetCssResource tabsCSS();

    @Source("tabs.gwt.css")
    public TabSetCssResource tabsCSSLandscape();

    @Source("toolbar.gwt.css")
    public ToolStripCssResource toolbarCSS();

    @Source("menu.gwt.css")
    public MenuCssResource menuCSS();

    @Source("window.gwt.css")
    public WindowCssResource windowCSS();

    @Source("window.gwt.css")
    public WindowCssResource windowCSSLandscape();
}
