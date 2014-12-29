package com.smartgwt.mobile.client.internal.theme.ios;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.theme.PickerCssResource;

@SGWTInternal
public interface PickerCssResourceIOS extends PickerCssResource {

    public String pickerFadeInAnimationName();
    public String pickerFadeOutAnimationName();

    public int pickerFadeInAnimationDurationMillis();
    public int pickerFadeOutAnimationDurationMillis();

    public int pickerSlotHeightPx();
    public int topPickerCoverHeightPx();

    @ClassName("pickerWrapper")
    public String pickerWrapperClass();

    @ClassName("pickerFrameWrapper")
    public String pickerFrameWrapperClass();

    @ClassName("pickerFrame")
    public String pickerFrameClass();

    @ClassName("topPickerCover")
    public String topPickerCoverClass();

    @ClassName("centerPickerCover")
    public String centerPickerCoverClass();

    @ClassName("bottomPickerCover")
    public String bottomPickerCoverClass();

    @ClassName("pickerDialsWrapper")
    public String pickerDialsWrapperClass();

    @ClassName("pickerDialWrapper")
    public String pickerDialWrapperClass();
}
