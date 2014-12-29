package com.smartgwt.mobile.client.widgets.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasValuesSelectedHandlers extends HasHandlers {
	
    HandlerRegistration addValuesSelectedHandler(ValuesSelectedHandler handler);

}
