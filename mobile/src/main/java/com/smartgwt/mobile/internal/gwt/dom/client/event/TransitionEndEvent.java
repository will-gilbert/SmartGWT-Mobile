package com.smartgwt.mobile.internal.gwt.dom.client.event;

import com.google.gwt.dom.client.NativeEvent;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class TransitionEndEvent extends NativeEvent {

    protected TransitionEndEvent() {}

    public final native String getPropertyName() /*-{
        return this.propertyName;
    }-*/;

    public final native float getElapsedTime() /*-{
        return this.elapsedTime;
    }-*/;

    public final native String getPseudoElement() /*-{
        return this.pseudoElement;
    }-*/;
}
