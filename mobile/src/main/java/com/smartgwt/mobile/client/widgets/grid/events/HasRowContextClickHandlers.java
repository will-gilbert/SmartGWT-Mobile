package com.smartgwt.mobile.client.widgets.grid.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasRowContextClickHandlers extends HasHandlers {

    public HandlerRegistration addRowContextClickHandler(RowContextClickHandler handler);
}
