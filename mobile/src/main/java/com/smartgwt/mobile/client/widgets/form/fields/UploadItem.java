package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.client.util.SC;

public class UploadItem extends FormItem {

    private final UploadItemImpl impl = GWT.create(UploadItemImpl.class);

    public UploadItem(String name) {
        super(name);
        internalSetElement(impl.createElement(this).<com.google.gwt.user.client.Element>cast());
        impl.create(this);
    }

    public UploadItem(String name, String title) {
        this(name);
        super.setTitle(title);
    }

    @Override
    public void destroy() {
        impl.destroyImpl(this);
        super.destroy();
    }

    private void internalSetElement(com.google.gwt.user.client.Element elem) {
        super.setElement(elem);
        impl.setElement(this, elem);
    }

    @Override
    protected void setElement(com.google.gwt.user.client.Element elem) {
        internalSetElement(elem);
    }

    @Override
    public void _setElementValue(Object displayValue, Object newValue) {
        if (newValue == null || "".equals(newValue)) {
            impl.clearElementValue(this);
            return;
        }
        SC.logWarn("Attempting to set the value for an upload form item.");
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        impl.onBrowserEvent(event);
    }
}
