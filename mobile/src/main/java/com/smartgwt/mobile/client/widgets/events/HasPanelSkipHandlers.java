package com.smartgwt.mobile.client.widgets.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasPanelSkipHandlers extends HasHandlers {

    public HandlerRegistration addPanelSkipHandler(PanelSkipHandler handler);
}
