package com.smartgwt.mobile.client.data.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasDataArrivedHandlers extends HasHandlers {

    public HandlerRegistration addDataArrivedHandler(DataArrivedHandler handler);
}
