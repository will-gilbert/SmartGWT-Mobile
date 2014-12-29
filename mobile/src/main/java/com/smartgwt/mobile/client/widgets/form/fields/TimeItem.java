package com.smartgwt.mobile.client.widgets.form.fields;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.widgets.Picker2;
import com.smartgwt.mobile.client.internal.widgets.events.ValuesSelectedEvent;
import com.smartgwt.mobile.client.internal.widgets.form.fields.TimeItemImpl;
import com.smartgwt.mobile.client.types.TimeDisplayFormat;
import com.smartgwt.mobile.client.util.DateUtil;
import com.smartgwt.mobile.client.util.LogicalTime;
import com.smartgwt.mobile.client.util.TimeUtil;
import com.smartgwt.mobile.client.widgets.Canvas;

public class TimeItem extends FormItem {

    private static final TimeDisplayFormat DEFAULT_TIME_FORMATTER = TimeDisplayFormat.TOSHORT24HOURTIME;

    private transient final TimeItemImpl impl;

    private Boolean allowEmptyValue;

    @SGWTInternal
    protected TimeItem(String name, TimeItemImpl impl) {
        super(name);
        this.impl = impl;
        super.setElement(impl.createElement(this));
        impl.create(this);
    }

    public TimeItem(String name) {
        this(name, GWT.<TimeItemImpl>create(TimeItemImpl.class));
    }

    public TimeItem(String name, String title) {
        this(name);
        super.setTitle(title);
    }

    public TimeItem(String name, String title, String hint) {
        this(name, title);
        internalSetHint(hint);
    }

    @Override
    public void destroy() {
        impl.destroyImpl(this);
        super.destroy();
    }

    public final Boolean getAllowEmptyValue() {
        return allowEmptyValue;
    }

    @SGWTInternal
    public final boolean _getAllowEmptyValue() {
        return Canvas._booleanValue(getAllowEmptyValue(), false);
    }

    public void setAllowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
        impl.setAllowEmptyValue(this, _getAllowEmptyValue());
    }

    @SGWTInternal
    public void _setElementReadOnly(boolean readOnly) {
        impl.setElementReadOnly(this, readOnly);
    }

    @Override
    public Object _getElementValue() {
        return impl.getElementValue(this);
    }

    @SGWTInternal
    public void _superSetElementValue(Object displayValue, Object newValue) {
        super._setElementValue(displayValue, newValue);
    }

    @SGWTInternal
    public void _setElementValue(Object displayValue, Object newValue) {
        impl.setElementValue(this, displayValue, newValue);
    }

    @SGWTInternal
    public void _superSetHint(String hint) {
        super.setHint(hint);
    }

    private void internalSetHint(String hint) {
        impl.setHint(this, hint);
    }

    @Override
    public void setHint(String hint) {
        internalSetHint(hint);
    }

    @Override
    public InputElement _getInputElement() {
        return impl.getInputElement(this);
    }

    @Override
    public boolean _isPickerEnabled() {
        return impl.isPickerEnabled(this);
    }

    @Override
    public final boolean _getShowHintInField() {
        return true;
    }

    public void setTextBoxStyle(String textBoxStyle) {
        final Element elem = getElement().<Element>cast();
        elem.setClassName(textBoxStyle);
    }

    public LogicalTime getValueAsTime() {
        if (!(_value instanceof Date)) return null;
        return (_value instanceof LogicalTime ? ((LogicalTime)_value) : DateUtil.getLogicalTimeOnly((Date)_value));
    }

    public String getValueAsString() {
        return _mapValueToDisplay(_value);
    }

    @Override
    protected Picker2 _createPicker() {
        return impl.createPicker(this);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        impl.onBrowserEvent(this, event);
    }

    @Override
    public void _onValuesSelected(ValuesSelectedEvent event) {
        impl.onValuesSelected(this, event);
    }

    @SGWTInternal
    public String _mapValueToDisplay(Object value) {
        if (!(value instanceof Date)) return "";
        TimeDisplayFormat displayFormat = _getTimeFormatter();
        if (displayFormat == null) displayFormat = DEFAULT_TIME_FORMATTER;
        return TimeUtil._toShortTime((Date)value, displayFormat, null);
    }
}
