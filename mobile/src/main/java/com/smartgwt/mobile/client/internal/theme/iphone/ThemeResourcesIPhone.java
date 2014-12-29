package com.smartgwt.mobile.client.internal.theme.iphone;

import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.theme.ios.NavigationBarCssResourceIOS;
import com.smartgwt.mobile.client.internal.theme.ios.ThemeResourcesIOS;
import com.smartgwt.mobile.client.internal.theme.ios.ToolStripCssResourceIOS;
import com.smartgwt.mobile.client.theme.BaseCssResource;
import com.smartgwt.mobile.client.theme.ButtonsCssResource;

@SGWTInternal
public interface ThemeResourcesIPhone extends ThemeResourcesIOS {

    @Source("pinstripe_backgroundtile.png")
    public DataResource pinstripeBackgroundTileImageData();
    @Source("pinstripe_backgroundtile_iOS6.png")
    public DataResource iOS6PinstripeBackgroundTileImageData();

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
    @Source("iOS6_back_arrow_tip.png")
    public DataResource iOS6BackButtonArrowTipImageData();
    @Source("iOS6_back_arrow_tip.png")
    public ImageResource iOS6BackButtonArrowTipImage();
    @Source("iOS6_next_arrow_tip.png")
    public DataResource iOS6NextButtonArrowTipImageData();
    @Source("iOS6_next_arrow_tip.png")
    public ImageResource iOS6NextButtonArrowTipImage();
    @Source("iOS6_back_arrow_tip~2.png")
    public DataResource retinaIOS6BackButtonArrowTipImageData();
    @Source("iOS6_next_arrow_tip~2.png")
    public DataResource retinaIOS6NextButtonArrowTipImageData();

    // The left (right) menu border image was created with the following steps:
    // 1. Create a new 1x38 image in GIMP, transparent background.
    // 2. Select the Blend Tool. Use the built-in gradient "FG to Transparent". Set the foreground
    //    color to white (black).
    // 3. Fill from (0.5, 0) to (0.5, 50).
    @Source("menuitem_border-right.png")
    public DataResource menuItemBorderRightImageData();

    @Source("menuitem_border-left.png")
    public DataResource menuItemBorderLeftImageData();

    // The reason why the search item icon appears as a DataResource and ImageResource is so
    // that the referenced image may be used with @url lines as well as value() calls.
    // https://groups.google.com/d/topic/google-web-toolkit/3120BA8fXHY/discussion
    @Source("com/smartgwt/mobile/client/internal/theme/ios/searchitem_icon.png")
    public DataResource searchItemIconImageData();
    @Source("com/smartgwt/mobile/client/internal/theme/ios/searchitem_icon.png")
    public ImageResource searchItemIconImage();
    @Source("com/smartgwt/mobile/client/internal/theme/ios/searchitem_icon~2.png")
    public DataResource retinaSearchItemIconImageData();

    @Source({ "com/smartgwt/mobile/client/theme/base.gwt.css", "com/smartgwt/mobile/client/internal/theme/ios/base.gwt.css", "base.gwt.css" })
    public BaseCssResource baseCSS();

    @Source({ "com/smartgwt/mobile/client/theme/base.gwt.css", "base_landscape.gwt.css" })
    public BaseCssResource baseCSSLandscape();

    @Source({ "com/smartgwt/mobile/client/theme/buttons.gwt.css", "com/smartgwt/mobile/client/internal/theme/ios/buttons.gwt.css", "buttons.gwt.css" })
    public ButtonsCssResource buttonsCSS();

    @Source({ "com/smartgwt/mobile/client/theme/menu.gwt.css", "menu.gwt.css" })
    public MenuCssResourceIPhone menuCSS();

    @Source({ "com/smartgwt/mobile/client/theme/navigationbar.gwt.css", "com/smartgwt/mobile/client/internal/theme/ios/navigationbar.gwt.css", "navigationbar.gwt.css" })
    public NavigationBarCssResourceIOS navigationBarCSS();

    @Source({ "com/smartgwt/mobile/client/theme/headings.gwt.css", "com/smartgwt/mobile/client/theme/buttons.gwt.css", "com/smartgwt/mobile/client/theme/popover.gwt.css", "popover.gwt.css" })
    public PopoverCssResourceIPhone popoverCSS();

    @Source({ "com/smartgwt/mobile/client/theme/toolbar.gwt.css", "com/smartgwt/mobile/client/internal/theme/ios/toolbar.gwt.css", "toolbar.gwt.css" })
    public ToolStripCssResourceIOS toolbarCSS();

    @Source({ "com/smartgwt/mobile/client/theme/buttons.gwt.css", "com/smartgwt/mobile/client/theme/window.gwt.css", "com/smartgwt/mobile/client/internal/theme/ios/window.gwt.css", "window.gwt.css" })
    public WindowCssResourceIPhone windowCSS();
}
