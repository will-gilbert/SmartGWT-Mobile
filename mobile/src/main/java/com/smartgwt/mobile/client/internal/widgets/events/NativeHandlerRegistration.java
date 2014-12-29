package com.smartgwt.mobile.client.internal.widgets.events;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class NativeHandlerRegistration implements HandlerRegistration {

    private Element element;
    private String type;
    private JavaScriptObject listener;
    private boolean useCapture;

    public NativeHandlerRegistration(Element element, String type, JavaScriptObject listener, boolean useCapture) {
        this.element = element;
        this.type = type;
        this.listener = listener;
        this.useCapture = useCapture;
    }

    @Override
    public native void removeHandler() /*-{
        var element = this.@com.smartgwt.mobile.client.internal.widgets.events.NativeHandlerRegistration::element;
        if (element) {
            var type = this.@com.smartgwt.mobile.client.internal.widgets.events.NativeHandlerRegistration::type;
            var listener = this.@com.smartgwt.mobile.client.internal.widgets.events.NativeHandlerRegistration::listener;
            var useCapture = this.@com.smartgwt.mobile.client.internal.widgets.events.NativeHandlerRegistration::useCapture;
            element.removeEventListener(type, listener, useCapture);
            delete this.@com.smartgwt.mobile.client.internal.widgets.events.NativeHandlerRegistration::listener;
            delete this.@com.smartgwt.mobile.client.internal.widgets.events.NativeHandlerRegistration::element;
        }
    }-*/;
}