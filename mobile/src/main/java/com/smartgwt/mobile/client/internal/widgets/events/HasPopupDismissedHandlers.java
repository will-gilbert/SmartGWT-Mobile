package com.smartgwt.mobile.client.internal.widgets.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public interface HasPopupDismissedHandlers extends HasHandlers {

    @SGWTInternal
    public HandlerRegistration _addPopupDismissedHandler(PopupDismissedHandler handler);
}
