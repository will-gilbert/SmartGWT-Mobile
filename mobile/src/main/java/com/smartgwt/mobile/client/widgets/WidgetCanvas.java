package com.smartgwt.mobile.client.widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.mobile.SGWTInternal;

public class WidgetCanvas extends Canvas {

    private Widget widget;

    public WidgetCanvas(Widget widget) {
        super(Document.get().createDivElement());
        getElement().setClassName("sc-widgetcanvas");

        this.widget = widget;
        add(widget, getElement());
    }

    @SGWTInternal
    public Widget _getWidget() {
        return widget;
    }
}
