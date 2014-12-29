package com.smartgwt.mobile.client.internal.theme;

import com.google.gwt.resources.client.ClientBundle;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public interface InternalThemeResources extends ClientBundle {

    @Source("alertview.gwt.css")
    @SGWTInternal
    public AlertViewCssResource alertviewCSS();

    @Source("picker.gwt.css")
    @SGWTInternal
    public PickerCssResource pickerCSS();

    @Source("popup.gwt.css")
    @SGWTInternal
    public PopupCssResource popupCSS();
}
