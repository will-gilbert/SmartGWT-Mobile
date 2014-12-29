package com.smartgwt.mobile.client.internal.widgets.form.fields;

import java.util.LinkedHashMap;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.internal.theme.PickerCssResource;
import com.smartgwt.mobile.client.internal.util.Pair;
import com.smartgwt.mobile.client.internal.widgets.Picker2;
import com.smartgwt.mobile.client.internal.widgets.PickerDial;
import com.smartgwt.mobile.client.internal.widgets.events.ValuesSelectedEvent;
import com.smartgwt.mobile.client.types.DateItemSelectorFormat;
import com.smartgwt.mobile.client.util.LogicalDate;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.DateItem;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;

class DateItemImplDefault extends DateItemImpl {

    private static final ListGridField ARTIFICIAL_YEAR_FIELD = new ListGridField("year"),
            ARTIFICIAL_MONTH_FIELD = new ListGridField("month"),
            ARTIFICIAL_DATE_FIELD = new ListGridField("date");
    private static final PickerCssResource CSS = Picker2.CSS;
    private static final LinkedHashMap<Integer, String> DEFAULT_MONTHS_MAP = new LinkedHashMap<Integer, String>();
    private static final LinkedHashMap<Integer, String> DEFAULT_DATES_MAP = new LinkedHashMap<Integer, String>();
    static {
        DEFAULT_MONTHS_MAP.put(0, "January");
        DEFAULT_MONTHS_MAP.put(1, "February");
        DEFAULT_MONTHS_MAP.put(2, "March");
        DEFAULT_MONTHS_MAP.put(3, "April");
        DEFAULT_MONTHS_MAP.put(4, "May");
        DEFAULT_MONTHS_MAP.put(5, "June");
        DEFAULT_MONTHS_MAP.put(6, "July");
        DEFAULT_MONTHS_MAP.put(7, "August");
        DEFAULT_MONTHS_MAP.put(8, "September");
        DEFAULT_MONTHS_MAP.put(9, "October");
        DEFAULT_MONTHS_MAP.put(10, "November");
        DEFAULT_MONTHS_MAP.put(11, "December");

        for (int i = 1; i <= 31; ++i) {
            DEFAULT_DATES_MAP.put(i, Integer.toString(i));
        }

        ARTIFICIAL_MONTH_FIELD.setValueMap(DEFAULT_MONTHS_MAP);
    }

    private PickerDial yearDial, monthDial, dateDial;
    private DateItemSelectorFormat origSelectorFormat;

    @Override
    public InputElement createElement(DateItem self) {
        final InputElement elem = Document.get().createTextInputElement();
        elem.setClassName(DynamicForm._CSS.textItemClass());
        return elem;
    }

    @Override
    public void create(DateItem self) {
        internalSetElementReadOnly(self);

        yearDial = new PickerDial();
        yearDial._setClassName(CSS.yearPickerDialClass(), false);
        yearDial.setFields(ARTIFICIAL_YEAR_FIELD);
        yearDial.setValueField(ARTIFICIAL_YEAR_FIELD.getName());
        monthDial = new PickerDial();
        monthDial._setClassName(CSS.monthPickerDialClass(), false);
        monthDial.setFields(ARTIFICIAL_MONTH_FIELD);
        monthDial.setValueField(ARTIFICIAL_MONTH_FIELD.getName());
        dateDial = new PickerDial();
        dateDial._setClassName(CSS.datePickerDialClass(), false);
        dateDial.setFields(ARTIFICIAL_DATE_FIELD);
        dateDial.setValueField(ARTIFICIAL_DATE_FIELD.getName());

        final int eventBits = (Event.ONCLICK |
                               (TouchEvent.isSupported() ? Event.TOUCHEVENTS : Event.ONMOUSEDOWN | Event.ONMOUSEMOVE | Event.ONMOUSEUP) |
                               Event.ONFOCUS | Event.ONBLUR);
        self.sinkEvents(eventBits);
    }

    @Override
    public void setAllowEmptyValue(DateItem self, boolean allowEmptyValue) {
        final Picker2 picker = self._getPicker();
        if (picker != null) picker.setShowClearButton(allowEmptyValue);
    }

    private void internalSetElementReadOnly(DateItem self) {
        getInputElement(self).setReadOnly(true);
    }

    @Override
    public void setElementReadOnly(DateItem self, boolean readOnly) {
        internalSetElementReadOnly(self);
    }

    @Override
    public InputElement getInputElement(DateItem self) {
        return self.getElement().<InputElement>cast();
    }

    @Override
    public boolean isPickerEnabled(DateItem self) {
        return true;
    }

    @Override
    public void setStartDate(DateItem self, LogicalDate startDate) {
        // TODO
    }

    @Override
    public void setEndDate(DateItem self, LogicalDate endDate) {
        // TODO
    }

    private Pair<RecordList, Record> createArtificialDateData(DateItem self, LogicalDate d) {
        final LinkedHashMap<Integer, String> datesMap = createDatesMap(self);
        return Picker2.createArtificialData(datesMap, ARTIFICIAL_DATE_FIELD.getName(), Integer.valueOf(d.getLogicalDate()));
    }

    private Pair<RecordList, Record> createArtificialMonthData(DateItem self, LogicalDate d) {
        final LinkedHashMap<Integer, String> monthsMap = createMonthsMap(self);
        return Picker2.createArtificialData(monthsMap, ARTIFICIAL_MONTH_FIELD.getName(), Integer.valueOf(d.getLogicalMonth()));
    }

    private Pair<RecordList, Record> createArtificialYearData(DateItem self, LogicalDate d) {
        final LinkedHashMap<Integer, String> yearsMap = createYearsMap(self);
        return Picker2.createArtificialData(yearsMap, ARTIFICIAL_YEAR_FIELD.getName(), Integer.valueOf(d.getLogicalYear()));
    }

    private LinkedHashMap<Integer, String> createDatesMap(DateItem self) {
        final LogicalDate startDate = self.getStartDate(),
                endDate = self.getEndDate();

        assert DateItem._isValidDateRange(startDate, endDate);
        LinkedHashMap<Integer, String> ret = DEFAULT_DATES_MAP;
        if (startDate != null && endDate != null) {
            if (startDate.getLogicalYear() == endDate.getLogicalYear() &&
                    startDate.getLogicalMonth() == endDate.getLogicalMonth())
            {
                ret = new LinkedHashMap<Integer, String>();
                final int endI = endDate.getLogicalDate();
                for (int i = startDate.getLogicalDate(); i <= endI; ++i) {
                    ret.put(i, Integer.toString(i));
                }
            }
        }
        return ret;
    }

    private LinkedHashMap<Integer, String> createMonthsMap(DateItem self) {
        final LogicalDate startDate = self.getStartDate(),
                endDate = self.getEndDate();

        assert DateItem._isValidDateRange(startDate, endDate);
        LinkedHashMap<Integer, String> ret = DEFAULT_MONTHS_MAP;
        if (startDate != null && endDate != null) {
            if (startDate.getLogicalYear() == endDate.getLogicalYear()) {
                ret = new LinkedHashMap<Integer, String>();
                final int endI = endDate.getLogicalMonth();
                for (int i = startDate.getLogicalMonth(); i <= endI; ++i) {
                    ret.put(i, DEFAULT_MONTHS_MAP.get(i));
                }
            }
        }
        return ret;
    }

    private LinkedHashMap<Integer, String> createYearsMap(DateItem self) {
        assert DateItem._isValidDateRange(self.getStartDate(), self.getEndDate());
        LogicalDate startDate, endDate;
        if (self.getStartDate() != null && self.getEndDate() != null) {
            startDate = self.getStartDate();
            endDate = self.getEndDate();
        } else {
            startDate = new LogicalDate();
            startDate = new LogicalDate(startDate.getLogicalYear() - 5, 6 - 1, 30);
            endDate = new LogicalDate(startDate.getLogicalYear() + 10, 6 - 1, 30);
        }
        final LinkedHashMap<Integer, String> ret = new LinkedHashMap<Integer, String>();
        final int endI = endDate.getLogicalYear();
        for (int i = startDate.getLogicalYear(); i <= endI; ++i) {
            ret.put(i, Integer.toString(i));
        }
        return ret;
    }

    @Override
    public Picker2 createPicker(DateItem self) {
        Pair<RecordList, Record> p;
        RecordList first;
        Record second;

        LogicalDate d = self.getValueAsDate();
        if (d == null) d = new LogicalDate();

        p = createArtificialYearData(self, d);
        first = p.getFirst();
        second = p.getSecond();
        if (second == null && first != null && !first.isEmpty()) second = first.get(0);
        yearDial.setData(first);
        yearDial.selectSingleRecord(second);

        p = createArtificialMonthData(self, d);
        first = p.getFirst();
        second = p.getSecond();
        if (second == null && first != null && !first.isEmpty()) second = first.get(0);
        monthDial.setData(first);
        monthDial.selectSingleRecord(second);

        p = createArtificialDateData(self, d);
        first = p.getFirst();
        second = p.getSecond();
        if (second == null && first != null && !first.isEmpty()) second = first.get(0);
        dateDial.setData(first);
        dateDial.selectSingleRecord(second);

        final Picker2 ret = new Picker2();
        origSelectorFormat = self._getSelectorFormat();
        switch (origSelectorFormat) {
            case DAY_MONTH:
                ret.setDials(dateDial, monthDial);
                break;
            case DAY_MONTH_YEAR:
                ret.setDials(dateDial, monthDial, yearDial);
                break;
            case MONTH_DAY:
                ret.setDials(monthDial, dateDial);
                break;
            case MONTH_DAY_YEAR:
                ret.setDials(monthDial, dateDial, yearDial);
                break;
            case MONTH_YEAR:
                ret.setDials(monthDial, yearDial);
                break;
            case YEAR_MONTH:
                ret.setDials(yearDial, monthDial);
                break;
            case YEAR_MONTH_DAY:
                ret.setDials(yearDial, monthDial, dateDial);
                break;
        }
        ret.setShowClearButton(self._getAllowEmptyValue());
        return ret;
    }

    @Override
    public void onValuesSelected(DateItem self, ValuesSelectedEvent event) {
        LogicalDate value = null;

        final Object[] selectedValues = event.getValues();
        if (selectedValues != null) {
            int dateIndex = -1, monthIndex = -1, fullYearIndex = -1;
            switch (origSelectorFormat) {
                case DAY_MONTH:
                    assert selectedValues.length == 2;
                    dateIndex = 0;
                    monthIndex = 1;
                    break;
                case DAY_MONTH_YEAR:
                    assert selectedValues.length == 3;
                    dateIndex = 0;
                    monthIndex = 1;
                    fullYearIndex = 2;
                    break;
                case MONTH_DAY:
                    assert selectedValues.length == 2;
                    monthIndex = 0;
                    dateIndex = 1;
                    break;
                case MONTH_DAY_YEAR:
                    assert selectedValues.length == 3;
                    monthIndex = 0;
                    dateIndex = 1;
                    fullYearIndex = 2;
                    break;
                case MONTH_YEAR:
                    assert selectedValues.length == 2;
                    monthIndex = 0;
                    fullYearIndex = 1;
                    break;
                case YEAR_MONTH:
                    assert selectedValues.length == 2;
                    fullYearIndex = 0;
                    monthIndex = 1;
                    break;
                case YEAR_MONTH_DAY:
                    assert selectedValues.length == 3;
                    fullYearIndex = 0;
                    monthIndex = 1;
                    dateIndex = 2;
                    break;
            }

            final LogicalDate d = new LogicalDate();
            int logicalYear = d.getLogicalYear(),
                    logicalMonth = d.getLogicalMonth(),
                    logicalDate = d.getLogicalDate();

            if (dateIndex != -1) {
                final Integer i = (Integer)selectedValues[dateIndex];
                if (i != null) logicalDate = i.intValue();
            }
            if (monthIndex != -1) {
                final Integer i = (Integer)selectedValues[monthIndex];
                if (i != null) logicalMonth = i.intValue();
            }
            if (fullYearIndex != -1) {
                final Integer i = (Integer)selectedValues[fullYearIndex];
                if (i != null) logicalYear = i.intValue();
            }

            value = new LogicalDate(logicalYear, logicalMonth, logicalDate);
        }

        self._updateValue(value);
        self.showValue(value);
    }
}
