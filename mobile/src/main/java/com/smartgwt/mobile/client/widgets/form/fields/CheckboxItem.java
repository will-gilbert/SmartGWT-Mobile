package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Event;

public class CheckboxItem extends FormItem {

    public CheckboxItem(String name) {
        super(name, Document.get().createCheckInputElement());

        sinkEvents(Event.ONCLICK | Event.ONTOUCHSTART);
	}

   public CheckboxItem(String name, String title) {
        this(name);
        setTitle(title);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        switch (event.getTypeInt()) {
            case Event.ONCLICK:
            case Event.ONTOUCHSTART:
                if (_isReadOnly()) event.preventDefault();
                break;
        }
    }

    @Override
    Boolean _getElementValue() {
        return getElement().<InputElement>cast().isChecked();
    }

    @Override
    public void _setElementValue(Object displayValue, Object newValue) {
        InputElement input = getElement().cast();
        boolean checked = false;
        if (displayValue instanceof Boolean) checked = ((Boolean)displayValue).booleanValue();
        input.setChecked(checked);
    }

    @Override
    public boolean validate() {
        return _value instanceof Boolean;
    }
}
