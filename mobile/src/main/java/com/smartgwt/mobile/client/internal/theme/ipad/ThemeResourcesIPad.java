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

package com.smartgwt.mobile.client.internal.theme.ipad;

import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.theme.ios.NavigationBarCssResourceIOS;
import com.smartgwt.mobile.client.internal.theme.ios.TableViewCssResourceIOS;
import com.smartgwt.mobile.client.internal.theme.ios.ThemeResourcesIOS;
import com.smartgwt.mobile.client.internal.theme.ios.ToolStripCssResourceIOS;
import com.smartgwt.mobile.client.internal.theme.iphone.MenuCssResourceIPhone;
import com.smartgwt.mobile.client.internal.theme.iphone.PopoverCssResourceIPhone;
import com.smartgwt.mobile.client.theme.BaseCssResource;
import com.smartgwt.mobile.client.theme.ButtonsCssResource;
import com.smartgwt.mobile.client.theme.FormCssResource;

@SGWTInternal
public interface ThemeResourcesIPad extends ThemeResourcesIOS {

    @Source("com/smartgwt/mobile/client/internal/theme/ios/alertview_background.png")
    public DataResource alertViewBackgroundImageData();
    @Source("com/smartgwt/mobile/client/internal/theme/ios/alertview_background.png")
    public ImageResource alertViewBackgroundImage();
    @Source("com/smartgwt/mobile/client/internal/theme/iphone/iOS6_back_arrow_tip.png")
    public DataResource iOS6BackButtonArrowTipImageData();
    @Source("com/smartgwt/mobile/client/internal/theme/iphone/iOS6_back_arrow_tip.png")
    public ImageResource iOS6BackButtonArrowTipImage();
    @Source("com/smartgwt/mobile/client/internal/theme/iphone/iOS6_next_arrow_tip.png")
    public DataResource iOS6NextButtonArrowTipImageData();
    @Source("com/smartgwt/mobile/client/internal/theme/iphone/iOS6_next_arrow_tip.png")
    public ImageResource iOS6NextButtonArrowTipImage();
    @Source("com/smartgwt/mobile/client/internal/theme/iphone/iOS6_back_arrow_tip~2.png")
    public DataResource retinaIOS6BackButtonArrowTipImageData();
    @Source("com/smartgwt/mobile/client/internal/theme/iphone/iOS6_next_arrow_tip~2.png")
    public DataResource retinaIOS6NextButtonArrowTipImageData();
    @Source("com/smartgwt/mobile/client/internal/theme/iphone/menuitem_border-right.png")
    public DataResource menuItemBorderRightImageData();
    @Source("com/smartgwt/mobile/client/internal/theme/iphone/menuitem_border-left.png")
    public DataResource menuItemBorderLeftImageData();
    @Source("com/smartgwt/mobile/client/internal/theme/ios/searchitem_icon.png")
    public DataResource searchItemIconImageData();
    @Source("com/smartgwt/mobile/client/internal/theme/ios/searchitem_icon.png")
    public ImageResource searchItemIconImage();
    @Source("com/smartgwt/mobile/client/internal/theme/ios/searchitem_icon~2.png")
    public DataResource retinaSearchItemIconImageData();
    @Source({ "com/smartgwt/mobile/client/theme/menu.gwt.css", "com/smartgwt/mobile/client/internal/theme/iphone/menu.gwt.css" })
    public MenuCssResourceIPhone menuCSS();
    @Source({ "com/smartgwt/mobile/client/theme/headings.gwt.css", "com/smartgwt/mobile/client/theme/buttons.gwt.css", "com/smartgwt/mobile/client/theme/popover.gwt.css", "com/smartgwt/mobile/client/internal/theme/iphone/popover.gwt.css" })
    public PopoverCssResourceIPhone popoverCSS();



    @Source("back_arrow_tip.png")
    public DataResource backButtonArrowTipImageData();
    @Source("back_arrow_tip.png")
    public ImageResource backButtonArrowTipImage();
    @Source("next_arrow_tip.png")
    public DataResource nextButtonArrowTipImageData();
    @Source("next_arrow_tip.png")
    public ImageResource nextButtonArrowTipImage();
    @Source("back_arrow_tip~2.png")
    public DataResource retinaBackButtonArrowTipImageData();
    @Source("next_arrow_tip~2.png")
    public DataResource retinaNextButtonArrowTipImageData();

    @Source({ "com/smartgwt/mobile/client/theme/base.gwt.css", "com/smartgwt/mobile/client/internal/theme/ios/base.gwt.css", "base.gwt.css" })
    public BaseCssResource baseCSS();

    @Source({ "com/smartgwt/mobile/client/theme/buttons.gwt.css", "com/smartgwt/mobile/client/internal/theme/ios/buttons.gwt.css", "buttons.gwt.css" })
    public ButtonsCssResource buttonsCSS();

    @Source({ "com/smartgwt/mobile/client/theme/form.gwt.css", "com/smartgwt/mobile/client/internal/theme/ios/form.gwt.css", "form.gwt.css" })
    public FormCssResource formCSS();

    @Source({ "com/smartgwt/mobile/client/theme/form.gwt.css", "form_landscape.gwt.css" })
    public FormCssResource formCSSLandscape();

    @Source({ "com/smartgwt/mobile/client/theme/navigationbar.gwt.css", "com/smartgwt/mobile/client/internal/theme/ios/navigationbar.gwt.css", "navigationbar.gwt.css" })
    public NavigationBarCssResourceIOS navigationBarCSS();

    @Source({ "com/smartgwt/mobile/client/theme/toolbar.gwt.css", "com/smartgwt/mobile/client/theme/form.gwt.css", "com/smartgwt/mobile/client/theme/tableview.gwt.css", "com/smartgwt/mobile/client/internal/theme/ios/tableview.gwt.css", "tableview.gwt.css" })
    public TableViewCssResourceIOS tableViewCSS();

    @Source({ "com/smartgwt/mobile/client/theme/toolbar.gwt.css", "com/smartgwt/mobile/client/theme/form.gwt.css", "com/smartgwt/mobile/client/theme/tableview.gwt.css", "tableview_landscape.gwt.css" })
    public TableViewCssResourceIOS tableViewCSSLandscape();

    @Source({ "com/smartgwt/mobile/client/theme/toolbar.gwt.css", "com/smartgwt/mobile/client/internal/theme/ios/toolbar.gwt.css", "toolbar.gwt.css" })
    public ToolStripCssResourceIOS toolbarCSS();

    @Source({ "com/smartgwt/mobile/client/internal/theme/alertview.gwt.css", "com/smartgwt/mobile/client/theme/window.gwt.css", "com/smartgwt/mobile/client/internal/theme/ios/window.gwt.css", "window.gwt.css" })
    public WindowCssResourceIPad windowCSS();
}
