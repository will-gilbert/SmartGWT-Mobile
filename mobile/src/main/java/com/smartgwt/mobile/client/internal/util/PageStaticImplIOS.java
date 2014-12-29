package com.smartgwt.mobile.client.internal.util;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.mobile.client.types.PageOrientation;

public class PageStaticImplIOS extends PageStaticImpl {

    private transient JavaScriptObject orientationchangeListenerFun;

    @Override
    public native PageOrientation getOrientation() /*-{
        // http://developer.apple.com/library/safari/#documentation/DataManagement/Reference/DOMWindowAdditionsReference/DOMWindowAdditions/DOMWindowAdditions.html#//apple_ref/javascript/instp/DOMWindow/orientation
        var isPortrait = $wnd.orientation == 0 ||
                         $wnd.orientation == 180;
        if (isPortrait) {
            return @com.smartgwt.mobile.client.types.PageOrientation::PORTRAIT;
        } else {
            return @com.smartgwt.mobile.client.types.PageOrientation::LANDSCAPE;
        }
    }-*/;

    @Override
    public native void setupOrientationChangeListener() /*-{
        // http://www.codecouch.com/2011/11/detecting-orientation-changes-in-mobile-safari-on-ios-and-other-supported-browsers/
        // http://developer.apple.com/library/safari/#documentation/AppleApplications/Reference/SafariWebContent/HandlingEvents/HandlingEvents.html#//apple_ref/doc/uid/TP40006511-SW16
        var orientationchangeListenerFun = this.@com.smartgwt.mobile.client.internal.util.PageStaticImplIOS::orientationchangeListenerFun;
        if (orientationchangeListenerFun == null) {
            this.@com.smartgwt.mobile.client.internal.util.PageStaticImplIOS::orientationchangeListenerFun = orientationchangeListenerFun = $entry(function () {
                var eventJ = @com.smartgwt.mobile.client.util.events.OrientationChangeEvent::new()();
                @com.smartgwt.mobile.client.util.Page::fireEvent(Lcom/google/gwt/event/shared/GwtEvent;)(eventJ);
            });
        }
        $wnd.removeEventListener("orientationchange", orientationchangeListenerFun, false);
        $wnd.addEventListener("orientationchange", orientationchangeListenerFun, false);
    }-*/;
}
