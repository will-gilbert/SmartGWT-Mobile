package com.smartgwt.mobile.client.internal.theme.ios;

import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.theme.PopupCssResource;
import com.smartgwt.mobile.client.internal.theme.ThemeResourcesBase;
import com.smartgwt.mobile.client.theme.BaseCssResource;
import com.smartgwt.mobile.client.theme.ButtonsCssResource;
import com.smartgwt.mobile.client.theme.DialogCssResource;
import com.smartgwt.mobile.client.theme.FormCssResource;
import com.smartgwt.mobile.client.theme.HeadingsCssResource;
import com.smartgwt.mobile.client.theme.ProgressbarCssResource;
import com.smartgwt.mobile.client.theme.ScrollablePanelCssResource;

@SGWTInternal
public interface ThemeResourcesIOS extends ThemeResourcesBase {

    @Source("DEBUG_BACKGROUND.png")
    public DataResource DEBUG_BACKGROUND_IMAGE_DATA();
    @Source("DEBUG_BACKGROUND.png")
    public ImageResource DEBUG_BACKGROUND_IMAGE();

    @Source("alertview_background.png")
    public DataResource alertViewBackgroundImageData();
    @Source("alertview_background.png")
    public ImageResource alertViewBackgroundImage();

    @Source("move_indicator.png")
    public DataResource moveIndicatorImageData();
    @Source("move_indicator.png")
    public ImageResource moveIndicatorImage();
    @Source("move_indicator~2.png")
    public DataResource retinaMoveIndicatorImageData();

    // The reason why the SwitchItem overlay images appear as a DataResource and ImageResource
    // is so that the referenced images may be used with @url lines as well as value() calls.
    // https://groups.google.com/d/topic/google-web-toolkit/3120BA8fXHY/discussion
    @Source("switchitem_leftoverlay.png")
    public DataResource switchItemLeftOverlayImageData();
    @Source("switchitem_leftoverlay.png")
    public ImageResource switchItemLeftOverlayImage();
    @Source("switchitem_middleoverlay.png")
    public DataResource switchItemMiddleOverlayImageData();
    @Source("switchitem_middleoverlay.png")
    public ImageResource switchItemMiddleOverlayImage();
    @Source("switchitem_rightoverlay.png")
    public DataResource switchItemRightOverlayImageData();
    @Source("switchitem_rightoverlay.png")
    public ImageResource switchItemRightOverlayImage();
    @Source("switchitem_leftoverlay~2.png")
    public DataResource retinaSwitchItemLeftOverlayImageData();
    @Source("switchitem_middleoverlay~2.png")
    public DataResource retinaSwitchItemMiddleOverlayImageData();
    @Source("switchitem_rightoverlay~2.png")
    public DataResource retinaSwitchItemRightOverlayImageData();

    @Source({ "com/smartgwt/mobile/client/theme/activityindicator.gwt.css", "activityindicator.gwt.css" })
    public ActivityIndicatorCssResourceIOS activityIndicatorCSS();

    @Source({ "com/smartgwt/mobile/client/internal/theme/alertview.gwt.css", "alertview.gwt.css" })
    public AlertViewCssResourceIOS alertviewCSS();

    @Source({ "com/smartgwt/mobile/client/theme/base.gwt.css", "base.gwt.css" })
    public BaseCssResource baseCSS();

    @Source({ "com/smartgwt/mobile/client/theme/buttons.gwt.css", "buttons.gwt.css" })
    public ButtonsCssResource buttonsCSS();

    @Source({ "com/smartgwt/mobile/client/theme/dialog.gwt.css", "dialog.gwt.css" })
    public DialogCssResource dialogCSS();

    @Source({ "com/smartgwt/mobile/client/theme/form.gwt.css", "form.gwt.css" })
    public FormCssResource formCSS();

    @Source({ "com/smartgwt/mobile/client/theme/headings.gwt.css", "headings.gwt.css" })
    public HeadingsCssResource headingsCSS();

    @Source({ "com/smartgwt/mobile/client/theme/navigationbar.gwt.css", "navigationbar.gwt.css" })
    public NavigationBarCssResourceIOS navigationBarCSS();

    @Override
    @Source({ "com/smartgwt/mobile/client/internal/theme/picker.gwt.css", "picker.gwt.css" })
    public PickerCssResourceIOS pickerCSS();

    @Source({ "com/smartgwt/mobile/client/internal/theme/popup.gwt.css", "popup.gwt.css" })
    public PopupCssResource popupCSS();

    @Override
    @Source({ "com/smartgwt/mobile/client/theme/progressbar.gwt.css", "progressbar.gwt.css" })
    public ProgressbarCssResource progressbarCSS();

    @Source({ "com/smartgwt/mobile/client/theme/scrollable.gwt.css", "scrollable.gwt.css" })
    public ScrollablePanelCssResource scrollableCSS();

    @Source({ "com/smartgwt/mobile/client/theme/slider.gwt.css", "slider.gwt.css" })
    public SliderCssResourceIOS sliderCSS();

    @Source({ "com/smartgwt/mobile/client/theme/switchitem.gwt.css", "switchitem.gwt.css" })
    public SwitchItemCssResourceIOS switchItemCSS();

    @Source({ "com/smartgwt/mobile/client/theme/toolbar.gwt.css", "com/smartgwt/mobile/client/theme/form.gwt.css", "com/smartgwt/mobile/client/theme/tableview.gwt.css", "tableview.gwt.css" })
    public TableViewCssResourceIOS tableViewCSS();

    @Source({ "com/smartgwt/mobile/client/theme/tabs.gwt.css", "com/smartgwt/mobile/client/internal/theme/tabs.gwt.css", "tabs.gwt.css" })
    public TabSetCssResourceIOS tabsCSS();

    @Source({ "com/smartgwt/mobile/client/theme/tabs.gwt.css", "com/smartgwt/mobile/client/internal/theme/tabs.gwt.css", "tabs_landscape.gwt.css" })
    public TabSetCssResourceIOS tabsCSSLandscape();

    @Source({ "com/smartgwt/mobile/client/theme/toolbar.gwt.css", "toolbar.gwt.css" })
    public ToolStripCssResourceIOS toolbarCSS();

    @Source({ "com/smartgwt/mobile/client/theme/window.gwt.css", "window.gwt.css" })
    public WindowCssResourceIOS windowCSS();
}
