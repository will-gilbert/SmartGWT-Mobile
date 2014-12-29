package com.smartgwt.mobile.client.widgets.form.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasSubmitValuesHandlers extends HasHandlers {

    public HandlerRegistration addSubmitValuesHandler(SubmitValuesHandler handler);
}
