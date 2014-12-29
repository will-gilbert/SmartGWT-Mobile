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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.util.HasSerializeableFields;
import com.smartgwt.mobile.client.rpc.RPCRequest;
import com.smartgwt.mobile.client.types.DSOperationType;
import com.smartgwt.mobile.client.types.TextMatchStyle;
import com.smartgwt.mobile.client.util.JSONEncoder;

public class DSRequest extends RPCRequest implements HasSerializeableFields {

    public DSRequest() {
    }

    public DSRequest(DSOperationType opType) {
        setOperationType(opType);
    }

    public DSRequest(DSOperationType opType, String operationId) {
        setOperationType(opType);
        setOperationId(operationId);
    }

    public DSRequest(DSOperationType opType, Record data) {
        setOperationType(opType);
        setData(data);
    }

    public DSRequest(DSOperationType opType, String operationId, Record data) {
        setOperationType(opType);
        setOperationId(operationId);
        setData(data);
    }

    public DSRequest(DSRequest dsRequest) {
        this();
        copyAttributes(dsRequest);
    }

    @SGWTInternal
    public final Map<String, ?> _getSerializableFields(List<?> list1, List<?> list2) {
        final String[] removeProperties = {
                "afterFlowCallback",
                "callback",
                "httpMethod",
                "originalData"
                };
        final Map<String, Object> masked = new HashMap<String, Object>(_getAttributes());
        for (final String removeProperty : removeProperties) {
            masked.remove(removeProperty);
        }
        if (masked.get("data") == null) masked.remove("data");
        final String sortByString = _getSortByString();
        if (sortByString != null && !sortByString.isEmpty()) masked.put("sortBy", sortByString);
        return masked;
    }

    public String getDataURL() {
        return (String)_attributes.get("dataURL");
    }

    public void setDataURL(String dataURL) {
        _attributes.put("dataURL", dataURL);
    }

    public void copyAttributes(DSRequest dsRequest) {
        if (dsRequest != null) _attributes.putAll(dsRequest._attributes);
    }

    public void copyAttributes(Map<String, Object> properties) {
        if (properties != null) _attributes.putAll(properties);
    }

    @SGWTInternal
    public String _serialize(boolean strict) {
        final JSONEncoder encoder = new JSONEncoder();
        encoder.setPrettyPrint(false);
        encoder.setStrictJSON(strict);
        encoder.setStrictQuoting(true);
        return encoder.encode(this);
    }

    @SGWTInternal
    public final DSCallback _getAfterFlowCallback() {
        return (DSCallback)_attributes.get("afterFlowCallback");
    }

    @SGWTInternal
    public void _setAfterFlowCallback(DSCallback afterFlowCallback) {
        _attributes.put("afterFlowCallback", afterFlowCallback);
    }

    /**
     * Returns the value of an <code>Authorization</code> header field to send with the request.
     * 
     * <p>The <code>Authorization</code> header field is used by some protocols to authenticate
     * requests with the server. For example, it is used by the OAuth 2 protocol to access a
     * protected resource.
     * 
     * <p>If not <code>null</code>, then the String value becomes the value of an <code>Authorization</code>
     * header field that is sent with the request.
     * 
     * @return the value of an <code>Authorization</code> header field to send with the request. Default value: <code>null</code>.
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.8">HTTP/1.1: Header Field Definitions. 14.8 Authorization.</a>
     */
    @SGWTInternal
    public final String getAuthorization() {
        return (String)_attributes.get("authorization");
    }

    @SGWTInternal
    public void setAuthorization(String authorization) {
        _attributes.put("authorization", authorization);
    }

    /**
     * Returns the query string parameters that should be sent.  Each value object should be a
     * String, String[], or List&lt;String&gt;.  The latter two are used for cases where the
     * same query string parameter name is used multiple times.
     * 
     * @return Map<String, Object>
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getParams() {
        return (Map<String, Object>)_attributes.get("params");
    }
    public void setParams(Map<String, Object> newParams) {
        _attributes.put("params", newParams);
    }

    @SGWTInternal
    public final OperationBinding _getOperation() {
        return (OperationBinding)_attributes.get("operation");
    }

    @SGWTInternal
    public void _setOperation(OperationBinding operation) {
        _attributes.put("operation", operation);
        _attributes.put("operationType", operation == null ? null : operation.getOperationType());
        _attributes.put("operationId", operation == null ? null : operation.getOperationId());
    }

    public final String getOperationId() {
        return (String)_attributes.get("operationId");
    }

    public void setOperationId(String newVal) {
        _attributes.put("operationId", newVal);
    }

    public final DSOperationType getOperationType() {
        return (DSOperationType)_attributes.get("operationType");
    }

    public void setOperationType(DSOperationType operationType) {
        _attributes.put("operationType", operationType);
    }

    public String getDataSource() {
        return (String)_attributes.get("dataSource");
    }
    public void setDataSource(String dataSource) {
        _attributes.put("dataSource", dataSource);
    }

    public final DSCallback getCallback() {
        return (DSCallback)_attributes.get("callback");
    }

    public void setCallback(DSCallback callback) {
        _attributes.put("callback", callback);
    }

    public String getComponentId() {
        return (String)_attributes.get("componentId");
    }
    public void setComponentId(String componentId) {
        _attributes.put("componentId", componentId);
    }

    public Object getData() {
        return _attributes.get("data");
    }

    public void setData(Object newData) {
        _attributes.put("data", newData);
    }

    /**
     * The {@link #getData() data} of the request before {@link com.smartgwt.mobile.client.data.DataSource#transformRequest(DSRequest)
     * DataSource.transformRequest()} was called.
     */
    public Object getOriginalData() {
        return _attributes.get("originalData");
    }

    public void setOriginalData(Object newOriginalData) {
        _attributes.put("originalData", newOriginalData);
    }

    public Record getOldValues() {
        return (Record)_attributes.get("oldValues");
    }
    public void setOldValues(Record oldValues) {
        _attributes.put("oldValues", oldValues);
    }

    public final SortSpecifier[] getSortBy() {
        return (SortSpecifier[])_attributes.get("sortBy");
    }

    public void setSortBy(SortSpecifier... sortBy) {
        _attributes.put("sortBy", sortBy);
    }

    @SGWTInternal
    public final String _getSortByString() {
        final SortSpecifier[] sortSpecifiers = getSortBy();
        if (sortSpecifiers == null || sortSpecifiers.length == 0) return null;
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sortSpecifiers.length; ++i) {
            if (i != 0) sb.append(',');
            sb.append(sortSpecifiers[i]);
        }
        return sb.toString();
    }

    public Integer getStartRow() {
        return (Integer)_attributes.get("startRow");
    }
    public void setStartRow(Integer startRow) {
        _attributes.put("startRow", startRow);
    }

    public Integer getEndRow() {
        return (Integer)_attributes.get("endRow");
    }
    public void setEndRow(Integer endRow) {
        _attributes.put("endRow", endRow);
    }

    public void setTextMatchStyle(TextMatchStyle textMatchStyle) {
        _attributes.put("textMatchStyle", textMatchStyle);
    }

    public TextMatchStyle getTextMatchStyle() {
        return (TextMatchStyle)_attributes.get("textMatchStyle");
    }

    /**
     * Returns the {@link com.google.gwt.i18n.shared.DateTimeFormat} used to serialize datetime
     * values.
     * @return the <code>DateTimeFormat</code> object used to serialize datetime values.
     */
    @SGWTInternal
    public final DateTimeFormat _getDatetimeFormat() {
        return (DateTimeFormat)_attributes.get("datetimeFormat");
    }

    @SGWTInternal
    public void _setDatetimeFormat(DateTimeFormat datetimeFormat) {
        _attributes.put("datetimeFormat", datetimeFormat);
    }
}
