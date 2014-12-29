package com.smartgwt.mobile.client.widgets.form.fields;

import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.internal.widgets.form.fields.CanCopyFieldConfiguration;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.form.DataManager;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.validator.Validator;

/**
 * Provides a skeletal implementation of the {@link com.smartgwt.mobile.client.widgets.form.fields.HasDataValue}
 * interface, to minimize the effort required to implement this interface and also to minimize
 * the impact of a change to the <code>HasDataValue</code> interface.
 */
public abstract class AbstractHasDataValue extends Canvas implements HasDataValue, CanCopyFieldConfiguration {

    private Boolean canEdit;
    private DataManager dataManager;
    private String fieldName;
    private Boolean showTitle;
    private Boolean stopOnError;
    private String title;
    private Validator[] validators;

    protected AbstractHasDataValue(String fieldName) {
        if (fieldName == null) throw new NullPointerException("`fieldName' cannot be `null'.");
        this.fieldName = fieldName;
    }

    protected AbstractHasDataValue(String fieldName, String title) {
        this(fieldName);
        internalSetTitle(title);
    }

    @Override
    public final Boolean getCanEdit() {
        return canEdit;
    }

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
        updateCanEdit();
    }

    @Override
    public final String getFieldName() {
        return fieldName;
    }

    protected void setFieldName(String fieldName) {
        if (dataManager != null) throw new IllegalStateException("The field name cannot be changed while a DataManager is set.");
        this.fieldName = fieldName;
    }

    protected final Boolean getRealShowTitle() {
        return showTitle;
    }

    @Override
    public Boolean getShowTitle() {
        return showTitle == null ? Boolean.valueOf(title != null) : showTitle;
    }

    public void setShowTitle(Boolean showTitle) {
        this.showTitle = showTitle;
    }

    @Override
    public final Boolean getStopOnError() {
        return stopOnError;
    }

    public void setStopOnError(Boolean stopOnError) {
        this.stopOnError = stopOnError;
    }

    @Override
    public final String getTitle() {
        return title;
    }

    private void internalSetTitle(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        internalSetTitle(title);
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
    }

    public abstract void showValue(Object value);

    /**
     * Default implementation of {@link com.smartgwt.mobile.client.widgets.form.fields.HasDataValue#updateCanEdit()}.
     * If overridden, be sure to call <code>super.updateCanEdit()</code>.
     */
    @Override
    public void updateCanEdit() {
        /*empty*/
    }
}
