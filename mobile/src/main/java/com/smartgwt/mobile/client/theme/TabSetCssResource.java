package com.smartgwt.mobile.client.theme;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Shared;

@Shared
public interface TabSetCssResource extends CssResource {

    @ClassName("sc-tabset")
    public String tabSetClass();

    @ClassName("tabPane")
    public String tabPaneClass();

    @ClassName("tabBar")
    public String tabBarClass();

    @ClassName("tab")
    public String tabClass();

    @ClassName("activeTab")
    public String activeTabClass();

    @ClassName("tabHasIcon")
    public String tabHasIconClass();

    @ClassName("tabIconPrerendered")
    public String tabIconPrerenderedClass();

    @ClassName("tabIconWrapper")
    public String tabIconWrapperClass();

    @ClassName("tabTitle")
    public String tabTitleClass();

    @ClassName("tabBadgeWrapper")
    public String tabBadgeWrapperClass();

    @ClassName("tabBadge")
    public String tabBadgeClass();

    @ClassName("tabBadgeContent")
    public String tabBadgeContentClass();
}
