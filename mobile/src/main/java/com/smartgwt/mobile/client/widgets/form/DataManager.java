package com.smartgwt.mobile.client.widgets.form;

import java.util.Map;

public interface DataManager {

    public Boolean getCanEdit();

    /**
     * Sets the value of a field, storing the value in the <code>DataManager</code>'s values map
     * and calling {@link com.smartgwt.mobile.client.widgets.form.fields.HasDataValue#showValue(Object)}
     * to update the corresponding {@link com.smartgwt.mobile.client.widgets.form.fields.HasDataValue}.
     * 
     * @param fieldName field name.
     * @param value new value of the field.
     * @see #storeValue(String, Object)
     */
    public void setValue(String fieldName, Object value);

    public Map<String, ?> getValues();

    public void setValues(Map<String, ?> values);

    /**
     * Clears this <code>DataManager</code>'s stored value for the named field. If <code>updateField</code>
     * is <code>true</code>, then {@link com.smartgwt.mobile.client.widgets.form.fields.HasDataValue#showValue(Object)
     * showValue(null)} will be called on the corresponding <code>HasDataValue</code>.
     * 
     * @param fieldName field name.
     * @param updateField whether to call showValue() on the corresponding <code>HasDataValue</code>.
     */
    public void clearValue(String fieldName, boolean updateField);

    /**
     * Clears all stored values.
     * 
     * @param updateFields whether to call {@link com.smartgwt.mobile.client.widgets.form.fields.HasDataValue#showValue(Object)
     * showValue(null)} on each <code>HasDataValue</code>.
     */
    public void clearValues(boolean updateFields);

    /**
     * Stores the given value in the <code>DataManager</code>'s values map.
     * 
     * <p><b>NOTE:</b> This method will not update the corresponding {@link com.smartgwt.mobile.client.widgets.form.fields.HasDataValue}
     * by calling the {@link com.smartgwt.mobile.client.widgets.form.fields.HasDataValue#showValue(Object)}
     * method. <code>storeValue()</code> is intended to be used by the <code>HasDataValue</code>
     * objects to notify this <code>DataManager</code> of a change in value. For the
     * variants that <em>do</em> call HasDataValue.showValue(), see {@link #setValue(String, Object)}
     * and {@link #setValues(Map)}.
     * 
     * @see #setValue(String, Object)
     * @see #setValues(Map)
     */
    public void storeValue(String fieldName, Object value);
}
