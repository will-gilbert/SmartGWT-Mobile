package com.smartgwt.mobile.client.widgets.menu.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasClickHandlers extends HasHandlers {

    public HandlerRegistration addClickHandler(ClickHandler handler);
}
