package com.smartgwt.mobile.client.widgets.form.events;

import com.google.gwt.event.shared.EventHandler;

public interface ItemChangedHandler extends EventHandler {

    /**
     * Handler fired when there is a changed() event fired on a FormItem within this form.
     */
    public void onItemChanged(ItemChangedEvent event);
}
