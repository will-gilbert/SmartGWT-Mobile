package com.smartgwt.mobile.client.widgets.form.fields.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasChangedHandlers extends HasHandlers {
    public HandlerRegistration addChangedHandler(ChangedHandler handler);
}
