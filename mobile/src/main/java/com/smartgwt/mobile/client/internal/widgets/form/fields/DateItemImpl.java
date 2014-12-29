package com.smartgwt.mobile.client.internal.widgets.form.fields;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.widgets.Picker2;
import com.smartgwt.mobile.client.internal.widgets.events.ValuesSelectedEvent;
import com.smartgwt.mobile.client.types.DateItemSelectorFormat;
import com.smartgwt.mobile.client.util.LogicalDate;
import com.smartgwt.mobile.client.widgets.form.fields.DateItem;

@SGWTInternal
public abstract class DateItemImpl {

    public abstract Element createElement(DateItem self);

    public void create(DateItem self) {}

    public void destroyImpl(DateItem self) {}

    public abstract void setAllowEmptyValue(DateItem self, boolean allowEmptyValue);

    public abstract void setElementReadOnly(DateItem self, boolean readOnly);

    public LogicalDate getElementValue(DateItem self) {
        throw new UnsupportedOperationException();
    }

    public void setElementValue(DateItem self, Object displayValue, Object newValue) {
        self._superSetElementValue(displayValue, newValue);
    }

    public void setHint(DateItem self, String hint) {
        self._superSetHint(hint);
    }

    public abstract InputElement getInputElement(DateItem self);

    public abstract boolean isPickerEnabled(DateItem self);

    public abstract void setStartDate(DateItem self, LogicalDate startDate);

    public abstract void setEndDate(DateItem self, LogicalDate endDate);

    public void setSelectorFormat(DateItem self, DateItemSelectorFormat selectorFormat) {}

    public Picker2 createPicker(DateItem self) {
        throw new UnsupportedOperationException();
    }

    public void onBrowserEvent(DateItem self, Event event) {}

    public void onValuesSelected(DateItem self, ValuesSelectedEvent event) {}
}
