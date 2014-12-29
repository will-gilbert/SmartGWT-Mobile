package com.smartgwt.mobile.client.widgets.form.fields;

import java.util.Iterator;
import java.util.LinkedHashMap;

import com.google.gwt.dom.client.InputElement;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.Criteria;
import com.smartgwt.mobile.client.data.DSCallback;
import com.smartgwt.mobile.client.data.DSRequest;
import com.smartgwt.mobile.client.data.DSResponse;
import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.internal.data.ArtificialRecord;
import com.smartgwt.mobile.client.internal.util.ObjectUtil;
import com.smartgwt.mobile.client.internal.util.Pair;
import com.smartgwt.mobile.client.internal.widgets.Picker2;
import com.smartgwt.mobile.client.internal.widgets.PickerDial;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;

public class SelectItem extends TextItem implements PickList {

    // Used to set the `PickerDial' fields if pickListFields is not explicitly set.
    private ListGridField artificialField;

    private Boolean allowEmptyValue;
    private Boolean defaultToFirstOption;
    private String optionOperationId;
    private PickerDial pickerDial;
    private Criteria pickListCriteria;
    private ListGridField[] pickListFields;

    public SelectItem(String name) {
        super(name);
        pickerDial = new PickerDial();
        pickerDial.setAllowEmptyValue(_getAllowEmptyValue());
        pickerDial.setEmptyDisplayValue(super._getEmptyDisplayValue());
        pickerDial.setValueField(name);
        artificialField = new ListGridField(name);
        pickerDial.setFields(artificialField);
        super._setElementReadOnly(true);
    }

    public SelectItem(String name, String title) {
        this(name);
        super.setTitle(title);
    }

    public SelectItem(String name, String title, String hint) {
        this(name, title);
        super.setHint(hint);
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
        pickerDial.setAllowEmptyValue(_getAllowEmptyValue());
    }

    public final Boolean getDefaultToFirstOption() {
        return defaultToFirstOption;
    }

    @SGWTInternal
    public final boolean _getDefaultToFirstOption() {
        return Canvas._booleanValue(getDefaultToFirstOption(), false);
    }

    public void setDefaultToFirstOption(Boolean defaultToFirstOption) {
        this.defaultToFirstOption = defaultToFirstOption;

        if (_getDefaultToFirstOption()) {
            if (getValue() == null) {
                setValue(_getFirstOptionValue());
            }
        }
    }

    @SGWTInternal
    public void _setElementReadOnly(boolean readOnly) {
        getElement().<InputElement>cast().setReadOnly(true);
    }

    @Override
    public void setEmptyDisplayValue(String emptyDisplayValue) {
        super.setEmptyDisplayValue(emptyDisplayValue);
        pickerDial.setEmptyDisplayValue(emptyDisplayValue);
    }

    @SGWTInternal
    public Object _getFirstOptionValue() {
        final RecordList records = pickerDial._getData();
        if (records == null || records.isEmpty()) return null;
        final Record firstRecord = records.get(0);
        return firstRecord.get(pickerDial.getValueField());
    }

    @Override
    public void setOptionDataSource(DataSource ds) {
        super.setOptionDataSource(ds);
        pickerDial.setDataSource(ds);
    }

    public final String getOptionOperationId() {
        return optionOperationId;
    }

    public void setOptionOperationId(String optionOperationId) {
        this.optionOperationId = optionOperationId;
    }

    public String getPickListBaseStyle() {
        return pickerDial.getBaseStyle();
    }

    public void setPickListBaseStyle(String pickListBaseStyle) {
        pickerDial.setBaseStyle(pickListBaseStyle);
    }

    /**
     * Returns the <code>Criteria</code> used to make fetches if data-bound.
     * 
     * <p>If this item has a data-bound <code>PickList</code> (for example {@link #getOptionDataSource() optionDataSource}
     * is set), this can be used to provide static filter criteria when retrieving the data for
     * the <code>PickList</code>.
     */
    @Override
    public final Criteria getPickListCriteria() {
        return pickListCriteria;
    }

    @Override
    public void setPickListCriteria(Criteria criteria) {
        this.pickListCriteria = criteria;
    }

    public final ListGridField[] getPickListFields() {
        return pickListFields;
    }

    public void setPickListFields(ListGridField... pickListFields) {
        this.pickListFields = pickListFields;
        if (pickListFields == null) pickerDial.setFields(artificialField);
        else pickerDial.setFields(pickListFields);
    }

    /**
     * Returns a set of filter criteria to be applied to the data displayed in the <code>PickList</code>
     * when it is shown.
     * 
     * <p>If this is a data-bound item the criteria will be passed as criteria to DataSource.fetchData().
     * By default returns {@link #getPickListCriteria() pickListCriteria} if specified, otherwise
     * an empty set of criteria so all records will be displayed.
     * 
     * <p><b>NOTE:</b> This is an override point - if overridden this method will be called by
     * the live form item during filtering.
     */
    protected Criteria getPickListFilterCriteria() {
        return getPickListCriteria();
    }

    public final Record getSelectedRecord() {
        final Record selectedRecord = pickerDial.getSelectedRecord();
        if (selectedRecord instanceof ArtificialRecord) return null;
        return selectedRecord;
    }

    @Override
    public void setValueField(String valueField) {
        super.setValueField(valueField);
        valueField = getValueFieldName();
        pickerDial.setValueField(valueField);
        artificialField.setName(valueField);
        refreshArtificialData();
    }

    @Override
    public void setValueMap(LinkedHashMap<?, String> valueMap) {
        super.setValueMap(valueMap);
        artificialField.setValueMap(valueMap);
        refreshArtificialData();
        if (getValue() == null && _getDefaultToFirstOption()) {
            setValue(_getFirstOptionValue());
        }
    }

    @Override
    protected Picker2 _createPicker() {
        final Picker2 ret = new Picker2();
        ret.setDials(pickerDial);
        return ret;
    }

    public void fetchData() {
        fetchData(null);
    }

    public void fetchData(DSCallback dsCallback) {
        final DataSource ds = getOptionDataSource();
        if (ds == null) {
            return;
        }
        final DSCallback userCallback = dsCallback;
        final DSRequest requestProperties = new DSRequest();
        if (optionOperationId != null) requestProperties.setOperationId(optionOperationId);
        ds.fetchData(getPickListFilterCriteria(), new DSCallback() {
            @Override
            public void execute(DSResponse dsResponse, Object rawData, DSRequest dsRequest) {
                final String valueField = getValueFieldName();

                final String displayField = getDisplayFieldName();

                final LinkedHashMap<Object, String> valueMap = new LinkedHashMap<Object, String>();
                final RecordList data = dsResponse.getDataAsRecordList();
                if (data != null) {
                    for (final Iterator<Record> it = data.iterator(); it.hasNext(); ) {
                        final Record record = it.next();
                        final Object value = record.get(valueField),
                                normalizedValue = ObjectUtil.normalize(value);
                        if (valueMap.containsKey(normalizedValue)) {
                            SC.logWarn("SelectItem DSCallback: Duplicate value " + (value == null ? "null" : value.toString()) + " using value field '" + valueField + "'");
                            it.remove();
                        } else {
                            valueMap.put(normalizedValue, record.getAttribute(displayField));
                        }
                    }
                }
                SelectItem.super.setValueMap(valueMap);

                artificialField.setValueMap(valueMap);

                pickerDial.setData(data);

                Object value = getValue();
                if (value == null && _getDefaultToFirstOption()) {
                    setValue(_getFirstOptionValue());
                } else {
                    // Update the dial selection.
                    if (value == null) {
                        value = _getDefaultValue();
                    }
                    pickerDial.selectSingleRecord(findRecordForValue(value));
                }

                if (userCallback != null) userCallback.execute(dsResponse, rawData, dsRequest);
            }
        }, requestProperties);
    }

    @Override
    public boolean _isPickerEnabled() {
        return true;
    }

    // Find the `Record' corresponding to the given value.
    private Record findRecordForValue(Object value) {
        final RecordList records = pickerDial._getData();
        if (records != null && !records.isEmpty()) {
            final String valueField = getValueFieldName();
            if (value == null && _getDefaultToFirstOption()) {
                return records.get(0);
            } else {
                if (value == null) value = _getDefaultValue();
                if (value != null) {
                    for (final Record record : records) {
                        final Object val = record.getAttributeAsObject(valueField);
                        if (ObjectUtil.abstractCompareValues(val, value)) {
                            return record;
                        }
                    }
                }
                if (!_getAllowEmptyValue()) return records.get(0);
            }
        }
        return null;
    }

    private void refreshArtificialData() {
        final LinkedHashMap<?, String> valueMap = _getValueMap();
        final String valueField = getValueFieldName();
        if (pickListFields == null) {
            Object value = getValue();
            if (value == null) {
                value = _getDefaultValue();
            }
            final Pair<RecordList, Record> p = Picker2.createArtificialData(valueMap, valueField, value);
            final RecordList data = p.getFirst();
            pickerDial.setData(data);
            Record recordToSelect = p.getSecond();
            if (recordToSelect == null && !_getAllowEmptyValue() && data != null && !data.isEmpty()) {
                recordToSelect = data.get(0);
            }
            pickerDial.selectSingleRecord(recordToSelect);
        }
    }

    @Override
    public void showValue(Object value) {
        super.showValue(value);
        pickerDial.selectSingleRecord(findRecordForValue(value));
    }
}
