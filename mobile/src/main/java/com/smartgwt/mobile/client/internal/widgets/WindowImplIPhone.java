/*
 * SmartGWT Mobile
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * SmartGWT Mobile is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT Mobile is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package com.smartgwt.mobile.client.internal.widgets;

import com.smartgwt.mobile.client.internal.theme.iphone.WindowCssResourceIPhone;
import com.smartgwt.mobile.client.types.AnimationEffect;
import com.smartgwt.mobile.client.widgets.Window;

public class WindowImplIPhone extends WindowImplIOS {

    private  static final WindowCssResourceIPhone CSS = (WindowCssResourceIPhone)Window._CSS;

    @Override
    public String lookupShowAnimationName(AnimationEffect animateShowEffect) {
        if (animateShowEffect == null) return null;
        switch (animateShowEffect) {
            case FADE:
                // use the same fade in animation as the modal mask
                return CSS.modalMaskFadeInAnimationName();
            default:
                return CSS.windowBackgroundSlideInAnimationName();
        }
    }

    @Override
    public String lookupHideAnimationName(AnimationEffect animateHideEffect) {
        if (animateHideEffect == null) return null;
        switch (animateHideEffect) {
            case FADE:
                // use the same fade out animation as the modal mask
                return CSS.modalMaskFadeOutAnimationName();
            default:
                return CSS.windowBackgroundSlideOutAnimationName();
        }
    }

    @Override
    public int getShowAnimationDurationMillis(Window self) {
        return CSS.windowBackgroundSlideInAnimationDurationMillis();
    }

    @Override
    public int getHideAnimationDurationMillis(Window self) {
        return CSS.windowBackgroundSlideOutAnimationDurationMillis();
    }
}
