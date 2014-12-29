package com.smartgwt.mobile.client.internal.widgets.form.fields;

import java.util.Date;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.client.internal.EventUtil;
import com.smartgwt.mobile.client.util.DateUtil;
import com.smartgwt.mobile.client.util.LogicalDate;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.DateItem;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperDocument;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperInputElement;

class DateItemImplNative extends DateItemImpl {

    static {
        SuperInputElement.maybeInit();
    }

    private SpanElement placeholderElement;
    private InputElement inputElement;

    @Override
    public Element createElement(DateItem self) {
        final SuperDocument doc = Document.get().<SuperDocument>cast();
        final DivElement elem = doc.createDivElement();
        elem.setClassName(DynamicForm._CSS.dateItemClass());

        inputElement = doc.createDateInputElement();
        elem.appendChild(inputElement);

        // The native date input control does not support the `placeholder' attribute.
        // http://www.w3.org/TR/html5/forms.html#date-state-(type=date)
        // To support FormItem.hint, we place the hint text into a separate element that is
        // stacked behind the native date input. We show and hide the `placeholderElement' as
        // appropriate.
        placeholderElement = doc.createSpanElement();
        placeholderElement.setClassName(DynamicForm._CSS.placeholderTextClass());
        elem.appendChild(placeholderElement);

        return elem;
    }

    @Override
    public void create(DateItem self) {
        // Note: iOS 5.1 Mobile Safari does not fire 'input' or 'change' events on a native date input.
        // https://bugs.webkit.org/show_bug.cgi?id=107427
        //
        // In iOS 5 & 5.1, we hide the hint text after the date input receives focus
        // (because today's date is automatically selected). Then, on blur, we check whether
        // the hint should be re-shown. This allows us to *almost* replicate the behavior in
        // iOS 6+ exactly. The only difference is that tapping the Clear button causes the
        // hint text to reappear in iOS 6+ immediately, but in iOS 5 & 5.1, the hint text
        // reappears after receiving the 'blur' event.
        self._sinkInputEvent();

        DOM.sinkEvents(inputElement.<com.google.gwt.user.client.Element>cast(), Event.FOCUSEVENTS);
    }

    @Override
    public void setAllowEmptyValue(DateItem self, boolean allowEmptyValue) {
        final SuperInputElement elem = self.getElement().<SuperInputElement>cast();
        elem.setRequired(!allowEmptyValue);
    }

    @Override
    public void setElementReadOnly(DateItem self, boolean readOnly) {
        inputElement.setReadOnly(readOnly);
    }

    @Override
    public LogicalDate getElementValue(DateItem self) {
        return inputElement.<SuperInputElement>cast().getValueAsDate();
    }

    @Override
    public void setElementValue(DateItem self, Object displayValue, Object newValue) {
        if (newValue == null || newValue instanceof LogicalDate) {
            inputElement.<SuperInputElement>cast().setValue((LogicalDate)newValue);
        } else if (newValue instanceof Date) {
            inputElement.<SuperInputElement>cast().setValue(DateUtil.getLogicalDateOnly((Date)newValue));
        } else {
            assert newValue != null;
            inputElement.setValue(newValue.toString());
        }
        showOrHidePlaceholderElement();
    }

    @Override
    public void setHint(DateItem self, String hint) {
        placeholderElement.setInnerText(hint);
    }

    @Override
    public InputElement getInputElement(DateItem self) {
        return inputElement;
    }

    @Override
    public boolean isPickerEnabled(DateItem self) {
        return false;
    }

    @Override
    public void setStartDate(DateItem self, LogicalDate startDate) {
        self.getElement().<SuperInputElement>cast().setMinDate(startDate);
    }

    @Override
    public void setEndDate(DateItem self, LogicalDate endDate) {
        self.getElement().<SuperInputElement>cast().setMaxDate(endDate);
    }

    @Override
    public void onBrowserEvent(DateItem self, Event event) {
        super.onBrowserEvent(self, event);

        final Element targetElem = EventUtil.getTargetElem(event);
        if (targetElem == null) return;

        if (Canvas._isIOS5()) {
            switch (event.getTypeInt()) {
                case Event.ONMOUSEUP:
                    if (!inputElement.isOrHasChild(targetElem)) {
                        inputElement.focus();
                    }
                    break;
                case Event.ONFOCUS:
                    if (inputElement.isOrHasChild(targetElem)) {
                        hidePlaceholderElement();
                    }
                    break;
                case Event.ONBLUR:
                    if (inputElement.isOrHasChild(targetElem)) {
                        showOrHidePlaceholderElement();

                        self._handleInput();
                    }
                    break;
            }
        } else {
            final String eventType = event.getType();
            if ("input".equals(eventType)) {
                showOrHidePlaceholderElement();

                self._handleInput();
            }
        }
    }

    private void hidePlaceholderElement() {
        placeholderElement.getStyle().setDisplay(Style.Display.NONE);
    }
    private void showOrHidePlaceholderElement() {
        final String currentValue = inputElement.getValue();
        // Hide the placeholder text when there is a value; otherwise, make it visible again.
        if (currentValue != null && !currentValue.isEmpty()) {
            hidePlaceholderElement();
        } else {
            placeholderElement.getStyle().clearDisplay();
        }
    }
}
