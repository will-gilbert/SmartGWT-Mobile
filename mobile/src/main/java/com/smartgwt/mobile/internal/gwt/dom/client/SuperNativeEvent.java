package com.smartgwt.mobile.internal.gwt.dom.client;

import com.google.gwt.dom.client.NativeEvent;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class SuperNativeEvent extends NativeEvent {

    protected SuperNativeEvent() {}

    public final native int getPageX() /*-{
        return this.pageX;
    }-*/;

    public final native int getPageY() /*-{
        return this.pageY;
    }-*/;

    public final native double getTimeStamp() /*-{
        return ("timeStamp" in this ? this.timeStamp : 0);
    }-*/;
}
