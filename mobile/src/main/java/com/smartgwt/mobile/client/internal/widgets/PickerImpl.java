package com.smartgwt.mobile.client.internal.widgets;

import java.util.List;

import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.client.internal.test.AutoTestLocatable;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;

abstract class PickerImpl {

    abstract void init(Picker2 self);

    AutoTestLocatable getChildFromLocatorSubstring(Picker2 self, String substring, int index, List<String> locatorArray, GetAttributeConfiguration configuration) {
        return self.super_getChildFromLocatorSubstring(substring, index, locatorArray, configuration);
    }

    Object getInnerAttributeFromSplitLocator(Picker2 self, List<String> locatorArray, GetAttributeConfiguration configuration) {
        return self.super_getInnerAttributeFromSplitLocator(locatorArray, configuration);
    }

    abstract void setDials(Picker2 self, PickerDial[] oldDials, PickerDial... dials);

    void setShowClearButton(Picker2 self, Boolean showClearButton) {}

    abstract void destroyPopup(Picker2 self);

    void doHide(Picker2 self) {}

    void doShow(Picker2 self) {}

    void onBrowserEvent(Picker2 self, Event event) {}
}
