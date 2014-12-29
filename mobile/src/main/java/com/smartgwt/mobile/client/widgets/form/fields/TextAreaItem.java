package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;

public class TextAreaItem extends FormItem {

    private boolean autoFit = true;
    private String textBoxStyle = DynamicForm._CSS.textItemClass();
    private Timer updateHeightTimer = null;

    public TextAreaItem(String name) {
        super(name, Document.get().createTextAreaElement());
        final TextAreaElement elem = getElement().<TextAreaElement>cast();
        elem.setClassName(textBoxStyle);
        elem.setRows(1);
        init();
    }

    public TextAreaItem(String name, String title) {
        this(name);
        super.setTitle(title);
    }

    public TextAreaItem(String name, String title, String hint) {
        this(name, title);
        super.setHint(hint);
    }

    private void init() {
        if (_changeOnKeypress) {
            _sinkInputEvent();
        }
    }

    @Override
    public void destroy() {
        if (updateHeightTimer != null) {
            updateHeightTimer.cancel();
            updateHeightTimer = null;
        }
        super.destroy();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (autoFit) _scheduleUpdateHeight();
    }

    /**
     * Whether the HTML &lt;textarea&gt; element automatically expands to fit (auto fit) its
     * contents.
     * 
     * @return <code>true</code> if auto fit is enabled; <code>false</code> otherwise. Default value: <code>true</code>
     */
    public final boolean getAutoFit() {
        return autoFit;
    }

    public void setAutoFit(Boolean autoFit) {
        final boolean newAutoFit = autoFit == null ? true : autoFit.booleanValue();
        if (this.autoFit == newAutoFit) return;

        this.autoFit = newAutoFit;
        if (newAutoFit) {
            getElement().<TextAreaElement>cast().setRows(1);
            if (isAttached()) _scheduleUpdateHeight();
        } else {
            getElement().<TextAreaElement>cast().setRows(4);
            // Clear an explicitly-set height.
            getElement().getStyle().clearHeight();

            // Cancel any scheduled call to updateHeight().
            if (updateHeightTimer != null) {
                updateHeightTimer.cancel();
                updateHeightTimer = null;
            }
        }
    }

    @Override
    public void _setElementReadOnly(boolean readOnly) {
        getElement().<TextAreaElement>cast().setReadOnly(_isPickerEnabled() || readOnly);
    }

    @Override
    String _getElementValue() {
        return getElement().<TextAreaElement>cast().getValue();
    }

    @Override
    public void _setElementValue(Object displayValue, Object newValue) {
        final TextAreaElement textarea = getElement().cast();
        textarea.setValue(displayValue == null ? "" : displayValue.toString());
        if (autoFit) _scheduleUpdateHeight();
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
        Object val = super.getValue();
        if (val == null) return "";
        return val.toString();
    }

    @Override
    void _handleChanged(Object value) {
        if (autoFit) _scheduleUpdateHeight();
        super._handleChanged(value);
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
            _handleInput();
        }
    }

    @SGWTInternal
    protected void _scheduleUpdateHeight() {
        assert autoFit;
        if (updateHeightTimer != null) return;
        updateHeightTimer = new Timer() {
            @Override
            public void run() {
                if (updateHeightTimer == this) {
                    updateHeightTimer = null;
                    updateHeight();
                }
            }
        };
        updateHeightTimer.schedule(1);
    }

    @Override
    public boolean _shouldApplyStaticTypeFormat() {
        return false;
    }

    private void updateHeight() {
        assert autoFit;
        final TextAreaElement textAreaElem = getElement().cast();
        // Note: Some auto-expanding <textarea> solutions temporarily set the height of the
        // <textarea> to 0px. This causes an issue on iOS 5.0 (and possibly other iOS versions
        // as well) where, after tapping the "Previous" button on the virtual keyboard to go to a
        // previous text input, the "Next" button is disabled. Using temporary heights of "auto"
        // or any non-zero length fixes the problem (the "Next" button is not disabled and
        // tapping it takes the keyboard focus back to the <textarea>).
        textAreaElem.getStyle().setProperty("height", "auto");
        textAreaElem.getStyle().setHeight(textAreaElem.getScrollHeight() + 20, Style.Unit.PX);
    }

    @Override
    public boolean validate() {
        return _value == null || _value instanceof String;
    }
}
