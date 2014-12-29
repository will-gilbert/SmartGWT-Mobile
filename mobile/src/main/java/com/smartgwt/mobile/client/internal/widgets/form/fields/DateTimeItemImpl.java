package com.smartgwt.mobile.client.internal.widgets.form.fields;

import java.util.Date;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.widgets.Picker2;
import com.smartgwt.mobile.client.internal.widgets.events.ValuesSelectedEvent;
import com.smartgwt.mobile.client.widgets.form.fields.DateTimeItem;

@SGWTInternal
public abstract class DateTimeItemImpl {

    public abstract Element createElement(DateTimeItem self);

    public void create(DateTimeItem self) {}

    public void destroyImpl(DateTimeItem self) {}

    public abstract void setAllowEmptyValue(DateTimeItem self, boolean allowEmptyValue);

    public abstract void setElementReadOnly(DateTimeItem self, boolean readOnly);

    public Date getElementValue(DateTimeItem self) {
        throw new UnsupportedOperationException();
    }

    public void setElementValue(DateTimeItem self, Object displayValue, Object newValue) {
        self._superSetElementValue(displayValue, newValue);
    }

    public void setHint(DateTimeItem self, String hint) {
        self._superSetHint(hint);
    }

    public abstract InputElement getInputElement(DateTimeItem self);

    public abstract boolean isPickerEnabled(DateTimeItem self);

    public Picker2 createPicker(DateTimeItem self) {
        throw new UnsupportedOperationException();
    }

    public void onBrowserEvent(DateTimeItem self, Event event) {}

    public void onValuesSelected(DateTimeItem self, ValuesSelectedEvent event) {}
}
