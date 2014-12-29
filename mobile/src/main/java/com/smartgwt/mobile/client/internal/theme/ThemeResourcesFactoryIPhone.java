package com.smartgwt.mobile.client.internal.theme;

import com.google.gwt.core.client.GWT;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.theme.iphone.ThemeResourcesIPhone;

@SGWTInternal
public class ThemeResourcesFactoryIPhone implements ThemeResourcesFactory {

    @Override
    public ThemeResourcesIPhone create() {
        return GWT.create(ThemeResourcesIPhone.class);
    }
}
