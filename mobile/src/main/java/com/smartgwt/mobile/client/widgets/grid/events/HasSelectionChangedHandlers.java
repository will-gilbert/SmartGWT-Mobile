package com.smartgwt.mobile.client.widgets.grid.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasSelectionChangedHandlers extends HasHandlers {

    public HandlerRegistration addSelectionChangedHandler(SelectionChangedHandler handler);
}
