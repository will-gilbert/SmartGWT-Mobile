package com.smartgwt.mobile.client.internal.widgets;

import com.google.gwt.user.client.ui.Image;
import com.smartgwt.mobile.client.widgets.icons.IconResources;

public class ActivityIndicatorImplAndroid extends ActivityIndicatorImpl {

    private Image image;

    public ActivityIndicatorImplAndroid() {
        image = new Image(IconResources.INSTANCE.activity_indicator());
        add(image, getElement());
    }
}
