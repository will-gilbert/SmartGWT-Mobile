package com.smartgwt.mobile.client.internal.theme;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.theme.ThemeResources;

@SGWTInternal
public interface ThemeResourcesBase extends ThemeResources {

    @Override
    @Source({ "com/smartgwt/mobile/client/theme/tabs.gwt.css", "tabs.gwt.css" })
    public TabSetCssResourceBase tabsCSS();
}
