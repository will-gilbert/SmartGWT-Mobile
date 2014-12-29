package com.smartgwt.mobile.client.widgets.tableview.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasDetailsSelectedHandlers extends HasHandlers {

    public HandlerRegistration addDetailsSelectedHandler(DetailsSelectedHandler handler);
}
