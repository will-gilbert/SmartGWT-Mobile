package com.smartgwt.mobile.client.theme;

import com.google.gwt.resources.client.CssResource.Shared;

@Shared
public interface ToolStripCssResource extends HeadingsCssResource, ButtonsCssResource {

    @ClassName("sc-toolbar")
    public String toolStripClass();

    @ClassName("plainIconToolStrip")
    public String plainIconToolStripClass();

    @ClassName("customTintedToolStrip")
    public String customTintedToolStripClass();
}
