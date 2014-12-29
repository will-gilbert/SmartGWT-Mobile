package com.smartgwt.mobile.client.widgets.form.fields;

import com.smartgwt.mobile.client.widgets.form.DataManager;
import com.smartgwt.mobile.client.widgets.form.validator.Validator;

/**
 * Defines the interface necessary for this object to be used as a field within a
 * {@link com.smartgwt.mobile.client.widgets.form.DynamicForm}.
 * 
 * <p>Any widget that implements this interface can be added directly to a <code>DynamicForm</code>.
 * 
 * @see com.smartgwt.mobile.client.widgets.form.DynamicForm#setFields(com.smartgwt.mobile.client.widgets.Canvas...)
 */
public interface HasDataValue {

    public Boolean getCanEdit();

    /**
     * If non-<code>null</code>, explicitly sets the editability of this field. A value of
     * {@link java.lang.Boolean#TRUE} means that this field is editable. A value of
     * {@link java.lang.Boolean#FALSE} means that this field is read-only.
     * 
     * <p>If not explicitly set, then this field should consider itself editable if its
     * {@link com.smartgwt.mobile.client.widgets.form.DataManager} is editable. See
     * {@link com.smartgwt.mobile.client.widgets.form.DataManager#getCanEdit()}.
     * 
     * @param canEdit an explicit override.
     */
    public void setCanEdit(Boolean canEdit);

    /**
     * Sets the associated {@link com.smartgwt.mobile.client.widgets.form.DataManager} instance.
     * 
     * <p>This <code>HasDataValue</code> instance uses the <code>DataManager</code> to post
     * notifications of changes to its value caused by user action.
     * 
     * @param dataManager the associated <code>DataManager</code>.
     * @see com.smartgwt.mobile.client.widgets.form.DataManager#storeValue(String, Object)
     */
    public void setDataManager(DataManager dataManager);

    /**
     * Returns this field's name.
     * 
     * @return this field's name.
     */
    public String getFieldName();

    /**
     * If <code>null</code> or {@link java.lang.Boolean#TRUE}, then this field's title will
     * be shown by the {@link com.smartgwt.mobile.client.widgets.form.DynamicForm}.
     * 
     * @return whether to show a title for this field.
     */
    public Boolean getShowTitle();

    public Boolean getStopOnError();

    /**
     * Returns this field's title.
     * 
     * @return this field's title.
     */
    public String getTitle();

    public Validator[] getValidators();

    /**
     * Compares two values for equality.
     * 
     * @param value1 first value.
     * @param value2 second value.
     * @return <code>true</code> if <code>value1</code> is equal to <code>value2</code> in the
     * context of this <code>HasDataValue</code>; <code>false</code> otherwise.
     */
    public boolean compareValues(Object value1, Object value2);

    /**
     * Called by the <code>DataManager</code> to notify this <code>HasDataValue</code> that
     * the <code>DataManager</code>'s value for this <code>HasDataValue</code> was changed
     * and that this <code>HasDataValue</code> needs to update its display field to the given
     * value.
     * 
     * <p><b>NOTE:</b> An implementation must not call the associated <code>DataManager</code>'s
     * {@link com.smartgwt.mobile.client.widgets.form.DataManager#storeValue(String, Object)}
     * method.
     * 
     * @param value the new value.
     */
    public void showValue(Object value);

    /**
     * Called by the {@link com.smartgwt.mobile.client.widgets.form.DataManager} to notify this <code>HasDataValue</code> that
     * the <code>DataManager</code>'s {@link com.smartgwt.mobile.client.widgets.form.DataManager#getCanEdit() canEdit}
     * attribute changed and that this <code>HasDataValue</code>'s editability may have changed
     * as a result.
     */
    public void updateCanEdit();
}
