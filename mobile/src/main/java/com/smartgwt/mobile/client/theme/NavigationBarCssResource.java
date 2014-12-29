package com.smartgwt.mobile.client.theme;

import com.google.gwt.resources.client.CssResource.Shared;

@Shared
public interface NavigationBarCssResource extends HeadingsCssResource, ButtonsCssResource {

    @ClassName("sc-navstack")
    public String navStackClass();

    @ClassName("sc-navigationbar")
    public String navigationBarClass();

    @ClassName("shadowlessNavigationBar")
    public String shadowlessNavigationBarClass();

    @ClassName("navigationButtons")
    public String navigationButtonsClass();
}
