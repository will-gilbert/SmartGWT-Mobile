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
import com.smartgwt.mobile.client.util.DateUtil;
import com.smartgwt.mobile.client.util.LogicalTime;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.TimeItem;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;

class TimeItemImplDefault extends TimeItemImpl {

    private static final int AM = 0, PM = 1;
    private static final ListGridField ARTIFICIAL_HOURS_FIELD = new ListGridField("h"),
            ARTIFICIAL_MINUTES_FIELD = new ListGridField("m"),
            ARTIFICIAL_AMPM_FIELD = new ListGridField("a");
    private static final PickerCssResource CSS = Picker2.CSS;
    private static final LinkedHashMap<Integer, String> DEFAULT_HOURS_MAP = new LinkedHashMap<Integer, String>();
    private static final LinkedHashMap<Integer, String> DEFAULT_MINUTES_MAP = new LinkedHashMap<Integer, String>();
    private static final LinkedHashMap<Integer, String> DEFAULT_AMPM_MAP = new LinkedHashMap<Integer, String>();
    private static final int MINUTES_STEP_SIZE = 5;
    static {
        for (int i = 0; i < 12; ++i) {
            DEFAULT_HOURS_MAP.put(i, Integer.toString(i + 1));
        }

        for (int i = 0; i < 60; i += MINUTES_STEP_SIZE) {
            DEFAULT_MINUTES_MAP.put(i, (i < 10 ? "0" : "") + i);
        }

        DEFAULT_AMPM_MAP.put(AM, SmartGwtMessages.INSTANCE.time_AMIndicator().trim());
        DEFAULT_AMPM_MAP.put(PM, SmartGwtMessages.INSTANCE.time_PMIndicator().trim());

        ARTIFICIAL_HOURS_FIELD.setValueMap(DEFAULT_HOURS_MAP);
        ARTIFICIAL_MINUTES_FIELD.setValueMap(DEFAULT_MINUTES_MAP);
        ARTIFICIAL_AMPM_FIELD.setValueMap(DEFAULT_AMPM_MAP);
    }

    private PickerDial hoursDial, minutesDial, ampmDial;

    @Override
    public Element createElement(TimeItem self) {
        final InputElement elem = Document.get().createTextInputElement();
        elem.setClassName(DynamicForm._CSS.textItemClass());
        return elem;
    }

    @Override
    public void create(TimeItem self) {
        internalSetElementReadOnly(self);

        hoursDial = new PickerDial();
        hoursDial._setClassName(CSS.hoursPickerDialClass(), false);
        hoursDial.setFields(ARTIFICIAL_HOURS_FIELD);
        hoursDial.setValueField(ARTIFICIAL_HOURS_FIELD.getName());
        minutesDial = new PickerDial();
        minutesDial._setClassName(CSS.minutesPickerDialClass(), false);
        minutesDial.setFields(ARTIFICIAL_MINUTES_FIELD);
        minutesDial.setValueField(ARTIFICIAL_MINUTES_FIELD.getName());
        ampmDial = new PickerDial();
        ampmDial._setClassName(CSS.ampmPickerDialClass(), false);
        ampmDial.setFields(ARTIFICIAL_AMPM_FIELD);
        ampmDial.setValueField(ARTIFICIAL_AMPM_FIELD.getName());

        final int eventBits = (Event.ONCLICK |
                               (TouchEvent.isSupported() ? Event.TOUCHEVENTS : Event.ONMOUSEDOWN | Event.ONMOUSEMOVE | Event.ONMOUSEUP) |
                               Event.ONFOCUS | Event.ONBLUR);
        self.sinkEvents(eventBits);
    }

    @Override
    public void setAllowEmptyValue(TimeItem self, boolean allowEmptyValue) {
        final Picker2 picker = self._getPicker();
        if (picker != null) picker.setShowClearButton(allowEmptyValue);
    }

    private void internalSetElementReadOnly(TimeItem self) {
        getInputElement(self).setReadOnly(true);
    }

    @Override
    public void setElementReadOnly(TimeItem self, boolean readOnly) {
        internalSetElementReadOnly(self);
    }

    @Override
    public InputElement getInputElement(TimeItem self) {
        return self.getElement().<InputElement>cast();
    }

    @Override
    public boolean isPickerEnabled(TimeItem self) {
        return true;
    }

    private Pair<RecordList, Record> createArtificialHoursData(LogicalTime t) {
        int h = t.getLogicalHours();
        if (h < 12) {
            h = h == 0 ? 11 : h - 1;
        } else {
            h = h == 12 ? 11 : h - 13;
        }
        return Picker2.createArtificialData(DEFAULT_HOURS_MAP, ARTIFICIAL_HOURS_FIELD.getName(), h);
    }

    private Pair<RecordList, Record> createArtificialMinutesData(LogicalTime t) {
        return Picker2.createArtificialData(DEFAULT_MINUTES_MAP, ARTIFICIAL_MINUTES_FIELD.getName(), t.getLogicalMinutes());
    }

    private Pair<RecordList, Record> createArtificialAMPMData(LogicalTime t) {
        return Picker2.createArtificialData(DEFAULT_AMPM_MAP, ARTIFICIAL_AMPM_FIELD.getName(), t.getLogicalHours() < 12 ? AM : PM);
    }

    @Override
    public Picker2 createPicker(TimeItem self) {
        Pair<RecordList, Record> p;
        RecordList first;
        Record second;

        LogicalTime t = self.getValueAsTime();
        if (t == null) t = DateUtil.getLogicalTimeOnly(new Date());

        if (MINUTES_STEP_SIZE > 1) {
            // Round the minutes.
            int h = t.getLogicalHours(), m = t.getLogicalMinutes();
            if (m >= 60 - (MINUTES_STEP_SIZE / 2.0)) {
                h = (h + 1) % 24;
            }
            m = MINUTES_STEP_SIZE * (int)Math.round(m / ((double)MINUTES_STEP_SIZE));
            t = new LogicalTime(h, m, t.getLogicalSeconds());
        }

        p = createArtificialHoursData(t);
        first = p.getFirst();
        second = p.getSecond();
        if (second == null && first != null && !first.isEmpty()) second = first.get(0);
        hoursDial.setData(first);
        hoursDial.selectSingleRecord(second);

        p = createArtificialMinutesData(t);
        first = p.getFirst();
        second = p.getSecond();
        if (second == null && first != null && !first.isEmpty()) second = first.get(0);
        minutesDial.setData(first);
        minutesDial.selectSingleRecord(second);

        p = createArtificialAMPMData(t);
        first = p.getFirst();
        second = p.getSecond();
        if (second == null && first != null && !first.isEmpty()) second = first.get(0);
        ampmDial.setData(first);
        ampmDial.selectSingleRecord(second);

        final Picker2 ret = new Picker2();
        ret._setClassName(CSS.timePickerClass(), false);
        ret.setDials(hoursDial, minutesDial, ampmDial);
        ret.setShowClearButton(self._getAllowEmptyValue());
        return ret;
    }

    @Override
    public void onValuesSelected(TimeItem self, ValuesSelectedEvent event) {
        LogicalTime value = null;

        final Object[] selectedValues = event.getValues();
        if (selectedValues != null) {
            int logicalHours = (Integer)selectedValues[0],
                    logicalMinutes = (Integer)selectedValues[1],
                    ampm = (Integer)selectedValues[2];
            if (ampm == AM) {
                logicalHours = logicalHours == 11 ? 0 : logicalHours + 1;
            } else {
                logicalHours = logicalHours == 11 ? 12 : logicalHours + 13;
            }
            value = new LogicalTime(logicalHours, logicalMinutes, 0);
        }

        self._updateValue(value);
        self.showValue(value);
    }
}
