package com.smartgwt.mobile.client.theme;

import com.google.gwt.resources.client.CssResource.Shared;

@Shared
public interface MenuCssResource extends BaseCssResource {

    @ClassName("sc-menu")
    public String menuClass();

    @ClassName("sc-menuitem")
    public String menuItemClass();

    //public String menuSeparatorItemClass();
}
