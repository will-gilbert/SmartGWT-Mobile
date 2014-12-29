package com.smartgwt.mobile.client.theme;

import com.google.gwt.resources.client.CssResource.Shared;

@Shared
public interface SwitchItemCssResource extends BaseCssResource {

    @ClassName("sc-switchitem")
    public String switchItemClass();

    @ClassName("switchedOn")
    public String switchedOnClass();

    @ClassName("switchedOff")
    public String switchedOffClass();

    @ClassName("switchedOnText")
    public String switchedOnTextClass();

    @ClassName("switchedOffText")
    public String switchedOffTextClass();

    @ClassName("switchItemKnob")
    public String switchItemKnobClass();
}
