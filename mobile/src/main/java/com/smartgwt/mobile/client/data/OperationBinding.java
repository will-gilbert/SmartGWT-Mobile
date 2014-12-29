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
import java.util.Map;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.types.DSDataFormat;
import com.smartgwt.mobile.client.types.DSOperationType;
import com.smartgwt.mobile.client.types.DSProtocol;

@SuppressWarnings("serial")
public class OperationBinding extends HashMap<String, Object> {

    public OperationBinding() {
    }
    
    public OperationBinding(DSOperationType operationType) {
        this(operationType, operationType == null ? null : operationType.getValue());
    }
    
    public OperationBinding(DSOperationType operationType, String operationId) {
        setOperationType(operationType);
        setOperationId(operationId);
    }

    public OperationBinding(Map<String, Object> properties) {
        putAll(properties);
    }

    public DSRequest getRequestProperties() {
        return (DSRequest)get("requestProperties");
    }

    /**
     * Which operationType this operationBinding is for.  This property is only settable on an operationBinding, not a
     * DataSource as a whole.
     *
     * @param operationType operationType
     */
    public void setOperationType(DSOperationType operationType) {
        put("operationType", operationType == null ? null : operationType.getValue());
    }

    /**
     * Which operationType this operationBinding is for.  This property can only be set on an operationBinding, not a
     * DataSource as a whole.
     * @return DSOperationType
     */
    public DSOperationType getOperationType()  {
        String res = (String) get("operationType");
        return res == null ? null : DSOperationType.valueOf(res.toUpperCase());
    }

    public String getOperationId() {
        return (String)get("operationId");
    }
    public void setOperationId(String operationId) {
        put("operationId", operationId);
    }
    
    public DSDataFormat getDataFormat() {
        return (DSDataFormat)get("dataFormat");
    }
    public void setDataFormat(DSDataFormat dataFormat) {
        put("dataFormat", dataFormat);
    }
    
    public DSProtocol getDataProtocol() {
        return (DSProtocol)get("dataProtocol");
    }
    public void setDataProtocol(DSProtocol protocol) {
        put("dataProtocol", protocol);
    }

    /**
     * The tag name of the data element, which holds record elements.  If <code>null</code>,
     * then the record elements are assumed to be children of the response root rather than
     * children of a data element.
     * 
     * @return the tag name of the data element, which holds record elements.  Default value "data".
     */
    public final String getDataTagName() {
        return (String)get("dataTagName");
    }

    @SGWTInternal
    public final String _getDataTagName(String defaultValue) {
        if (containsKey("dataTagName")) return (String)get("dataTagName");
        return defaultValue;
    }

    public void setDataTagName(String dataTagName) {
        put("dataTagName", dataTagName);
    }

    public String getRecordName() {
        return (String)get("recordName");
    }
    public void setRecordName(String recordName) {
        put("recordName", recordName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || ! (obj instanceof OperationBinding)) return false;

        OperationBinding opBinding = (OperationBinding)obj;
        if (getOperationType() != opBinding.getOperationType()) return false;

        String operationId = getOperationId();
        String otherOperationId = opBinding.getOperationId();
        if (operationId == null) return otherOperationId == null;
        else return operationId.equals(otherOperationId);
    }
}
