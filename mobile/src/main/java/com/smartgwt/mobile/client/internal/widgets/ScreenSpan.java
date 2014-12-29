package com.smartgwt.mobile.client.internal.widgets;

import com.google.gwt.dom.client.Document;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.Canvas;

@SGWTInternal
public class ScreenSpan extends Canvas {

    public ScreenSpan() {
        super(Document.get().createDivElement());
    }
}
