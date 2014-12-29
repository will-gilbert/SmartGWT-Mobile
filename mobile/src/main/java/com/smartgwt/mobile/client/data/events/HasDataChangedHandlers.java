package com.smartgwt.mobile.client.data.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasDataChangedHandlers extends HasHandlers {

    public HandlerRegistration addDataChangedHandler(DataChangedHandler handler);
}
