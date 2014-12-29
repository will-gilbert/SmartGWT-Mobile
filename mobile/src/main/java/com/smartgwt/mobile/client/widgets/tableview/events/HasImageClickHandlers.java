package com.smartgwt.mobile.client.widgets.tableview.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasImageClickHandlers extends HasHandlers {

    public HandlerRegistration addImageClickHandler(ImageClickHandler handler);
}
