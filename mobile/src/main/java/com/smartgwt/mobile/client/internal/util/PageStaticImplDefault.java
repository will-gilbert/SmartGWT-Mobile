package com.smartgwt.mobile.client.internal.util;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.mobile.client.types.PageOrientation;
import com.smartgwt.mobile.client.util.Page;

public class PageStaticImplDefault extends PageStaticImpl {

    private transient PageOrientation currentOrientation;
    private transient int currentWidth;
    private transient JavaScriptObject resizeListenerFun;

    public PageStaticImplDefault() {
        currentWidth = -1;
        currentOrientation = getOrientation();
        currentWidth = Page.getWidth();
    }

    @Override
    public final native PageOrientation getOrientation() /*-{
        var width = @com.smartgwt.mobile.client.util.Page::getWidth()();
        if (width == this.@com.smartgwt.mobile.client.internal.util.PageStaticImplDefault::currentWidth) {
            return this.@com.smartgwt.mobile.client.internal.util.PageStaticImplDefault::currentOrientation;
        }

        var isPortrait;
        if ("matchMedia" in $wnd) {
            isPortrait = $wnd.matchMedia("(orientation: portrait)").matches;
        } else {
            isPortrait = @com.smartgwt.mobile.client.util.Page::getHeight() >= width;
        }
        if (isPortrait) {
            return @com.smartgwt.mobile.client.types.PageOrientation::PORTRAIT;
        } else {
            return @com.smartgwt.mobile.client.types.PageOrientation::LANDSCAPE;
        }
    }-*/;

    @Override
    public native void setupOrientationChangeListener() /*-{
        var resizeListenerFun = this.@com.smartgwt.mobile.client.internal.util.PageStaticImplDefault::resizeListenerFun;
        if (resizeListenerFun == null) {
            var self = this;
            this.@com.smartgwt.mobile.client.internal.util.PageStaticImplDefault::resizeListenerFun = resizeListenerFun = $entry(function () {
                var width = @com.smartgwt.mobile.client.util.Page::getWidth()();
                if (width == self.@com.smartgwt.mobile.client.internal.util.PageStaticImplDefault::currentWidth) {
                    return;
                }

                var orientation = self.@com.smartgwt.mobile.client.internal.util.PageStaticImplDefault::getOrientation()();
                if (orientation != self.@com.smartgwt.mobile.client.internal.util.PageStaticImplDefault::currentOrientation) {
                    self.@com.smartgwt.mobile.client.internal.util.PageStaticImplDefault::currentWidth = width;
                    self.@com.smartgwt.mobile.client.internal.util.PageStaticImplDefault::currentOrientation = orientation;
                    var eventJ = @com.smartgwt.mobile.client.util.events.OrientationChangeEvent::new()();
                    @com.smartgwt.mobile.client.util.Page::fireEvent(Lcom/google/gwt/event/shared/GwtEvent;)(eventJ);
                }
            });
        }
        $wnd.removeEventListener("resize", resizeListenerFun, false);
        $wnd.addEventListener("resize", resizeListenerFun, false);
    }-*/;
}
