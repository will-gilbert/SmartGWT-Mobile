package com.smartgwt.mobile.client.internal.widgets.form.fields;

import java.util.Date;
import java.util.LinkedHashMap;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.i18n.SmartGwtMessages;
import com.smartgwt.mobile.client.internal.theme.PickerCssResource;
import com.smartgwt.mobile.client.internal.util.Pair;
import com.smartgwt.mobile.client.internal.widgets.Picker2;
import com.smartgwt.mobile.client.internal.widgets.PickerDial;
import com.smartgwt.mobile.client.internal.widgets.events.ValuesSelectedEvent;
import com.smartgwt.mobile.client.types.DateItemSelectorFormat;
import com.smartgwt.mobile.client.util.DateUtil;
import com.smartgwt.mobile.client.util.LogicalDate;
import com.smartgwt.mobile.client.util.LogicalTime;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;

class DateTimeItemImplDefault extends DateTimeItemImpl {

    private static final ListGridField ARTIFICIAL_YEAR_FIELD = new ListGridField("y"),
            ARTIFICIAL_MONTH_FIELD = new ListGridField("M"),
            ARTIFICIAL_DATE_FIELD = new ListGridField("d"),
            ARTIFICIAL_HOURS_FIELD = new ListGridField("k"),
            ARTIFICIAL_MINUTES_FIELD = new ListGridField("m");
    private static final PickerCssResource CSS = Picker2.CSS;
    private static final LinkedHashMap<Integer, String> DEFAULT_YEARS_MAP = new LinkedHashMap<Integer, String>();
    private static final LinkedHashMap<Integer, String> DEFAULT_MONTHS_MAP = new LinkedHashMap<Integer, String>();
    private static final LinkedHashMap<Integer, String> DEFAULT_DATES_MAP = new LinkedHashMap<Integer, String>();
    private static final LinkedHashMap<Integer, String> DEFAULT_HOURS_MAP = new LinkedHashMap<Integer, String>();
    private static final LinkedHashMap<Integer, String> DEFAULT_MINUTES_MAP = new LinkedHashMap<Integer, String>();
    private static final int MINUTES_STEP_SIZE = 5;
    static {
        DEFAULT_MONTHS_MAP.put(0, SmartGwtMessages.INSTANCE.date_shortMonthNames_1());
        DEFAULT_MONTHS_MAP.put(1, SmartGwtMessages.INSTANCE.date_shortMonthNames_2());
        DEFAULT_MONTHS_MAP.put(2, SmartGwtMessages.INSTANCE.date_shortMonthNames_3());
        DEFAULT_MONTHS_MAP.put(3, SmartGwtMessages.INSTANCE.date_shortMonthNames_4());
        DEFAULT_MONTHS_MAP.put(4, SmartGwtMessages.INSTANCE.date_shortMonthNames_5());
        DEFAULT_MONTHS_MAP.put(5, SmartGwtMessages.INSTANCE.date_shortMonthNames_6());
        DEFAULT_MONTHS_MAP.put(6, SmartGwtMessages.INSTANCE.date_shortMonthNames_7());
        DEFAULT_MONTHS_MAP.put(7, SmartGwtMessages.INSTANCE.date_shortMonthNames_8());
        DEFAULT_MONTHS_MAP.put(8, SmartGwtMessages.INSTANCE.date_shortMonthNames_9());
        DEFAULT_MONTHS_MAP.put(9, SmartGwtMessages.INSTANCE.date_shortMonthNames_10());
        DEFAULT_MONTHS_MAP.put(10, SmartGwtMessages.INSTANCE.date_shortMonthNames_11());
        DEFAULT_MONTHS_MAP.put(11, SmartGwtMessages.INSTANCE.date_shortMonthNames_12());

        for (int i = 1; i <= 31; ++i) {
            DEFAULT_DATES_MAP.put(i, Integer.toString(i));
        }

        for (int i = 1; i < 10; ++i) {
            DEFAULT_HOURS_MAP.put(i, "0" + i);
        }
        for (int i = 10; i < 24; ++i) {
            DEFAULT_HOURS_MAP.put(i, Integer.toString(i));
        }
        DEFAULT_HOURS_MAP.put(0, "24");

        for (int i = 0; i < 60; i += MINUTES_STEP_SIZE) {
            DEFAULT_MINUTES_MAP.put(i, (i < 10 ? "0" : "") + i);
        }

        ARTIFICIAL_YEAR_FIELD.setValueMap(DEFAULT_YEARS_MAP);
        ARTIFICIAL_MONTH_FIELD.setValueMap(DEFAULT_MONTHS_MAP);
        ARTIFICIAL_HOURS_FIELD.setValueMap(DEFAULT_HOURS_MAP);
        ARTIFICIAL_MINUTES_FIELD.setValueMap(DEFAULT_MINUTES_MAP);
    }

    private PickerDial yearDial, monthDial, dateDial, hoursDial, minutesDial;
    private DateItemSelectorFormat origSelectorFormat;

    @Override
    public Element createElement(DateTimeItem self) {
        final InputElement elem = Document.get().createTextInputElement();
        elem.setClassName(DynamicForm._CSS.textItemClass());
        return elem;
    }

    @Override
    public void create(DateTimeItem self) {
        internalSetElementReadOnly(self);

        yearDial = new PickerDial();
        yearDial._setClassName(CSS.dtYearPickerDialClass(), false);
        yearDial.setFields(ARTIFICIAL_YEAR_FIELD);
        yearDial.setValueField(ARTIFICIAL_YEAR_FIELD.getName());
        monthDial = new PickerDial();
        monthDial._setClassName(CSS.dtMonthPickerDialClass(), false);
        monthDial.setFields(ARTIFICIAL_MONTH_FIELD);
        monthDial.setValueField(ARTIFICIAL_MONTH_FIELD.getName());
        dateDial = new PickerDial();
        dateDial._setClassName(CSS.dtDatePickerDialClass(), false);
        dateDial.setFields(ARTIFICIAL_DATE_FIELD);
        dateDial.setValueField(ARTIFICIAL_DATE_FIELD.getName());
        hoursDial = new PickerDial();
        hoursDial._setClassName(CSS.dtHoursPickerDialClass(), false);
        hoursDial.setFields(ARTIFICIAL_HOURS_FIELD);
        hoursDial.setValueField(ARTIFICIAL_HOURS_FIELD.getName());
        minutesDial = new PickerDial();
        minutesDial._setClassName(CSS.dtMinutesPickerDialClass(), false);
        minutesDial.setFields(ARTIFICIAL_MINUTES_FIELD);
        minutesDial.setValueField(ARTIFICIAL_MINUTES_FIELD.getName());

        final int eventBits = (Event.ONCLICK |
                               (TouchEvent.isSupported() ? Event.TOUCHEVENTS : Event.ONMOUSEDOWN | Event.ONMOUSEMOVE | Event.ONMOUSEUP) |
                               Event.ONFOCUS | Event.ONBLUR);
        self.sinkEvents(eventBits);
    }

    @Override
    public void setAllowEmptyValue(DateTimeItem self, boolean allowEmptyValue) {
        final Picker2 picker = self._getPicker();
        if (picker != null) picker.setShowClearButton(allowEmptyValue);
    }

    private void internalSetElementReadOnly(DateTimeItem self) {
        getInputElement(self).setReadOnly(true);
    }

    @Override
    public void setElementReadOnly(DateTimeItem self, boolean readOnly) {
        internalSetElementReadOnly(self);
    }

    @Override
    public InputElement getInputElement(DateTimeItem self) {
        return self.getElement().<InputElement>cast();
    }

    @Override
    public boolean isPickerEnabled(DateTimeItem self) {
        return true;
    }

    private Pair<RecordList, Record> createArtificialDateData(Date d) {
        return Picker2.createArtificialData(DEFAULT_DATES_MAP, ARTIFICIAL_DATE_FIELD.getName(), Integer.valueOf(d.getDate()));
    }

    private Pair<RecordList, Record> createArtificialHoursData(Date d) {
        return Picker2.createArtificialData(DEFAULT_HOURS_MAP, ARTIFICIAL_HOURS_FIELD.getName(), d.getHours());
    }

    private Pair<RecordList, Record> createArtificialMinutesData(Date d) {
        return Picker2.createArtificialData(DEFAULT_MINUTES_MAP, ARTIFICIAL_MINUTES_FIELD.getName(), d.getMinutes());
    }

    private Pair<RecordList, Record> createArtificialMonthData(Date d) {
        return Picker2.createArtificialData(DEFAULT_MONTHS_MAP, ARTIFICIAL_MONTH_FIELD.getName(), Integer.valueOf(d.getMonth()));
    }

    private Pair<RecordList, Record> createArtificialYearData(Date d) {
        final LinkedHashMap<Integer, String> yearsMap = createYearsMap(d);
        return Picker2.createArtificialData(yearsMap, ARTIFICIAL_YEAR_FIELD.getName(), Integer.valueOf(d.getYear() + 1900));
    }

    @Override
    public Picker2 createPicker(DateTimeItem self) {
        Pair<RecordList, Record> p;
        RecordList first;
        Record second;

        Date d = self.getValueAsDate();
        if (d == null) d = new Date();

        if (MINUTES_STEP_SIZE > 1) {
            // Round the minutes.
            int m = d.getMinutes();
            d.setMinutes(MINUTES_STEP_SIZE * (int)Math.round(m / ((double)MINUTES_STEP_SIZE)));
            if (m >= 60 - (MINUTES_STEP_SIZE / 2.0)) {
                d.setHours(d.getHours() + 1);
            }
            m = d.getMinutes();
        }

        p = createArtificialYearData(d);
        first = p.getFirst();
        second = p.getSecond();
        if (second == null && first != null && !first.isEmpty()) second = first.get(0);
        yearDial.setData(first);
        yearDial.selectSingleRecord(second);

        p = createArtificialMonthData(d);
        first = p.getFirst();
        second = p.getSecond();
        if (second == null && first != null && !first.isEmpty()) second = first.get(0);
        monthDial.setData(first);
        monthDial.selectSingleRecord(second);

        p = createArtificialDateData(d);
        first = p.getFirst();
        second = p.getSecond();
        if (second == null && first != null && !first.isEmpty()) second = first.get(0);
        dateDial.setData(first);
        dateDial.selectSingleRecord(second);

        p = createArtificialHoursData(d);
        first = p.getFirst();
        second = p.getSecond();
        if (second == null && first != null && !first.isEmpty()) second = first.get(0);
        hoursDial.setData(first);
        hoursDial.selectSingleRecord(second);

        p = createArtificialMinutesData(d);
        first = p.getFirst();
        second = p.getSecond();
        if (second == null && first != null && !first.isEmpty()) second = first.get(0);
        minutesDial.setData(first);
        minutesDial.selectSingleRecord(second);

        final Picker2 ret = new Picker2();
        origSelectorFormat = self._getSelectorFormat();
        switch (origSelectorFormat) {
            case DAY_MONTH_YEAR:
                ret.setDials(dateDial, monthDial, yearDial, hoursDial, minutesDial);
                break;
            case MONTH_DAY_YEAR:
                ret.setDials(monthDial, dateDial, yearDial, hoursDial, minutesDial);
                break;
            case YEAR_MONTH_DAY:
                ret.setDials(yearDial, monthDial, dateDial, hoursDial, minutesDial);
                break;
            case DAY_MONTH:
            case MONTH_DAY:
            case MONTH_YEAR:
            case YEAR_MONTH:
                assert false;
                throw new UnsupportedOperationException();
        }
        ret.setShowClearButton(self._getAllowEmptyValue());
        return ret;
    }

    private LinkedHashMap<Integer, String> createYearsMap(Date d) {
        final LinkedHashMap<Integer, String> ret = new LinkedHashMap<Integer, String>();
        final int year = d.getYear() + 1900;
        for (int i = -10; i <= 10; ++i) {
            final int y = year + i;
            final int r = y % 100;
            // i18n TODO Add message for abbreviated year string
            final String twoDigitY = "\u2019" + (r < 10 ? "0" + r : Integer.toString(r));
            ret.put(y, twoDigitY);
            DEFAULT_YEARS_MAP.put(y, twoDigitY);
        }
        return ret;
    }

    @Override
    public void onValuesSelected(DateTimeItem self, ValuesSelectedEvent event) {
        Date value = null;

        final Object[] selectedValues = event.getValues();
        if (selectedValues != null) {
            int dateIndex = -1, monthIndex = -1, fullYearIndex = -1;
            switch (origSelectorFormat) {
                case DAY_MONTH_YEAR:
                    assert selectedValues.length == 5;
                    dateIndex = 0;
                    monthIndex = 1;
                    fullYearIndex = 2;
                    break;
                case MONTH_DAY_YEAR:
                    assert selectedValues.length == 5;
                    monthIndex = 0;
                    dateIndex = 1;
                    fullYearIndex = 2;
                    break;
                case YEAR_MONTH_DAY:
                    assert selectedValues.length == 5;
                    fullYearIndex = 0;
                    monthIndex = 1;
                    dateIndex = 2;
                    break;
                case DAY_MONTH:
                case MONTH_DAY:
                case MONTH_YEAR:
                case YEAR_MONTH:
                    assert false;
                    throw new UnsupportedOperationException();
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

            final int logicalHours = (Integer)selectedValues[3],
                    logicalMinutes = (Integer)selectedValues[4];
            value = DateUtil.combineLogicalDateAndTime(new LogicalDate(logicalYear, logicalMonth, logicalDate), new LogicalTime(logicalHours, logicalMinutes, 0));
        }

        self._updateValue(value);
        self.showValue(value);
    }
}
