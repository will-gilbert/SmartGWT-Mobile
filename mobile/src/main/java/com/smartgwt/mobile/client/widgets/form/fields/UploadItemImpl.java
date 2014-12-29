package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
abstract class UploadItemImpl {

    abstract Element createElement(UploadItem self);

    void create(UploadItem self) {}

    void destroyImpl(UploadItem self) {}

    void setElement(UploadItem self, com.google.gwt.user.client.Element elem) {}

    abstract void clearElementValue(UploadItem self);

    void onBrowserEvent(Event event) {}
}
