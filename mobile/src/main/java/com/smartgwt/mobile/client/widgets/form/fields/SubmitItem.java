package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.client.internal.util.ElementUtil;
import com.smartgwt.mobile.client.theme.ButtonsCssResource;
import com.smartgwt.mobile.client.widgets.BaseButton;
import com.smartgwt.mobile.client.widgets.BaseButton.ButtonType;

/**
 * {@link FormItem} for a submit button.
 * <p>
 * On iOS and Android, to receive notification when the user taps the return key on the soft
 * keyboard ("Go"/"Search" button), there must be at least one SubmitItem among the form's
 * items.
 */
public class SubmitItem extends FormItem {

    private static final ButtonsCssResource CSS = BaseButton._CSS;

    public SubmitItem(String name) {
        this(name, "Submit");
    }

    public SubmitItem(String name, String title) {
        super(name, Document.get().createSubmitInputElement());
        _setClassName(CSS.buttonClass(), false);
        _setClassName(ButtonType.BORDERED._getClassNames(), false);
        super.setShowTitle(false);
        super.setTitle(title);
        super.showValue(title);

        sinkEvents(Event.ONCLICK);
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        showValue(title);
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
        return true;
    }
}
