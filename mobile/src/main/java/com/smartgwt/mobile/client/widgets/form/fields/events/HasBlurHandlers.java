package com.smartgwt.mobile.client.widgets.form.fields.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBlurHandlers extends HasHandlers {
    public HandlerRegistration addBlurHandler(BlurHandler handler);
}
