package com.smartgwt.mobile.client.internal.widgets;

import java.util.List;

import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;

abstract class PickerDialImpl {

    abstract void init(PickerDial self);

    void destroyImpl(PickerDial self) {}

    Object _getAttributeFromSplitLocator(PickerDial self, List<String> locatorArray, GetAttributeConfiguration configuration) {
        return self.super_getAttributeFromSplitLocator(locatorArray, configuration);
    }

    abstract void onBrowserEvent(PickerDial self, Event event);

    abstract void onRecordSelected(PickerDial self, Record selectedRecord);

    abstract void refreshRows(PickerDial self);
}
