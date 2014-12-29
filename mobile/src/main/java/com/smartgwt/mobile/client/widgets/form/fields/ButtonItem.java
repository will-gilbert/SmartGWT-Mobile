package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.client.internal.util.ElementUtil;
import com.smartgwt.mobile.client.theme.ButtonsCssResource;
import com.smartgwt.mobile.client.widgets.BaseButton;
import com.smartgwt.mobile.client.widgets.BaseButton.ButtonType;

public class ButtonItem extends FormItem {

    private static final ButtonsCssResource CSS = BaseButton._CSS;

    public ButtonItem(String name) {
        super(name, Document.get().createButtonInputElement());
        _setClassName(CSS.buttonClass(), false);
        _setClassName(ButtonType.BORDERED._getClassNames(), false);

        sinkEvents(Event.ONCLICK);
    }

    public ButtonItem(String name, String title) {
        this(name);
        super.setTitle(title);
    }

    @Override
    public boolean _isPickerEnabled() {
        return _getValueMap() != null;
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        if (isEnabled() && event.getTypeInt() == Event.ONCLICK) {
            final Element activeElement = ElementUtil.getActiveElement();
            if (activeElement != null) activeElement.blur();
        }
    }

    @Override
    protected void _onStart(Event event, Touch touch) {
        super._onStart(event, touch);
        getElement().addClassName(CSS.touchedButtonClass());
    }

    @Override
    protected void _onEnd(Event event) {
        getElement().removeClassName(CSS.touchedButtonClass());
        super._onEnd(event);
    }

    @Override
    public boolean validate() {
        return _value instanceof String;
    }
}
