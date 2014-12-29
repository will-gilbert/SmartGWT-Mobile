package com.smartgwt.mobile.client.internal.widgets.menu.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public interface HasBeforeMenuHiddenHandlers extends HasHandlers {

    public HandlerRegistration _addBeforeMenuHiddenHandler(BeforeMenuHiddenHandler handler);
}
