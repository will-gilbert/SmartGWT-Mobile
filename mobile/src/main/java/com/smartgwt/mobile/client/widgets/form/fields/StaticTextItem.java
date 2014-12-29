package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;

public class StaticTextItem extends FormItem {

    public StaticTextItem(String name) {
        super(name);
        init();
    }

    public StaticTextItem(String name, String title) {
        super(name, title);
        init();
    }

    private void init() {
        final Element elem = Document.get().createDivElement();
        elem.setClassName(DynamicForm._CSS.staticTextItemClass());
        setElement(elem);
    }

    @Override
    public void _setElementValue(Object displayValue, Object newValue) {
        final String html;
        if (displayValue == null) html = "";
        else html = displayValue.toString();
        getElement().setInnerHTML(html);
    }
}
