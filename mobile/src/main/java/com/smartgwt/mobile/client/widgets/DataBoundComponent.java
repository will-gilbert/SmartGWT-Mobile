/*
 * SmartGWT Mobile
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * SmartGWT Mobile is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT Mobile is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package com.smartgwt.mobile.client.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.Criteria;
import com.smartgwt.mobile.client.data.DSCallback;
import com.smartgwt.mobile.client.data.DSRequest;
import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.data.ResultSet;
import com.smartgwt.mobile.client.data.SortSpecifier;
import com.smartgwt.mobile.client.data.events.HasDataChangedHandlers;
import com.smartgwt.mobile.client.internal.Array;
import com.smartgwt.mobile.client.internal.HasReferenceCount;
import com.smartgwt.mobile.client.internal.RecordSelection;
import com.smartgwt.mobile.client.internal.Selection;
import com.smartgwt.mobile.client.internal.data.HasCriteria;
import com.smartgwt.mobile.client.internal.data.HasRecordCanSelectPropertyAttribute;
import com.smartgwt.mobile.client.internal.data.HasSortSpecifiers;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.widgets.ValidationOptions;
import com.smartgwt.mobile.client.internal.widgets.ValidationResult;
import com.smartgwt.mobile.client.types.DSOperationType;
import com.smartgwt.mobile.client.types.FetchMode;
import com.smartgwt.mobile.client.types.TextMatchStyle;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.form.fields.HasDataValue;
import com.smartgwt.mobile.client.widgets.form.validator.Validator;
import com.smartgwt.mobile.client.widgets.grid.events.HasSelectionUpdatedHandlers;
import com.smartgwt.mobile.client.widgets.grid.events.SelectionUpdatedEvent;
import com.smartgwt.mobile.client.widgets.grid.events.SelectionUpdatedHandler;
import com.smartgwt.mobile.client.widgets.layout.VLayout;

public abstract class DataBoundComponent<D extends List</*? extends */Record> & HasDataChangedHandlers> extends VLayout implements HasSelectionUpdatedHandlers, HasSortSpecifiers {

    private static final int DEFAULT_DATA_PAGE_SIZE = 25;

    private static final Set<String> PROPERTIES_TO_SKIP = new HashSet<String>();
    @SGWTInternal
    public static void _cloneComponentValues(@SuppressWarnings("rawtypes") DataBoundComponent component, Object storedValues, Object values, DataSource dataSource, String dataPath, List<String> defaultPaths) {
        if (values == null) return;
        final boolean getDefaults = (defaultPaths != null);

        Boolean dsDeepClone = (dataSource != null ? dataSource._getDeepCloneOnEdit() : null);
        if (dsDeepClone == null) dsDeepClone = component._getDeepCloneOnEdit();
        final boolean deepClone = (dsDeepClone == null ? DataSource._deepCloneOnEdit : dsDeepClone.booleanValue());

        if (Array.isArray(values)) values = Array.asList(values);
        if (values instanceof List) {
            assert storedValues instanceof List;
            final List<Object> list = (List<Object>)values,
                    storedList = (List<Object>)storedValues;
            final int list_size = list.size();
            for (int i = 0; i < list_size; ++i) {
                Object value = list.get(i);
                if (Array.isArray(value)) value = Array.asList(value);

                if (value == null || value instanceof CharSequence || value instanceof Character ||
                    value instanceof Boolean || value instanceof Number)
                {
                    if (storedList.size() <= i) storedList.add(value);
                    else storedList.set(i, value);
                } else if (value instanceof Date) {
                    value = ((Date)value).clone();
                    if (storedList.size() <= i) storedList.add(value);
                    else storedList.set(i, value);
                } else if (value instanceof List) {
                    final Object targetObj = new ArrayList<Object>();
                    _cloneComponentValues(component, targetObj, value, dataSource, (getDefaults ? dataPath : null), defaultPaths);
                    if (storedList.size() <= i) storedList.add(targetObj);
                    else storedList.set(i, targetObj);
                } else if (value instanceof Map) {
                    final Object targetObj = new HashMap<Object, Object>();
                    _cloneComponentValues(component, targetObj, value, dataSource, (getDefaults ? dataPath : null), defaultPaths);
                    if (storedList.size() <= i) storedList.add(targetObj);
                    else storedList.set(i, targetObj);
                }
            }
            return;
        }

        assert values instanceof Map;
        final Map<String, Object> map = (Map<String, Object>)values;
        assert storedValues instanceof Map;
        final Map<String, Object> storedMap = (Map<String, Object>)storedValues;
        for (final Map.Entry<String, Object> e : map.entrySet()) {
            final String prop = e.getKey();
            if (PROPERTIES_TO_SKIP.contains(prop)) continue;

            final String fullDataPath;
            if (getDefaults) {
                fullDataPath = (dataPath != null ? dataPath : "") + prop;
                if (component instanceof DynamicForm) {
                    final Canvas item = ((DynamicForm)component).getItem(fullDataPath);
                    if (item instanceof FormItem && Canvas._booleanValue(((FormItem)item)._isSetToDefaultValue(), false)) {
                        defaultPaths.add(fullDataPath);
                    }
                }
            } else fullDataPath = null;

            Object propValue = e.getValue();
            if (Array.isArray(propValue)) propValue = Array.asList(propValue);
            if (propValue instanceof Date) {
                storedMap.put(prop, ((Date)propValue).clone());
            } else if (propValue instanceof Map) {
                // Shallow-clone objects that do not have a corresponding DataSourceField.
                final DataSourceField field = (dataSource != null ? dataSource.getField(prop) : null);
                if (field == null) {
                    storedMap.put(prop, propValue);
                } else {
                    // TODO SimpleType.duplicate(), DataSourceField.deepCloneOnEdit
                    if (deepClone) {
                        final Map<String, Object> copy = new HashMap<String, Object>();
                        _cloneComponentValues(component, copy, propValue, DataSource.getDataSource(field.getType()), (getDefaults ? (fullDataPath + "/") : null), defaultPaths);
                        storedMap.put(prop, copy);
                    } else {
                        storedMap.put(prop, propValue);
                    }
                }
            } else if (propValue instanceof List) {
                final DataSourceField field = (dataSource != null ? dataSource.getField(prop) : null);
                if (field == null) {
                    storedMap.put(prop, propValue);
                } else {
                    // TODO SimpleType.duplicate, DataSourceField.deepCloneOnEdit
                    if (deepClone) {
                        final List<Object> copy = new ArrayList<Object>();
                        _cloneComponentValues(component, copy, propValue, DataSource.getDataSource(field.getType()), (getDefaults ? (fullDataPath + "/") : null), defaultPaths);
                        storedMap.put(prop, copy);
                    } else {
                        storedMap.put(prop, propValue);
                    }
                }
            } else {
                storedMap.put(prop, propValue);
            }
        }
    }

    @SGWTInternal
    public static void _duplicateValues(@SuppressWarnings("rawtypes") DataBoundComponent component, Map values, Map targetVals, List<String> defaultPaths) {
        final DataSource ds = component.getDataSource();
        _cloneComponentValues(component, targetVals, values, ds, null, defaultPaths);
    }

    private FetchMode dataFetchMode = FetchMode.PAGED;
    private int dataPageSize = DEFAULT_DATA_PAGE_SIZE;
    private DataSource dataSource;
    private Boolean useAllDataSourceFields = Boolean.FALSE;
    private Criteria initialCriteria = null;
    private D data;
    private Boolean deepCloneOnEdit;
    private String titleField;
    private String infoField = "info";
    private String dataField;
    private String descriptionField = "description";
    private String iconField = "icon";
    private String fetchOperation;
    private Selection<Record> selection;
    @SGWTInternal
    protected SortSpecifier[] _sortSpecifiers;
    private Boolean stopOnError;

    @Override
    public void destroy() {
        _destroySelectionModel();
        if (data instanceof HasReferenceCount) {
            ((HasReferenceCount)data)._unref();
        }
        super.destroy();
    }

    public final FetchMode getDataFetchMode() {
        return dataFetchMode;
    }

    public void setDataFetchMode(FetchMode fetchMode) {
        this.dataFetchMode = fetchMode;
        if (data != null && data instanceof ResultSet) {
            ((ResultSet)data).setFetchMode(fetchMode);
        }
    }

    public final int getDataPageSize() {
        return dataPageSize;
    }

    public void setDataPageSize(Integer dataPageSize) {
        this.dataPageSize = dataPageSize == null ? DEFAULT_DATA_PAGE_SIZE : dataPageSize.intValue();
    }

    @Override
    public Object _getInnerAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration) {
        switch (configuration.getAttribute()) {
            case VALUE:
                if (locatorArray.size() == 1) {
                    final String substring = locatorArray.get(0);
                    if ("hasData".equals(substring)) return Boolean.valueOf(data != null && !data.isEmpty());
                } else if (locatorArray.size() == 2) {
                    String substring = locatorArray.get(0);
                    if ("data".equals(substring)) {
                        substring = locatorArray.get(1);
                        if ("length".equals(substring) || "size".equals(substring)) {
                            return (data == null ? null : Integer.valueOf(data.size()));
                        }
                    }
                }
                break;
            default:
                break;
        }
        return super._getInnerAttributeFromSplitLocator(locatorArray, configuration);
    }

    @SGWTInternal
    protected void _createSelectionModel() {
        if (selection != null) _destroySelectionModel();

        Selection<Record> selection = new RecordSelection(data);
        if (this instanceof HasRecordCanSelectPropertyAttribute) {
            selection.setCanSelectProperty(((HasRecordCanSelectPropertyAttribute)this).getRecordCanSelectProperty());
        }

        this.selection = selection;
    }

    @SGWTInternal
    protected void _destroySelectionModel() {
        if (selection == null) return;
        selection._destroy();
        selection = null;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public final DataSource getDataSource() {
        return dataSource;
    }

    public final Boolean getUseAllDataSourceFields() {
        return useAllDataSourceFields;
    }

    public void setUseAllDataSourceFields(Boolean useAllDataSourceFields) {
        this.useAllDataSourceFields = useAllDataSourceFields;
    }

    @SGWTInternal
    public Criteria _getInitialCriteria() {
        return initialCriteria;
    }

    public final Criteria getCriteria() {
        if (data == null || data.size() == 0) return new Criteria(initialCriteria);
        else if (data instanceof HasCriteria) return new Criteria(((HasCriteria)data).getCriteria());
        else return null;
    }

    public void setCriteria(Criteria criteria) {
        if (data instanceof HasCriteria) ((HasCriteria)data).setCriteria(criteria);
        else initialCriteria = criteria;
    }

    @SGWTInternal
    public final D _getData() {
        return data;
    }

    @SGWTInternal
    public final Boolean _getDeepCloneOnEdit() {
        return deepCloneOnEdit;
    }

    @SGWTInternal
    public void _setDeepCloneOnEdit(Boolean deepCloneOnEdit) {
        this.deepCloneOnEdit = deepCloneOnEdit;
    }

    public final RecordList getRecordList() {
        return (data instanceof RecordList ? (RecordList)data : null);
    }

    public final ResultSet getResultSet() {
        return (data instanceof ResultSet ? ((ResultSet)data) : null);
    }

    private static int nextNoNullOrDuplicateRecordsRunNum = 1;
    private static <D extends List</*? extends */Record>> boolean noNullOrDuplicateRecords(D data) {
        if (data != null && !data.isEmpty()) {
            final Integer thisNoNullOrDuplicateRecordsRunNum = Integer.valueOf(nextNoNullOrDuplicateRecordsRunNum++);
            final int data_size = data.size();
            for (int i = 0; i < data_size; ++i) {
                final Record record = data.get(i);
                assert record != null : "The Record at index " + i + " of the given RecordList is null.";
                final Integer noNullOrDuplicateRecordsRunNum = record.getAttributeAsInt("$noNullOrDuplicateRecordsRunNum");
                assert noNullOrDuplicateRecordsRunNum == null ||
                        !noNullOrDuplicateRecordsRunNum.equals(thisNoNullOrDuplicateRecordsRunNum) : "The Record at index " + i + " is contained in the RecordList two or more times (the exact same Record object). Each Record in the RecordList must be a distinct Object.";
                record.setAttribute("$noNullOrDuplicateRecordsRunNum", thisNoNullOrDuplicateRecordsRunNum);
            }
        }
        return true;
    }

    public void setData(D newData) {
        if (data != newData) {
            assert noNullOrDuplicateRecords(newData);
            _destroySelectionModel();
            if (data instanceof HasReferenceCount) {
                ((HasReferenceCount)data)._unref();
            }
            data = newData;
            if (newData instanceof HasReferenceCount) {
                ((HasReferenceCount)data)._addRef();
            }
            _createSelectionModel();
        }
    }

    public void setTitleField(String titleField) {
        this.titleField = titleField;
    }

    public final String getTitleField() {
        if (this.titleField != null) {
            return this.titleField;
        }
        if (dataSource != null) {
            return dataSource.getTitleField();
        }
        // Return default name for title attribute.
        return "title";
    }

    public void setInfoField(String infoField) {
        this.infoField = infoField;
    }

    public final String getInfoField() {
        if (infoField != null) {
            return infoField;
        }
        if (dataSource != null) {
            return dataSource.getInfoField();
        }
        return null;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    public final String getDataField() {
        if (dataField != null) {
            return dataField;
        }
        if (dataSource != null) {
            return dataSource.getDataField();
        }
        return null;
    }

    public void setDescriptionField(String descriptionField) {
        this.descriptionField = descriptionField;
    }

    public final String getDescriptionField() {
        if (descriptionField != null) {
            return descriptionField;
        }
        if (dataSource != null) {
            return dataSource.getDescriptionField();
        }
        return null;
    }

    /**
     * Sets the name of the field to use for the icon of a record. The value should be a
     * <code>String</code> containing the URI of the icon. Alternatively, the field value
     * may be an instance of {@link com.google.gwt.resources.client.ImageResource} or
     * {@link com.google.gwt.user.client.ui.Image}.
     * 
     * <p>In the case of {@link com.smartgwt.mobile.client.widgets.tableview.TableView}, this
     * only applies if {@link com.smartgwt.mobile.client.widgets.tableview.TableView#getShowIcons()
     * showIcons} is <code>true</code>.
     * 
     * @param iconField the name of the field containing the icon's URI.
     */
    public void setIconField(String iconField) {
        this.iconField = iconField;
    }

    public final String getIconField() {
        if (iconField != null) {
            return iconField;
        }
        if (dataSource != null) {
            return dataSource.getIconField();
        }
        return null;
    }

    @SGWTInternal
    protected final Selection<Record> _getSelectionObject() {
        return selection;
    }

    public SortSpecifier[] getSort() {
        return SortSpecifier._shallowClone(_sortSpecifiers);
    }

    public void setSort(SortSpecifier... sortSpecifiers) {
        _sortSpecifiers = SortSpecifier._shallowClone(sortSpecifiers);
        if (data != null && sortSpecifiers != null && sortSpecifiers.length > 0) {
            // TODO reset contexts
            if (data instanceof HasSortSpecifiers) ((HasSortSpecifiers)data).setSort(_sortSpecifiers);
            // else if (this.data.sortByProperty) // TODO
        }
    }

    public final Boolean getStopOnError() {
        return stopOnError;
    }

    public void setStopOnError(Boolean stopOnError) {
        this.stopOnError = stopOnError;
    }

    public boolean isSelected(Record record) {
        if (record == null || selection == null) return false;
        return selection.isSelected(record);
    }

    public boolean isPartiallySelected(Record record) {
        if (record == null || selection == null) return false;
        return selection.isPartiallySelected(record);
    }

    public final void selectRecord(Record record) {
        selectRecord(record, true);
    }

    public final void selectRecord(Record record, boolean state) {
        selectRecords(new Record[] { record }, state);
    }

    public final void selectSingleRecord(Record record) {
        if (selection == null) return;
        selection.deselectAll();
        selectRecord(record);
    }

    public final void deselectRecord(Record record) {
        selectRecord(record, false);
    }

    public final void selectRecords(Record[] records) {
        selectRecords(records, true);
    }

    public void selectRecords(Record[] records, boolean state) {
        if (selection != null) {
            selection.selectList(records, state);
            _fireSelectionUpdated();
        }
    }

    public final void deselectRecords(Record[] records) {
        selectRecords(records, false);
    }

    public void deselectAllRecords() {
        if (selection != null) {
            selection.deselectAll();
            _fireSelectionUpdated();
        }
    }

    @SGWTInternal
    protected void _fireSelectionUpdated() {
        SelectionUpdatedEvent.fire(this);
    }

    @SGWTInternal
    protected Record[] _getSelection(boolean excludePartialSelections) {
        if (selection == null) return null;
        return selection.getSelection(excludePartialSelections);
    }

    public Record[] getSelectedRecords() {
        return _getSelection(false);
    }

    public Record getSelectedRecord() {
        if (selection == null) return null;
        return selection.getSelectedRecord();
    }

    @SGWTInternal
    public DSRequest _buildRequest(DSRequest context, DSOperationType operationType, DSCallback callback) {
        if (context == null) context = new DSRequest();

        context._setAfterFlowCallback(callback);

        if (context.getOperationId() == null) {
            if (operationType == DSOperationType.FETCH) context.setOperationId(getFetchOperation());
        }

        context._setOperation(DataSource._makeDefaultOperation(getDataSource(), operationType, context.getOperationId()));
        return context;
    }

    public final String getFetchOperation() {
        return fetchOperation;
    }

    public void setFetchOperation(String fetchOperation) {
        this.fetchOperation = fetchOperation;
    }

    public final void fetchData() {
        fetchData(_getInitialCriteria());
    }

    public final void fetchData(Criteria criteria) {
        fetchData(criteria, null);
    }

    public final void fetchData(Criteria criteria, DSCallback callback) {
        fetchData(criteria, callback, null);
    }

    public void fetchData(Criteria criteria, DSCallback callback, DSRequest requestProperties) {
        if (requestProperties == null) requestProperties = new DSRequest();
        if (requestProperties.getTextMatchStyle() == null) requestProperties.setTextMatchStyle(TextMatchStyle.EXACT);
        _filter(DSOperationType.FETCH, criteria, callback, requestProperties);
    }

    public void invalidateCache() {
        if (data instanceof ResultSet) {
            ((ResultSet)data).invalidateCache();
        }
    }

    public void filterData() {
        if (dataSource != null) {
            if (dataSource.getRequestProperties() == null) {
                dataSource.setRequestProperties(new HashMap<String, Object>());
            }
            dataSource.getRequestProperties().put("textMatchStyle", TextMatchStyle.SUBSTRING);
            fetchData();            
        }
    }

    @SGWTInternal
    public void _filter(DSOperationType type, Criteria criteria, DSCallback callback, DSRequest requestProperties) {
        // TODO onFetchData()

        requestProperties = _buildRequest(requestProperties, type, callback);

        _filterWithCriteria(criteria, requestProperties, requestProperties);
    }

    @SGWTInternal
    public void _filterWithCriteria(Criteria criteria, Object operation, DSRequest context) {
        // TODO checkEmptyCriteria
        Criteria filterCriteria = criteria;

        D dataModel = data;

        if (dataModel instanceof ResultSet) {
            D updatedModel = _updateDataModel(filterCriteria, operation, context);
            if (updatedModel != null) dataModel = updatedModel;
        } else {
            dataModel = _createDataModel(filterCriteria, operation, context);
        }

        setData(dataModel);
    }

    @SGWTInternal
    public D _createDataModel(Criteria filterCriteria, Object operation, DSRequest context) {
        if (dataSource == null) {
            SC.logWarn("No DataSource or invalid DataSource specified, can't create data model");
            return null;
        }

        final ResultSet resultSet = new ResultSet(dataSource);
        resultSet._setContext(context);
        resultSet.setCriteria(filterCriteria);

        SortSpecifier[] sortSpecifiers = this.getSort();
        if (sortSpecifiers != null && sortSpecifiers.length > 0) {
            resultSet.setSort(sortSpecifiers);
        }

        return (D)resultSet;
    }

    @SGWTInternal
    public Boolean _resolveStopOnError(Boolean stopOnError, Boolean fieldStopOnError, Boolean formStopOnError) {
        if (stopOnError != null) return stopOnError;
        return ((fieldStopOnError == null &&
                 (formStopOnError != null && formStopOnError.booleanValue())) ||
                (fieldStopOnError != null && fieldStopOnError.booleanValue()) ||
                false);
    }

    @SGWTInternal
    public D _updateDataModel(Criteria filterCriteria, Object operation, DSRequest context) {
        if (!(data instanceof ResultSet)) return null;

        final ResultSet resultSet = (ResultSet)data;
        if (!resultSet.allRowsCached()) {
            resultSet.invalidateCache();
        }
        resultSet._setContext(context);
        resultSet.setCriteria(filterCriteria);
        return (D)resultSet;
    }

    @SGWTInternal
    public ValidationResult _validateField(HasDataValue field, Validator[] validators, Object value, Record record, ValidationOptions options) {
        if (validators == null) return null;

        final ArrayList<String> errors = new ArrayList<String>();
        boolean validated = false;
        Boolean stopOnError = null;
        final ValidationResult result = new ValidationResult();
        result.setValid(true);
        result.setResultingValue(null);

        // loop through validators
        for (final Validator validator : validators) {
            if (validator == null) continue;

            // TODO
        }

        // TODO Process server-side validators

        result.setStopOnError(!errors.isEmpty() &&
                              _resolveStopOnError(stopOnError, field.getStopOnError(), stopOnError));

        // Populate remainder of result object
        final Map<String, ArrayList<String>> resultErrors;
        if (errors.isEmpty()) resultErrors = null;
        else {
            resultErrors = new HashMap<String, ArrayList<String>>();
            resultErrors.put(field.getFieldName(), errors);
        }
        result.setErrors(resultErrors);
        result.setValid(errors.isEmpty());
        return (validated ? result : null);
    }

    @SGWTInternal
    public ValidationResult _validateFieldAndDependencies(HasDataValue field, Validator[] validators, Object newValue, Record record, ValidationOptions options) {
        final Map<String, ArrayList<String>> errors = new HashMap<String, ArrayList<String>>();
        boolean validated = false;
        final ValidationResult result = new ValidationResult();
        result.setValid(true);
        result.setResultingValue(null);

        // Apply newValue to record so that dependencies can reference it
        // If a validator changes newValue, the new value will overwrite this one.
        record.setAttribute(field.getFieldName(), newValue);

        // Process all validators for this field
        final ValidationResult fieldResult = _validateField(field, field.getValidators(), newValue, record, options);
        if (fieldResult != null) {
            // TODO
        }

        result.setErrors(errors);
        return (validated ? result : null);
    }

    @Override
    public HandlerRegistration addSelectionUpdatedHandler(SelectionUpdatedHandler handler) {
        return addHandler(handler, SelectionUpdatedEvent.getType());
    }
}
