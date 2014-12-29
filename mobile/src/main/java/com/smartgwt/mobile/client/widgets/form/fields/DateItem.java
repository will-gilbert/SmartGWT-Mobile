package com.smartgwt.mobile.client.widgets.form.fields;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.widgets.Picker2;
import com.smartgwt.mobile.client.internal.widgets.events.ValuesSelectedEvent;
import com.smartgwt.mobile.client.internal.widgets.form.fields.DateItemImpl;
import com.smartgwt.mobile.client.types.DateDisplayFormat;
import com.smartgwt.mobile.client.types.DateItemSelectorFormat;
import com.smartgwt.mobile.client.util.DateDisplayFormatter;
import com.smartgwt.mobile.client.util.DateUtil;
import com.smartgwt.mobile.client.util.EnumUtil;
import com.smartgwt.mobile.client.util.LogicalDate;
import com.smartgwt.mobile.client.widgets.Canvas;

public class DateItem extends FormItem {

    @SGWTInternal
    public static boolean _isValidDateRange(LogicalDate startDate, LogicalDate endDate) {
        if (startDate == null || endDate == null) return true;
        return (startDate.compareTo(endDate) <= 0);
    }

    private transient DateDisplayFormatter formatter;
    private transient final DateItemImpl impl;

    private Boolean allowEmptyValue;
    private LogicalDate endDate;
    private DateItemSelectorFormat selectorFormat;
    private LogicalDate startDate;

    @SGWTInternal
    protected DateItem(String name, DateItemImpl impl) {
        super(name);
        this.impl = impl;
        super.setElement(impl.createElement(this));
        updateFormatter();
        impl.create(this);
    }

    public DateItem(String name) {
        this(name, GWT.<DateItemImpl>create(DateItemImpl.class));
    }

    public DateItem(String name, String title) {
        this(name);
        super.setTitle(title);
    }

    public DateItem(String name, String title, String hint) {
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

    /**
     * Sets the format in which dates are displayed.
     * <p>
     * <b>NOTE:</b> This setting does not have an effect if this <code>DateItem</code> is
     * using a native HTML5 date input.
     */
    @Override
    public void setDateFormatter(DateDisplayFormat dateFormatter) {
        super.setDateFormatter(dateFormatter);
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

    public final LogicalDate getEndDate() {
        return endDate;
    }

    /**
     * @deprecated Use {@link #setEndDate(LogicalDate)} instead.
     */
    @Deprecated
    public final void setEndDate(Date endDate) {
        setEndDate(DateUtil.getLogicalDateOnly(endDate));
    }

    public void setEndDate(LogicalDate endDate) {
        if (!_isValidDateRange(startDate, endDate)) throw new IllegalArgumentException("endDate");
        this.endDate = endDate;
        impl.setEndDate(this, endDate);
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

    public final LogicalDate getStartDate() {
        return startDate;
    }

    /**
     * @deprecated Use {@link #setStartDate(LogicalDate)} instead.
     */
    @Deprecated
    public final void setStartDate(Date startDate) {
        setStartDate(DateUtil.getLogicalDateOnly(startDate));
    }

    public void setStartDate(LogicalDate startDate) {
        if (!_isValidDateRange(startDate, endDate)) throw new IllegalArgumentException("startDate");
        this.startDate = startDate;
        impl.setStartDate(this, startDate);
    }

    /**
     * The format used for the date picker, which determines the display order of the date
     * components (month, date, and year) on the picker.
     * <p>
     * <b>NOTE:</b> This setting does not have an effect if this <code>DateItem</code> is
     * using a native HTML5 date input.
     */
    public final DateItemSelectorFormat getSelectorFormat() {
        return selectorFormat;
    }

    @SGWTInternal
    public final DateItemSelectorFormat _getSelectorFormat() {
        DateItemSelectorFormat ret = getSelectorFormat();
        if (ret == null) {
            final String inputFormat = DateUtil._mapDisplayFormatToInputFormat(_getDateFormatter());
            ret = EnumUtil.getEnum(DateItemSelectorFormat.values(), inputFormat);
        }
        return ret;
    }

    /**
     * Sets the display order of the date components (month, date, and year) on the picker.
     * <p>
     * <b>NOTE:</b> This setting does not have an effect if this <code>DateItem</code> is
     * using a native HTML5 date input.
     */
    public void setSelectorFormat(DateItemSelectorFormat selectorFormat) {
        this.selectorFormat = selectorFormat;
        updateFormatter();
        impl.setSelectorFormat(this, selectorFormat);
    }

    public void setTextBoxStyle(String textBoxStyle) {
        final Element elem = getElement().<Element>cast();
        elem.setClassName(textBoxStyle);
    }

    public LogicalDate getValueAsDate() {
        if (!(_value instanceof Date)) return null;
        return (_value instanceof LogicalDate ? ((LogicalDate)_value) : DateUtil.getLogicalDateOnly((Date)_value));
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
        assert formatter != null;
        return formatter.format((Date)value);
    }

    @Override
    public void _updateDateFormatter() {
        updateFormatter();
    }

    private void updateFormatter() {
        final DateDisplayFormat displayFormat = _getDateFormatter();
        if (displayFormat != null) formatter = DateUtil._getDateDisplayFormatter(displayFormat);
        else formatter = DateUtil._getShortDateDisplayFormatter();
        assert formatter != null;
    }
}
