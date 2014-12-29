package com.smartgwt.mobile.client.internal.util;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.types.PageOrientation;

@SGWTInternal
public abstract class PageStaticImpl {

    public abstract PageOrientation getOrientation();

    public abstract void setupOrientationChangeListener();
}
