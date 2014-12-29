package com.smartgwt.mobile.client.widgets.form.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasItemChangedHandlers extends HasHandlers {

    public HandlerRegistration addItemChangedHandler(ItemChangedHandler handler);
}
