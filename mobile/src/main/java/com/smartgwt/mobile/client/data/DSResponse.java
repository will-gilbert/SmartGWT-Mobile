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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.util.XMLUtil;
import com.smartgwt.mobile.client.rpc.RPCResponse;
import com.smartgwt.mobile.client.types.DSOperationType;

public class DSResponse extends RPCResponse {

    private void init(DSRequest dsRequest) {
        _setTransactionNum(dsRequest._getTransactionNum());
        setOperationType(dsRequest.getOperationType());
        setOperationId(dsRequest.getOperationId());
        setDataSource(dsRequest.getDataSource());
    }

    DSResponse(DSRequest dsRequest, int status, Element responseEl) {
        init(dsRequest);
        Integer startRow = null, endRow = null, totalRows = null;
        Map<String, String> errors = null;

        // Look for <status>, <startRow>, <endRow>, <totalRows>, and <errors> child elements of responseEl.
        NodeList children = responseEl.getChildNodes();
        for (int childIndex = 0; childIndex < children.getLength(); ++childIndex) {
            Node child = children.item(childIndex);

            if (! (child instanceof Element)) continue;
            Element childEl = (Element)child;
            String childTagName = childEl.getTagName();
            if ("status".equals(childTagName)) {
                try {
                    status = Integer.parseInt(XMLUtil.getTextContent(childEl).trim());
                } catch (NumberFormatException ex) {}
            } else if ("startRow".equals(childTagName)) {
                try {
                    startRow = Integer.valueOf(XMLUtil.getTextContent(childEl).trim());
                } catch (NumberFormatException ex) {}
            } else if ("endRow".equals(childTagName)) {
                try {
                    endRow = Integer.valueOf(XMLUtil.getTextContent(childEl).trim());
                } catch (NumberFormatException ex) {}
            } else if ("totalRows".equals(childTagName)) {
                try {
                    totalRows = Integer.valueOf(XMLUtil.getTextContent(childEl).trim());
                } catch (NumberFormatException ex) {}
            } else if ("errors".equals(childTagName)) {
                errors = new HashMap<String, String>();
                final NodeList errorsElemChildren = childEl.getChildNodes();
                for (int errorsChildIndex = 0; errorsChildIndex < errorsElemChildren.getLength(); ++errorsChildIndex) {
                    final Node errorsChild = errorsElemChildren.item(errorsChildIndex);
                    if (!(errorsChild instanceof Element)) continue;
                    final Element errorsChildElem = (Element)errorsChild;

                    // Find the <errorMessage> element.
                    Element errorMessageElem = null;
                    final NodeList errorsChildElemChildren = errorsChildElem.getChildNodes();
                    for (int errorsChildChildIndex = 0; errorsChildChildIndex < errorsChildElemChildren.getLength(); ++errorsChildChildIndex) {
                        final Node errorsChildChild = errorsChildElemChildren.item(errorsChildChildIndex);
                        if (!(errorsChildChild instanceof Element)) continue;
                        final Element errorsChildChildElem = (Element)errorsChildChild;
                        if ("errorMessage".equals(errorsChildChildElem.getTagName())) {
                            errorMessageElem = errorsChildChildElem;
                            break;
                        }
                    }

                    if (errorMessageElem != null) {
                        errors.put(errorsChildElem.getTagName(), XMLUtil.getTextContent(errorMessageElem));
                    }
                }
            }
        }

        setStatus(status);

        if (startRow != null) setStartRow(startRow);
        if (endRow != null) setEndRow(endRow);
        if (totalRows != null) setTotalRows(totalRows);
        setErrors(errors);

        // Note: No need to handle the data element here because DataSource will call
        // extractRecordElements() followed by extractRecordList().
    }

    // Note: `responseObj' may be `null'.
    DSResponse(DSRequest dsRequest, int status, JSONObject responseObj) {
        init(dsRequest);
        Map<String, String> errors = null;

        if (responseObj != null) {
            JSONValue val = responseObj.get("status");
            JSONNumber num = (val == null ? null : val.isNumber());
            if (num != null) {
                status = (int)num.doubleValue();
            }
            val = responseObj.get("startRow");
            num = (val == null ? null : val.isNumber());
            if (num != null) {
                setStartRow(Integer.valueOf((int)num.doubleValue()));
            }
            val = responseObj.get("endRow");
            num = (val == null ? null : val.isNumber());
            if (num != null) {
                setEndRow(Integer.valueOf((int)num.doubleValue()));
            }
            val = responseObj.get("totalRows");
            num = (val == null ? null : val.isNumber());
            if (num != null) {
                setTotalRows(Integer.valueOf((int)num.doubleValue()));
            }
            val = responseObj.get("errors");
            JSONObject errorsObj = (val == null ? null : val.isObject());
            if (errorsObj != null) {
                errors = new HashMap<String, String>();
                for (final String key : errorsObj.keySet()) {
                    val = errorsObj.get(key);
                    if (val == null || val.isNull() != null) continue;
                    final String errorMessage;
                    JSONString str = val.isString();
                    if (str != null) errorMessage = str.stringValue();
                    else errorMessage = val.toString();
                    errors.put(key, errorMessage);
                }
            }
        }

        setStatus(status);
        if (errors != null) setErrors(errors);

        // No need to handle the response data here.
    }

    DSResponse(DSRequest dsRequest, int status, Map<String, Object> response) {
        init(dsRequest);
        Number n = (Number)response.get("status");
        if (n != null) setStatus(n.intValue());
        else setStatus(status);

        n = (Number)response.get("startRow");
        if (n != null) setStartRow(n.intValue());
        n = (Number)response.get("endRow");
        if (n != null) setEndRow(n.intValue());
        n = (Number)response.get("totalRows");
        if (n != null) setTotalRows(n.intValue());
        @SuppressWarnings("unchecked")
        final Map<String, Object> errors = (Map<String, Object>)response.get("errors");
        setErrors(errors);
        final Object data = response.get("data");
        // If it's an error that isn't a validation error, expect the data member to be an
        // error message rather than a list of records
        if (getStatus() < 0 && getStatus() != -4) {
            setFailureMessage((String)data);
        } else {
            if (data instanceof Map) {
                setData((Record)data);
            } else {
                setData((RecordList)data);
            }
        }
    }

    DSResponse(DSRequest dsRequest, int status) {
        this(dsRequest);
        setStatus(status);
    }

    public DSResponse(DSRequest dsRequest) {
        init(dsRequest);
    }

    public DSResponse(String dataSource) {
        setDataSource(dataSource);
    }

    public DSResponse(String dataSource, DSOperationType operationType) {
        this(dataSource);
        setOperationType(operationType);
    }

    public DSResponse(String dataSource, DSOperationType operationType, Record... data) {
        this(dataSource, operationType);
        setData(data);
    }

    public DSOperationType getOperationType() {
        return (DSOperationType)_attributes.get("operationType");
    }

    public void setOperationType(DSOperationType operationType) {
        _attributes.put("operationType", operationType);
    }

    public String getOperationId() {
        return (String)_attributes.get("operationId");
    }

    public void setOperationId(String newVal) {
        _attributes.put("operationId", newVal);
    }

    public String getDataSource() {
        return (String)_attributes.get("dataSource");
    }

    public void setDataSource(String dataSource) {
        _attributes.put("dataSource", dataSource);
    }

    public String getContentType() {
        return (String)_attributes.get("contentType");
    }

    public void setContentType(String contentType) {
        _attributes.put("contentType", contentType);
    }

    /**
     * Returns the DSResponse's data member as a RecordList - synonym for getDataAsRecordList()
     * @return RecordList
     */
    public RecordList getRecordList() {
        return getDataAsRecordList();
    }

    /**
     * Returns the DSResponse's data member as a Record array - synonym for getData()
     * @return Record[]
     */
    public Record[] getRecords() {
        return getData();
    }

    /**
     * Returns the DSResponse's data member as a single Record.  Note that it will return the
     * first record if there is more than one
     * @return Record
     */
    public Record getRecord() {
        final Object data = _attributes.get("data");
        if (data instanceof RecordList) {
            RecordList records = (RecordList)data;
            return records.isEmpty() ? null : records.get(0);
        } else if (data instanceof Record[]) {
            Record[] records = (Record[])data;
            return records.length == 0 ? null : records[0];
        }
        return (Record)data;
    }

    public void setRecord(Record datum) {
        _attributes.put("data", datum);
    }

    public void setStartRow(Integer startRow) {
        _attributes.put("startRow", startRow);
    }
    public Integer getStartRow() { return (Integer)_attributes.get("startRow"); }

    public void setEndRow(Integer endRow) {
        _attributes.put("endRow", endRow);
    }
    public Integer getEndRow() { return (Integer)_attributes.get("endRow"); }

    public void setTotalRows(Integer totalRows) {
        _attributes.put("totalRows", totalRows);
    }
    public Integer getTotalRows() { return (Integer)_attributes.get("totalRows"); }

    protected void setInvalidateCache(boolean invalidateCache) {
        _attributes.put("invalidateCache", invalidateCache);
    }
    public boolean getInvalidateCache() {
        Boolean rtn = (Boolean)_attributes.get("invalidateCache");
        return rtn == null ? false : rtn.booleanValue();
    }

    protected void setFailureMessage(String message) {
        _attributes.put("failureMessage", message);
    }
    public String getFailureMessage() { return (String)_attributes.get("failureMessage"); }

    public void setErrors(Map<String, ?> errors) {
        _attributes.put("errors", errors);
    }
    @SuppressWarnings("unchecked")
    public Map<String, Object> getErrors() { return (Map<String, Object>)_attributes.get("errors"); }

    public void setData(Record... data) {
        _attributes.put("data", data);
    }

    public void setData(RecordList data) {
        _attributes.put("data", data);
    }

    @SGWTInternal
    public void _setData(String data) {
        _attributes.put("data", data);
    }

    /**
     * Returns the DSResponse's data member as a RecordList - synonym for getRecords()
     * @return Record[]
     */
    public Record[] getData() {
        Object data = _attributes.get("data");
        if (data == null || data instanceof CharSequence) return null;
        if (data instanceof Record) {
            Record[] records = new Record[1];
            records[0] = (Record)data;
            return records;
        } else {
            RecordList recordList = getRecordList();
            Record[] records = new Record[recordList.size()];
            int i = 0;
            for (Iterator<Record> it = recordList.iterator(); it.hasNext(); ) {
                records[i++] = it.next();
            }
            return records;
        }
    }

    @SGWTInternal
    public Object _getData() {
        return _attributes.get("data");
    }

    @SGWTInternal
    public void _setData(Object data) {
        _attributes.put("data", data);
    }

    /**
     * Returns the DSResponse's data member as a RecordList - synonym for getRecordList()
     * @return RecordList
     */
    public RecordList getDataAsRecordList() {
        final Object data = _attributes.get("data");
        if (data == null || data instanceof CharSequence) return null;
        if (data instanceof RecordList) return (RecordList)data;
        if (data instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<Record> records = (Collection<Record>)data;
            return new RecordList(records);
        }
        if (data instanceof Record[]) {
            return new RecordList((Record[])data);
        }
        RecordList ret = new RecordList();
        ret.add((Record)data);
        return ret;
    }

    @SGWTInternal
    public int _getNumRecords() {
        final Object data = _attributes.get("data");
        if (data instanceof List) return ((List<?>)data).size();
        else if (data instanceof Record[]) return ((Record[])data).length;
        else if (data instanceof Record) return 1;
        return 0;
    }
}
