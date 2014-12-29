package com.smartgwt.mobile.internal.gwt.dom.client.event;

import com.google.gwt.dom.client.NativeEvent;

public class AnimationEndEvent extends NativeEvent {

    protected AnimationEndEvent() {}

    public final native String getAnimationName() /*-{
        return this.animationName;
    }-*/;

    public final native double getElapsedTime() /*-{
        return this.elapsedTime;
    }-*/;
}
