package com.smartgwt.mobile.client.internal.widgets;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.data.ResultSet;
import com.smartgwt.mobile.client.data.events.DataArrivedEvent;
import com.smartgwt.mobile.client.data.events.DataArrivedHandler;
import com.smartgwt.mobile.client.data.events.DataChangedEvent;
import com.smartgwt.mobile.client.data.events.DataChangedHandler;
import com.smartgwt.mobile.client.internal.SelectedSetEvent;
import com.smartgwt.mobile.client.internal.SelectedSetHandler;
import com.smartgwt.mobile.client.internal.Selection;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.theme.PickerCssResource;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.widgets.grid.ListGrid;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;

@SGWTInternal
public class PickerDial extends ListGrid {

    private static final PickerCssResource CSS = Picker2.CSS;

    private final PickerDialImpl impl = GWT.create(PickerDialImpl.class);

    private boolean allowEmptyValue;
    private HandlerRegistration dataArrivedRegistration,
            dataChangedRegistration;
    private String emptyDisplayValue;
    private HandlerRegistration selectedSetRegistration;
    private String valueField = Picker2.ARTIFICIAL_FIELD_NAME;
    private Integer width;

    public PickerDial() {
        setSelectionType(SelectionStyle.SINGLE);

        impl.init(this);
    }

    @Override
    public void destroy() {
        if (selectedSetRegistration != null) {
            selectedSetRegistration.removeHandler();
            selectedSetRegistration = null;
        }
        if (dataChangedRegistration != null) {
            dataChangedRegistration.removeHandler();
            dataChangedRegistration = null; 
        }
        if (dataArrivedRegistration != null) {
            dataArrivedRegistration.removeHandler();
            dataArrivedRegistration = null;
        }
        impl.destroyImpl(this);
        super.destroy();
    }

    public final boolean getAllowEmptyValue() {
        return allowEmptyValue;
    }

    public void setAllowEmptyValue(boolean allowEmptyValue) {
        if (isAttached()) throw new IllegalStateException("Cannot change PickerDial.allowEmptyValue while attached");
        this.allowEmptyValue = allowEmptyValue;
        _refreshRows();
    }

    final Object super_getAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration) {
        return super._getAttributeFromSplitLocator(locatorArray, configuration);
    }

    @Override
    public Object _getAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration) {
        return impl._getAttributeFromSplitLocator(this, locatorArray, configuration);
    }

    @Override
    public final String getBaseStyle(Record record, int rowNum, int colNum) {
        return super.getBaseStyle(record, rowNum, colNum);
    }

    @Override
    public void setBaseStyle(String baseStyle) {
        super.setBaseStyle(baseStyle);
        _refreshRows();
    }

    @Override
    public void setData(RecordList records) {
        final RecordList currentData = _getData();
        if (currentData != records) {
            if (currentData != null) {
                if (dataArrivedRegistration != null) {
                    dataArrivedRegistration.removeHandler();
                    dataArrivedRegistration = null;
                }
                if (dataChangedRegistration != null) {
                    dataChangedRegistration.removeHandler();
                    dataChangedRegistration = null;
                }
            }
            assert dataArrivedRegistration == null;
            assert dataChangedRegistration == null;
            super.setData(records);

            if (records instanceof RecordList) {
                if (records instanceof ResultSet) {
                    final ResultSet recordsResultSet = (ResultSet)records;
                    recordsResultSet.setFetchMode(getDataFetchMode());
                    dataArrivedRegistration = recordsResultSet.addDataArrivedHandler(new DataArrivedHandler() {
                        @Override
                        public void onDataArrived(DataArrivedEvent event) {
                            PickerDial.this.onDataArrived(event.getStartRow(), event.getEndRow());
                        }
                    });
                }
                dataChangedRegistration = ((RecordList)records).addDataChangedHandler(new DataChangedHandler() {
                    @Override
                    public void onDataChanged(DataChangedEvent event) {
                        PickerDial.this.onDataChanged();
                    }
                });
            } else assert !(records instanceof ResultSet);
        }

        _refreshRows();
    }

    @Override
    public void setDataSource(DataSource ds) {
        if (getDataSource() != ds) {
            super.setDataSource(ds);
        }
    }

    public final String _getEmptyDisplayValue() {
        return (emptyDisplayValue == null ? "" : emptyDisplayValue);
    }

    public void setEmptyDisplayValue(String emptyDisplayValue) {
        if (isAttached()) throw new IllegalStateException("Cannot change PickerDial.emptyDisplayValue while attached");
        this.emptyDisplayValue = emptyDisplayValue;
    }

    public void setFields(ListGridField... fields) {
        if (getFields() != fields) {
            super.setFields(fields);

            _refreshRows();
        }
    }

    public final String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    @Override
    protected void _createSelectionModel() {
        if (selectedSetRegistration != null) {
            selectedSetRegistration.removeHandler();
            selectedSetRegistration = null;
        }

        super._createSelectionModel();
        final Selection<Record> selection = _getSelectionObject();
        if (selection != null) {
            selectedSetRegistration = selection._addSelectedSetHandler(new SelectedSetHandler() {

                @Override
                public void _onSelectedSet(SelectedSetEvent event) {
                    if (event.getState()) {
                        onRecordSelected((Record)event.getSelectionItem());
                    }
                }
            });
        }
    }

    @Override
    protected void _destroySelectionModel() {
        if (selectedSetRegistration != null) {
            selectedSetRegistration.removeHandler();
            selectedSetRegistration = null;
        }
        super._destroySelectionModel();
    }

    @SGWTInternal
    String _formatCellValue(Record record, int rowNum, String fieldName) {
        final ListGridField[] fields = getFields();
        if (fields != null) {
            final ListGridField field = getField(fieldName);
            String value = field._formatCellValue(record.getAttributeAsObject(fieldName), record, rowNum, getFieldNum(fieldName), this);
            if (field._getEscapeHTML()) {
                value = SafeHtmlUtils.htmlEscape(value);
            }
            return value;
        }
        return record.getAttribute(fieldName);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        impl.onBrowserEvent(this, event);
    }

    private void onDataArrived(int start, int end) {
        _refreshRows();
    }

    private void onDataChanged() {
        _refreshRows();
    }

    private void onRecordSelected(Record record) {
        impl.onRecordSelected(this, record);
    }

    @SGWTInternal
    public void _refreshRows() {
        impl.refreshRows(this);
    }
}