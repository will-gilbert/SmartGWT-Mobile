package com.smartgwt.mobile.client.widgets.form.fields;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.widgets.Picker2;
import com.smartgwt.mobile.client.internal.widgets.events.ValuesSelectedEvent;
import com.smartgwt.mobile.client.internal.widgets.form.fields.DateTimeItemImpl;
import com.smartgwt.mobile.client.types.DateDisplayFormat;
import com.smartgwt.mobile.client.types.DateItemSelectorFormat;
import com.smartgwt.mobile.client.util.DateDisplayFormatter;
import com.smartgwt.mobile.client.util.DateUtil;
import com.smartgwt.mobile.client.util.EnumUtil;
import com.smartgwt.mobile.client.util.LogicalTime;
import com.smartgwt.mobile.client.widgets.Canvas;

public class DateTimeItem extends FormItem {

    private transient DateDisplayFormatter formatter;
    private transient final DateTimeItemImpl impl;

    private Boolean allowEmptyValue;

    @SGWTInternal
    protected DateTimeItem(String name, DateTimeItemImpl impl) {
        super(name);
        this.impl = impl;
        super.setElement(impl.createElement(this));
        impl.create(this);
    }

    public DateTimeItem(String name) {
        this(name, GWT.<DateTimeItemImpl>create(DateTimeItemImpl.class));
    }

    public DateTimeItem(String name, String title) {
        this(name);
        super.setTitle(title);
    }

    public DateTimeItem(String name, String title, String hint) {
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

    public final DateItemSelectorFormat _getSelectorFormat() {
        final String inputFormat = DateUtil._mapDisplayFormatToInputFormat(_getDateFormatter());
        assert inputFormat != null;
        final DateItemSelectorFormat ret = EnumUtil.getEnum(DateItemSelectorFormat.values(), inputFormat);
        assert ret != null;
        return ret;
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

    public Date getValueAsDate() {
        if (!(_value instanceof Date)) return null;
        return (Date)_value;
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
        assert formatter != null;
        return formatter.format((Date)value);
    }

    @Override
    public void _updateDateFormatter() {
        updateFormatter();
    }

    private void updateFormatter() {
        final DateDisplayFormat displayFormat = _getDatetimeFormatter();
        if (displayFormat != null) formatter = DateUtil._getDateDisplayFormatter(displayFormat);
        else formatter = DateUtil._getShortDatetimeDisplayFormatter();
        assert formatter != null;
    }
}
