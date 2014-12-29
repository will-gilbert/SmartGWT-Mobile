package com.smartgwt.mobile.internal.gwt.dom.client;

import com.google.gwt.dom.client.Document;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class SuperDocument extends Document {

    protected SuperDocument() {}

    public final native SuperElement getActiveElement() /*-{
        return this.activeElement;
    }-*/;

    public final native SuperElement getElementFromPoint(double x, double y) /*-{
        return this.elementFromPoint(x, y);
    }-*/;

    private final native SuperInputElement createInputElement(String type) /*-{
        var ret = this.createElement("INPUT");
        ret.type = type;
        return ret;
    }-*/;

    public final SuperInputElement createDateInputElement() {
        return createInputElement("date");
    }

    public final SuperInputElement createDatetimeInputElement() {
        return createInputElement("datetime");
    }

    public final SuperInputElement createLocalDatetimeInputElement() {
        return createInputElement("datetime-local");
    }

    public final SuperInputElement createTimeInputElement() {
        return createInputElement("time");
    }
}
