package com.smartgwt.mobile.client.widgets.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasButtonClickHandlers extends HasHandlers {

    public HandlerRegistration addButtonClickHandler(ButtonClickHandler handler);
}
