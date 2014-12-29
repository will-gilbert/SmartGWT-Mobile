package com.smartgwt.mobile.client.internal.theme.ios;

import com.google.gwt.resources.client.CssResource.Shared;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.theme.NavigationBarCssResource;

@Shared
@SGWTInternal
public interface NavigationBarCssResourceIOS extends NavigationBarCssResource, LayoutCssResourceIOS {

    public String navbarFadeInAnimationName();
    public String navbarFadeOutAnimationName();
    public String navbarSlideInFromRightAnimationName();
    public String navbarHeadingSlideInFromRightAnimationName();
    public String navbarSlideInFromLeftAnimationName();
    public String navbarHeadingSlideInFromLeftAnimationName();
    public String navbarSlideOutRightAnimationName();
    public String navbarHeadingSlideOutRightAnimationName();
    public String navbarSlideOutLeftAnimationName();
    public String navbarHeadingSlideOutLeftAnimationName();

    public int navbarAnimationDurationMillis();

    @ClassName("navbuttons")
    public String navbuttonsClass();

    @ClassName("sc-toolbar-left")
    public String toolbarLeftClass();

    @ClassName("sc-toolbar-center")
    public String toolbarCenterClass();

    @ClassName("sc-toolbar-right")
    public String toolbarRightClass();

    public String navbarFadeInClass();
    public String navbarFadeOutClass();
    public String navbarSlideInFromRightClass();
    public String navbarSlideInFromLeftClass();
    public String navbarSlideOutRightClass();
    public String navbarSlideOutLeftClass();
}
