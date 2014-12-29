package com.smartgwt.mobile.client.widgets.tableview.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasRecordNavigationClickHandlers extends HasHandlers {

    public HandlerRegistration addRecordNavigationClickHandler(RecordNavigationClickHandler handler);
}
