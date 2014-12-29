package com.smartgwt.mobile.client.internal.widgets.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public interface HasPickerHiddenHandlers extends HasHandlers {

    @SGWTInternal
    public HandlerRegistration _addPickerHiddenHandler(PickerHiddenHandler handler);
}
