package com.smartgwt.mobile.client.theme;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Shared;

@Shared
public interface HeadingsCssResource extends CssResource {

    @ClassName("sc-heading")
    public String headingClass();

    @ClassName("h1")
    public String heading1Class();

    @ClassName("h2")
    public String heading2Class();

    @ClassName("headingContent")
    public String headingContentClass();
}
