package com.smartgwt.mobile.showcase.client.widgets.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ImageResources extends ClientBundle {

    public static ImageResources INSTANCE = GWT.create(ImageResources.class);

    @Source("ipod.png")
    ImageResource ipod();

    @Source("phone.png")
    ImageResource phone();
}
