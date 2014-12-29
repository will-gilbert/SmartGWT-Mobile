package com.smartgwt.mobile.client.widgets.form.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasFormSubmitFailedHandlers extends HasHandlers {

    public HandlerRegistration addFormSubmitFailedHandler(FormSubmitFailedHandler handler);
}
