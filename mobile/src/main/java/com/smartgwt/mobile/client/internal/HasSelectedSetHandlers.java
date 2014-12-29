package com.smartgwt.mobile.client.internal;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.smartgwt.mobile.SGWTInternal;

public interface HasSelectedSetHandlers extends HasHandlers {

    @SGWTInternal
    public HandlerRegistration _addSelectedSetHandler(SelectedSetHandler handler);
}
