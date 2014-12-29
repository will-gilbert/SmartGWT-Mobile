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
 
package com.smartgwt.mobile.client.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.Header;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.xml.client.Attr;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.events.ErrorEvent;
import com.smartgwt.mobile.client.data.events.HandleErrorHandler;
import com.smartgwt.mobile.client.data.events.HasHandleErrorHandlers;
import com.smartgwt.mobile.client.internal.Array;
import com.smartgwt.mobile.client.internal.data.CanFormatDateTime;
import com.smartgwt.mobile.client.internal.data.events.DSDataChangedEvent;
import com.smartgwt.mobile.client.internal.data.events.DSDataChangedHandler;
import com.smartgwt.mobile.client.internal.data.events.HasDSDataChangedHandlers;
import com.smartgwt.mobile.client.internal.util.HTTPHeadersMap;
import com.smartgwt.mobile.client.internal.util.URIBuilder;
import com.smartgwt.mobile.client.internal.util.XMLUtil;
import com.smartgwt.mobile.client.json.JSONUtils;
import com.smartgwt.mobile.client.rpc.RPCManager;
import com.smartgwt.mobile.client.rpc.RPCResponse;
import com.smartgwt.mobile.client.types.DSDataFormat;
import com.smartgwt.mobile.client.types.DSOperationType;
import com.smartgwt.mobile.client.types.DSProtocol;
import com.smartgwt.mobile.client.types.TextMatchStyle;
import com.smartgwt.mobile.client.util.JSOHelper;
import com.smartgwt.mobile.client.util.Page;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.Canvas;

//import com.allen_sauer.gwt.log.client.Log;

/**
 * A minimal, standalone implementation of a SmartClient DataSource that
 * communicates with the server using plain REST/HTTP messages and allows a pure GWT client to 
 * make use of the SmartClient Server without requiring any other client-side elements of 
 * SmartClient or SmartGWT.  It is intended for applications that need to be as lightweight as 
 * possible, such as those intended to run on mobile phones and other limited devices
 */
public class DataSource implements HasDSDataChangedHandlers, HasHandleErrorHandlers {

    @SGWTInternal
    public static boolean _deepCloneOnEdit = true;

    // Path to the DataSourceLoader servlet, which we'll use to get a shared DataSource config
    // from the server
    private static String dsLoaderUrl;

    @SGWTInternal
    public static int _numDSRequestsSent = 0;

    @SGWTInternal
    public static boolean _serializeTimeAsDatetime = false;

    @SGWTInternal
    public static final TimeZone _UTC = TimeZone.createTimeZone(0);

    private static RequestBuilder.Method getHttpMethod(String methodName) {
        if (methodName == null) return null;
        if ("DELETE".equals(methodName)) return RequestBuilder.DELETE;
        else if ("GET".equals(methodName)) return RequestBuilder.GET;
        else if ("HEAD".equals(methodName)) return RequestBuilder.HEAD;
        else if ("POST".equals(methodName)) return RequestBuilder.POST;
        else if ("PUT".equals(methodName)) return RequestBuilder.PUT;
        return null;
    }

    @SGWTInternal
    public static OperationBinding _makeDefaultOperation(DataSource dataSource, DSOperationType operationType, String operationId) {
        if (dataSource != null) {
            OperationBinding operation = dataSource.getOperationBinding(operationType, operationId);
            if (operation == null) {
                operation = new OperationBinding(operationType, operationId);
            }
            return operation;
        }

        return null;
    }

    static {
        setLoaderUrl("[ISOMORPHIC]/DataSourceLoader");
    }

    private HandlerManager handlerManager = null;

    private Map<String, Object> attributes = new HashMap<String, Object>();

    void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }
    <MK, MV> Map<MK, MV> getAttributeAsMap(String key) {
        @SuppressWarnings("unchecked")
        Map<MK, MV> ret = (Map<MK, MV>)attributes.get(key);
        return ret;
    }

    public DataSource() {
        attributes.put("sendMetaData", Boolean.TRUE);
        attributes.put("metaDataPrefix", "_");
        attributes.put("jsonPrefix", "<SCRIPT>//'\"]]>>isc_JSONResponseStart>>");
        attributes.put("jsonSuffix", "//isc_JSONResponseEnd");
    }

    public DataSource(String ID) {
        this();
        attributes.put("ID", ID);
        _register(ID, this);
    }

    public void setID(String id)  {
        attributes.put("ID", id);
    }

    public final String getID()  {
        return (String)attributes.get("ID");
    }

    public void setAutoDeriveSchema(Boolean autoDeriveSchema)  {
        setAttribute("autoDeriveSchema", autoDeriveSchema);
    }

    public Boolean getAutoDeriveSchema()  {
        return (Boolean)attributes.get("autoDeriveSchema");
    }

    public void setCallbackParam(String callbackParam) {
        setAttribute("callbackParam", callbackParam);
    }

    public String getCallbackParam()  {
        return (String)attributes.get("callbackParam");
    }

    public void setCriteriaPolicy(String criteriaPolicy) {
        setAttribute("criteriaPolicy", criteriaPolicy);
    }

    public String getCriteriaPolicy()  {
        return (String)attributes.get("criteriaPolicy");
    }

    public final DSProtocol getDataProtocol() {
        return (DSProtocol)attributes.get("dataProtocol");
    }

    public void setDataProtocol(DSProtocol dataProtocol) {
        attributes.put("dataProtocol", dataProtocol);
    }

    public void setDataURL(String dataURL) {
        attributes.put("dataURL", dataURL);
    }

    public final String getDataURL() {
        return (String)attributes.get("dataURL");
    }

    @SGWTInternal
    public final Boolean _getDeepCloneOnEdit() {
        return (Boolean)attributes.get("deepCloneOnEdit");
    }

    @SGWTInternal
    public void _setDeepCloneOnEdit(Boolean deepCloneOnEdit) {
        attributes.put("deepCloneOnEdit", deepCloneOnEdit);
    }

    public void setFetchDataURL(String dataURL) {
        setAttribute("fetchDataURL", dataURL);
    }
    
    public String getFetchDataURL()  {
        return (String)attributes.get("fetchDataURL");
    }

    public void setAddDataURL(String dataURL) {
        setAttribute("addDataURL", dataURL);
    }
    
    public String getAddDataURL()  {
        return (String)attributes.get("addDataURL");
    }

    public void setUpdateDataURL(String dataURL) {
        setAttribute("updateDataURL", dataURL);
    }
    
    public String getUpdateDataURL()  {
        return (String)attributes.get("updateDataURL");
    }

    public void setRemoveDataURL(String dataURL) {
        setAttribute("removeDataURL", dataURL);
    }
    
    public String getRemoveDataURL()  {
        return (String)attributes.get("removeDataURL");
    }
    
    public String getValidateDataURL()  {
        return (String)attributes.get("validateDataURL");
    }
    public void setValidateDataURL(String dataURL) {
        attributes.put("validateDataURL", dataURL);
    }

    public String getCustomDataURL()  {
        return (String)attributes.get("customDataURL");
    }

    public void setCustomDataURL(String dataURL) {
        attributes.put("customDataURL", dataURL);
    }

    public final String getDataTagName() {
        return (String)attributes.get("dataTagName");
    }

    @SGWTInternal
    public final String _getDataTagName() {
        if (attributes.containsKey("dataTagName")) return (String)attributes.get("dataTagName");
        return "data";
    }

    public void setDataTagName(String dataTagName) {
        attributes.put("dataTagName", dataTagName);
    }

    public void setDropExtraFields(Boolean dropExtraFields) {
        setAttribute("dropExtraFields", dropExtraFields);
    }

    public Boolean getDropExtraFields()  {
        return (Boolean)attributes.get("dropExtraFields");
    }

    public void setIconField(String iconField) {
        setAttribute("iconField", iconField);
    }

    public String getIconField()  {
        return (String)attributes.get("iconField");
    }
    public void setDataField(String dataField) {
        setAttribute("dataField", dataField);
    }

    public String getDataField()  {
        return (String)attributes.get("dataField");
    }

    public void setJsonPrefix(String jsonPrefix) {
        setAttribute("jsonPrefix", jsonPrefix);
    }

    public final String getJsonPrefix()  {
        return (String)attributes.get("jsonPrefix");
    }

    public void setJsonSuffix(String jsonSuffix) {
        setAttribute("jsonSuffix", jsonSuffix);
    }

    public final String getJsonSuffix()  {
        return (String)attributes.get("jsonSuffix");
    }

    public void setPluralTitle(String pluralTitle) {
        setAttribute("pluralTitle", pluralTitle);
    }

    public String getPluralTitle()  {
        return (String)attributes.get("pluralTitle");
    }

    public void setPreventHTTPCaching(Boolean preventHTTPCaching) {
        setAttribute("preventHTTPCaching", preventHTTPCaching);
    }

    public Boolean getPreventHTTPCaching()  {
        return (Boolean)attributes.get("preventHTTPCaching");
    }

    public void setQualifyColumnNames(Boolean qualifyColumnNames) {
        setAttribute("qualifyColumnNames", qualifyColumnNames);
    }

    public Boolean getQualifyColumnNames()  {
        return (Boolean)attributes.get("qualifyColumnNames");
    }

    public final String getRecordName() {
        return (String)attributes.get("recordName");
    }

    @SGWTInternal
    public final String _getRecordName() {
        String ret = getRecordName();
        if (ret == null) ret = "record";
        return ret;
    }

    public void setRecordName(String recordName) {
        attributes.put("recordName", recordName);
    }

    public void setRequiredMessage(String requiredMessage) {
        setAttribute("requiredMessage", requiredMessage);
    }

    public String getRequiredMessage()  {
        return (String)attributes.get("requiredMessage");
    }

    public void setSendExtraFields(Boolean sendExtraFields) {
        setAttribute("sendExtraFields", sendExtraFields);
    }

    public Boolean getSendExtraFields()  {
        return (Boolean)attributes.get("sendExtraFields");
    }

    public void setServerConstructor(String serverConstructor) {
        setAttribute("serverConstructor", serverConstructor);
    }

    public String getServerConstructor()  {
        return (String)attributes.get("serverConstructor");
    }


    public void setShowLocalFieldsOnly(Boolean showLocalFieldsOnly) {
        setAttribute("showLocalFieldsOnly", showLocalFieldsOnly);
    }

    public Boolean getShowLocalFieldsOnly()  {
        return (Boolean)attributes.get("showLocalFieldsOnly");
    }

    public void setShowPrompt(Boolean showPrompt) {
        attributes.put("showPrompt", showPrompt);
    }

    public Boolean getShowPrompt()  {
        return (Boolean)attributes.get("showPrompt");
    }

    public void setStrictSQLFiltering(Boolean strictSQLFiltering) {
        setAttribute("strictSQLFiltering", strictSQLFiltering);
    }

    public Boolean getStrictSQLFiltering()  {
        return (Boolean)attributes.get("strictSQLFiltering");
    }

    public void setTitle(String title) {
        setAttribute("title", title);
    }

    public String getTitle()  {
        return (String)attributes.get("title");
    }

    public void setTitleField(String titleField) {
        setAttribute("titleField", titleField);
    }

    public String getTitleField()  {
        return (String)attributes.get("titleField");
    }
    
    public void setInfoField(String infoField) {
        setAttribute("infoField", infoField);
    }

    public String getInfoField()  {
        return (String)attributes.get("infoField");
    }
    
    public void setDescriptionField(String descriptionField) {
        setAttribute("descriptionField", descriptionField);
    }

    public String getDescriptionField()  {
        return (String)attributes.get("descriptionField");
    }

    public void setInheritsFrom(DataSource parentDataSource) {
        setAttribute("inheritsFrom", parentDataSource.getID());
    }

    public void setInheritsFrom(String parentDataSourceID) {
        setAttribute("inheritsFrom", parentDataSourceID);
    }

    public String getInheritsFrom() {
        return (String)attributes.get("inheritsFrom");
    }

    public void setUseFlatFields(Boolean useFlatFields) {
        setAttribute("useFlatFields", useFlatFields);
    }

    public Boolean getUseFlatFields()  {
        return (Boolean)attributes.get("useFlatFields");
    }

    public void setUseParentFieldOrder(Boolean useParentFieldOrder) {
        setAttribute("useParentFieldOrder", useParentFieldOrder);
    }

    public Boolean getUseParentFieldOrder()  {
        return (Boolean)attributes.get("useParentFieldOrder");
    }

    public void setValidateRelatedRecords(Boolean validateRelatedRecords) {
        setAttribute("validateRelatedRecords", validateRelatedRecords);
    }

    public Boolean getValidateRelatedRecords()  {
        return (Boolean)attributes.get("validateRelatedRecords");
    }

    public void setRequestProperties(Map<String, Object> requestProperties) {
        setAttribute("requestProperties", requestProperties);
    }

    public Map<String, Object> getRequestProperties() {
        return getAttributeAsMap("requestProperties");
    }

    /**
     * Controls whether this <code>DataSource</code> will strictly conform to the
     * <a href="http://json.org/">JSON data format</a> when serializing JSON.
     * 
     * <p>If this attribute is not <code>true</code>, then JSON data generated for data source
     * requests will be in a more relaxed JSON format in the sense that only <code>'\b'</code>,
     * <code>'\t'</code>, <code>'\n'</code>, <code>'\f'</code>, <code>'\r'</code>, <code>'"'</code>,
     * and <code>'\\'</code> are escaped within string literals. If this attribute is <code>true</code>,
     * then all Unicode control characters (those having the Cc character property), <code>'"'</code>,
     * and <code>'\\'</code> are escaped.
     * 
     * <p>The effect of this attribute is not the same as Smart GWT's DataSource.useStrictJSON.
     * SGWT DataSource.useStrictJSON controls the <em>server</em>'s conformance to the JSON format
     * whereas SGWT.mobile DataSource.strictJSON controls the <em>client</em>'s conformance.
     * SGWT.mobile always requires strict JSON from the server, and will send useStrictJSON=true
     * with requests.
     * 
     * @return whether strict JSON is used. Default value: <code>false</code>
     * @see <a href="http://www.fileformat.info/info/unicode/category/Cc/list.htm">Unicode Characters in the 'Other, Control' Category</a>
     */
    public final Boolean getStrictJSON() {
        return (Boolean)attributes.get("strictJSON");
    }

    public void setStrictJSON(Boolean strictJSON) {
        attributes.put("strictJSON", strictJSON);
    }

    // ********************* Methods ***********************

    private boolean hasSuperDS() {
        return getInheritsFrom() != null;
    }

    private DataSource superDS() {
        String id = getInheritsFrom();
        return (id != null ? getDataSource(id) : null);
    }

    private LinkedHashMap<String, DataSourceField> fields = new LinkedHashMap<String, DataSourceField>();

    private LinkedHashMap<String, DataSourceField> getLocalFields() {
        return fields;
    }

    private LinkedHashMap<String, DataSourceField> mergedFields = null;

    private static boolean fromBoolean(boolean defaultValue, Boolean flag) {
        return (flag == null ? defaultValue : flag.booleanValue());
    }

    @SGWTInternal
    public Map<String, DataSourceField> _getMergedFields() {
        if (mergedFields != null) {
            return mergedFields;
        }

        if (!hasSuperDS() || this == superDS()) {
            return (mergedFields = getLocalFields());
        }

        DataSource superDS = superDS();
        if (superDS == null) {
            SC.logWarn("DataSource " + getID() + " inheritsFrom " + getInheritsFrom() +
                    ", but there is no DataSource of that name currently loaded. " +
                    "Ignoring the inheritsFrom declaration.");
            return (mergedFields = getLocalFields());
        }

        mergedFields = new LinkedHashMap<String, DataSourceField>();

        boolean showLocalFieldsOnly = fromBoolean(false, getShowLocalFieldsOnly());
        if (showLocalFieldsOnly) {
            setUseParentFieldOrder(false);
        }
        boolean useParentFieldOrder = fromBoolean(false, getUseParentFieldOrder());
        boolean autoDeriveSchema = fromBoolean(false, getAutoDeriveSchema());

        LinkedHashMap<String, DataSourceField> localFields = getLocalFields(),
            superFields = (LinkedHashMap<String, DataSourceField>)superDS._getMergedFields();

        // Define a boolean flag to use to run the inner loop twice.
        boolean flag = false;
        do {
            // If useParentFieldOrder is true then iterate over the parent data source's fields first,
            // then the local fields.
            boolean areSuperFields = flag ^ useParentFieldOrder;
            for (DataSourceField field : (areSuperFields ? superFields : localFields).values()) {
                String fieldName = field.getName();
                if (mergedFields.containsKey(fieldName)) {
                    continue; // The field was already added.
                }

                DataSourceField localField, superField, mergedField;
                if (areSuperFields) {
                    localField = localFields.get(fieldName);
                    superField = field;
                } else {
                    localField = field;
                    superField = superFields.get(fieldName);
                }

                if (localField != null && superField != null) {
                    mergedField = DataSourceField.combineFieldData(localField, superField);
                    if (superField.isHidden() && !localField.isHidden() &&
                        !fromBoolean(false, localField.isInapplicable()) && !autoDeriveSchema)
                    {
                        mergedField.setHidden(false);
                    }
                } else if (superField != null) {
                    mergedField = new DataSourceField(superField);
                    if (showLocalFieldsOnly) {
                        mergedField.setHidden(true);
                    }
                } else {
                    mergedField = localField;
                }
                mergedFields.put(mergedField.getName(), mergedField);
            }
            flag = !flag;
        } while (flag);

        return mergedFields;
    }

    /**
     * Return the field definition object.
     * @param fieldName Name of the field to retrieve
     *
     * @return DataSourceField field object
     */
    public DataSourceField getField(String fieldName) {
        return _getMergedFields().get(fieldName);
    }

    /**
     * Returns a pointer to the primaryKey field for this DataSource
     *
     * @return primary key field object
     */
    public DataSourceField getPrimaryKeyField() {
        return getPrimaryKeyField(_getMergedFields());
    }

    private static DataSourceField getPrimaryKeyField(Map<String, DataSourceField> fields) {
        for (DataSourceField field : fields.values()) {
            Boolean isPrimaryKey = field.isPrimaryKey();
            if (isPrimaryKey != null && isPrimaryKey.booleanValue()) {
                return field;
            }
        }
        return null;
    }

    /**
     * Returns the primary key fieldName for this DataSource
     *
     * @return primary key field name
     */
    public String getPrimaryKeyFieldName() {
        DataSourceField field = getPrimaryKeyField();
        return field == null ? null : field.getName();
    }

    // NOTE: For now, this method never returns a negative result (which would indicate more
    // restrictive criteria, and no need for a server visit, if the criteriaPolicy is
    // DROP_ON_SHORTENING).  We return 0 if the criteria are the same, and 1 otherwise
    protected int compareCriteria(Criteria newCriteria, Criteria oldCriteria) {
        if (oldCriteria == null) {
            if (newCriteria == null) return 0;
            else return 1;
        } else if (newCriteria == null) {
            return 1;
        }

        final Map<String, Object> oldCriteriaValues = oldCriteria.getValues(),
                newCriteriaValues = newCriteria.getValues();
        if (oldCriteriaValues.size() != newCriteriaValues.size()) return 1;
        for (Map.Entry<String, Object> e : oldCriteriaValues.entrySet()) {
            final String key = e.getKey();
            final Object oldValue = e.getValue();
            final Object newValue = newCriteriaValues.get(key);
            if (oldValue != null && !oldValue.equals(newValue)) {
                return 1;
            }
        }
        return 0;
    }

    public void performCustomOperation(String operationId, Record data, DSCallback callback, DSRequest props) {
        if (props == null) props = new DSRequest();
        props.setOperationId(operationId);
        performDSOperation(DSOperationType.CUSTOM, data, callback, props);
    }

            
    public void validateData(Record data, DSCallback callback) {
        performDSOperation(DSOperationType.VALIDATE, data, callback, null);
    }

    public void fetchData() {
        performDSOperation(DSOperationType.FETCH, null, null, null);
    }

    public void fetchData(Criteria criteria) {
        performDSOperation(DSOperationType.FETCH, criteria, null, null);
    }

    public void fetchData(Criteria criteria, DSCallback callback) {
        performDSOperation(DSOperationType.FETCH, criteria, callback, null);
    }

    public void fetchData(Criteria criteria, DSCallback callback, DSRequest props) {
        performDSOperation(DSOperationType.FETCH, criteria, callback, props);
    }

    public void addData(Record record, DSCallback callback, DSRequest props) {
        performDSOperation(DSOperationType.ADD, record, callback, props);
    }

    public void addData(Record record, DSCallback callback) {
        performDSOperation(DSOperationType.ADD, record, callback, null);
    }

    public void addData(Record record) {
        performDSOperation(DSOperationType.ADD, record, null, null);
    }

    public void updateData(Record record, DSCallback callback, DSRequest props) {
        performDSOperation(DSOperationType.UPDATE, record, callback, props);
    }

    public void updateData(Record record, DSCallback callback) {
        performDSOperation(DSOperationType.UPDATE, record, callback, null);
    }

    public void updateData(Record record) {
        performDSOperation(DSOperationType.UPDATE, record, null, null);
    }

    public void removeData(Record record, DSCallback callback, DSRequest props) {
        performDSOperation(DSOperationType.REMOVE, record, callback, props);
    }

    public void removeData(Record record, DSCallback callback) {
        performDSOperation(DSOperationType.REMOVE, record, callback, null);
    }

    public void removeData(Record record) {
        performDSOperation(DSOperationType.REMOVE, record, null, null);
    }

    private void performDSOperation(DSOperationType opType, Record data, DSCallback callback,
                    DSRequest props) 
    {
        // Form a DSRequest
        final DSRequest dsRequest = new DSRequest();
        dsRequest.setOperationType(opType);
        dsRequest.setDataSource(getID());
        dsRequest.setData(data);
        dsRequest.setCallback(callback);
        if (props != null) dsRequest.copyAttributes(props);

        // Declined to implement a ton of involved sort logic here...

        sendDSRequest(dsRequest);
    }

    public void sendRequest(DSRequest dsRequest) {
        if (dsRequest == null) return;

        dsRequest.setDataSource(getID());

        sendDSRequest(dsRequest);
    }

    private void sendDSRequest(DSRequest dsRequest) {
        // Provide default requestProperties for the operationBinding and the DataSource as a
        // whole
        DSRequest requestProperties = new DSRequest(dsRequest);
        if (getRequestProperties() != null) dsRequest.copyAttributes(getRequestProperties());
        OperationBinding opBinding = getOperationBinding(dsRequest);
        if (opBinding != null && opBinding.getRequestProperties() != null) {
            dsRequest.copyAttributes(opBinding.getRequestProperties());
        }
        dsRequest.copyAttributes(requestProperties);

        dsRequest.setShowPrompt(getShowPrompt());

        _sendGWTRequest(dsRequest);
    }

    protected static Element extractDataElement(Element rootEl, String dataTagName) {
        Element dataEl = null;
        if (dataTagName == null) {
            dataEl = rootEl;
        } else {
            // Find the <data> element.
            NodeList children = rootEl.getChildNodes();
            for (int childIndex = 0; childIndex < children.getLength(); ++childIndex) {
                Node child = children.item(childIndex);
                if (! (child instanceof Element)) continue;
                Element childEl = (Element)child;
                if (dataTagName.equals(childEl.getTagName())) {
                    dataEl = childEl;
                    break;
                }
            }
        }
        if (dataEl == null) {
            dataEl = rootEl;
        }
        return dataEl;
    }

    protected static List<Element> extractRecordElements(Element dataEl, String recordName) {
        if (recordName == null) recordName = "record";

        List<Element> recordNodes = new ArrayList<Element>();
        NodeList children = dataEl.getChildNodes();
        for (int childIndex = 0; childIndex < children.getLength(); ++childIndex) {
            Node child = children.item(childIndex);
            if (! (child instanceof Element)) continue;
            Element childEl = (Element)child;
            if (recordName.equals(childEl.getTagName())) {
                recordNodes.add(childEl);
            }
        }

        return recordNodes;
    }

    protected RecordList extractRecordList(List<? extends Node> recordNodes) {
        return extractRecordList(recordNodes, _getMergedFields());
    }

    protected RecordList extractRecordList(JSONArray dataArr) {
        return extractRecordList(dataArr, _getMergedFields());
    }

    private static Record extractRecord(Element recordEl, Map<String, DataSourceField> fields) {
        Map<String, ArrayList<Element>> collectedNestedRecordNodes = null;
        final Map<String, Object> attributes = new HashMap<String, Object>();

        NodeList children = recordEl.getChildNodes();
        for (int childIndex = 0; childIndex < children.getLength(); ++childIndex) {
            Node child = children.item(childIndex);
            if (! (child instanceof Element)) continue;

            Element childEl = (Element)child;
            String elementName = childEl.getTagName();
            final DataSourceField field = fields.get(elementName);
            final String typeName = (field == null ? null : field.getType());
            SimpleType type = (typeName == null ? null : SimpleType.getType(typeName));

            final DataSource typeDS;
            if (type == null &&
                field != null &&
                (typeDS = field.getTypeAsDataSource()) != null)
            {
                final Map<String, DataSourceField> nestedFields = typeDS._getMergedFields();
                if (collectedNestedRecordNodes == null) {
                    collectedNestedRecordNodes = new HashMap<String, ArrayList<Element>>();
                }

                final Boolean multiple = field.isMultiple();
                if (multiple != null && multiple.booleanValue()) {
                    final String childTagName = field.getChildTagName();
                    if (childTagName == null) {
                        ArrayList<Element> l = collectedNestedRecordNodes.get(elementName);
                        if (l == null) {
                            l = new ArrayList<Element>();
                            collectedNestedRecordNodes.put(elementName, l);
                        }
                        l.add(childEl);
                    } else {
                        List<Element> nestedRecordElements = new ArrayList<Element>();
                        NodeList innerChildren = childEl.getChildNodes();
                        for (int innerChildIndex = 0; innerChildIndex < innerChildren.getLength(); ++innerChildIndex) {
                            Node innerChild = innerChildren.item(innerChildIndex);
                            if (! (innerChild instanceof Element)) continue;
                            Element innerChildEl = (Element)innerChildren.item(innerChildIndex);
                            if (! childTagName.equals(innerChildEl.getTagName())) continue;
                            nestedRecordElements.add(innerChildEl);
                        }
                        attributes.put(elementName, extractRecordList(nestedRecordElements, nestedFields));
                    }
                } else {
                    attributes.put(elementName, extractRecord(childEl, nestedFields));
                }
            } else {
                if (type == null) type = SimpleType.TEXT_TYPE;
                assert type != null;

                final Boolean multiple = (field == null ? null : field.isMultiple());
                if (multiple != null && multiple.booleanValue()) {
                    assert field != null;
                    String childTagName = field.getChildTagName();
                    if (childTagName == null) childTagName = "value";
                    final List<Object> l = new ArrayList<Object>();
                    NodeList innerChildren = childEl.getChildNodes();
                    for (int innerChildIndex = 0; innerChildIndex < innerChildren.getLength(); ++innerChildIndex) {
                        Node innerChild = innerChildren.item(innerChildIndex);
                        if (! (innerChild instanceof Element)) continue;
                        Element innerChildEl = (Element)innerChildren.item(innerChildIndex);
                        if (! childTagName.equals(innerChildEl.getTagName())) continue;
                        l.add(type._fromNode(innerChildEl));
                    }
                    attributes.put(elementName, l);
                } else {
                    attributes.put(elementName, type._fromNode(child));
                }
            }
        }

        NamedNodeMap attrs = recordEl.getAttributes();
        for (int attrIndex = 0; attrIndex < attrs.getLength(); ++attrIndex) {
            Attr attr = (Attr)attrs.item(attrIndex);
            String attrName = attr.getName();

            DataSourceField field = fields.get(attrName);
            if (field != null && field.getName().equals(attrName)) {
                String typeName = field.getType();
                SimpleType type = typeName == null ? null : SimpleType.getType(typeName);
                if (type == null) type = SimpleType.TEXT_TYPE;

                attributes.put(attrName, type.parseInput(attr.getValue().trim()));
            }
        }

        if (collectedNestedRecordNodes != null) {
            for (Map.Entry<String, ArrayList<Element>> e : collectedNestedRecordNodes.entrySet()) {
                final DataSourceField field = fields.get(e.getKey());
                assert field != null;
                final DataSource typeDS = field.getTypeAsDataSource();
                assert typeDS != null;
                final Map<String, DataSourceField> nestedFields = typeDS._getMergedFields();
                assert nestedFields != null;
                attributes.put(e.getKey(), extractRecordList(e.getValue(), nestedFields));
            }
        }

        final Record record = new Record();
        record.putAll(attributes);
        return record;
    }

    private static Object fromJSONValue(JSONValue val, SimpleType type) {
        final JSONObject obj = val.isObject();
        // Special handling if we happen to encounter an object that is not a Date: Convert to
        // a `Record' instead of trying to do something with the stringified representation.
        if (obj != null &&
            !(((Object)obj.getJavaScriptObject()) instanceof Date) &&
            !JSOHelper.isDate(obj.getJavaScriptObject()))
        {
            return extractRecord(obj, Collections.<String, DataSourceField>emptyMap());
        } else {
            return type._fromVal(val);
        }
    }
    private static Record extractRecord(JSONObject datumObj, Map<String, DataSourceField> fields) {
        final Map<String, Object> attributes = new HashMap<String, Object>();

        for (final String key : datumObj.keySet()) {
            final JSONValue val = datumObj.get(key);
            if (val == null) continue;

            final DataSourceField field = fields.get(key);
            final String typeName = (field == null ? null : field.getType());
            SimpleType type = typeName == null ? null : SimpleType.getType(typeName);

            final DataSource typeDS;
            if (type == null &&
                field != null &&
                (typeDS = field.getTypeAsDataSource()) != null)
            {
                final Map<String, DataSourceField> nestedFields = typeDS._getMergedFields();

                final Boolean multiple = field.isMultiple();
                if (multiple != null && multiple.booleanValue()) {
                    final JSONArray nestedDataArr = val.isArray();
                    attributes.put(key, nestedDataArr == null ? null : extractRecordList(nestedDataArr, nestedFields));
                } else {
                    final JSONObject nestedDatumObj = val.isObject();
                    attributes.put(key, nestedDatumObj == null ? null : extractRecord(nestedDatumObj, nestedFields));
                }
            } else {
                if (type == null) type = SimpleType.TEXT_TYPE;
                assert type != null;

                final Boolean multiple = (field == null ? null : field.isMultiple());
                if (multiple != null && multiple.booleanValue()) {
                    assert field != null;
                    final List<Object> l = new ArrayList<Object>();
                    JSONArray arr = val.isArray();
                    if (arr == null) {
                        l.add(fromJSONValue(val, type));
                    } else {
                        for (int i = 0; i < arr.size(); ++i) {
                            JSONValue subVal = arr.get(i);
                            if (subVal == null) continue;
                            l.add(fromJSONValue(subVal, type));
                        }
                    }
                    attributes.put(key, l);
                } else {
                    attributes.put(key, fromJSONValue(val, type));
                }
            }
        }

        final Record record = new Record();
        record.putAll(attributes);
        return record;
    }

    private static RecordList extractRecordList(List<? extends Node> recordNodes, Map<String, DataSourceField> fields) {
        final DataSourceField pkField = getPrimaryKeyField(fields);
        final String pkFieldName = pkField == null ? "id" : pkField.getName();

        RecordList records = new RecordList();

        for (int nodeIndex = 0; nodeIndex < recordNodes.size(); ++nodeIndex) {
            Node recordNode = recordNodes.get(nodeIndex);
            if (recordNode instanceof Element) {
                Element recordEl = (Element)recordNode;
                records.add(extractRecord(recordEl, fields));
            } else {
                // Assume that the node is the record's ID.
                String idValue = recordNode.getNodeValue();
                if (idValue != null) {
                    idValue = idValue.trim();
                    final Record record = new Record();
                    record.setAttribute(pkFieldName, idValue);
                    records.add(record);
                }
            }
        }

        return records;
    }

    private static RecordList extractRecordList(JSONArray dataArr, Map<String, DataSourceField> fields) {
        final DataSourceField pkField = getPrimaryKeyField(fields);
        final String pkFieldName = pkField == null ? "id" : pkField.getName();

        final RecordList records = new RecordList();
        for (int i = 0; i < dataArr.size(); ++i) {
            final JSONValue datumVal = dataArr.get(i);
            if (datumVal == null) continue;
            JSONObject datumObj = datumVal.isObject();
            if (datumObj != null) {
                records.add(extractRecord(datumObj, fields));
            } else {
                if (datumVal.isNull() != null) continue;

                String idValue;
                JSONString datumStr = datumVal.isString();
                if (datumStr != null) idValue = datumStr.stringValue();
                else idValue = datumVal.toString();

                idValue = idValue.trim();
                final Record record = new Record();
                record.setAttribute(pkFieldName, idValue);
                records.add(record);
            }
        }

        return records;
    }

    @SGWTInternal
    protected void _sendGWTRequest(DSRequest dsRequest) {
        final int transactionNum = com.smartgwt.mobile.client.rpc.RPCManager._getNextTransactionNum();
        dsRequest._setTransactionNum(transactionNum);
        // For the time being, the request ID and transactionNum are the same.
        dsRequest.setRequestId(Integer.toString(transactionNum));

        final boolean strictJSON = Canvas._booleanValue(getStrictJSON(), false);
        final DSOperationType opType = dsRequest.getOperationType();
        final OperationBinding opBinding = getOperationBinding(dsRequest);

        final DSDataFormat dataFormat;
        if (opBinding == null) dataFormat = DSDataFormat.JSON;
        else if (opBinding.getDataFormat() != null) dataFormat = opBinding.getDataFormat();
        else dataFormat = DSDataFormat.JSON;
        assert dataFormat != null;

        DSProtocol protocol;
        if (opBinding != null && opBinding.getDataProtocol() != null) {
            protocol = opBinding.getDataProtocol();
        } else {
            protocol = getDataProtocol();
            if (protocol == null) {
                protocol = (opType == null || opType == DSOperationType.FETCH)
                           ? null
                           : DSProtocol.POSTMESSAGE;
            }
        }

        final Object originalData = dsRequest.getData();
        Object transformedData;
        switch (protocol == null ? DSProtocol.GETPARAMS : protocol) {
            case GETPARAMS:
            case POSTPARAMS:
                transformedData = transformRequest(dsRequest);
                if (transformedData == null) transformedData = Collections.EMPTY_MAP;
                else if (!(transformedData instanceof Map)) {
                    // TODO Issue a warning.
                    transformedData = Collections.EMPTY_MAP;
                }
                break;
            case POSTMESSAGE:
                transformedData = transformRequest(dsRequest);
                if (!(transformedData instanceof String)) {
                    if (dataFormat != DSDataFormat.JSON) {
                        throw new UnsupportedOperationException("Only serialization of DSRequests in JSON format is supported.");
                    }
                    transformedData = dsRequest._serialize(strictJSON);
                    if (dsRequest.getContentType() == null) {
                        // For best interoperability with ASP.NET AJAX services, send Content-Type:application/json.
                        // http://weblogs.asp.net/scottgu/archive/2007/04/04/json-hijacking-and-how-asp-net-ajax-1-0-mitigates-these-attacks.aspx
                        dsRequest.setContentType("application/json;charset=UTF-8");
                    }
                }
                break;
            default:
                assert protocol != null;
                throw new UnsupportedOperationException("In transforming the DSRequest, failed to handle case:  protocol:" + protocol.getValue());
        }
        if (transformedData != dsRequest) {
            dsRequest.setData(transformedData);
        }
        dsRequest.setOriginalData(originalData);
        if (dsRequest.getDataSource() == null) dsRequest.setDataSource(getID());
        final DSRequest finalDSRequest = dsRequest;
        assert finalDSRequest.getOperationType() == opType;

        if (protocol == null) {
            assert opType == DSOperationType.FETCH;
            if (transformedData == null ||
                (transformedData instanceof Map && ((Map<?, ?>)transformedData).isEmpty()))
            {
                protocol = DSProtocol.GETPARAMS;
            } else {
                protocol = DSProtocol.POSTMESSAGE;
                transformedData = dsRequest._serialize(strictJSON);
                if (dsRequest.getContentType() == null) {
                    dsRequest.setContentType("application/json;charset=UTF-8");
                }
            }
        }

        URIBuilder workBuilder;

        {
            String work = finalDSRequest.getDataURL();

            if (work == null) {
                if (opType != null) {
                    switch (opType) {
                        case FETCH:
                            work = getFetchDataURL();
                            break;
                        case ADD:
                            work = getAddDataURL();
                            break;
                        case UPDATE:
                            work = getUpdateDataURL();
                            break;
                        case REMOVE:
                            work = getRemoveDataURL();
                            break;
                        case VALIDATE:
                            work = getValidateDataURL();
                            break;
                        case CUSTOM:
                            work = getCustomDataURL();
                            break;
                    }
                }

                // common url
                if (work == null) {
                    work = getDataURL();

                    // construct default url
                    if (work == null) {
                        work = RPCManager.getActionURL();
                        if (work.endsWith("/")) {
                            work = work.substring(0, work.length() - 1);
                        }
                    }
                }
            }

            workBuilder = new URIBuilder(work);
        }

        // build up the query string
        final DateTimeFormat datetimeFormat = finalDSRequest._getDatetimeFormat();

        {
            Map<String, Object> params = finalDSRequest.getParams();

            if (protocol == DSProtocol.GETPARAMS || protocol == DSProtocol.POSTPARAMS) {
                if (params == null) params = new LinkedHashMap<String, Object>();

                if (protocol == DSProtocol.GETPARAMS) {
                    assert transformedData instanceof Map;
                    @SuppressWarnings("unchecked")
                    final Map<String, Object> m = (Map<String, Object>)transformedData;
                    params.putAll(m);
                }

                if (getSendMetaData()) {
                    String metaDataPrefix = getMetaDataPrefix();
                    if (metaDataPrefix == null) metaDataPrefix = "_";

                    params.put(metaDataPrefix + "operationType", opType);
                    params.put(metaDataPrefix + "operationId", finalDSRequest.getOperationId());
                    params.put(metaDataPrefix + "startRow", finalDSRequest.getStartRow());
                    params.put(metaDataPrefix + "endRow", finalDSRequest.getEndRow());
                    params.put(metaDataPrefix + "sortBy", finalDSRequest._getSortByString());
                    params.put(metaDataPrefix + "useStrictJSON", Boolean.TRUE);
                    params.put(metaDataPrefix + "textMatchStyle", finalDSRequest.getTextMatchStyle());
                    params.put(metaDataPrefix + "oldValues", finalDSRequest.getOldValues());
                    params.put(metaDataPrefix + "componentId", finalDSRequest.getComponentId());

                    params.put(metaDataPrefix + "dataSource", dsRequest.getDataSource());
                    params.put("isc_metaDataPrefix", metaDataPrefix);
                }

                params.put("isc_dataFormat", dataFormat.getValue());
            }

            if (params != null) {
                for (final Map.Entry<String, Object> e : params.entrySet()) {
                    workBuilder.setQueryParam(e.getKey(), e.getValue(), strictJSON, false, datetimeFormat);
                }
            }
        }

        // automatically add the data format even to user-provided dataURLs unless they contain the param already
        if (!workBuilder.containsQueryParam("isc_dataFormat")) {
            workBuilder.appendQueryParam("isc_dataFormat", dataFormat.getValue());
        }

        if (protocol == DSProtocol.POSTPARAMS) {
            assert transformedData instanceof Map;
            @SuppressWarnings("unchecked")
            final Map<String, Object> m = (Map<String, Object>)transformedData;

            String requestContentType = finalDSRequest.getContentType();
            if (requestContentType != null) requestContentType = requestContentType.trim();

            if (requestContentType == null || requestContentType.startsWith("application/x-www-form-urlencoded")) {
                URIBuilder postBodyBuilder = new URIBuilder("");
                for (final Map.Entry<String, Object> e : m.entrySet()) {
                    postBodyBuilder.setQueryParam(e.getKey(), e.getValue(), strictJSON, false, datetimeFormat);
                }
                // Exclude the '?'.
                transformedData = postBodyBuilder.toString().substring(1);
            } //else if (requestContentType.startsWith("multipart/form-data")) {} // TODO
            else {
                throw new IllegalArgumentException("Request content type '" + requestContentType + "' is not supported.");
            }
        }

        RequestBuilder.Method httpMethod = getHttpMethod(finalDSRequest.getHttpMethod());
        if (httpMethod == null) {
            if (protocol == DSProtocol.GETPARAMS) httpMethod = RequestBuilder.GET;
            else if (protocol == DSProtocol.POSTPARAMS ||
                     protocol == DSProtocol.POSTMESSAGE)
            {
                httpMethod = RequestBuilder.POST;
            }
            else {
                if (opType == null || opType == DSOperationType.FETCH) {
                    httpMethod = RequestBuilder.GET;
                } else {
                    httpMethod = RequestBuilder.POST;
                }
            }
        } else if (httpMethod == RequestBuilder.GET) {
            if (protocol == DSProtocol.POSTPARAMS ||
                //protocol == DSProtocol.POSTXML
                protocol == DSProtocol.POSTMESSAGE)
            {
                // TODO Warn that GET requests do not support bodies.
                httpMethod = RequestBuilder.POST;
            }
        }

        String requestContentType = finalDSRequest.getContentType();
        if (requestContentType != null) {
            if (httpMethod == RequestBuilder.GET) {
                // TODO Warn that GET requests do not support bodies.
                requestContentType = null;
            }
        } else {
            if (protocol == DSProtocol.POSTPARAMS) requestContentType = "application/x-www-form-urlencoded";
            //else if (protocol == DSProtocol.POSTXML) requestContentType = "text/xml";
        }

        final RequestBuilder rb = new RequestBuilder(httpMethod, workBuilder.toString());
        final Integer timeoutMillis = finalDSRequest.getTimeout();
        rb.setTimeoutMillis(timeoutMillis == null ? RPCManager._getDefaultTimeoutMillis() : Math.max(1, timeoutMillis.intValue()));

        final String authorization = finalDSRequest.getAuthorization();
        if (authorization != null) rb.setHeader("Authorization", authorization);

        final Map<String, String> httpHeaders = finalDSRequest.getHttpHeaders();
        if (httpHeaders != null) {
            for (Map.Entry<String, String> entry : httpHeaders.entrySet()) {
                rb.setHeader(entry.getKey(), entry.getValue());
            }
        }

        if (dataFormat == DSDataFormat.XML) {
            rb.setHeader("Accept", "application/xml,text/xml,*/*");
        } else if (dataFormat == DSDataFormat.JSON) {
            rb.setHeader("Accept", "application/json,*/*");
        }

        if (requestContentType != null) {
            rb.setHeader("Content-Type", requestContentType);
        }

        if (httpMethod != RequestBuilder.GET) {
            switch (protocol) {
                case POSTPARAMS: // `transformedData` has already been created and is now a String.
                case POSTMESSAGE:
                    rb.setRequestData((String)transformedData);
                    break;
                case GETPARAMS:
                    // Already handled earlier when the query params were appended to `workBuilder'.
                    break;
                default:
                    throw new UnsupportedOperationException("In setting the request data, failed to handle case protocol:" + protocol);
            }
        }

        rb.setCallback(new RequestCallback() {
            @Override
            public void onError(Request request, Throwable exception) {
                final DSResponse dsResponse = new DSResponse(finalDSRequest);
                final int status;
                if (exception instanceof RequestTimeoutException) status = RPCResponse.STATUS_SERVER_TIMEOUT;
                else status = RPCResponse.STATUS_FAILURE;
                dsResponse.setStatus(status);
                onError(dsResponse);
            }

            private void onError(DSResponse dsResponse) {
                final DSRequest dsRequest = finalDSRequest;
                final boolean errorEventCancelled = ErrorEvent._fire(DataSource.this, dsRequest, dsResponse);
                if (!errorEventCancelled) RPCManager._handleError(dsResponse, dsRequest);
            }

            @Override
            public void onResponseReceived(Request request, Response response) {
                assert response != null;

                String responseText = response.getText();
                if (responseText == null) responseText = "";
                assert responseText != null;

                int httpResponseCode = response.getStatusCode();

                final HTTPHeadersMap responseHTTPHeaders = new HTTPHeadersMap();
                for (final Header h : response.getHeaders()) {
                    if (h != null) {
                        responseHTTPHeaders.put(h.getName(), h.getValue());
                    }
                }

                int status = 0;
                if (0 == httpResponseCode || // file:// requests (e.g. if Showcase is packaged with PhoneGap.)
                    (200 <= httpResponseCode && httpResponseCode < 300) ||
                    httpResponseCode == 304) // 304 Not Modified
                {
                    status = RPCResponse.STATUS_SUCCESS;
                } else {
                    status = RPCResponse.STATUS_FAILURE;
                    final DSResponse errorResponse = new DSResponse(finalDSRequest);
                    errorResponse.setStatus(RPCResponse.STATUS_FAILURE);
                    errorResponse.setHttpResponseCode(httpResponseCode);
                    errorResponse._setHttpHeaders(responseHTTPHeaders);
                    onError(errorResponse);
                    return;
                }

                Object rawResponse;
                final DSResponse dsResponse;

                String origResponseContentType = responseHTTPHeaders.get("Content-Type");
                if (origResponseContentType == null ||
                    (origResponseContentType = origResponseContentType.trim()).length() == 0)
                {
                    origResponseContentType = "application/octet-stream";
                }

                String responseContentType = origResponseContentType;
                // remove the media type parameter if present
                // http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.7
                final int semicolonPos = responseContentType.indexOf(';');
                if (semicolonPos != -1) {
                    responseContentType = responseContentType.substring(0, semicolonPos).trim();
                    if (responseContentType.length() == 0) responseContentType = "application/octet-stream";
                }

                if (dataFormat == DSDataFormat.CUSTOM) {
                    rawResponse = responseText;

                    dsResponse = new DSResponse(finalDSRequest, status);
                    dsResponse.setHttpResponseCode(httpResponseCode);
                    dsResponse._setHttpHeaders(responseHTTPHeaders);
                    dsResponse.setContentType(origResponseContentType);

                    transformResponse(dsResponse, finalDSRequest, responseText);
                } else {
                    if (dataFormat == DSDataFormat.XML) {
                        final Element rootEl;
                        if (responseText.isEmpty()) {
                            rawResponse = rootEl = null;
                            dsResponse = new DSResponse(finalDSRequest, status);
                        } else {
                            final Document document;
                            try {
                                document = XMLParser.parse(responseText);
                            } catch (DOMParseException ex) {
                                onError(request, ex);
                                return;
                            }

                            rootEl = document.getDocumentElement();
                            rawResponse = rootEl;

                            dsResponse = new DSResponse(finalDSRequest, status, rootEl);

                            String dataTagName, recordName;
                            if (opBinding == null) {
                                dataTagName = _getDataTagName();
                                recordName = null;
                            } else {
                                dataTagName = opBinding._getDataTagName(_getDataTagName());
                                recordName = opBinding.getRecordName();
                            }
                            if (recordName == null) recordName = _getRecordName();

                            final Element dataEl = extractDataElement(rootEl, dataTagName);
                            final List<Element> recordNodes = extractRecordElements(dataEl, recordName);

                            if (recordNodes != null && !recordNodes.isEmpty()) {
                                final RecordList records = extractRecordList(recordNodes);
                                dsResponse.setData(records);
                            } else if (rootEl.equals(dataEl)) {
                                dsResponse._setData(XMLUtil.getTextContent(dataEl));
                            }
                        }
                        dsResponse.setHttpResponseCode(httpResponseCode);
                        dsResponse._setHttpHeaders(responseHTTPHeaders);

                        transformResponse(dsResponse, finalDSRequest, rootEl);
                    } else {
                        String jsonPrefix = getJsonPrefix();
                        if (jsonPrefix == null) jsonPrefix = "";
                        String jsonSuffix = getJsonSuffix();
                        if (jsonSuffix == null) jsonSuffix = "";

                        // auto-detect default wrapper text returned by RestHandler
                        if (responseText.startsWith(jsonPrefix) &&
                            responseText.endsWith(jsonSuffix))
                        {
                            responseText = responseText.substring(jsonPrefix.length(),
                                                                  responseText.length() - jsonSuffix.length());
                            responseContentType = "application/json";
                        }

                        if (dataFormat == DSDataFormat.JSON) {
                            if (responseText.isEmpty()) {
                                rawResponse = null;
                                dsResponse = new DSResponse(finalDSRequest, status);
                            } else {
                                JSONObject responseObj;
                                try {
                                    responseObj = JSONParser.parseLenient(responseText).isObject();
                                } catch (JSONException ex) {
                                    onError(request, ex);
                                    return;
                                }
                                if (responseObj != null && responseObj.containsKey("response")) {
                                    JSONValue val = responseObj.get("response");
                                    responseObj = (val == null ? null : val.isObject());
                                }
                                rawResponse = responseObj;

                                dsResponse = new DSResponse(finalDSRequest, status, responseObj);

                                if (responseObj != null && responseObj.containsKey("data")) {
                                    final JSONValue dataVal = responseObj.get("data");
                                    assert dataVal != null;

                                    final JSONString dataStr = dataVal.isString();
                                    if (dataStr != null) {
                                        dsResponse._setData(dataStr.stringValue());
                                    } else {
                                        JSONArray dataArr = dataVal.isArray();
                                        if (dataArr == null) {
                                            JSONObject datumObj = dataVal.isObject();
                                            if (datumObj != null) {
                                                dataArr = new JSONArray();
                                                dataArr.set(0, datumObj);
                                            }
                                        }
                                        if (dataArr != null) {
                                            final RecordList records = extractRecordList(dataArr);
                                            dsResponse.setData(records);
                                        }
                                    }
                                }
                            }
                            dsResponse.setHttpResponseCode(httpResponseCode);
                            dsResponse._setHttpHeaders(responseHTTPHeaders);

                            transformResponse(dsResponse, finalDSRequest, rawResponse);
                        } else {
                            throw new UnsupportedOperationException("Unhandled dataFormat:" + dataFormat);
                        }
                    }
                }

                if (dsResponse.getInvalidateCache()) {
                    //invalidateDataSourceDataChangedHandlers(finalDSRequest, dsResponse);
                }

                status = dsResponse.getStatus();
                if (status >= 0) {
                    DSDataChangedEvent.fire(DataSource.this, dsResponse, finalDSRequest);
                } else {
                    // Unless it was a validation error, or the request specified willHandleError,
                    // go through centralized error handling (if alerting the failure string
                    // can be dignified with such a name!)
                    if (status != -4 && !finalDSRequest._getWillHandleError()) {
                        onError(dsResponse);
                        return;
                    }
                }

                // fireResponseCallbacks
                final DSCallback callback = finalDSRequest.getCallback(),
                        afterFlowCallback = finalDSRequest._getAfterFlowCallback();
                if (callback != null) {
                    callback.execute(dsResponse, rawResponse, finalDSRequest);
                }
                if (afterFlowCallback != null && afterFlowCallback != callback) {
                    afterFlowCallback.execute(dsResponse, rawResponse, finalDSRequest);
                }
            }
        });

        try {
            rb.send();
        } catch(RequestException re) {
            re.printStackTrace();
        }
        ++_numDSRequestsSent;
    }

    public PopupPanel showPrompt() {
        PopupPanel prompt = new PopupPanel();
        prompt.setModal(true);
        prompt.setWidget(new Label("Contacting server..."));
        prompt.center();
        prompt.show();
        return prompt;
    }

    public void hidePrompt(PopupPanel prompt) {
        prompt.hide();
    }

    @SuppressWarnings("unused")
    private String getDataURL(DSRequest dsRequest) {
        OperationBinding binding = getOperationBinding(dsRequest);
        if (binding != null && binding.get("dataURL") != null) {
            return (String)binding.get("dataURL");
        }
        final String dataURL = getDataURL();
        if (dataURL != null) return dataURL;
        return getDefaultDataURL();
    }

    @SuppressWarnings("unused")
    private static boolean isRecord(Object data) {
        return data instanceof Record;
    }

    public void updateCaches(DSResponse dsResponse) {
        updateCaches(dsResponse, null);
    }

    public void updateCaches(DSResponse dsResponse, DSRequest dsRequest) {
        if (dsResponse == null) return;
        if (dsRequest == null) {
            dsRequest = new DSRequest(dsResponse.getOperationType());
            dsRequest.setDataSource(getID());
        } else {
            String dsId = dsRequest.getDataSource();
            if (dsId == null) {
                dsId = dsResponse.getDataSource();
                if (dsId == null) dsId = getID();
                dsRequest.setDataSource(dsId);
            }
        }

        Object updateData = dsResponse._getData();
        boolean forceCacheInvalidation = dsResponse.getInvalidateCache();
        Integer responseCode = dsResponse.getHttpResponseCode();

        if (updateData == null && ! forceCacheInvalidation &&
            (responseCode == null || ! (responseCode.intValue() >= 200 && responseCode < 300)))
        {
            SC.logWarn("Empty results returned on '" + dsRequest.getOperationType().getValue() + 
                    "' on dataSource '" + dsRequest.getDataSource() + 
                    "', unable to update resultSet(s) on DataSource " + getID() +
                    ".  Return affected records to ensure cache consistency.");
            return;
        }

        // XXX if (this.cacheAllData && this.hasAllData())

        DSDataChangedEvent.fire(this, dsResponse, dsRequest);
    }

    @SuppressWarnings("unused")
    private static boolean isRecordArray(Object data) {
        return data instanceof Record[];
    }
    
    public void setOperationBindings(OperationBinding... operationBindings) {
        attributes.put("operationBindings", operationBindings);
    }

    protected OperationBinding[] getOperationBindings() {
        return (OperationBinding[])attributes.get("operationBindings");
    }

    protected OperationBinding getOperationBinding(DSRequest dsRequest) {
        DSOperationType operationType = dsRequest.getOperationType();
        String operationId = dsRequest.getOperationId();
        return getOperationBinding(operationType, operationId);
    }

    protected OperationBinding getOperationBinding(DSOperationType operationType, String operationId) {
        OperationBinding[] bindings = getOperationBindings();
        if (operationType == null || bindings == null) return null;

        // look for a binding specific to the operationId (eg myFetchSchema) if passed
        if (operationId != null) {
            for (int i = 0; i < bindings.length; i++) {
                DSOperationType bindingType = bindings[i].getOperationType();
                String bindingId = bindings[i].getOperationId();
                if (bindingType != null && bindingType == operationType &&
                    bindingId != null && bindingId.equals(operationId))
                {
                    return bindings[i];
                }
            }
        }

        // look for a binding for this operationType
        for (int i = 0; i < bindings.length; i++) {
            DSOperationType bindingType = bindings[i].getOperationType();
            if (bindingType != null && bindingType.equals(operationType)) {
                return bindings[i];
            }
        }
        return null;
    }

    /**
     * If <code>true</code>, meta data will be included in the parameters sent to the server,
     * with each meta data parameter prefixed with {@link #getMetaDataPrefix() metaDataPrefix}.
     * Applies only when {@link OperationBinding#getDataProtocol() OperationBinding.dataProtocol} is
     * {@link com.smartgwt.mobile.client.types.DSProtocol#GETPARAMS} or
     * {@link com.smartgwt.mobile.client.types.DSProtocol#POSTPARAMS}.
     * 
     * @return whether to send operation meta data with the parameters.  Default value: <code>true</code>.
     */
    public boolean getSendMetaData() {
        Boolean ret = (Boolean)attributes.get("sendMetaData");
        return ret != null && ret.booleanValue();
    }
    /**
     * Setter for {@link #getSendMetaData() sendMetaData}.
     */
    public void setSendMetaData(Boolean sendMetaData) {
        attributes.put("sendMetaData", sendMetaData);
    }

    /**
     * If {@link #getSendMetaData() sendMetaData} is <code>true</code> and
     * {@link OperationBinding#getDataProtocol() OperationBinding.dataProtocol} is
     * {@link com.smartgwt.mobile.client.types.DSProtocol#GETPARAMS} or
     * {@link com.smartgwt.mobile.client.types.DSProtocol#POSTPARAMS}, then this is the
     * prefix that is added to meta data property names.
     * 
     * @return the meta data prefix.  Default value: "_".
     */
    public String getMetaDataPrefix() {
        return (String)attributes.get("metaDataPrefix");
    }
    /**
     * Setter for {@link #getMetaDataPrefix() metaDataPrefix}.
     */
    public void setMetaDataPrefix(String metaDataPrefix) {
        attributes.put("metaDataPrefix", metaDataPrefix);
    }

    /**
     * Transforms <code>DSRequest</code> metadata into a format understood by the server.
     * 
     * <p>The following table lists the return type expected by <code>DataSource</code>:
     * <table border="1" cellpadding="5">
     *   <tr>
     *     <th>{@link com.smartgwt.mobile.client.data.OperationBinding#getDataFormat() OperationBinding.dataFormat}</th>
     *     <th>{@link com.smartgwt.mobile.client.data.OperationBinding#getDataProtocol() OperationBinding.dataProtocol}</th>
     *     <th>Expected return type</th>
     *   </tr>
     *   <tr>
     *     <td><code>ISCSERVER</code></td>
     *     <td>N/A</td>
     *     <td><code>Object</code>, suitable for serialization</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="3"><code>XML</code>, <code>JSON</code>, <code>CUSTOM</code></td>
     *     <td><code>GETPARAMS</code>, <code>POSTPARAMS</code></td>
     *     <td><code>Map&lt;String,&nbsp;Object&gt;</code></td>
     *   </tr>
     *   <tr>
     *      <td><code>POSTMESSAGE</code></td>
     *      <td><code>String</code></td>
     *   </tr>
     *   <tr>
     *      <td><code>POSTXML</code></td>
     *      <td><code>String</code> or {@link com.google.gwt.xml.client.Document}</td>
     *   </tr>
     * </table>
     * 
     * <p>An implementation may augment <code>dsRequest</code> with additional <code>DSRequest</code>
     * metadata such as by adding to {@link DSRequest#getParams() DSRequest.params}.
     * 
     * @param dsRequest (in/out) the <code>DSRequest</code> to transform.
     * @return the transformed data.
     */
    protected Object transformRequest(DSRequest dsRequest) {
        final Object data = dsRequest.getData();
        if (data == null) return null;

        if (data instanceof List) {
            final List<?> list = (List<?>)data;
            final List<Object> listCopy = new ArrayList<Object>(list.size());
            for (Object obj : list) {
                listCopy.add(_prepareData(obj));
            }
            return listCopy;
        }

        return _prepareData(dsRequest.getData());
    }

    @SGWTInternal
    public Object _prepareData(Object data) {
        if (data == null) return null;

        Map<String, DataSourceField> fields = _getMergedFields();
        if (data instanceof Map && fields != null) {
            @SuppressWarnings("unchecked")
            final Map<String, Object> record = (Map<String, Object>)data;
            Record recordCopy = null;

            for (DataSourceField field : fields.values()) {
                final String fieldName = field.getName();
                final Object obj = record.get(fieldName);

                if (obj instanceof Date) {
                    if (recordCopy == null) {
                        recordCopy = new Record();
                        recordCopy.putAll(record);
                    }

                    final String typeName = field.getType();
                    if ("date".equalsIgnoreCase(typeName)) {
                        recordCopy.put(fieldName, SimpleType.DATE_FORMAT.format((Date)obj));
                    } else if ("time".equalsIgnoreCase(typeName)) {
                        recordCopy.put(fieldName, SimpleType.TIME_FORMAT.format((Date)obj));
                    } else {
                        CanFormatDateTime canFormatDateTime;
                        if ("datetime".equalsIgnoreCase(typeName)) canFormatDateTime = (CanFormatDateTime)SimpleType.DATETIME_TYPE;
                        else {
                            final SimpleType type = SimpleType.getType(typeName);
                            if (!(type instanceof CanFormatDateTime)) {
                                SC.logWarn("Unknown date & time type '" + typeName + "'. Assuming 'datetime'.");
                                canFormatDateTime = (CanFormatDateTime)SimpleType.DATETIME_TYPE;
                            } else canFormatDateTime = (CanFormatDateTime)type;
                        }
                        recordCopy.put(fieldName, canFormatDateTime.format((Date)obj, _UTC));
                    }
                }
            }
            if (recordCopy != null) return recordCopy;
        }
        return data;
    }

    public Record[] getUpdatedData(DSRequest dsRequest, DSResponse dsResponse) {
        return getUpdatedData(dsRequest, dsResponse, false);
    }

    public Record[] getUpdatedData(DSRequest dsRequest, DSResponse dsResponse, boolean useDataFromRequest) {
        Record[] updateData = dsResponse.getData();
        if (useDataFromRequest &&
            dsResponse.getStatus() == 0 &&
            (updateData == null || updateData.length == 0))
        {
            final Object requestData = dsRequest.getOriginalData();
            if (requestData != null) {
                if (dsRequest.getOperationType() == DSOperationType.UPDATE) {
                    // if operationType is an update, request data will be sparse so need to combine 
                    // with oldValues
                    final Record updateDatum = new Record();
                    final Record oldValues = dsRequest.getOldValues();
                    if (oldValues != null) {
                        updateDatum.putAll(oldValues);
                    }

                    if (Array.isArray(requestData)) {
                        assert requestData instanceof Record[];
                        updateDatum.putAll(((Record[])requestData)[0]);
                    } else {
                        assert requestData instanceof Record;
                        updateDatum.putAll((Record)requestData);
                    }
                    updateData = new Record[] { updateDatum };
                } else {
                    // for add or delete old values are irrelevant
                    if (!Array.isArray(requestData)) {
                        assert requestData instanceof Record;
                        updateData = new Record[] { (Record)requestData };
                    } else if (requestData != null) {
                        assert requestData instanceof Record[];
                        final int requestData_length = ((Record[])requestData).length;
                        updateData = new Record[requestData_length];
                        for (int i = 0; i < requestData_length; ++i) {
                            updateData[i] = new Record();
                            updateData[i].putAll(((Record[])requestData)[i]);
                        }
                    }
                }
            }
        }
        return updateData;
    }

    /**
     * Transforms service-specific metadata to <code>DSResponse</code> metadata.
     * 
     * <p>The following table lists the type of <code>data</code>:
     * <table border="1" cellpadding="5">
     *   <tr>
     *     <th>{@link com.smartgwt.mobile.client.data.OperationBinding#getDataFormat() OperationBinding.dataFormat}</th>
     *     <th>Type of <code>data</code></th>
     *   </tr>
     *   <tr>
     *     <td><code>ISCSERVER</code>, <code>JSON</code></td>
     *     <td><code>Map&lt;String, Object&gt;</code>, as returned by {@link com.smartgwt.mobile.client.json.JSONUtils#serverResponseToMap(String)}.</td>
     *   </tr>
     *   <tr>
     *     <td><code>XML</code></td>
     *     <td>{@link com.google.gwt.xml.client.Element}, representing the root element, or "document element" of the response document.</td>
     *   </tr>
     *   <tr>
     *     <td><code>CUSTOM</code></td>
     *     <td><code>String</code>, the contents of the HTTP response body</td>
     *   </tr>
     * </table>
     * 
     * <p><b>NOTE:</b> <code>data</code> may be <code>null</code> if the request was not
     * successful.  For example, if <code>dataFormat</code> is <code>XML</code>, but the server
     * response was HTTP 403 Forbidden, then there usually is no response body to parse into
     * a <code>Document</code>.
     * 
     * @param response (in/out) the <code>DSResponse</code> to transform.
     * @param request (in) the <code>DSRequest</code> resulting in a data source response.
     * @param data (in) data from the server.
     */
    protected void transformResponse(DSResponse response, DSRequest request, Object data) {
        return;
    }

    public void setRequestProperties(DSRequest requestProperties) throws IllegalStateException {
        setAttribute("requestProperties", requestProperties);
    }


    public void setFields(DataSourceField... fieldDefs) throws IllegalStateException {
        fields.clear();
        for (DataSourceField field : fieldDefs) {
            if (field == null) continue;
            addField(field);
        }
    }

    public void addField(DataSourceField field) throws IllegalStateException {
        DataSourceField oldField = fields.put(field.getName(), field);
        if (oldField != null && oldField != field) {
            throw new IllegalArgumentException("DataSource " + getID() + " already has a field named " + field.getName() + ".");
        }
    }

    public DataSourceField[] getFields() {
        Map<String, DataSourceField> fields = _getMergedFields();
        DataSourceField[] fieldArray = new DataSourceField[fields.size()];
        int c = 0;
        for (DataSourceField field : fields.values()) {
            fieldArray[c++] = field;
        }
        return fieldArray;
    }

    public String[] getFieldNames() {
        return getFieldNames(false);
    }

    public String[] getFieldNames(boolean excludeHidden) {
        Map<String, DataSourceField> fields = _getMergedFields();
        List<String> names = new ArrayList<String>();
        for (DataSourceField field : fields.values()) {
            boolean hidden = field.isHidden();
            if (!excludeHidden || !hidden) names.add(field.getName());
        }
        String[] nameArray = new String[names.size()];
        for (int i = 0; i < names.size(); i++) {
            nameArray[i] = names.get(i);
        }
        return nameArray;
    }

    // Client-side filtering

    // NOTE: We do not currently support proper client-side filtering, because of the
    // complexities of managing and keeping synchronized a partial cache.  However, we do need
    // a client-side function that can decide whether a given record matches the current filter;
    // this is an important element of cache synchronization, because it enables us to determine
    // whether newly added records should be added to the cache, and whether changes to existing
    // records should cause them to be removed from the cache.  Therefore, we also implement
    // client-side filtering if we know we have a full local cache (ie, the client has queried
    // the server with empty criteria at some point).

    // NOTE: Simple criteria only.

    public boolean recordMatchesCriteria(Record record, Criteria criteria, TextMatchStyle style) {
        // We cannot handle AdvancedCriteria, so just include all records
        if (criteria == null || (criteria instanceof Criterion) || criteria.isAdvanced()) return true;

        for (Map.Entry<String, Object> e : criteria.getValues().entrySet()) {
            final String property = e.getKey();
            Object value1 = record.get(property);
            Object value2 = e.getValue();
            if (!propertyMatches(value1, value2, style)) {
                return false;
            }
        }
        return true;
    }

    public boolean propertyMatches(Object property, Object criterion, TextMatchStyle style) {

        // Deal with nulls first - we take the view that null != "" != 0 != false
        if (property == null) {
            if (criterion == null) return true;
            else return false;
        } else if (criterion == null) {
            return false;
        }

        if (property instanceof Number) {
            if (criterion instanceof Number) {
                return ((Number) property).doubleValue() == ((Number) criterion).doubleValue();
            } else {
                return ((Number) property).doubleValue() == Double.parseDouble(criterion.toString());
            }
        }

        if (property instanceof Boolean) {
            if (criterion instanceof Boolean) {
                return property.equals(criterion);
            } else {
                return property.equals(Boolean.parseBoolean(criterion.toString()));
            }
        }

        if (property instanceof Date) {
            if (criterion instanceof Date) {
                return ((Date)property).getTime() == ((Date)criterion).getTime();
            } else {
                // Doesn't seem worth trying to parse here - we don't have normal Java date
                // support in GWT, and the alternative DateTimeFormat class requires that you
                // choose a precise date format to parse - any we chose would be completely
                // arbitrary, so it seems like we should just take the view that dates can only
                // be meaningfully compared to other dates
                return false;
            }
        }

        // Fall back to string comparison
        String left, right;
        // Doing this because I've seen a GWT bug in the past where calling toString() on a String
        // creates an odd Javascript object that proceeds to break things.  May well have been
        // fixed by now.
        if (property instanceof String) left = (String)property;
        else left = property.toString();
        if (criterion instanceof String) right = (String)criterion;
        else right = criterion.toString();

        switch(style) {
            case EXACT: return left.equals(right);
            case STARTS_WITH: return left.startsWith(right);
            case SUBSTRING: return left.indexOf(right) != -1;
        }

        return false;
    }


    @SGWTInternal
    protected HandlerManager _createHandlerManager() {
        return new HandlerManager(this);
    }

    @SGWTInternal
    protected final HandlerManager _ensureHandlers() {
        if (handlerManager == null) {
            handlerManager = _createHandlerManager();
        }
        return handlerManager;
    }

    @Override
    public HandlerRegistration _addDSDataChangedHandler(DSDataChangedHandler handler) {
        return _ensureHandlers().addHandler(DSDataChangedEvent.getType(), handler);
    }

    @Override
    public final void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }

    // ********************* Static Methods ***********************
            
    private static Map<String, DataSource> dataSources = new HashMap<String, DataSource>();

    @SGWTInternal
    public static void _register(String ID, DataSource ds) {
        dataSources.put(ID, ds);
    }

    public static DataSource getDataSource(String ID) {
        return dataSources.get(ID);
    }

    public static void load(String ID, LoadDSCallback callback) {
        loadDataSource(ID, callback);
    }

    public static void loadWithParents(String ID, LoadDSCallback callback) {
        loadDataSource(ID, callback, true);
    }

    public static void loadDataSource(String ID, LoadDSCallback callback) {
        loadDataSource(ID, callback, false);
    }

    private static void loadDataSource(String ID, LoadDSCallback callback, boolean loadParents) {
        String work = dsLoaderUrl;
        if (work.endsWith("/")) {
            work = work.substring(0, work.length() - 1);
        }
        work += "?dataSource=" + ID;
        if (loadParents) {
            work += "&loadParents=true";
        }
        RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, work);
        rb.setRequestData("");

        final LoadDSCallback finalCallback = callback;

        rb.setCallback(new RequestCallback() {

            public void onResponseReceived(Request request, Response response) {
                String creationText = null;
                
                if (response != null) {
                	creationText = response.getText();
                }
                if (creationText == null || creationText.trim().length() == 0) {
                    //Log.error("ERROR!  Null or empty response in DataSource loadDataSource");
                    if (finalCallback != null) finalCallback.execute(null);
                    return;
                }

                assert response != null;
                int status = response.getStatusCode();
                if (status < 200 || status > 299) {
                    //Log.error("DataSource loadDataSource returned HTTP status " + status);
                    if (finalCallback != null) finalCallback.execute(null);
                    return;                	
                }

                ArrayList<DataSource> dataSources = createDataSourcesFromJSON(creationText, false);
                if (finalCallback != null) {
                    DataSource[] dsList = dataSources.toArray(new DataSource[dataSources.size()]);
                    finalCallback.execute(dsList);
                }
            }

            public void onError(Request request, Throwable exception) {
            	//Log.error("loadDataSource error: ", exception);
                if (finalCallback != null) finalCallback.execute(null);
            }
        });

        try {
            rb.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses a JSON string containing one or more <code>DataSource</code> definitions.  This method returns the first
     * <code>DataSource</code> that is defined in the JSON.  The remaining <code>DataSource</code>s will be
     * accessible via {@link DataSource#getDataSource getDataSource()}.
     *
     * <p><code>fromJSON()</code> requires that the input JSON be in the same form as the JSON returned by the
     * <code>DataSourceLoader</code> servlet.  This method is an alternative to {@link DataSource#load load()},
     * {@link DataSource#loadDataSource loadDataSource()}, or {@link DataSource#loadWithParents loadWithParents()}
     * because it allows the loading of <code>DataSource</code>s from a cached copy of <code>DataSourceLoader</code>
     * output.</p>
     *
     * @param jsonText a JSON string
     * @return the first <code>DataSource</code> defined in the JSON, or <code>null</code> if no <code>DataSource</code>
     * is defined.
     */
    public static DataSource fromJSON(String jsonText) {
        createDataSourcesFromJSON(jsonText, true);
        DataSource ret = firstDefinedDataSource;
        firstDefinedDataSource = null;
        return ret;
    }

    private static ArrayList<DataSource> createDataSourcesFromJSON(String jsonText, boolean lookForFirstDS) {
        // Initialize the global state that is used during eval() to keep track of the created
        // DataSources
        firstDefinedDataSource = null;
        lookingForFirstDefinedDataSource = lookForFirstDS;
        createdDataSources = new ArrayList<DataSource>();

        if (jsonText != null && !jsonText.isEmpty() && jsonText.charAt(0) != '<') {
            // eval() the JSON string
            JSONUtils.eval(jsonText);
        } else {
            SC.logWarn("Cannot parse the DataSource definition: " + jsonText);
        }

        // Clear the global state and return.
        ArrayList<DataSource> ret = createdDataSources;
        createdDataSources = null;
        lookingForFirstDefinedDataSource = false;
        return ret;
    }

    // A list to store all DataSources created by fromConfig() during the evaluation of JavaScript
    // sent from the server.
    private static ArrayList<DataSource> createdDataSources = null;

    // The DataSource corresponding to the first occurrence of "isc.DataSource.create" in a
    // parsed JSON string.
    private static DataSource firstDefinedDataSource = null;
    private static boolean lookingForFirstDefinedDataSource = false;

    protected static DataSource fromConfig(JavaScriptObject configObj) {
        Map<String, Object> config = JSONUtils.toRecord(configObj);

        final DataSource ds = new DataSource((String)config.get("ID"));
        if (lookingForFirstDefinedDataSource && firstDefinedDataSource == null) {
            firstDefinedDataSource = ds;
        }
        if (createdDataSources != null) {
            createdDataSources.add(ds);
        }

        @SuppressWarnings("unchecked")
        List<Record> fields = (List<Record>)config.get("fields");
        DataSourceField[] dsFields = null;
        if (fields != null) {
            dsFields = new DataSourceField[fields.size()];
            for (int i = 0; i < fields.size(); i++) {
                dsFields[i] = new DataSourceField(fields.get(i));
            }
        }
        @SuppressWarnings("unchecked")
        List<Record> bindings = (List<Record>)config.get("operationBindings");
        OperationBinding[] operationBindings = null;
        if (bindings != null) {
        	operationBindings = new OperationBinding[bindings.size()];
	        if (fields != null) {
	            for (int i = 0; i < bindings.size(); i++) {
                    OperationBinding binding = new OperationBinding(bindings.get(i));
	                operationBindings[i] = new OperationBinding(binding);
	            }
	        }
        }

        for (Map.Entry<String, Object> e : config.entrySet()) {
            ds.attributes.put(e.getKey(), e.getValue());
        }

        if (dsFields != null) {
            ds.setFields(dsFields);
        }
        if (operationBindings != null) {
            ds.setOperationBindings(operationBindings);
        }

        Object inheritsFrom = config.get("inheritsFrom");
        if (inheritsFrom instanceof DataSource) {
            ds.setInheritsFrom((DataSource)inheritsFrom);

            if (lookingForFirstDefinedDataSource && inheritsFrom == firstDefinedDataSource) {
                firstDefinedDataSource = ds;
            }
        } else if (inheritsFrom instanceof String) {
            ds.setInheritsFrom((String)inheritsFrom);
        }

        return ds;
    }

   
    public static void setDefaultDataURL(String value) {
        RPCManager.setActionURL(value);
    }
    
    public static String getDefaultDataURL() {
        return RPCManager.getActionURL();
    }

    public static void setLoaderUrl(String loaderUrl) {
        DataSource.dsLoaderUrl = Page.getURL(loaderUrl);
    }

    public static String getLoaderUrl() {
        return dsLoaderUrl;
    }

    public static boolean isUpdateOperation(DSOperationType opType) {
        return opType == DSOperationType.UPDATE ||
               opType == DSOperationType.ADD ||
               opType == DSOperationType.REMOVE;
    }

    @Override
    public HandlerRegistration addHandleErrorHandler(HandleErrorHandler handler) {
        return _ensureHandlers().addHandler(ErrorEvent._getType(), handler);
    }
}
