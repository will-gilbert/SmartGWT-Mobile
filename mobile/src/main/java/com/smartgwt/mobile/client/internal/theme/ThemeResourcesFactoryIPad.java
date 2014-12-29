package com.smartgwt.mobile.client.internal.theme;

import com.google.gwt.core.client.GWT;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.theme.ipad.ThemeResourcesIPad;

@SGWTInternal
public class ThemeResourcesFactoryIPad implements ThemeResourcesFactory {

    @Override
    public ThemeResourcesIPad create() {
        return GWT.create(ThemeResourcesIPad.class);
    }
}
