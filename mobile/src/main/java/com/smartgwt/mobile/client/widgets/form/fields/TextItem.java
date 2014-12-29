package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.client.internal.types.AndroidWindowSoftInputMode;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;

/**
 * {@link FormItem} for managing a text field.
 * <p>
 * On iOS and Android, the soft keyboard shows a return key labeled "Go". When the user taps
 * the return key, as long as there is a {@link SubmitItem} among the form's items, then a
 * {@link com.smartgwt.mobile.client.widgets.form.events.SubmitValuesEvent} will be fired on
 * the form.
 */
public class TextItem extends FormItem {

    String inputType = "text";
    RegExp regExp;
    private String textBoxStyle = DynamicForm._CSS.textItemClass();

    public TextItem(String name) {
        super(name, Document.get().createTextInputElement());
        getElement().setClassName(textBoxStyle);
        init();
    }

    public TextItem(String name, String title) {
        this(name);
        super.setTitle(title);
    }

    public TextItem(String name, String title, String hint) {
        this(name, title);
        super.setHint(hint);
    }

    private void init() {
        if (_changeOnKeypress) {
            _sinkInputEvent();
        }
    }

    public void setBrowserInputType(String browserInputType) {
        assert !"file".equals(browserInputType) : "Changing the input type to \"file\" will not work in WebKit browsers: https://bugs.webkit.org/show_bug.cgi?id=44881";
        if (browserInputType == null) browserInputType = "text";
        else if ("phone".equals(browserInputType)) browserInputType = "tel";
        final Element elem = getElement();
        if (browserInputType == "digits") {
            // http://developer.apple.com/library/safari/#documentation/AppleApplications/Reference/SafariHTMLRef/Articles/InputTypes.html%23//apple_ref/doc/uid/TP40008055-SW3
            elem.setAttribute("type", "number");
            elem.setAttribute("pattern", "\\d*");
        } else {
            elem.setAttribute("type", browserInputType);
            elem.removeAttribute("pattern");
        }
    }

    @Override
    public void _setElementReadOnly(boolean readOnly) {
        getElement().<InputElement>cast().setReadOnly(_isPickerEnabled() || readOnly);
    }

    public final Boolean getShowHintInField() {
        return null;
    }

    @Override
    public final boolean _getShowHintInField() {
        return Canvas._booleanValue(getShowHintInField(), true);
    }

    public final String getTextBoxStyle() {
        return textBoxStyle;
    }

    public void setTextBoxStyle(String textBoxStyle) {
        this.textBoxStyle = textBoxStyle;
        getElement().setClassName(textBoxStyle);
    }

    public String getValueAsString() {
        Object value = super.getValue();
        if (value == null) return "";
        else return value.toString();
    }

    @Override
    public boolean _nativeElementBlur() {
        boolean returnVal = super._nativeElementBlur();

        _refreshDisplayValue();

        return returnVal;
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final String eventType = event.getType();
        if ("input".equals(eventType)) {
            if (AndroidWindowSoftInputMode.ADJUST_PAN == Canvas._getAndroidWindowSoftInputMode()) {
                Canvas._fireRequestScrollToEvent(_getInputElement());
            }

            _handleInput();
        }
    }

    @Override
    public boolean _shouldApplyStaticTypeFormat() {
        return false;
    }

    @Override
    public boolean validate() {
        return getValue() instanceof String;
    }
}
