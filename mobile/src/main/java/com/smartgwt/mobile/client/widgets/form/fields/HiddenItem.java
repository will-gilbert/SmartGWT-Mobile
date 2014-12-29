package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.dom.client.Document;

public class HiddenItem extends FormItem {

    public HiddenItem(String name) {
        super(name, Document.get().createHiddenInputElement());
    }

    @Override
    public boolean validate() {
        return true;
    }
}
