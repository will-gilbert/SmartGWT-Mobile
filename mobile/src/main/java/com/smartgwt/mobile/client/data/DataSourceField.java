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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.smartgwt.mobile.client.widgets.Canvas;

public class DataSourceField {

    private static int typeDSCount = 0;

    private Map<String, Object> attributes = new HashMap<String, Object>();

    public DataSourceField() {
    }

    DataSourceField(Record record) {
        attributes.putAll(record);
        final Object valueMapObj = record.getAttributeAsObject("valueMap");
        if (valueMapObj instanceof List) {
            @SuppressWarnings("unchecked")
            final List<Object> values = (List<Object>)valueMapObj;
            final LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
            for (int i = 0; i < values.size(); ++i) {
                final String str = "" + values.get(i);
                valueMap.put(str, str);
            }
            attributes.put("valueMap", valueMap);
        }
    }

    public DataSourceField(String fieldName) {
        attributes.put("name", fieldName);
    }

    public DataSourceField(String fieldName, String type) {
        attributes.put("name", fieldName);
        attributes.put("type", type);
    }

    public DataSourceField(DataSourceField field) {
        attributes.putAll(field.attributes);
    }

    public static DataSourceField combineFieldData(DataSourceField localField, DataSourceField dsField) {
        if (dsField == null && localField == null) {
            return null;
        } else if (dsField == null) {
            return new DataSourceField(localField);
        } else if (localField == null) {
            return new DataSourceField(dsField);
        }

        DataSourceField retField = new DataSourceField(localField);
        Map<String, Object> attributes = retField.attributes;
        for (Map.Entry<String, Object> entry : dsField.attributes.entrySet()) {
            String key = entry.getKey();
            if (attributes.get(key) == null) {
                attributes.put(key, entry.getValue());
            }
        }

        retField.setName(localField.getName());
        return retField;
    }

    private Boolean getBoolean(String key) {
        final Object obj = attributes.get(key);
        if (obj == null) return null;
        if (obj instanceof Boolean) return (Boolean)obj;
        final String str = obj.toString().trim();
        if ("true".equalsIgnoreCase(str) || "1".equals(str)) return Boolean.TRUE;
        else if ("false".equalsIgnoreCase(str) || "0".equals(str)) return Boolean.FALSE;
        return null;
    }

    private boolean getboolean(String key, boolean nullEquivalent) {
        return Canvas._booleanValue(getBoolean(key), nullEquivalent);
    }

    private Integer getInteger(String key) {
        Object obj = attributes.get(key);
        if (obj instanceof Number) return Integer.valueOf(((Number)obj).intValue());
        return null;
    }

    public final String getName() {
        return (String)attributes.get("name");
    }

    public void setName(String name) {
        attributes.put("name", name);
    }

    /**
     * Indicates that this field should always be array-valued (Java type {@link java.util.List}).
     * If the value is singular, it will be wrapped in an array.
     * 
     * <p>Specifically for XML serialization and deserialization, <code>multiple:true</code>
     * behaves similarly to the <a href="http://www.zvon.org/xxl/soapReference/Output/type_array.html">SOAP array idiom</a>;
     * that is, there is a "wrapper element" having the field name as its tag name and one
     * or more child "value" elements that specify the values of the array.
     * 
     * <p>The {@link #getChildTagName() childTagName} attribute allows the tag name of the
     * child "value" elements to be customized.
     */
    public Boolean isMultiple() {
        return getBoolean("multiple");
    }

    public void setMultiple(Boolean multiple) {
        attributes.put("multiple", multiple);
    }

    /**
     * Sets nested fields.
     *
     * @param nestedFields the nested fields.
     * @deprecated The preferred approach is to create a DataSource with the nested fields and
     * call {@link #setTypeAsDataSource(DataSource)}.
     */
    @Deprecated
    public void setNestedFields(DataSourceField... nestedFields) {
        final DataSource typeDS = new DataSource("_typeDS" + (typeDSCount++));
        typeDS.setFields(nestedFields);
        setTypeAsDataSource(typeDS);
    }

    /**
     * For a field that is multiple:true, the tag name of each subelement.
     * 
     * <p>If <code>null</code>, then the child tag name is presumed to be "value".
     * 
     * @return String the tag name of subelements.
     */
    public String getChildTagName() {
        return (String)attributes.get("childTagName");
    }
    public void setChildTagName(String childTagName) {
        attributes.put("childTagName", childTagName);
    }

    public void setDerived(boolean derived) {
        attributes.put("derived", derived);
    }

    public boolean isDerived() {
        return getboolean("derived", false);
    }

    public void setHidden(boolean hidden) {
        attributes.put("hidden", hidden);
    }

    public boolean isHidden() {
        return getboolean("hidden", false);
    }

    public Boolean getRequired() {
        return getBoolean("required");
    }

    public void setRequired(Boolean required) {
        attributes.put("required", required);
    }

    /**
     * The name of a registered {@link com.smartgwt.mobile.client.data.SimpleType}.
     * 
     * @return the name of a registered SimpleType representing the type of this field.
     */
    public final String getType() {
        return (String)attributes.get("type");
    }

    public void setType(String type) {
        attributes.put("type", type);
    }

    public final DataSource getTypeAsDataSource() {
        return DataSource.getDataSource(getType());
    }

    /**
     * Sets the field type to a DataSource, which allows you to model nested structures.
     * <p>
     * When the type of the field is a DataSource, then this field will have
     * {@link com.smartgwt.mobile.client.data.RecordList} or {@link com.smartgwt.mobile.client.data.Record}
     * values, depending on whether this field is {@link #isMultiple() multiple:true} or not,
     * respectively. The fields of this DataSource are called "nested fields". The nested fields
     * are referred to when parsing DataSource responses for the DataSource corresponding to this
     * DataSourceField.
     *
     * <h4>JSON responses</h4>
     * <p>When a field has nested fields, then the JSON value for the field should either be an
     * object or an array of objects, depending on the value of <code>multiple</code>. If
     * <code>multiple</code> is true, then the value should be an array; otherwise, the value
     * should be an object.
     *
     * <table border="1" cellpadding="5">
     *   <caption>Example JSON data source responses and corresponding <code>DataSourceField</code> configurations</caption>
     *   <tr>
     *     <th>&nbsp;</th>
     *     <th>Example 1</th>
     *     <th>Example 2</th>
     *   </tr>
     *   <tr>
     *     <th>Data source response</th>
     *     <td>
     *       <pre>{
     *    "response": {
     *        "status": 0,
     *        "startRow": 0,
     *        "endRow": 1,
     *        "totalRows": 1,
     *        "data": [
     *            {
     *                "nestedRecord": {
     *                    "id": 7,
     *                    "title": "Nest #7"
     *                }
     *            }
     *        ]
     *    }
     *}</pre>
     *     </td>
     *     <td>
     *       <pre>{
     *    "response": {
     *        "status": 0,
     *        "startRow": 0,
     *        "endRow": 1,
     *        "totalRows": 1,
     *        "data": [
     *            {
     *                "nestedRecords": [
     *                    { "id": 7, "title": "Nest #7" },
     *                    { "id": 91, "title": "Nest #91" },
     *                    { "id": 12, "title": "Nest #12" },
     *                    { "id": 2, "title": "Nest #2" }
     *                ]
     *            }
     *        ]
     *    }
     *}</pre>
     *     </td>
     *   </tr>
     *   <tr>
     *     <th>{@link #getName() name}</th>
     *     <td><code>"nestedRecord"</code></td>
     *     <td><code>"nestedRecords"</code></td>
     *   </tr>
     *   <tr>
     *     <th>{@link #isMultiple() multiple}</th>
     *     <td>false</td>
     *     <td>true</td>
     *   </tr>
     * </table>
     *
     * <h4>XML responses</h4>
     * <p>If <code>multiple</code> is true, {@link #getChildTagName() childTagName}
     * is still applicable.  If not null, as in Example 3 below, <code>childTagName</code>
     * is the name of subelements that will be parsed as records using the nested fields.
     * Otherwise, as in Example 2 below, subelements of the record element having tag name
     * {@link #getName() name} are collected and processed as the subelements.
     * 
     * <table border="1" cellpadding="5">
     *   <caption>Example XML data source responses and corresponding <code>DataSourceField</code> configurations</caption>
     *   <tr>
     *     <th>&nbsp;</th>
     *     <th>Example 1</th>
     *     <th>Example 2</th>
     *     <th>Example 3</th>
     *   </tr>
     *   <tr>
     *     <th>Data source response</th>
     *     <td>
     *       <pre>&lt;response&gt;
     *  &lt;status&gt;0&lt;/status&gt;
     *  &lt;startRow&gt;0&lt;/startRow&gt;
     *  &lt;endRow&gt;1&lt;/endRow&gt;
     *  &lt;totalRows&gt;1&lt;/totalRows&gt;
     *  &lt;data&gt;
     *    &lt;record&gt;
     *      &lt;nestedRecord&gt;
     *        &lt;id&gt;7&lt;/id&gt;
     *        &lt;title&gt;Nest #7&lt;/title&gt;
     *      &lt;/nestedRecord&gt;
     *    &lt;/record&gt;
     *  &lt;/data&gt;
     *&lt;/response&gt;</pre>
     *     </td>
     *     <td>
     *       <pre>&lt;response&gt;
     *  &lt;status&gt;0&lt;/status&gt;
     *  &lt;startRow&gt;0&lt;/startRow&gt;
     *  &lt;endRow&gt;1&lt;/endRow&gt;
     *  &lt;totalRows&gt;1&lt;/totalRows&gt;
     *  &lt;data&gt;
     *    &lt;record&gt;
     *      &lt;nestedRecord id="7" title="Nest #7"/&gt;
     *      &lt;nestedRecord id="91" title="Nest #91"/&gt;
     *      &lt;nestedRecord id="12" title="Nest #12"/&gt;
     *      &lt;nestedRecord id="2" title="Nest #2"/&gt;
     *    &lt;/record&gt;
     *  &lt;/data&gt;
     *&lt;/response&gt;</pre>
     *     </td>
     *     <td>
     *       <pre>&lt;response&gt;
     *  &lt;status&gt;0&lt;/status&gt;
     *  &lt;startRow&gt;0&lt;/startRow&gt;
     *  &lt;endRow&gt;1&lt;/endRow&gt;
     *  &lt;totalRows&gt;1&lt;/totalRows&gt;
     *  &lt;data&gt;
     *    &lt;record&gt;
     *      &lt;nestedRecords&gt;
     *        &lt;nestedRecord id="7" title="Nest #7"/&gt;
     *        &lt;nestedRecord id="91" title="Nest #91"/&gt;
     *        &lt;nestedRecord id="12" title="Nest #12"/&gt;
     *        &lt;nestedRecord id="2" title="Nest #2"/&gt;
     *      &lt;/nestedRecords&gt;
     *    &lt;/record&gt;
     *  &lt;/data&gt;
     *&lt;/response&gt;</pre>
     *     </td>
     *   </tr>
     *   <tr>
     *     <th>{@link #getName() name}</th>
     *     <td><code>"nestedRecord"</code></td>
     *     <td><code>"nestedRecord"</code></td>
     *     <td><code>"nestedRecords"</code></td>
     *   </tr>
     *   <tr>
     *     <th>{@link #isMultiple() multiple}</th>
     *     <td>false</td>
     *     <td>true</td>
     *     <td>true</td>
     *   </tr>
     *   <tr>
     *     <th>{@link #getChildTagName() childTagName}</th>
     *     <td><em>N/A</em></td>
     *     <td>null</td>
     *     <td><code>"nestedRecord"</code></td>
     *   </tr>
     * </table>
     * 
     * @see #isMultiple()
     * @see #getChildTagName()
     */
    public void setTypeAsDataSource(DataSource typeDS) {
        setType(typeDS.getID());
    }

    public final String getEditorType() {
        return (String)attributes.get("editorType");
    }

    public final void setEditorType(String editorType) {
        attributes.put("editorType", editorType);
    }

    public final String getHint() {
        return (String)attributes.get("hint");
    }

    public void setHint(String hint) {
        attributes.put("hint", hint);
    }

    public void setLength(Long length) {
        attributes.put("length", length);
    }

    public final Integer getLength() {
        return getInteger("length");
    }

    public void setTitle(String title) {
        attributes.put("title", title);
    }

    public final String getTitle() {
        return (String)attributes.get("title");
    }

    public final Boolean getShowTitle() {
        return getBoolean("showTitle");
    }

    public void setShowTitle(Boolean showTitle) {
        attributes.put("showTitle", showTitle);
    }

    public void setPrimaryKey(Boolean pk) {
        attributes.put("primaryKey", pk);
    }

    public Boolean isPrimaryKey() {
        return getBoolean("primaryKey");
    }

    public void setForeignKey(String fk) {
        attributes.put("foreignKey", fk);
    }

    public String getForeignKey() {
        return (String)attributes.get("foreignKey");
    }

    public void setInapplicable(Boolean inapplicable) {
        attributes.put("inapplicable", inapplicable);
    }

    public Boolean isInapplicable()  {
        return getBoolean("inapplicable");
    }

    public final LinkedHashMap<?, String> getValueMap() {
        @SuppressWarnings("unchecked")
        final LinkedHashMap<?, String> m = (LinkedHashMap<?, String>)attributes.get("valueMap");
        return m;
    }

    public void setValueMap(LinkedHashMap<?, String> valueMap) {
        attributes.put("valueMap", valueMap);
    }

    public final Boolean getCanEdit() {
        return getBoolean("canEdit");
    }

    public final void setCanEdit(Boolean canEdit) {
        attributes.put("canEdit", canEdit);
    }

    public final String getOnText() {
        return (String)attributes.get("onText");
    }

    public final void setOnText(String onText) {
        attributes.put("onText", onText);
    }

    public final String getOffText() {
        return (String)attributes.get("offText");
    }

    public final void setOffText(String offText) {
        attributes.put("offText", offText);
    }

    public final String getSubrecordsProperty() {
        return (String)attributes.get("subrecordsProperty");
    }

    public final void setSubrecordsProperty(String subrecordsProperty) {
        attributes.put("subrecordsProperty", subrecordsProperty);
    }
}
