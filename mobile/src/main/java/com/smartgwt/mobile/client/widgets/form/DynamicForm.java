package com.smartgwt.mobile.client.widgets.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.Criteria;
import com.smartgwt.mobile.client.data.Criterion;
import com.smartgwt.mobile.client.data.DSCallback;
import com.smartgwt.mobile.client.data.DSRequest;
import com.smartgwt.mobile.client.data.DSResponse;
import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.internal.Array;
import com.smartgwt.mobile.client.internal.util.ObjectUtil;
import com.smartgwt.mobile.client.internal.widgets.DataBoundEditorComponent;
import com.smartgwt.mobile.client.internal.widgets.form.fields.CanCopyFieldConfiguration;
import com.smartgwt.mobile.client.internal.widgets.form.fields.NativeDateItem;
import com.smartgwt.mobile.client.internal.widgets.form.fields.NativeDateTimeItem;
import com.smartgwt.mobile.client.internal.widgets.form.fields.NativeTimeItem;
import com.smartgwt.mobile.client.theme.FormCssResource;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.types.DSOperationType;
import com.smartgwt.mobile.client.types.DateDisplayFormat;
import com.smartgwt.mobile.client.types.Encoding;
import com.smartgwt.mobile.client.types.FormMethod;
import com.smartgwt.mobile.client.types.FormStyle;
import com.smartgwt.mobile.client.types.TimeDisplayFormat;
import com.smartgwt.mobile.client.util.DateUtil;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.DataBoundComponent;
import com.smartgwt.mobile.client.widgets.form.events.FormSubmitFailedEvent;
import com.smartgwt.mobile.client.widgets.form.events.FormSubmitFailedHandler;
import com.smartgwt.mobile.client.widgets.form.events.HasFormSubmitFailedHandlers;
import com.smartgwt.mobile.client.widgets.form.events.HasItemChangedHandlers;
import com.smartgwt.mobile.client.widgets.form.events.HasSubmitValuesHandlers;
import com.smartgwt.mobile.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.mobile.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.mobile.client.widgets.form.events.SubmitValuesEvent;
import com.smartgwt.mobile.client.widgets.form.events.SubmitValuesHandler;
import com.smartgwt.mobile.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.mobile.client.widgets.form.fields.DateItem;
import com.smartgwt.mobile.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.mobile.client.widgets.form.fields.EmailItem;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.form.fields.HasDataValue;
import com.smartgwt.mobile.client.widgets.form.fields.HiddenItem;
import com.smartgwt.mobile.client.widgets.form.fields.PasswordItem;
import com.smartgwt.mobile.client.widgets.form.fields.PhoneItem;
import com.smartgwt.mobile.client.widgets.form.fields.SearchItem;
import com.smartgwt.mobile.client.widgets.form.fields.SelectItem;
import com.smartgwt.mobile.client.widgets.form.fields.SliderItem;
import com.smartgwt.mobile.client.widgets.form.fields.SwitchItem;
import com.smartgwt.mobile.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.mobile.client.widgets.form.fields.TextItem;
import com.smartgwt.mobile.client.widgets.form.fields.TimeItem;
import com.smartgwt.mobile.client.widgets.form.fields.UrlItem;

/**
 * A form component that displays a collection of form items (widgets implementing the
 * {@link com.smartgwt.mobile.client.widgets.form.fields.HasDataValue} interface) which represent
 * user input controls. The most common items added to a <code>DynamicForm</code> are
 * {@link com.smartgwt.mobile.client.widgets.form.fields.TextItem}s or other components extending
 * {@link com.smartgwt.mobile.client.widgets.form.fields.FormItem}, but SmartGWT.mobile also
 * supports adding a {@link com.smartgwt.mobile.client.widgets.tableview.TableView} to a
 * <code>DynamicForm</code> with no additional configuration. See the Forms &rarr; Inputs
 * Showcase sample for an example of a basic form, and the Forms &rarr; Embedded TableView
 * sample for an example of adding a <code>TableView</code> to a <code>DynamicForm</code>.
 * <p>
 * Just like Smart&nbsp;GWT, a <code>DynamicForm</code> can be bound to a {@link com.smartgwt.mobile.client.data.DataSource}.
 * When using <code>DataSource</code> binding, you can also add additional form items not specified
 * by the <code>DataSource</code>, or rely on autoconfiguration of fields from the <code>DataSource</code>,
 * optionally overriding any properties on the automatically generated form items without having
 * to re-declare any information that comes from the <code>DataSource</code>. To better
 * accommodate mobile devices, <code>DynamicForm</code> autoconfiguration may display certain
 * HTML5 input types by default according to the rules:
 * <table border="1" cellpadding="5">
 * <tbody>
 * <tr>
 *   <th>Field {@link com.smartgwt.mobile.client.data.DataSourceField#getEditorType() editorType}</th>
 *   <th>Field {@link com.smartgwt.mobile.client.data.DataSourceField#getType() type}</th>
 *   <th>Result</th>
 * </tr>
 * <tr><td>"TextItem"</td><td>"date"</td><td>HTML5 date picker (input type="date")</td></tr>
 * <tr><td>"TextItem"</td><td>"time"</td><td>HTML5 time picker (input type="time")</td></tr>
 * <tr><td>"TextItem"</td><td>"datetime"</td><td>HTML5 date &amp; time picker (input type="datetime")</td></tr>
 * </tbody>
 * </table>
 * Note that certain mobile platforms do not support all of the HTML5 input types. See the
 * Forms &rarr; Autoconfigured from DataSource Showcase sample for an example of autoconfiguration.
 */
public class DynamicForm extends DataBoundEditorComponent<RecordList> implements DataManager,
HasFormSubmitFailedHandlers, HasItemChangedHandlers, HasSubmitValuesHandlers {

    @SGWTInternal
    public static final FormCssResource _CSS = ThemeResources.INSTANCE.formCSS();

    // Implements SmartClient Array.equals()
    @SGWTInternal
    public static boolean _compareLists(List<?> value1, List<?> value2) {
        if (value1 == null) return (value2 == null);
        else if (value2 == null) return false;
        assert (value1 != null && value2 != null);

        final int value1_size = value1.size();
        if (value2.size() != value1_size) return false;
        for (int i = 0; i < value1_size; ++i) {
            if (!ObjectUtil.abstractCompareValues(value1.get(i), value2.get(i))) return false;
        }
        return true;
    }

    /**
     * Do two field values match?  Used wherever we need to compare field values.
     * Handles all expected data types.
     * Used to detect changes to values (e.g. <code>valuesHaveChanged()</code>).
     */
    @SGWTInternal
    public static boolean _compareValues(Object value1, Object value2) {
        if (ObjectUtil.abstractCompareValues(value1, value2)) return true;
        else if (value1 == null) return (value2 == null);
        else if (value2 == null) {
            assert value1 != null;
            return false;
        }
        assert value1 != null && value2 != null;

        if (Array.isArray(value1)) value1 = Array.asList(value1);
        if (Array.isArray(value2)) value2 = Array.asList(value2);

        if (value1 instanceof Date && value2 instanceof Date) {
            return (DateUtil.compareDates((Date)value1, (Date)value2) == 0);
        } else if (value1 instanceof List && value2 instanceof List) {
            return _compareLists((List<?>)value1, (List<?>)value2);
        } else {
            if (value1 instanceof Map && value2 instanceof Map) {
                final Map<?, ?> map1 = ((Map<?, ?>)value1),
                        map2 = (Map<?, ?>)value2;
                for (final Map.Entry<?, ?> e : map1.entrySet()) {
                    final Object attr = e.getKey();
                    if (!ObjectUtil.abstractCompareValues(map2.get(attr), e.getValue())) return false;
                }
                // If `map2' has any keys that `map1' does not, then they are unequal.
                for (final Map.Entry<?, ?> e : map2.entrySet()) {
                    final Object attr = e.getKey();
                    if (!map1.containsKey(attr)) return false;
                }
            }
        }
        return false;
    }

    private static native FormElement createFormElement() /*-{
        var formElem = $doc.createElement("form");
        formElem.method = "POST";
        formElem.onsubmit = $entry(function () {
            var ret = @com.smartgwt.mobile.client.widgets.form.DynamicForm::handleNativeSubmit(Lcom/google/gwt/dom/client/FormElement;)(this);
            if (@com.smartgwt.mobile.client.util.JSOHelper::isJavaBoolean(Ljava/lang/Object;)(ret)) {
                ret = ret.@java.lang.Boolean::booleanValue()();
                if (ret === false) return false;
            }
        });
        return formElem;
    }-*/;

    private static Boolean handleNativeSubmit(FormElement formElem) {
        if (formElem != null) {
            final EventListener eventListener = DOM.getEventListener(formElem.<com.google.gwt.user.client.Element>cast());
            if (eventListener instanceof DynamicForm) {
                return ((DynamicForm)eventListener)._handleNativeSubmit();
            }
        }
        return null;
    }

    public static boolean _valuesHaveChanged(DynamicForm form, Map<?, ?> values, Map<?, ?> oldValues) {
        boolean changed = false;
        for (final Map.Entry<?, ?> e : values.entrySet()) {
            final Object prop = e.getKey();
            if (prop == null) continue;
            final Canvas item = form.getItem(prop.toString());
            if (item instanceof HasDataValue) {
                changed = !((HasDataValue)item).compareValues(e.getValue(), oldValues.get(prop));
            } else {
                final Object value = e.getValue(),
                        oldValue = oldValues.get(prop);
                if (value instanceof Map && oldValue instanceof Map) {
                    boolean innerChanged = _valuesHaveChanged(form, (Map<?, ?>)value, (Map<?, ?>)oldValue);
                    if (innerChanged) {
                        changed = true;
                    }
                } else {
                    changed = !_compareValues(value, oldValue);
                }
            }
            if (changed) return true;
        }
        return changed;
    }

    /**
     * All fields being shown by this <code>DynamicForm</code>, whether constructed from
     * {@link com.smartgwt.mobile.client.data.DataSourceField}s or specifically added by
     * {@link #setFields(Canvas...)}.
     */
    private final transient Map<String, Canvas> allFields = new LinkedHashMap<String, Canvas>();

    private final transient DynamicFormImpl impl = GWT.create(DynamicFormImpl.class);

    private String action;
    private Boolean canEdit = null;
    private DateDisplayFormat dateFormatter;
    private DateDisplayFormat datetimeFormatter;
    private Encoding encoding;

    /**
     * The fields that have been specifically added via {@link #setFields(Canvas...)}.
     */
    private Map<String, Canvas> fields = null;

    private final FormElement formElem;
    private FormStyle formStyle = FormStyle.STYLE1;

    private Record oldValues;
    private FormMethod method;
    private List<String> rememberedDefault;
    private DSOperationType saveOperationType;
    private String target;
    private TimeDisplayFormat timeFormatter;
    private final Map<String, Object> values = new HashMap<String, Object>();

    public DynamicForm() {
        formElem = createFormElement();
        DOM.setEventListener(formElem.<com.google.gwt.user.client.Element>cast(), this);
        formElem.setClassName(_CSS.style1FormClass());
        getElement().appendChild(formElem);
    }

    @Override
    public void destroy() {
        impl.destroyImpl(this);
        super.destroy();
    }

    public final String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
        formElem.setAction(action);
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
        readdFields();
    }

    @Override
    public void setUseAllDataSourceFields(Boolean useAllDataSourceFields) {
        if (_isDifferent(getUseAllDataSourceFields(), useAllDataSourceFields, false)) {
            super.setUseAllDataSourceFields(useAllDataSourceFields);
            readdFields();
        }
    }

    public final DateDisplayFormat getDateFormatter() {
        return dateFormatter;
    }

    public void setDateFormatter(DateDisplayFormat dateFormatter) {
        if (this.dateFormatter != dateFormatter) {
            this.dateFormatter = dateFormatter;
            for (final Canvas field : allFields.values()) {
                if (!(field instanceof FormItem)) continue;
                final FormItem formItem = (FormItem)field;
                formItem._updateDateFormatter();
            }
        }
    }

    public final DateDisplayFormat getDatetimeFormatter() {
        return datetimeFormatter;
    }

    public void setDatetimeFormatter(DateDisplayFormat datetimeFormatter) {
        if (this.datetimeFormatter != datetimeFormatter) {
            this.datetimeFormatter = datetimeFormatter;
            for (final Canvas field : allFields.values()) {
                if (!(field instanceof FormItem)) continue;
                final FormItem formItem = (FormItem)field;
                formItem._updateDateFormatter();
            }
        }
    }

    public final Encoding getEncoding() {
        return encoding;
    }

    public void setEncoding(Encoding encoding) {
        this.encoding = encoding;
        if (encoding == null) encoding = Encoding.NORMAL;
        switch (encoding) {
            case NORMAL:
                formElem.setEnctype("application/x-www-form-urlencoded");
                break;
            case MULTIPART:
                formElem.setEnctype("multipart/form-data");
                break;
        }
    }

    @SGWTInternal
    public final FormElement _getForm() {
        return formElem;
    }

    /**
     * The display style of this <code>DynamicForm</code>.
     * 
     * <p>{@link com.smartgwt.mobile.client.types.FormStyle#STYLE1 FormStyle.STYLE1} makes the
     * form look like a grouped <code>TableView</code>. {@link com.smartgwt.mobile.client.types.FormStyle#STYLE2 FormStyle.STYLE2}
     * makes the form look like the Edit Info form of the iPhone Contacts app.
     * 
     * @return the display style. Default value: <code>FormStyle.STYLE1</code>
     */
    public final FormStyle getFormStyle() {
        return formStyle;
    }

    public void setFormStyle(FormStyle formStyle) {
        final FormStyle newFormStyle = formStyle == null ? FormStyle.STYLE1 : formStyle;
        if (this.formStyle != newFormStyle) {
            this.formStyle = newFormStyle;
            switch (newFormStyle) {
                case STYLE1:
                    formElem.removeClassName(_CSS.style2FormClass());
                    formElem.addClassName(_CSS.style1FormClass());
                    break;
                case STYLE2:
                    formElem.removeClassName(_CSS.style1FormClass());
                    formElem.addClassName(_CSS.style2FormClass());
                    break;
            }
        }
    }

    @Override
    public final Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        if ((this.canEdit == null && canEdit != null) ||
            (this.canEdit != null && (canEdit == null ||
                                      this.canEdit.booleanValue() != canEdit.booleanValue())))
        {
            this.canEdit = canEdit;
            for (final Canvas field : allFields.values()) {
                ((HasDataValue)field).updateCanEdit();
            }
        }
    }

    @SGWTInternal
    final Map<String, Canvas> _getAllFieldsMap() {
        return allFields;
    }

    public final Canvas getField(String name) {
        return allFields.get(name);
    }

    public final Canvas getItem(String name) {
        return getField(name);
    }

    public final Canvas[] getFields() {
        return fields == null ? null : fields.values().toArray(new Canvas[fields.size()]);
    }

    public final Canvas[] getItems() {
        return getFields();
    }

    /**
     * Sets the form's fields.
     * 
     * <p><b>NOTE:</b> Each <code>Canvas</code> instance must implement the
     * {@link com.smartgwt.mobile.client.widgets.form.fields.HasDataValue} interface.
     * 
     * @param fields the form's fields.
     * @throws IllegalArgumentException if one of the fields does not implement the <code>HasDataValue</code> interface
     * or if two different fields have the same name.
     * @throws NullPointerException if one of the fields does not have a name.
     */
    public void setFields(Canvas... fields) {
        if (fields != null) {
            for (int i = 0; i < fields.length; ++i) {
                if (!(fields[i] instanceof HasDataValue)) throw new IllegalArgumentException("`fields[" + i + "]' does not implement the `HasDataValue' interface.");
            }
        }

        final Map<String, Canvas> fieldsMap;
        if (fields == null) {
            fieldsMap = null;
        } else {
            fieldsMap = new LinkedHashMap<String, Canvas>();
            for (int i = 0; i < fields.length; ++i) {
                final Canvas field = fields[i];
                final String fieldName = ((HasDataValue)field).getFieldName();
                if (fieldName == null) throw new NullPointerException("`fields[" + i + "]' needs a name.");
                final Canvas prevField = fieldsMap.put(fieldName, field);
                if (prevField != null) {
                    if (prevField == field) SC.logWarn("DynamicForm.setFields(): `fields' contains " + field + " twice.");
                    else throw new IllegalArgumentException("Different fields were specified having the same name, '" + fieldName + "'.");
                }
            }
        }

        if (this.fields != null) {
            for (final Iterator<Map.Entry<String, Canvas>> it = this.allFields.entrySet().iterator(); it.hasNext(); /*empty*/) {
                final Map.Entry<String, Canvas> e = it.next();
                if (this.fields.containsKey(e.getKey()) && (fieldsMap == null || !fieldsMap.containsKey(e.getKey()))) {
                    it.remove();
                    unaddField(e.getValue());
                }
            }
        }
        this.fields = fieldsMap;
        readdFields();
    }

    private void readdFields() {
        final boolean useAllDataSourceFields = (getUseAllDataSourceFields() == null ? false : getUseAllDataSourceFields().booleanValue());
        values.clear();
        for (final Canvas field : allFields.values()) {
            ((HasDataValue)field).setDataManager(null);
            // Do not call unaddField() because `field' may be an autoconfigured field that will
            // be re-added below.
            impl.removeField(this, field);
        }

        final DataSource dataSource = getDataSource();
        final DataSourceField[] dataSourceFields = dataSource == null ? null : dataSource.getFields();
        if (dataSource != null && dataSourceFields != null) {
            // Un-add autoconfigured fields that are not used anymore.
            for (final Iterator<Map.Entry<String, Canvas>> it = allFields.entrySet().iterator(); it.hasNext(); /*empty*/) {
                final Map.Entry<String, Canvas> e = it.next();
                final Canvas overriddenField;
                if ((fields == null || (overriddenField = fields.get(e.getKey())) == null || overriddenField.getClass().equals(FormItem.class)) &&
                    dataSource.getField(e.getKey()) == null)
                {
                    it.remove();
                    unaddField(e.getValue());
                }
            }
        }
        if (fields != null) {
            // Un-add fields for which there is now an override in `this.fields'.
            for (final Iterator<Map.Entry<String, Canvas>> it = allFields.entrySet().iterator(); it.hasNext(); /*empty*/) {
                final Map.Entry<String, Canvas> e = it.next();
                final Canvas overriddenField = fields.get(e.getKey());
                if (overriddenField != null && !overriddenField.getClass().equals(FormItem.class)) {
                    it.remove();
                    unaddField(e.getValue());
                }
            }
        }
        allFields.clear();
        if (fields != null) {
            allFields.putAll(fields);
        }

        if (dataSourceFields != null) {
            final Map<String, Canvas> autoconfiguredFields = new LinkedHashMap<String, Canvas>();
            for (final DataSourceField dataSourceField : dataSourceFields) {
                final String fieldName = dataSourceField.getName(),
                             title = dataSourceField.getTitle();
                if (fieldName == null) continue;
                Canvas field = allFields.get(fieldName);
                if (field instanceof FormItem && FormItem.class.equals(field.getClass())) {
                    field = null;
                }
                if (field == null && (useAllDataSourceFields || allFields.containsKey(fieldName))) {
                    final Boolean hidden = dataSourceField.isHidden();
                    if (hidden != null && hidden.booleanValue()) continue;

                    final String editorType = dataSourceField.getEditorType(),
                            type = dataSourceField.getType();
                    // Handle the editorType first because that overrides selection based on
                    // the type or other configuration.
                    if (editorType != null) {
                        if ("EmailItem".equals(editorType)) {
                            field = new EmailItem(fieldName, title);
                        } else if ("HiddenItem".equals(editorType)) {
                            field = new HiddenItem(fieldName);
                        } else if ("PasswordItem".equals(editorType)) {
                            field = new PasswordItem(fieldName, title);
                        } else if ("PhoneItem".equals(editorType)) {
                            field = new PhoneItem(fieldName, title);
                        } else if ("SearchItem".equals(editorType)) {
                            field = new SearchItem(fieldName, title);
                        } else if ("SelectItem".equals(editorType)) {
                            field = new SelectItem(fieldName, title);
                        } else if ("TextAreaItem".equals(editorType)) {
                            field = new TextAreaItem(fieldName, title);
                        } else if ("UrlItem".equals(editorType)) {
                            field = new UrlItem(fieldName, title);
                        } else if ("DateItem".equals(editorType)) {
                            field = new DateItem(fieldName, title);
                        } else if ("TimeItem".equals(editorType)) {
                            field = new TimeItem(fieldName, title);
                        } else if ("DateTimeItem".equals(editorType)) {
                            field = new DateTimeItem(fieldName, title);
                        } else if ("TextItem".equals(editorType)) {
                            if ("date".equals(type)) {
                                field = new NativeDateItem(fieldName, title);
                            } else if ("time".equals(type)) {
                                field = new NativeTimeItem(fieldName, title);
                            } else if ("datetime".equals(type)) {
                                field = new NativeDateTimeItem(fieldName, title);
                            }
                        }
                    }
                    if (field == null) {
                        if ("boolean".equals(type)) {
                            if ("CheckboxItem".equals(editorType)) {
                                field = new CheckboxItem(fieldName, title);
                            } else {
                                field = new SwitchItem(fieldName, title);
                            }
                        } else if ("float".equals(type) || "double".equals(type)) {
                            if ("TextItem".equals(editorType)) {
                                field = new TextItem(fieldName, type);
                            } else {
                                field = new SliderItem(fieldName, title);
                            }
                        } else if ("password".equals(type)) {
                            field = new PasswordItem(fieldName, title);
                        } else if ("date".equals(type)) {
                            field = new DateItem(fieldName, title);
                        } else if ("time".equals(type)) {
                            field = new TimeItem(fieldName, title);
                        } else if ("datetime".equals(type)) {
                            field = new DateTimeItem(fieldName, title);
                        } else {
                            field = new TextItem(fieldName, title);
                        }
                    }
                }
                if (field instanceof CanCopyFieldConfiguration) {
                    ((CanCopyFieldConfiguration)field)._copyFieldConfiguration(dataSourceField);
                }
                if (field != null) autoconfiguredFields.put(fieldName, field);
            }

            allFields.putAll(autoconfiguredFields);
        }

        if (fields != null) {
            for (final Iterator<Map.Entry<String, Canvas>> it = fields.entrySet().iterator(); it.hasNext(); /*empty*/) {
                final Map.Entry<String, Canvas> e = it.next();
                Canvas field = e.getValue();
                if (field instanceof FormItem && field.getClass().equals(FormItem.class)) {
                    field = allFields.get(e.getKey());
                    if (field == null) field = new TextItem(e.getKey());
                    e.setValue(field);
                }
                allFields.put(e.getKey(), field);
            }

            for (final Canvas field : allFields.values()) {
                ((HasDataValue)field).setDataManager(this);
            }
        }

        impl.addFields(this, allFields.values());
    }

    private void unaddField(Canvas canvas) {
        impl.removeField(this, canvas);
        // TODO If the field is autocreated, should we destroy() it?
    }

    /**
     * Alias for {@link #setFields(Canvas...)}.
     * @param items the form's fields.
     */
    public final void setItems(Canvas... fields) {
        setFields(fields);
    }

    public final FormMethod getMethod() {
        return method;
    }

    public void setMethod(FormMethod method) {
        this.method = method;
        if (method == null) method = FormMethod.POST;
        switch (method) {
            case GET:
                formElem.setMethod("GET");
                break;
            case POST:
                formElem.setMethod("POST");
                break;
        }
    }

    public final DSOperationType getSaveOperationType() {
        return saveOperationType;
    }

    public void setSaveOperationType(DSOperationType saveOperationType) {
        if (saveOperationType == null) saveOperationType = DSOperationType.ADD;
        else if (saveOperationType != DSOperationType.ADD && saveOperationType != DSOperationType.UPDATE) {
            SC.logWarn("Invalid saveOperationType:'" + saveOperationType.getValue() + "'. Assuming 'add'.");
            saveOperationType = DSOperationType.ADD;
        }
        this.saveOperationType = saveOperationType;
        if (saveOperationType == DSOperationType.ADD) oldValues = null;
    }

    public final String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
        formElem.setTarget(target);
    }

    public final TimeDisplayFormat getTimeFormatter() {
        return timeFormatter;
    }

    public void setTimeFormatter(TimeDisplayFormat timeFormatter) {
        this.timeFormatter = timeFormatter;
    }

    @SGWTInternal
    final void _add(Widget child, com.google.gwt.user.client.Element container) {
        super.add(child, container);
    }

    @SGWTInternal
    public <T extends Map<String, Object>> T _putValues(T values) {
        if (values != null) {
            values.putAll(this.values);
        }
        return values;
    }

    @Override
    public final Map<String, Object> getValues() {
        return _putValues(new HashMap<String, Object>());
    }

    public final Criteria getValuesAsCriteria() {
       final Criteria criteria = new Criteria();
       assert allFields != null;
       for (final Map.Entry<String, Canvas> e : allFields.entrySet()) {
           final Canvas field = e.getValue();
           if (field instanceof FormItem) {
               final Criterion criterion = ((FormItem)field).getCriterion();
               if (criterion != null) {
                   criteria.addCriteria(criterion.getFieldName(), criterion.getValue());
                   continue;
               }
           }
           criteria.addCriteria(e.getKey(), values.get(e.getKey()));
       }
       return criteria;
    }

    public final Record getValuesAsRecord() {
        return _putValues(new Record());
    }

    public void rememberValues() {
        final Map<String, Object> values = getValues();
        final Record oldVals = new Record();
        final List<String> rememberedDefault = new ArrayList<String>();
        DataBoundComponent._duplicateValues(this, values, oldVals, rememberedDefault);
        this.oldValues = oldVals;
        this.rememberedDefault = rememberedDefault;
    }

    public void clearErrors() {
        impl.clearErrors(this);
    }

    @Override
    public void setValue(String fieldName, Object value) {
        values.put(fieldName, value);
        Canvas field = allFields.get(fieldName);
        if (field != null) {
            ((HasDataValue)field).showValue(value);
        }
    }

    @Override
    public void setValues(Map<String, ?> values) {
        assert allFields != null;
        // Reset fields for which there is no value in `values'.
        for (final Map.Entry<String, Canvas> e : allFields.entrySet()) {
            if (values == null || !values.containsKey(e.getKey())) {
                final Canvas field = e.getValue();
                this.values.put(e.getKey(), null);
                ((HasDataValue)field).showValue(null);
            }
        }

        if (values != null) {
            for (final Map.Entry<String, ?> e : values.entrySet()) {
                setValue(e.getKey(), e.getValue());
            }
        }

        // remember the values so we can undo things
        rememberValues();
    }

    @Override
    public void storeValue(String fieldName, Object value) {
        values.put(fieldName, value);
    }

    @SGWTInternal
    public void _clearItemValue(HasDataValue item) {
        clearValue(item.getFieldName(), false);
    }

    public final void clearValue(String fieldName) {
        clearValue(fieldName, true);
    }

    @Override
    public void clearValue(String fieldName, boolean updateField) {
        assert allFields != null;
        final Canvas field = allFields.get(fieldName);
        if (field != null) {
            if (updateField) ((HasDataValue)field).showValue(null);
            values.remove(fieldName);
        }
    }

    public final void clearValues() {
        clearValues(true);
    }

    @Override
    public void clearValues(boolean updateFields) {
        if (updateFields) {
            setValues(null);
        } else {
            values.clear();
        }
        clearErrors();
    }

    public void setErrors(Map<String, ?> errors, boolean showErrors) {
        clearErrors();
        if (errors == null) return;
        for (final String fieldName : errors.keySet()) {
            final Canvas field = allFields.get(fieldName);
            if (field != null) {
                field.getElement().addClassName(_CSS.formItemHasInvalidValueClass());
                final Object errorObj = errors.get(fieldName);
                if (errorObj != null) {
                    impl.showError(this, showErrors, field, errorObj);
                }
            }
        }
    }

    @Override
    public void editRecord(Record record) {
        setValues(record);
        setSaveOperationType(DSOperationType.UPDATE);
    }

    public final void editNewRecord() {
        editNewRecord(null);
    }

    public void editNewRecord(Map<String, ?> initialValues) {
        clearValues();
        setSaveOperationType(DSOperationType.ADD);
        if (initialValues != null) setValues(initialValues);
    }

    public final void editNewRecord(Record initialValues) {
        editNewRecord((Map<String, ?>)initialValues);
    }

    @SGWTInternal
    public Boolean _handleNativeSubmit() {
        SubmitValuesEvent._fire(this);
        return Boolean.FALSE;
    }

	public void saveData() {
		saveData(null);
	}

    public void saveData(final DSCallback callback) {
        final DataSource ds = getDataSource();
        if (ds != null) {
            final DSCallback saveCallback = new DSCallback() {
                @Override
                public void execute(DSResponse dsResponse, Object rawData, DSRequest dsRequest) {
                    setErrors(dsResponse.getErrors(), true);
                    if (callback != null) callback.execute(dsResponse, rawData, dsRequest);
                }
            };

            if (saveOperationType == null || saveOperationType == DSOperationType.ADD) {
                ds.addData(getValuesAsRecord(), saveCallback);
            } else {
                assert saveOperationType == DSOperationType.UPDATE;
                final DSRequest requestProps = new DSRequest();
                requestProps.setOldValues(oldValues);
                ds.updateData(getValuesAsRecord(), saveCallback, requestProps);
            }
        }
    }

    public native void submitForm() /*-{
        var form = this.@com.smartgwt.mobile.client.widgets.form.DynamicForm::_getForm()();
        try {
            // Call the submit() method from the prototype in case a form control has the name
            // 'submit'.
            // https://developer.mozilla.org/en-US/docs/DOM/form.submit
            // http://stackoverflow.com/questions/2718799/unable-to-submit-an-html-form-after-intercepting-the-submit-with-javascript
            return HTMLFormElement.prototype.submit.call(form);
        } catch (e) {
            @com.smartgwt.mobile.client.util.SC::logWarn(Ljava/lang/String;)("Form submission was unsuccessful. In some browsers this can occur when " +
                "an upload item is present and has an invalid value.\n" + e.message);
            this.@com.smartgwt.mobile.client.widgets.form.DynamicForm::fireFormSubmitFailedEvent()();
        }
    }-*/;

    public boolean valuesHaveChanged() {
        final Record oldVals = (this.oldValues == null ? new Record() : this.oldValues);
        return DynamicForm._valuesHaveChanged(this, getValues(), oldVals);
    }

    @Override
    public HandlerRegistration addFormSubmitFailedHandler(FormSubmitFailedHandler handler) {
        return addHandler(handler, FormSubmitFailedEvent.getType());
    }

    @Override
    public HandlerRegistration addItemChangedHandler(ItemChangedHandler handler) {
        return addHandler(handler, ItemChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addSubmitValuesHandler(SubmitValuesHandler handler) {
        return addHandler(handler, SubmitValuesEvent._getType());
    }

    private void fireFormSubmitFailedEvent() {
        FormSubmitFailedEvent._fire(this);
    }
}
