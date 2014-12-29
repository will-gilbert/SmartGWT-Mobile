package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;

class UploadItemImplDefault extends UploadItemImpl {

    @Override
    Element createElement(UploadItem self) {
        final Element ret = Document.get().createFileInputElement();
        ret.setClassName(DynamicForm._CSS.uploadItemClass());
        return ret;
    }

    @Override
    void clearElementValue(UploadItem self) {
        self.getElement().<InputElement>cast().setValue("");
    }
}
