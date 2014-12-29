package com.smartgwt.mobile.client.widgets.menu;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.Canvas;

@SGWTInternal
abstract class MenuImpl {

    Element createElement() {
        final Element ret = Document.get().createDivElement();
        ret.setClassName(Menu._CSS.menuClass());
        return ret;
    }

    abstract void init(Menu self);

    abstract void destroyImpl(Menu self);

    void onBrowserEvent(Menu self, Event event) {}

    abstract void setItems(Menu self, List<? extends MenuItem> items);

    abstract void showAt(Menu self, Canvas target, int x, int y, int minX, int maxX, int minY, int maxY);

    abstract void hide(Menu self);
}
