package com.smartgwt.mobile.client.widgets.form.fields;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.Criterion;
import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.internal.Array;
import com.smartgwt.mobile.client.internal.EventHandler;
import com.smartgwt.mobile.client.internal.EventUtil;
import com.smartgwt.mobile.client.internal.test.AutoTestLocatable;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.types.AndroidWindowSoftInputMode;
import com.smartgwt.mobile.client.internal.util.ObjectUtil;
import com.smartgwt.mobile.client.internal.widgets.Picker2;
import com.smartgwt.mobile.client.internal.widgets.ValidationResult;
import com.smartgwt.mobile.client.internal.widgets.events.HasValuesSelectedHandlers;
import com.smartgwt.mobile.client.internal.widgets.events.PickerHiddenEvent;
import com.smartgwt.mobile.client.internal.widgets.events.PickerHiddenHandler;
import com.smartgwt.mobile.client.internal.widgets.events.ValuesSelectedEvent;
import com.smartgwt.mobile.client.internal.widgets.events.ValuesSelectedHandler;
import com.smartgwt.mobile.client.internal.widgets.form.fields.HasDefaultDynamicValue;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.DateDisplayFormat;
import com.smartgwt.mobile.client.types.OperatorId;
import com.smartgwt.mobile.client.types.TimeDisplayFormat;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.form.DataManager;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.FormItemValueFormatter;
import com.smartgwt.mobile.client.widgets.form.FormItemValueParser;
import com.smartgwt.mobile.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.mobile.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.mobile.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.mobile.client.widgets.form.fields.events.HasBlurHandlers;
import com.smartgwt.mobile.client.widgets.form.fields.events.HasChangedHandlers;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperDocument;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperElement;

public class FormItem extends AbstractHasDataValue implements ValuesSelectedHandler, HasBlurHandlers, HasChangedHandlers, HasKeyPressHandlers, HasValuesSelectedHandlers {

    private static boolean inputActive = false;
    private static int lastNumResizes = -1;
    private static Timer scrollTo0_0Timer;
    private static Timer hideAddressBarTimer;

    private transient Picker2 picker;

    @SGWTInternal
    boolean _changeOnKeypress = true;
    private Object changeValue;
    private DateDisplayFormat dateFormatter;
    private DateDisplayFormat datetimeFormatter;
    private Object defaultValue;
    private String displayField;
    private FormItemValueFormatter editorValueFormatter;
    private FormItemValueParser editorValueParser;
    private String emptyDisplayValue;
    private String hint;
    private String multipleValueSeparator;
    private DataSource optionDataSource;
    private Boolean required;
    private Boolean setToDefault;
    private boolean shouldShowErrorIcon = true;
    private boolean shouldShowErrorText = false;
    private Boolean showHint;
    private boolean showingInFieldHint = false;
    private Alignment textAlign;
    private TimeDisplayFormat timeFormatter;
    @SGWTInternal
    Object _value;
    private String valueField;
    private FormItemValueFormatter valueFormatter;
    private LinkedHashMap<?, String> valueMap;

    private transient boolean touchActive;
    private transient boolean couldBeShowingSoftKeyboard;
    private transient Timer startTimer;
    private transient Timer showPickerTimer;
    private transient Integer touchIdentifier;
    private transient int touchPointX, touchPointY;
    private transient boolean shouldShowPickerOnEnd;

    public FormItem(String name) {
        super(name);
    }

    public FormItem(String name, String title) {
        super(name, title);
    }

    @SGWTInternal
    protected FormItem(String name, Element elem) {
        super(name);
        super.setElement(elem);
        internalSetFieldName(name);
        getElement().setId(name);
        _setClassName("sc-layout-flex0", false);
        sinkEvents(Event.ONCLICK | Event.ONMOUSEDOWN | Event.ONMOUSEMOVE | Event.ONMOUSEUP | Event.TOUCHEVENTS | Event.ONFOCUS | Event.ONBLUR);
        _sinkFocusInEvent();
        _sinkFocusOutEvent();
    }

    @Override
    protected void onUnload() {
        final SuperElement activeElem = Document.get().<SuperDocument>cast().getActiveElement();
        // If this FormItem has the focus, detaching it from the document will cause a 'blur'
        // event to be fired, but we won't receive notification via onBrowserEvent().
        if (activeElem != null && getElement().isOrHasChild(activeElem)) {
            onBlur(null);
        }
        super.onUnload();
    }

    @Override
    public AutoTestLocatable _getChildFromLocatorSubstring(String substring, int index, List<String> locatorArray, GetAttributeConfiguration configuration) {
        if (substring.equals("picker")) return picker;
        return super._getChildFromLocatorSubstring(substring, index, locatorArray, configuration);
    }

    @Override
    public Object _getInnerAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration) {
        switch (configuration.getAttribute()) {
            case IS_CLICKABLE:
                if (locatorArray.isEmpty() && picker != null) {
                    return Boolean.FALSE;
                }
                break;
            case VALUE:
                if (locatorArray.size() == 1) {
                    final String valueName = locatorArray.get(0);
                    if ("value".equals(valueName)) return getValue();
                    if ("elementValue".equals(valueName)) return _getElementValue();
                    if ("valueMap".equals(valueName)) return _getValueMap();
                    if ("defaultValue".equals(valueName)) return _getDefaultValue();
                    if ("name".equals(valueName)) return getName();
                    if ("title".equals(valueName)) return getTitle();
                    if ("displayField".equals(valueName)) return getDisplayFieldName();
                    if ("valueField".equals(valueName)) return getValueFieldName();
                    if ("hint".equals(valueName) ||
                        "placeholder".equals(valueName))
                    {
                        return getHint();
                    }
                    if ("required".equals(valueName)) return required;
                    if ("hasValueMap".equals(valueName)) {
                        final Map<?, String> valueMap = _getValueMap();
                        return Boolean.valueOf(valueMap != null && !valueMap.isEmpty());
                    }
                }
                break;
            default:
                break;
        }
        return super._getInnerAttributeFromSplitLocator(locatorArray, configuration);
    }

    @Override
    public void _copyFieldConfiguration(DataSourceField field) {
        super._copyFieldConfiguration(field);
        if (hint == null) {
            String hint = field.getHint();
            if (hint == null) hint = field.getTitle();
            setHint(hint);
        }
        if (required == null) setRequired(field.getRequired());
        if (valueMap == null) setValueMap(field.getValueMap());
    }

    @SGWTInternal
    public void _handleInput() {
        if (_changeOnKeypress) {
            _updateValue();
        } // else TODO length 
    }

    public final DynamicForm getForm() {
        final DataManager dataManager = getDataManager();
        return (dataManager instanceof DynamicForm ? ((DynamicForm)dataManager) : null);
    }

	public Criterion getCriterion() {
        return new Criterion(getFieldName(), OperatorId.EQUALS, _value);
    }

    @Override
    public void setDataManager(DataManager dataManager) {
        super.setDataManager(dataManager);
        if (dataManager == null || dataManager instanceof DynamicForm) _updateDateFormatter();
    }

    /**
     * The format in which dates are displayed for this item.
     * <p>
     * <b>NOTE:</b> In the case of a {@link com.smartgwt.mobile.client.widgets.form.fields.DateItem},
     * this setting does not have an effect if using a native HTML5 date input.
     */
    public final DateDisplayFormat getDateFormatter() {
        return dateFormatter;
    }

    @SGWTInternal
    public final DateDisplayFormat _getDateFormatter() {
        if (dateFormatter != null) return this.dateFormatter;
        // TODO Handle type
        final DynamicForm form = getForm();
        if (form != null) {
            return form.getDateFormatter();
        }
        return null;
    }

    public void setDateFormatter(DateDisplayFormat dateFormatter) {
        if (this.dateFormatter != dateFormatter) {
            this.dateFormatter = dateFormatter;
            _updateDateFormatter();
        }
    }

    /**
     * The format in which datetimes are displayed for this item.
     * <p>
     * <b>NOTE:</b> In the case of a {@link com.smartgwt.mobile.client.widgets.form.fields.DateTimeItem},
     * this setting does not have an effect if using a native HTML5 datetime input.
     */
    public final DateDisplayFormat getDatetimeFormatter() {
        return datetimeFormatter;
    }

    @SGWTInternal
    public final DateDisplayFormat _getDatetimeFormatter() {
        if (datetimeFormatter != null) return this.datetimeFormatter;
        // TODO Handle type
        final DynamicForm form = getForm();
        if (form != null) {
            return form.getDatetimeFormatter();
        }
        return null;
    }

    public void setDatetimeFormatter(DateDisplayFormat datetimeFormatter) {
        if (this.datetimeFormatter != datetimeFormatter) {
            this.datetimeFormatter = datetimeFormatter;
            _updateDateFormatter();
        }
    }

	@SGWTInternal
    public final Object _getDefaultValue() {
        if (this instanceof HasDefaultDynamicValue) {
            final DataManager dataManager = getDataManager();
            final Map<String, ?> values = (dataManager == null ? null : dataManager.getValues());
            return ((HasDefaultDynamicValue)this)._getDefaultDynamicValue(this, dataManager, values);
        }
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public final String getDisplayField() {
        return displayField;
    }

    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    public final String getDisplayFieldName() {
        final String displayField = getDisplayField();
        if (displayField != null) {
            return displayField;
        }
        final String valueFieldName = getValueFieldName();
        if (optionDataSource != null) {
            DataSourceField dataSourceField = optionDataSource.getField(valueFieldName);
            if (dataSourceField != null && dataSourceField.isHidden()) {
                return optionDataSource.getTitleField();
            }
        }
        return valueFieldName;
    }

    @SGWTInternal
    public Object _getDisplayValue(Object value) {
        return _mapValueToDisplay(value);
    }

    public final FormItemValueFormatter getEditorValueFormatter() {
        return editorValueFormatter;
    }

    public void setEditorValueFormatter(FormItemValueFormatter editorValueFormatter) {
        this.editorValueFormatter = editorValueFormatter;
    }

    public final FormItemValueParser getEditorValueParser() {
        return editorValueParser;
    }

    public void setEditorValueParser(FormItemValueParser editorValueParser) {
        this.editorValueParser = editorValueParser;
    }

    @Override
    protected void setElement(com.google.gwt.user.client.Element elem) {
        super.setElement(elem);
        internalSetFieldName(getFieldName());
    }

    public final String getEmptyDisplayValue() {
        return emptyDisplayValue;
    }

    @SGWTInternal
    public final String _getEmptyDisplayValue() {
        return (emptyDisplayValue == null ? "" : emptyDisplayValue);
    }

    public void setEmptyDisplayValue(String emptyDisplayValue) {
        this.emptyDisplayValue = emptyDisplayValue;
    }

    private void internalSetFieldName(String fieldName) {
        final Element elem = getElement();
        final String tagName = elem.getTagName();
        if ("INPUT".equals(tagName) ||
            "SELECT".equals(tagName) ||
            "TEXTAREA".equals(tagName))
        {
            elem.setAttribute("name", fieldName);
        }
    }

    @Override
    protected void setFieldName(String fieldName) {
        super.setFieldName(fieldName);
        internalSetFieldName(fieldName);
    }

    public final String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
        if (hint != null && _getShowHint() && _getShowHintInField()) {
            getElement().setAttribute("placeholder", hint);
            showingInFieldHint = true;
        } else {
            if (showingInFieldHint) {
                getElement().removeAttribute("placeholder");
                showingInFieldHint = false;
            }
        }
    }

    @SGWTInternal
    public Element _getInputElement() {
        return getElement();
    }

    @SGWTInternal
    public final String _getMultipleValueSeparator() {
        return (multipleValueSeparator == null ? ", " : multipleValueSeparator);
    }

    /**
     * Alias for {@link #getFieldName()}.
     * @return the field's name.
     */
    public final String getName() {
        return getFieldName();
    }

    public final void setName(String name) {
        setFieldName(name);
    }

    public final DataSource getOptionDataSource() {
        return optionDataSource;
    }

    public void setOptionDataSource(DataSource ds) {
        this.optionDataSource = ds;
    }

    @SGWTInternal
    public final Picker2 _getPicker() {
        return picker;
    }

    @SGWTInternal
    public final boolean _isReadOnly() {
        final Boolean canEdit = getCanEdit();
        if (canEdit != null) return !canEdit.booleanValue();
        final DataManager dataManager = getDataManager();
        final Boolean dmCanEdit;
        if (dataManager != null && (dmCanEdit = dataManager.getCanEdit()) != null && !dmCanEdit.booleanValue()) return true;
        return false;
    }

    @Override
    public void updateCanEdit() {
        final boolean isReadOnly = _isReadOnly();
        _setElementReadOnly(isReadOnly);
    }

    @SGWTInternal
    public void _setElementReadOnly(boolean readOnly) {}

    public final Boolean getRequired() {
        return required;
    }

    @SGWTInternal
    public final boolean _getRequired() {
        return _booleanValue(getRequired(), false);
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    @SGWTInternal
    public final Boolean _isSetToDefaultValue() {
        return setToDefault;
    }

    public final Boolean getShowHint() {
        return showHint;
    }

    @SGWTInternal
    public final boolean _getShowHint() {
        return Canvas._booleanValue(getShowHint(), true);
    }

    public void setShowHint(Boolean showHint) {
        this.showHint = showHint;
        setHint(getHint());
    }

    @SGWTInternal
    public boolean _getShowHintInField() {
        return false;
    }

    public final Alignment getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(Alignment textAlign) {
        this.textAlign = textAlign;
        if (textAlign == null) getElement().getStyle().clearProperty("textAlign");
        else {
            getElement().getStyle().setProperty("textAlign", textAlign.getValue());
        }
    }

    /**
     * The format in which times are displayed for this item.
     * <p>
     * <b>NOTE:</b> In the case of a {@link com.smartgwt.mobile.client.widgets.form.fields.TimeItem},
     * this setting does not have an effect if using a native HTML5 time input.
     */
    public final TimeDisplayFormat getTimeFormatter() {
        return timeFormatter;
    }

    @SGWTInternal
    public final TimeDisplayFormat _getTimeFormatter() {
        if (timeFormatter != null) return this.timeFormatter;
        // TODO Handle type
        final DynamicForm form = getForm();
        if (form != null) {
            return form.getTimeFormatter();
        }
        return null;
    }

    public void setTimeFormatter(TimeDisplayFormat timeFormatter) {
        this.timeFormatter = timeFormatter;
    }

    public Object getValue() {
        return _value;
    }

    public void setValue(Object newValue) {
        setValue(newValue, false, false);
    }

    protected Object setValue(Object newValue, boolean allowNullValue, boolean timeCritical) {

        // TODO _setValueCalled, resetCursor, _pendingUpdate

        boolean isDefault = false;
        if (newValue == null && ! allowNullValue) {
            Object defaultVal = _getDefaultValue();
            if (defaultVal != null) {
                isDefault = true;
                newValue = defaultVal;
            }
        }

        // TODO length

        saveValue(newValue, isDefault);

        showValue(newValue);

        return newValue;
    }

    public final String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    public final String getValueFieldName() {
        final String valueField = getValueField();
        if (valueField != null) {
            return valueField;
        }
        final String name = getName();
        if (name != null) {
            return name;
        }
        return "name";
    }

    @SGWTInternal
    public void _checkForEditorExit() {
        _handleEditorExit();
    }

    @SGWTInternal
    public boolean _elementBlur() {
        _checkForEditorExit();

        BlurEvent.fire(this, getForm(), this);

        return true;
    }

    @SGWTInternal
    public Object _formatDataType(Object value) {
        final boolean applyStaticTypeFormat = _shouldApplyStaticTypeFormat();
        if (applyStaticTypeFormat) {
            if (valueFormatter != null) {
                final DynamicForm form = getForm();
                final Record record = (form == null ? new Record() : form.getValuesAsRecord());
                return valueFormatter.formatValue(value, record, form, this);
            }
        } else if (editorValueFormatter != null) {
            final DynamicForm form = getForm();
            final Record record = (form == null ? new Record() : form.getValuesAsRecord());
            return editorValueFormatter.formatValue(value, record, form, this);
        }

        if (value instanceof Date) {
            // TODO
        }

        if (value == null) value = _getEmptyDisplayValue();
        return value;
    }

    @SGWTInternal
    public void _handleEditorExit() {
        if (getForm() == null) return;

        //Object value = getValue();

        // TODO _performValidateOnEditorExit, implicitSave
    }

    @SGWTInternal
    public Object _mapDisplayToValue(String displayValue) {
        Object value = _parseDisplayValue(displayValue);
        return _unmapKey(value);
    }

    @SGWTInternal
    public Object _mapKey(Object key, boolean dontReturnKey) {
        Object defaultValue = (dontReturnKey ? null : key);
        if (valueMap == null) return defaultValue;

        // return isc.getValueForKey(key, valueMap, defaultValue);
        final Object normalizedKey = ObjectUtil.normalize(key);
        if (valueMap.containsKey(normalizedKey)) {
            final Object val = valueMap.get(normalizedKey);
            if (val != null) return val;
        }
        return defaultValue;
    }

    @SGWTInternal
    public Object _mapValueToDisplay(Object value) {
        Object displayValue;
        if (Array.isArray(value)) {
            final StringBuilder sb = new StringBuilder();
            final String multipleValueSeparator = _getMultipleValueSeparator();
            Array.forEach(value, new Array.ArrayElementCallback() {
                @Override
                public void execute(final int index, Object element) {
                    if (index != 0) sb.append(multipleValueSeparator);
                    sb.append(_mapValueToDisplay(element));
                }
            });
            displayValue = sb.toString();
        } else {
            displayValue = _mapKey(value, true);
            if (displayValue == null) {
                final String displayField = getDisplayFieldName();
                if (displayField != null) {
                    // TODO Try looking in the option data source's cache data.
                }
            }
            displayValue = _formatDataType(displayValue != null ? displayValue : value);
        }
        return displayValue;
    }

    @SGWTInternal
    public boolean _nativeElementBlur() {
        return _elementBlur();
    }

    @SGWTInternal
    public Object _parseDisplayValue(String displayValue) {
        final boolean applyStaticTypeFormat = _shouldApplyStaticTypeFormat();
        Object value = displayValue;
        if (!applyStaticTypeFormat) {
            if (editorValueParser != null) {
                value = editorValueParser.parseValue(displayValue, getForm(), this);
            }

            // TODO Handle date parsing
        }
        return value;
    }

    @SGWTInternal
    public void _refreshDisplayValue() {
        final Object value = getValue();
        final Object displayValue = _mapValueToDisplay(value);
        _setElementValue(displayValue, value);
    }

    @SGWTInternal
    public boolean _shouldApplyStaticTypeFormat() {
        return true;
    }

    @Override
    public void showValue(Object value) {
        _value = value;
        final Object displayValue = _getDisplayValue(value);
        _setElementValue(displayValue, value);
    }

    @SGWTInternal
    public void _updateValue() {
        // TODO hasElement()

        Object newValue = _getElementValue();
        _updateValue(newValue);
    }

    @SGWTInternal
    public void _updateValue(Object newValue) {
        // TODO _pendingCompletion

        // unmap the value if necessary
        if (newValue instanceof String) newValue = _mapDisplayToValue((String)newValue);

        _storeValue(newValue);
    }

    @SGWTInternal
    public boolean _storeValue(Object newValue) {
        if (compareValues(newValue, _value)) {
            //SC.logWarn("FI._updateValue: not saving, value unchanged: " + value);
            return true;
        }

        // TODO changingValue

        boolean returnVal = handleChange(newValue, _value);

        // Ensure we have the latest value
        newValue = changeValue;

        // TODO updateAppearance()

        if (! compareValues(newValue, _value)) saveValue(newValue, false);
        changeValue = null;

        _handleChanged(_value);
        return returnVal;
    }

    private void saveValue(Object value, boolean isDefault) {
        this._value = value;

        // TODO _setToDefault, form.clearItemValue() or form.saveItemValue()

        final DataManager dataManager = getDataManager();
        if (dataManager == null) return;

        dataManager.storeValue(getName(), value);
    }

    private boolean handleChange(Object newValue, Object value) {
        // TODO
        changeValue = newValue;
        return true;
    }

    @SGWTInternal
    void _handleChanged(Object value) {
        final DataManager dataManager = getDataManager();
        if (dataManager != null) {
            if (dataManager instanceof DynamicForm) {
                ChangedEvent.fire(this, ((DynamicForm)dataManager), this, value);
                if (dataManager != null) {
                    ItemChangedEvent.fire(((DynamicForm)dataManager), ((DynamicForm)dataManager), this, value);
                }
            }
        }
    }

    @SGWTInternal
    Object _getElementValue() {
        final Element inputElement = _getInputElement();
        return inputElement == null ? null : inputElement.<InputElement>cast().getValue();
    }

    @SGWTInternal
    public void _setElementValue(Object displayValue, Object newValue) {
        final Element inputElement = _getInputElement();
        if (inputElement != null) {
            String str = (displayValue == null ? null : displayValue.toString());
            if (str == null) str = "";
            inputElement.<InputElement>cast().setValue(str);
        }
    }

    public final FormItemValueFormatter getValueFormatter() {
        return valueFormatter;
    }

    public void setValueFormatter(FormItemValueFormatter valueFormatter) {
        this.valueFormatter = valueFormatter;
    }

    @SGWTInternal
    protected final LinkedHashMap<?, String> _getValueMap() {
        return valueMap;
    }

    public final void setValueMap(String... values) {
        final LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        if (values != null) {
            for (final String value : values) {
                valueMap.put(value,  value);
            }
        }
        setValueMap(valueMap);
    }

    /**
     * Set the valueMap for this item.
     * 
     * <p>Note: the keys of <code>valueMap</code> must be normalized according to the rules
     * applied by {@link com.smartgwt.mobile.client.internal.util.ObjectUtil#normalize(Object)}.
     * 
     * @param valueMap the value map.
     */
    public void setValueMap(LinkedHashMap<?, String> valueMap) {
        this.valueMap = valueMap;
        setValue(_value);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final Element target = EventUtil.getTargetElem(event);
        if (target != null && getElement().isOrHasChild(target)) {
            final JsArray<Touch> touches;
            final int clientY;
            final Element inputElement;
            switch (event.getTypeInt()) {
                case Event.ONMOUSEDOWN:
                    _onStart(event, null);
                    break;
                case Event.ONTOUCHSTART:
                    touches = event.getTouches();
                    if (touches.length() == 1 && touchIdentifier == null) {
                        _onStart(event, touches.get(0));
                    } else {
                        // Another finger is touching the screen.
                        _onEnd(event);
                    }
                    break;
                case Event.ONMOUSEMOVE:
                    if (touchActive) {
                        clientY = event.getClientY();
                        if (Math.abs(touchPointY - clientY) >= 10) {
                            _onEnd(event);
                        }
                    }
                    break;
                case Event.ONTOUCHMOVE:
                    if (touchActive) {
                        touches = event.getTouches();
                        if (touches.length() == 1 && touchIdentifier != null) {
                            final Touch touch = touches.get(0);
                            if (touch.getIdentifier() == touchIdentifier.intValue()) {
                                clientY = touch.getClientY();
                                if (Math.abs(touchPointY - clientY) >= 10) {
                                    _onEnd(event);
                                }
                            }
                        }
                    }
                    break;
                case Event.ONMOUSEUP:
                case Event.ONMOUSEOUT:
                case Event.ONTOUCHEND:
                case Event.ONTOUCHCANCEL:
                    _onEnd(event);
                    break;
                case Event.ONFOCUS:
                    inputElement = _getInputElement();

                    // Work-around the issue that an input element can receive keyboard focus
                    // even if it was not the original target of a tap.
                    // See http://stackoverflow.com/questions/11064680/is-this-a-bug-tapping-an-element-can-result-in-a-different-element-receiving-keyboard-focus
                    if (!Canvas.isIPad() && !Canvas.isIPhone() && !inputActive && EventHandler.lastTouchStartEvent != null) {
                        touches = EventHandler.lastTouchStartEvent.getTouches();
                        if (touches != null && inputElement != null) {
                            final Element touchTarget;
                            if (touches.length() != 1 ||
                                (touchTarget = EventUtil.getElement(touches.get(0).getTarget())) == null ||
                                // Check that the last touch target was an INPUT or LABEL element.
                                // Don't require the touch target to be a descendant of this FormItem's
                                // element because the user could have pressed the Previous or Next button
                                // to get to this FormItem.
                                (!"INPUT".equals(touchTarget.getTagName()) &&
                                 !"LABEL".equals(touchTarget.getTagName()) &&
                                 !"TEXTAREA".equals(touchTarget.getTagName())))
                            {
                                inputElement.blur();
                                return;
                            }
                        }
                    }

                    // When an input element receives focus, the mobile OS might be about to
                    // display a virtual keyboard for entering a value. If using non-native
                    // scrolling on iOS or Android, when the virtual keyboard is opened and
                    // windowSoftInputMode is adjustResize (which appears to be the default;
                    // but see, e.g., http://stackoverflow.com/questions/16276306/ ), the page
                    // is resized and the newly-focused input element could be hidden out of
                    // view as a result.
                    //
                    // The SGWT.mobile approach to this problem is to fire a custom, bubbling
                    // 'scRequestScrollTo' event on the element. This custom event bubbles up
                    // to the nearest ScrollablePanel (if any), which will try to scroll the
                    // element into view.
                    //
                    // `ScrollablePanelImplNonNative' has its own 'resize' handler that will
                    // examine the document's activeElement and auto-scroll the active element
                    // into view as if an 'scRequestScrollTo' were fired on the activeElement.
                    // Because the 'focus' event is fired *before* the virtual keyboard appears
                    // on screen (unless the virtual keyboard is already on screen), to avoid
                    // the extra work of processing an unnecessary 'scRequestScrollTo' event,
                    // simply wait for the coming 'resize' event. We only need to fire an
                    // 'scRequestScrollTo' event for the case where the user is moving focus
                    // between inputs, leaving the virtual keyboard on screen.
                    //
                    // Only fire the 'scRequestScrollTo' event if lastNumResizes (set on 'blur'
                    // of any `FormItem') equals the current number of 'resize' events ever fired.
                    // This is the case when transferring focus to another `FormItem' while leaving
                    // the virtual keyboard on screen.
                    //
                    // The above strategy is done when the 'android.windowSoftInputMode' property
                    // is set to 'adjustResize'. For 'adjustPan', we just send an scRequestScrollTo
                    // on 'input' in case the input is partially cut off. See TextItem#onBrowserEvent()
                    //
                    // A web page apparently cannot change android.windowSoftInputMode:
                    // http://stackoverflow.com/questions/10774260/what-is-the-equivalent-of-androidwindowsoftinputmode-for-a-web-site
                    if (AndroidWindowSoftInputMode.ADJUST_RESIZE == Canvas._getAndroidWindowSoftInputMode() &&
                        inputElement != null &&
                        lastNumResizes == EventHandler.numResizes)
                    {
                        Canvas._fireRequestScrollToEvent(inputElement);
                    }

                    if (EventHandler.couldShowSoftKeyboard(event)) {
                        if (Canvas._isStandaloneMode()) {
                            if (scrollTo0_0Timer != null) {
                                scrollTo0_0Timer.cancel();
                                scrollTo0_0Timer = null;
                            }
                        }

                        if (hideAddressBarTimer != null) {
                            hideAddressBarTimer.cancel();
                            hideAddressBarTimer = null;
                        }
                    }

                    inputActive = true;
                    break;
                case Event.ONBLUR:
                    onBlur(event);
                    break;
            }
        }
    }

    @SGWTInternal
    protected void _onStart(Event event, Touch touch) {
        shouldShowPickerOnEnd = false;
        final SuperDocument doc = Document.get().<SuperDocument>cast();
        final SuperElement activeElement = doc.getActiveElement();
        couldBeShowingSoftKeyboard = EventHandler.couldShowSoftKeyboard(activeElement);
        if (!isEnabled()) return;

        touchActive = true;
        if (touch != null) {
            touchIdentifier = touch.getIdentifier();
            touchPointX = touch.getClientX();
            touchPointY = touch.getClientY();
        } else {
            touchPointX = event.getClientX();
            touchPointY = event.getClientY();
        }

        if (startTimer == null) {
            startTimer = new Timer() {
                @Override
                public void run() {
                    assert startTimer == this;
                    shouldShowPickerOnEnd = true;
                    startTimer = null;
                }
            };
            startTimer.schedule(150);
        }
    }

    @SGWTInternal
    protected void _onEnd(Event event) {
        final boolean shouldShowPicker = this.shouldShowPickerOnEnd || startTimer != null;
        this.shouldShowPickerOnEnd = false;
        if (event.getTypeInt() == Event.ONMOUSEUP ||
            event.getTypeInt() == Event.ONTOUCHEND)
        {
            if (shouldShowPicker && isEnabled() && !_isReadOnly() && _isPickerEnabled()) {
                boolean showPicker = true;
                if (!Canvas.isIPad() && !Canvas.isIPhone() && EventHandler.lastTouchStartEvent != null) {
                    final JsArray<Touch> touches = EventHandler.lastTouchStartEvent.getTouches();
                    if (touches != null) {
                        final Element touchTarget;
                        if (touches.length() != 1 ||
                            (touchTarget = EventUtil.getElement(touches.get(0).getTarget())) == null ||
                            !getElement().isOrHasChild(touchTarget))
                        {
                            showPicker = false;
                        }
                    }
                }
                if (showPicker) {
                    // In Chrome 35 and WebView (Chrome 33.0.0.0) for Android 4.4.3, the soft
                    // keyboard remained showing when the focus was shifted to a field with a
                    // synthetic picker.
                    if (couldBeShowingSoftKeyboard) {
                        final SuperDocument doc = Document.get().<SuperDocument>cast();
                        final SuperElement activeElement = doc.getActiveElement();
                        activeElement.blur();
                        // Delay the call to showPicker() to give the soft keyboard a chance
                        // to disappear if it was on screen.
                        if (showPickerTimer != null) showPickerTimer.cancel();
                        showPickerTimer = new Timer() {
                            @Override
                            public void run() {
                                assert showPickerTimer == this;
                                showPickerTimer = null;
                                showPicker();
                            }
                        };
                        showPickerTimer.schedule(250);
                    } else {
                        showPicker();
                    }
                }
            }
        }
        if (startTimer != null) {
            startTimer.cancel();
            startTimer = null;
        }
        touchIdentifier = null;
        touchActive = false;
    }

    private void onBlur(Event event) {
        if (inputActive) {
            inputActive = false;
            if (event != null && _nativeElementBlur() == false) event.preventDefault();
        }
        lastNumResizes = EventHandler.numResizes;

        // In iOS standalone mode (such as when a web page is added to the home screen),
        // when the keyboard is shown, then hidden, we need to scroll to 0, 0 so that the
        // app is not partially cut off by the status bar. `window.scrollTo(0,0)' resets
        // the positioning of the app below the status bar.
        //
        // Do this in a timeout so that there isn't a strange jump in scroll position
        // when switching between form items while the keyboard is open via tapping
        // a different <input>.
        if (Canvas._isStandaloneMode()) {
            if (scrollTo0_0Timer != null) scrollTo0_0Timer.cancel();
            scrollTo0_0Timer = new Timer() {
                @Override
                public void run() {
                    scrollTo0_0Timer = null;
                    Window.scrollTo(0, 0);
                }
            };
            scrollTo0_0Timer.schedule(1);
        }

        if (hideAddressBarTimer != null) hideAddressBarTimer.cancel();
        hideAddressBarTimer = new Timer() {
            @Override
            public void run() {
                hideAddressBarTimer = null;
                Canvas._hideAddressBarNow();
            }
        };
        hideAddressBarTimer.schedule(1);
    }

    protected void onChanged(Event event) {
        final DataManager dataManager = getDataManager();
        if (dataManager instanceof DynamicForm) {
            final DynamicForm form = (DynamicForm)dataManager;
            new Timer() {
                @Override
                public void run() {
                    ChangedEvent.fire(FormItem.this, form, FormItem.this, _value);
                }
            }.schedule(20);
        }
    }

    @SGWTInternal
    public boolean _isPickerEnabled() {
        return false;
    }

    @SGWTInternal
    @SuppressWarnings("unchecked")
    protected Picker2 _createPicker() {
        return new Picker2(new LinkedHashMap[] { valueMap }, new Object[] { getValue() });
    }

    public void showPicker() {
        if (showPickerTimer != null) {
            showPickerTimer.cancel();
            showPickerTimer = null;
        }
        if (this.picker != null && (this.picker._isShowing() || this.picker._isShown())) {
            return;
        }
        final Picker2 picker = this.picker = _createPicker();
        final HandlerRegistration valuesSelectedRegistration = picker._addValuesSelectedHandler(this);
        new PickerHiddenHandler() {

            private HandlerRegistration pickerHiddenRegistration = picker._addPickerHiddenHandler(this);

            @Override
            public void _onPickerHidden(PickerHiddenEvent event) {
                pickerHiddenRegistration.removeHandler();
                valuesSelectedRegistration.removeHandler();
                picker.destroy();
                if (FormItem.this.picker == picker) {
                    FormItem.this.picker = null;
                }

                final DataManager dataManager = getDataManager();
                if (dataManager instanceof DynamicForm) {
                    BlurEvent.fire(FormItem.this, ((DynamicForm)dataManager), FormItem.this);
                }
            }
        };
        picker.show();
    }

    @Override
    public void _onValuesSelected(ValuesSelectedEvent event) {
        final Object[] values = event.getValues();
        if (values == null) setValue(null);
        else {
            if (values.length == 1) {
                _updateValue(values[0]);
                showValue(values[0]);
            } else {
                _updateValue(values);
                showValue(values);
            }
        }
    }

    @SGWTInternal
    public Object _unmapKey(Object value) {
        LinkedHashMap<?, String> map = _getValueMap();
        if (map == null) return value;

        //result = isc.getKeyForValue(value, map);
        Object result = value;
        if (value == null) {
            for (final Map.Entry<?, String> e : map.entrySet()) {
                if (e.getValue() == null) {
                    result = e.getKey();
                    break;
                }
            }
        } else if (value instanceof String) {
            for (final Map.Entry<?, String> e : map.entrySet()) {
                final String mappedValue = e.getValue();
                if (mappedValue != null && mappedValue.equals(value)) {
                    result = e.getKey();
                    break;
                }
            }
        }

        // if getKeyForValue returns the same value it was passed, and that happens to also
        // be the emptyDisplayValue for this item, don't allow the emptyDisplayValue to be
        // promoted to the internal value
        if (((result == null && value == null) ||
             (result != null && (result == value || result.equals(value)))) &&
            _getEmptyDisplayValue().equals(result))
        {
            result = "";

            final String valueField = getValueFieldName();
            if (valueField != null) {
                // TODO Try looking in the option data source's cache data.
            }
        }
        return result;
    }

    // Used to notify this item that either the dateFormatter or datetimeFormatter may have changed.
    @SGWTInternal
    public void _updateDateFormatter() {
        /*empty*/
    }

    public boolean validate() {
        List<String> fieldErrors = null;
        @SuppressWarnings("unused")
        Map<String, ArrayList<String>> allErrors = null;
        @SuppressWarnings("unused")
        Boolean stopOnError = Boolean.FALSE;

        final DynamicForm form = getForm();

        final Object value = getValue();
        final Record record = new Record();
        record.putAll(form.getValues());
        final ValidationResult fieldResult = form._validateFieldAndDependencies(this, getValidators(), value, record, null);

        String storeErrorAs = getName();

        if (fieldResult != null) {
            // if the validator returned a resultingValue, use that as the new value
            // whether the validator passed or failed.  This lets us transform data
            // (such as with the mask validator).
            final Object resultingValue = fieldResult.getResultingValue();
            if (resultingValue != null) {
                setValue(resultingValue);
            }
            if (!fieldResult.isValid()) {
                fieldErrors = fieldResult.getErrors().get(storeErrorAs);
            }
            stopOnError = fieldResult.getStopOnError();

            // Even though the changed field may be valid, there may be other fields
            // that are no longer valid because of a dependency. These errors should
            // be shown on the form.
            allErrors = fieldResult.getErrors();
        }

        // TODO

        return (fieldErrors == null || fieldErrors.isEmpty());
    }

    @Override
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        return addHandler(handler, BlurEvent.getType());
    }

    @Override
    public HandlerRegistration addChangedHandler(ChangedHandler handler) {
        return addHandler(handler, ChangedEvent.getType());
    }

    @Override
    public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
        return addHandler(handler, KeyPressEvent.getType());
    }
    
    @Override
    public HandlerRegistration _addValuesSelectedHandler(ValuesSelectedHandler handler) {
        return addHandler(handler, com.smartgwt.mobile.client.internal.widgets.events.ValuesSelectedEvent.getType());
    }
}
