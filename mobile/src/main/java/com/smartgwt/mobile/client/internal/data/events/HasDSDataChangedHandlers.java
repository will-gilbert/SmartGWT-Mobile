package com.smartgwt.mobile.client.internal.data.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public interface HasDSDataChangedHandlers extends HasHandlers {

    @SGWTInternal
    public HandlerRegistration _addDSDataChangedHandler(DSDataChangedHandler handler);
}
