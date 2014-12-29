package com.smartgwt.mobile.client.internal.widgets;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.internal.data.ArtificialRecord;
import com.smartgwt.mobile.client.internal.test.AutoTest;
import com.smartgwt.mobile.client.internal.test.AutoTestLocatable;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.theme.PickerCssResource;
import com.smartgwt.mobile.client.internal.util.ObjectUtil;
import com.smartgwt.mobile.client.internal.util.Pair;
import com.smartgwt.mobile.client.internal.widgets.events.HasPickerHiddenHandlers;
import com.smartgwt.mobile.client.internal.widgets.events.HasValuesSelectedHandlers;
import com.smartgwt.mobile.client.internal.widgets.events.PickerHiddenEvent;
import com.smartgwt.mobile.client.internal.widgets.events.PickerHiddenHandler;
import com.smartgwt.mobile.client.internal.widgets.events.ValuesSelectedEvent;
import com.smartgwt.mobile.client.internal.widgets.events.ValuesSelectedHandler;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;

@SGWTInternal
public class Picker2 extends Popup implements HasValuesSelectedHandlers, HasPickerHiddenHandlers {

    static final String ARTIFICIAL_FIELD_NAME = "value";

    @SGWTInternal
    public static final PickerCssResource CSS = ThemeResources.INSTANCE.pickerCSS();

    public static Pair<RecordList, Record> createArtificialData(LinkedHashMap<?, String> valueMap, String valueFieldName, Object value) {
        final RecordList records = new RecordList();
        Record selectedRecord = null;
        if (valueMap != null) {
            for (final Object key : valueMap.keySet()) {
                final Record record = new ArtificialRecord();
                record.setAttribute(valueFieldName, key);
                if (ObjectUtil.abstractCompareValues(key, value)) {
                    selectedRecord = record;
                }
                records.add(record);
            }
        }
        return Pair.create(records, selectedRecord);
    }

    public static PickerDial createArtificialDial(LinkedHashMap<?, String> valueMap, Object value) {
        final ListGridField artificialField = new ListGridField(ARTIFICIAL_FIELD_NAME);
        artificialField.setValueMap(valueMap);
        final PickerDial ret = new PickerDial();
        final Pair<RecordList, Record> p = createArtificialData(valueMap, ARTIFICIAL_FIELD_NAME, value);
        ret.setData(p.getFirst());
        // setData() creates a new selection model. The dial selection needs to be set after
        // this selection model is created.
        ret.selectSingleRecord(p.getSecond());
        ret.setFields(artificialField);
        return ret;
    }

    private PickerImpl impl = GWT.create(PickerImpl.class);

    private PickerDial[] dials;

    public Picker2() {
        _setIsModal(true);
        impl.init(this);
    }

    public Picker2(LinkedHashMap<?, String>[] valueMaps, Object[] values) {
        this();

        if (valueMaps != null && valueMaps.length != 0) {
            // Create artificial dials.
            PickerDial[] artificialDials = new PickerDial[valueMaps.length];
            for (int i = 0; i < valueMaps.length; ++i) {
                final LinkedHashMap<?, String> valueMap = valueMaps[i];
                artificialDials[i] = createArtificialDial(valueMap, values[i]);
            }
            setDials(artificialDials);
        }
    }

    final AutoTestLocatable super_getChildFromLocatorSubstring(String substring, int index, List<String> locatorArray, GetAttributeConfiguration configuration) {
        return super._getChildFromLocatorSubstring(substring, index, locatorArray, configuration);
    }

    @Override
    public AutoTestLocatable _getChildFromLocatorSubstring(String substring, int index, List<String> locatorArray, GetAttributeConfiguration configuration) {
        if (dials != null && dials.length != 0) {
            if (substring.equals("dial") && dials.length == 1) return dials[0];
            if (substring.startsWith("dial[")) {
                final Pair<String, Map<String, String>> p = AutoTest.parseLocatorFallbackPath(substring);
                if (p != null) {
                    assert "dial".equals(p.getFirst());
                    final Map<String, String> configObj = p.getSecond();
                    final String valueOnly = configObj.get(AutoTest.FALLBACK_VALUE_ONLY_FIELD);
                    if (valueOnly != null) {
                        int i = -1;
                        try {
                            i = Integer.parseInt(valueOnly, 10);
                        } catch (NumberFormatException ex) {}
                        if (i >= 0 && i < dials.length) return dials[i];
                    }
                }
            }
        }
        return impl.getChildFromLocatorSubstring(this, substring, index, locatorArray, configuration);
    }

    final Object super_getInnerAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration) {
        return super._getInnerAttributeFromSplitLocator(locatorArray, configuration);
    }

    @Override
    public Object _getInnerAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration) {
        return impl.getInnerAttributeFromSplitLocator(this, locatorArray, configuration);
    }

    public final PickerDial[] getDials() {
        return dials;
    }

    public void setDials(PickerDial... dials) {
        if (this.dials != dials) {
            final PickerDial[] oldDials = this.dials;
            this.dials = dials;
            impl.setDials(this, oldDials, dials);
        }
    }

    public final Object[] getSelectedValues() {
        if (dials == null) return null;
        final Object[] ret = new Object[dials.length];
        for (int i = 0; i < dials.length; ++i) {
            final PickerDial dial = dials[i];
            final Record selectedRecord = dial.getSelectedRecord();
            if (selectedRecord != null) ret[i] = selectedRecord.getAttributeAsObject(dial.getValueField());
        }
        return ret;
    }

    public void setSelectedValues(Object[] selectedValues) {
        // TODO
    }

    public void setShowClearButton(Boolean showClearButton) {
        impl.setShowClearButton(this, showClearButton);
    }

    @SGWTInternal
    void _add(Widget child, com.google.gwt.user.client.Element container) {
        super.add(child, container);
    }

    @Override
    protected void _destroyPopup() {
        impl.destroyPopup(this);
    }

    public final void hide() {
        PopupManager.requestHide(this);
    }

    @Override
    protected void _doHide() {
        impl.doHide(this);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        impl.onBrowserEvent(this, event);
    }

    public final void show() {
        PopupManager.requestShow(this);
    }

    @Override
    protected void _doShow() {
        impl.doShow(this);
    }

    @Override
    public HandlerRegistration _addValuesSelectedHandler(ValuesSelectedHandler handler) {
        return addHandler(handler, ValuesSelectedEvent.getType());
    }

    @Override
    public HandlerRegistration _addPickerHiddenHandler(PickerHiddenHandler handler) {
        return addHandler(handler, PickerHiddenEvent.getType());
    }
}
