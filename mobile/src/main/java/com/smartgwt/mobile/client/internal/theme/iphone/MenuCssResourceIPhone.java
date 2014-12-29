package com.smartgwt.mobile.client.internal.theme.iphone;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.theme.MenuCssResource;

@SGWTInternal
public interface MenuCssResourceIPhone extends MenuCssResource {

    public int menuOffsetHeightPx();
    public int menuBorderRightWidthPx();
    public int menuBorderLeftWidthPx();
    public int menuBorderRadiusPx();
    public int menuTipWrapperOffsetHeight();
    public int menuTipBeforePseudoelementLeftPx();
    public int arrowItemWidthPx();

    public String menuFadeInAnimationName();
    public int menuFadeInAnimationDurationMillis();
    public String menuFadeOutAnimationName();
    public int menuFadeOutAnimationDurationMillis();

    public int pageFlipAnimationDurationMillis();

    @ClassName("first")
    public String firstClass();

    @ClassName("last")
    public String lastClass();

    @ClassName("content")
    public String contentClass();

    @ClassName("label")
    public String labelClass();

    // The height of the .sc-menu element is menuOffsetHeightPx() + menuTipWrapperOffsetHeight() - 1
    // because the menu tip wrapper overlaps the menu page element 1px.

    public String fadeInClass();
    public String fadeOutClass();

    // A content measurer is used to measure the width of the content for MenuItems.
    public String contentMeasurerClass();

    public String screenCoverClass();

    public String menuPageClass();

    public String arrowItemClass();

    public String menuTipWrapperClass();

    public String menuTipClass();
}
