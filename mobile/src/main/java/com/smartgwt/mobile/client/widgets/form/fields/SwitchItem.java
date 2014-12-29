package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.internal.util.ElementUtil;
import com.smartgwt.mobile.client.theme.SwitchItemCssResource;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurEvent;

public class SwitchItem extends FormItem {

    @SGWTInternal
    public static final SwitchItemCssResource _CSS = ThemeResources.INSTANCE.switchItemCSS();

    private final SwitchItemImpl impl = GWT.create(SwitchItemImpl.class);
    private String offText = null, onText = null;

    public SwitchItem(String name) {
        super(name, Document.get().createDivElement());
        getElement().addClassName(_CSS.switchItemClass());
        impl.create(this);
        impl.setOnText(this, "On");
        impl.setOffText(this, "Off");
        impl.setChecked(this, false);
    }

    public SwitchItem(String name, String title) {
        this(name);
        setTitle(title);
    }

    @Override
    public void destroy() {
        impl.destroyImpl(this);
        super.destroy();
    }

    @Override
    public void setVisible(boolean visible) {
        final boolean wasVisible = isVisible();
        super.setVisible(visible);
        if (wasVisible != visible) impl.setVisible(this, visible);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        impl.onLoad(this);
    }

    @Override
    public void _copyFieldConfiguration(DataSourceField field) {
        super._copyFieldConfiguration(field);
        final String onText = field.getOnText();
        if (this.onText == null && onText != null) setOnText(onText);
        final String offText = field.getOffText();
        if (this.offText == null && offText != null) setOffText(offText);
    }

    public final String getOnText() {
        return onText;
    }

    public void setOnText(String onText) {
        impl.setOnText(this, onText);
        this.onText = onText;
    }

    public final String getOffText() {
        return offText;
    }

    public void setOffText(String offText) {
        impl.setOffText(this, offText);
        this.offText = offText;
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        switch (event.getTypeInt()) {
            case Event.ONCLICK:
                if (!isEnabled() || _isReadOnly()) return;
                setChecked(!isChecked());
                BlurEvent.fire(this, getForm(), this);
                break;
        }
    }

    public final boolean isChecked() {
        return impl.isChecked(this);
    }

    public void setChecked(boolean checked) {
        setValue(Boolean.valueOf(checked));
    }

    @Override
    Boolean _getElementValue() {
        return ElementUtil.hasClassName(getElement(), _CSS.switchedOnClass());
    }

    @Override
    public void _setElementValue(Object displayValue, Object newValue) {
        boolean checked = false;
        if (displayValue != null && displayValue instanceof Boolean) checked = ((Boolean)displayValue).booleanValue();
        impl.setChecked(this, checked);
    }

    @Override
    public boolean validate() {
        return true;
    }
}
