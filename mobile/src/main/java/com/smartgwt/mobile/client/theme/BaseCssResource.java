package com.smartgwt.mobile.client.theme;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Shared;

@Shared
public interface BaseCssResource extends CssResource {

    @ClassName("disabled")
    public String disabledClass();

    @ClassName("selected")
    public String selectedClass();
}
