package com.smartgwt.mobile.client.widgets;

import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
abstract class ScrollablePanelImpl {

    abstract void init(ScrollablePanel self);

    void destroy(ScrollablePanel self) {}

    void onLoad(ScrollablePanel self) {}

    void onUnload(ScrollablePanel self) {}

    int getScrollTop(ScrollablePanel self) {
        return self.getElement().getScrollTop();
    }

    abstract void reset(ScrollablePanel self);

    abstract void onBrowserEvent(ScrollablePanel self, Event event);

    abstract void onContentChange(ScrollablePanel self);
}
