package com.smartgwt.mobile.internal.gwt.dom.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public interface TransitionEndHandler extends EventHandler {

    public void onTransitionEnd(TransitionEndEvent event);
}
