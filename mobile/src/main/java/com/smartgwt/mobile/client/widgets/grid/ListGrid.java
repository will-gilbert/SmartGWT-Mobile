package com.smartgwt.mobile.client.widgets.grid;

import java.util.Arrays;
import java.util.List;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.internal.util.NativeIntMap;
import com.smartgwt.mobile.client.internal.util.NativeObjectMap;
import com.smartgwt.mobile.client.internal.widgets.form.fields.CanCopyFieldConfiguration;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.widgets.DataBoundComponent;
import com.smartgwt.mobile.client.widgets.form.DataManager;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.HasDataValue;
import com.smartgwt.mobile.client.widgets.form.validator.Validator;

/**
 * Placeholder for the Smart GWT <code>ListGrid</code> type.
 * 
 * <p><code>ListGrid</code> implements the {@link com.smartgwt.mobile.client.widgets.form.fields.HasDataValue}
 * interface, so a <code>ListGrid</code> instance may be used as a field within a
 * {@link com.smartgwt.mobile.client.widgets.form.DynamicForm}.
 * When being used as a field, this <code>ListGrid</code> edits the list of subrecords
 * in the {@link com.smartgwt.mobile.client.widgets.grid.ListGrid#getSubrecordsProperty() subrecordsProperty}
 * attribute of the {@link com.smartgwt.mobile.client.widgets.grid.ListGrid#getMainRecord() mainRecord}.
 * 
 * <p>Note that this class is not intended to be used directly. SmartGWT.mobile applications
 * are encouraged to use the {@link com.smartgwt.mobile.client.widgets.tableview.TableView}
 * class instead.
 */
public class ListGrid extends DataBoundComponent<RecordList> implements HasDataValue, CanCopyFieldConfiguration {

    private String baseStyle;
    private Boolean canEdit;
    private DataManager dataManager;
    private String fieldName = "_grid";
    private ListGridField[] fields;
    private transient NativeObjectMap<ListGridField> fieldsByName;
    @SGWTInternal
    protected transient NativeIntMap fieldNums_;
    private Record mainRecord = null;
    private String recordBaseStyleProperty;
    private SelectionStyle selectionType;
    private Boolean showTitle;
    private Boolean stopOnError;
    private String subrecordsProperty = fieldName;
    private String title;
    private Validator[] validators;

    public ListGrid() {}

    public ListGrid(String fieldName) {
        this();
        this.fieldName = fieldName;
    }

    public ListGrid(String fieldName, String title) {
        this(fieldName);
        this.title = title;
    }

    public final String getBaseStyle() {
        return baseStyle;
    }

    public void setBaseStyle(String baseStyle) {
        this.baseStyle = baseStyle;
    }

    protected String getBaseStyle(Record record, int rowNum, int colNum) {
        final String recordBaseStyleProperty = _getRecordBaseStyleProperty();
        String ret = null;
        if (record != null && recordBaseStyleProperty != null) {
            ret = record.getAttribute(recordBaseStyleProperty);
        }
        if (ret == null && fields != null && 0 <= colNum && colNum < fields.length) {
            ret = fields[colNum].getBaseStyle();
        }
        if (ret == null) ret = getBaseStyle();
        return ret;
    }

    @Override
    public final Boolean getCanEdit() {
        return canEdit;
    }

    /**
     * Setter for {@link #getCanEdit() canEdit}.
     */
    @Override
    public void setCanEdit(Boolean canEdit) {
        if ((this.canEdit == null && canEdit != null) ||
            (this.canEdit != null && (canEdit == null ||
                                      this.canEdit.booleanValue() != canEdit.booleanValue())))
        {
            this.canEdit = canEdit;
            updateCanEdit();
        }
    }

    public final DataManager getDataManager() {
        return dataManager;
    }

    @Override
    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
        if (dataManager != null) {
            updateCanEdit();
            final Record[] value = getFieldValue();
            dataManager.storeValue(fieldName, value);
            showValue(value);
        }
    }

    public final ListGridField getField(String fieldName) {
        if (fields == null) return null;
        if (fieldsByName == null) {
            fieldsByName = NativeObjectMap.create();
            for (final ListGridField field : fields) {
                fieldsByName.put(field.getName(), field);
            }
        }
        return fieldsByName.get(fieldName);
    }

    /**
     * Returns the name of this <code>ListGrid</code> when used as a field in a
     * {@link com.smartgwt.mobile.client.widgets.form.DynamicForm}.
     * 
     * @return this field's name. Default value: "_grid"
     */
    @Override
    public final String getFieldName() {
        return fieldName;
    }

    /**
     * Sets the field name returned by the {@link com.smartgwt.mobile.client.widgets.form.fields.HasDataValue#getFieldName()}
     * API.
     * 
     * @param fieldName the name of this <code>ListGrid</code> when used as a field in a
     * {@link com.smartgwt.mobile.client.widgets.form.DynamicForm}.
     */
    public void setFieldName(String fieldName) {
        if (dataManager != null) throw new IllegalStateException("The field name cannot be changed while a `DataManager' is set.");
        this.fieldName = fieldName;
    }

    public String getFieldName(int colNum) {
        if (fields == null || colNum < 0 || fields.length <= colNum) return null;
        return fields[colNum].getName();
    }

    public final int getFieldNum(String fieldName) {
        if (fields == null) return -1;
        if (fieldNums_ == null) {
            fieldNums_ = NativeIntMap.create();
            for (int i = 0; i < fields.length; ++i) {
                fieldNums_.put(fields[i].getName(), i);
            }
        }
        return fieldNums_.get(fieldName, -1);
    }

    public final ListGridField[] getFields() {
        return fields;
    }

    public void setFields(ListGridField... fields) {
        this.fields = fields;
        fieldsByName = null;
        fieldNums_ = null;
    }

    /**
     * When this <code>ListGrid</code> is being used as a field within a
     * {@link com.smartgwt.mobile.client.widgets.form.DynamicForm}, the main <code>Record</code>
     * containing the subrecords that this <code>ListGrid</code> is editing.
     * 
     * @return the main record.
     */
    public final Record getMainRecord() {
        return mainRecord;
    }

    /**
     * Setter for {@link #getMainRecord() mainRecord}.
     * @param mainRecord the main record.
     */
    public void setMainRecord(Record mainRecord) {
        this.mainRecord = mainRecord;
        if (dataManager != null) dataManager.storeValue(fieldName, getFieldValue());
    }

    public final String getRecordBaseStyleProperty() {
        return recordBaseStyleProperty;
    }

    @SGWTInternal
    public final String _getRecordBaseStyleProperty() {
        String ret = getRecordBaseStyleProperty();
        return (ret == null ? "_baseStyle" : ret);
    }

    public void setRecordBaseStyleProperty(String recordBaseStyleProperty) {
        this.recordBaseStyleProperty = recordBaseStyleProperty;
    }

    public final SelectionStyle getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(SelectionStyle selectionType) {
        if (this.selectionType == selectionType) {
            return;
        }
        this.selectionType = selectionType;
        if (selectionType == null || selectionType == SelectionStyle.NONE) {
            deselectAllRecords();
        } else if (selectionType == SelectionStyle.SINGLE) {
            selectSingleRecord(getSelectedRecord());
        }
    }

    @Override
    public final Boolean getShowTitle() {
        return showTitle == null ? Boolean.valueOf(title != null) : showTitle;
    }

    /**
     * Setter for {@link #getShowTitle() HasDataValue.showTitle}.
     * 
     * @param showTitle if <code>null</code> or {@link java.lang.Boolean#TRUE}, show the
     * {@link #getTitle()} title for this <code>ListGrid</code> being used as a field in a
     * {@link com.smartgwt.mobile.client.widgets.form.DynamicForm}.
     */
    public void setShowTitle(Boolean showTitle) {
        this.showTitle = showTitle;
    }

    /**
     * Returns the title of this <code>ListGrid</code> when used as a field in a
     * {@link com.smartgwt.mobile.client.widgets.form.DynamicForm}.
     * 
     * @return this field's title.
     */
    @Override
    public final String getTitle() {
        return title;
    }

    /**
     * Sets the field title returned by the {@link com.smartgwt.mobile.client.widgets.form.fields.HasDataValue#getTitle()}
     * API.
     * 
     * @param title the title of this <code>ListGrid</code> when used as a field in a
     * {@link com.smartgwt.mobile.client.widgets.form.DynamicForm}.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * When this <code>ListGrid</code> is being used as a field within a
     * {@link com.smartgwt.mobile.client.widgets.form.DynamicForm}, the name of the subrecords property
     * of the {@link #getMainRecord() mainRecord} that this <code>ListGrid</code> is editing.
     * 
     * @return the subrecords property of the <code>mainRecord</code>. Default value: "_grid"
     */
    public final String getSubrecordsProperty() {
        return subrecordsProperty;
    }

    public void setSubrecordsProperty(String subrecordsProperty) {
        if (subrecordsProperty == null) throw new NullPointerException("`subrecordsProperty' cannot be `null'.");
        if (!this.subrecordsProperty.equals(subrecordsProperty)) {
            this.subrecordsProperty = subrecordsProperty;
            if (dataManager != null) {
                final Record[] value = getFieldValue();
                dataManager.storeValue(fieldName, value);
                showValue(value);
            }
        }
    }

    public Record[] getFieldValue() {
        if (mainRecord != null) {
            final Object subrecords = mainRecord.getAttributeAsObject(subrecordsProperty);
            if (subrecords instanceof Record[]) {
                return (Record[])subrecords;
            } else if (subrecords instanceof List) {
                final List<?> subrecordsList = (List<?>)subrecords;
                return subrecordsList.toArray(new Record[subrecordsList.size()]);
            } else if (subrecords instanceof Record) {
                return new Record[] { (Record)subrecords };
            }
        }
        return null;
    }

    @Override
    public final Validator[] getValidators() {
        return validators;
    }

    public void setValidators(Validator... validators) {
        this.validators = validators;
    }

    @Override
    public boolean compareValues(Object value1, Object value2) {
        return DynamicForm._compareValues(value1, value2);
    }

    @Override
    public void _copyFieldConfiguration(DataSourceField field) {
        if (title == null) setTitle(field.getTitle());
        if (showTitle == null) setShowTitle(field.getShowTitle());
        if (canEdit == null) setCanEdit(field.getCanEdit());
        if (subrecordsProperty == null) setSubrecordsProperty(field.getSubrecordsProperty());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void showValue(Object value) {
        final RecordList data = new RecordList();
        if (value instanceof Record[]) data.addAll(Arrays.asList((Record[])value));
        else if (value instanceof List) data.addAll((List<? extends Record>)value);
        else if (value instanceof Record) data.add((Record)value);
        setData(data);
        if (mainRecord != null) {
            mainRecord.setAttribute(subrecordsProperty, value);
        }
    }

    @Override
    public void updateCanEdit() {}
}
