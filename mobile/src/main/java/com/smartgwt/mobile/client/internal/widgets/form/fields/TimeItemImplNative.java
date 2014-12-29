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
import com.smartgwt.mobile.client.util.LogicalTime;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.TimeItem;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperDocument;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperInputElement;

// Note: This implementation closely follows the implementation of `DateItemImplNative'.
// See `DateItemImplNative.java' for comments explaining the approach.
class TimeItemImplNative extends TimeItemImpl {

    static {
        SuperInputElement.maybeInit();
    }

    private SpanElement placeholderElement;
    private InputElement inputElement;

    @Override
    public Element createElement(TimeItem self) {
        final SuperDocument doc = Document.get().<SuperDocument>cast();
        final DivElement elem = doc.createDivElement();
        elem.setClassName(DynamicForm._CSS.timeItemClass());

        inputElement = doc.createTimeInputElement();
        elem.appendChild(inputElement);

        placeholderElement = doc.createSpanElement();
        placeholderElement.setClassName(DynamicForm._CSS.placeholderTextClass());
        elem.appendChild(placeholderElement);

        return elem;
    }

    @Override
    public void create(TimeItem self) {
        self._sinkInputEvent();

        DOM.sinkEvents(inputElement.<com.google.gwt.user.client.Element>cast(), Event.FOCUSEVENTS);
    }

    @Override
    public void setAllowEmptyValue(TimeItem self, boolean allowEmptyValue) {
        final SuperInputElement elem = self.getElement().<SuperInputElement>cast();
        elem.setRequired(!allowEmptyValue);
    }

    @Override
    public void setElementReadOnly(TimeItem self, boolean readOnly) {
        inputElement.setReadOnly(readOnly);
    }

    @Override
    public LogicalTime getElementValue(TimeItem self) {
        return inputElement.<SuperInputElement>cast().getValueAsTime();
    }

    @Override
    public void setElementValue(TimeItem self, Object displayValue, Object newValue) {
        if (newValue == null || newValue instanceof LogicalTime) {
            inputElement.<SuperInputElement>cast().setValue((LogicalTime)newValue);
        } else if (newValue instanceof Date) {
            inputElement.<SuperInputElement>cast().setValue(DateUtil.getLogicalTimeOnly((Date)newValue));
        } else {
            assert newValue != null;
            inputElement.setValue(newValue.toString());
        }
        showOrHidePlaceholderElement();
    }

    @Override
    public void setHint(TimeItem self, String hint) {
        placeholderElement.setInnerText(hint);
    }

    @Override
    public InputElement getInputElement(TimeItem self) {
        return inputElement;
    }

    @Override
    public boolean isPickerEnabled(TimeItem self) {
        return false;
    }

    @Override
    public void onBrowserEvent(TimeItem self, Event event) {
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
        if (currentValue != null && !currentValue.isEmpty()) {
            hidePlaceholderElement();
        } else {
            placeholderElement.getStyle().clearDisplay();
        }
    }
}
