package com.smartgwt.mobile.client.internal.widgets.form.fields;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.widgets.Picker2;
import com.smartgwt.mobile.client.internal.widgets.events.ValuesSelectedEvent;
import com.smartgwt.mobile.client.util.LogicalTime;
import com.smartgwt.mobile.client.widgets.form.fields.TimeItem;

@SGWTInternal
public abstract class TimeItemImpl {

    public abstract Element createElement(TimeItem self);

    public void create(TimeItem self) {}

    public void destroyImpl(TimeItem self) {}

    public abstract void setAllowEmptyValue(TimeItem self, boolean allowEmptyValue);

    public abstract void setElementReadOnly(TimeItem self, boolean readOnly);

    public LogicalTime getElementValue(TimeItem self) {
        throw new UnsupportedOperationException();
    }

    public void setElementValue(TimeItem self, Object displayValue, Object newValue) {
        self._superSetElementValue(displayValue, newValue);
    }

    public void setHint(TimeItem self, String hint) {
        self._superSetHint(hint);
    }

    public abstract InputElement getInputElement(TimeItem self);

    public abstract boolean isPickerEnabled(TimeItem self);

    public Picker2 createPicker(TimeItem self) {
        throw new UnsupportedOperationException();
    }

    public void onBrowserEvent(TimeItem self, Event event) {}

    public void onValuesSelected(TimeItem self, ValuesSelectedEvent event) {}
}
