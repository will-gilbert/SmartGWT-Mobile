package com.smartgwt.mobile.client.theme;

import com.google.gwt.resources.client.CssResource.Shared;

@Shared
public interface SliderCssResource extends BaseCssResource {

    @ClassName("sc-slideritem")
    public String sliderItemClass();

    @ClassName("sliderKnob")
    public String sliderKnobClass();
}
