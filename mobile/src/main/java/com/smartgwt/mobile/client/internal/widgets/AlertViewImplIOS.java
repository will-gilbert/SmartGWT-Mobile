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

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.theme.ios.AlertViewCssResourceIOS;
import com.smartgwt.mobile.client.types.AnimationEffect;
import com.smartgwt.mobile.client.widgets.Dialog;
import com.smartgwt.mobile.client.widgets.Window;

@SGWTInternal
public class AlertViewImplIOS extends WindowImplIOS implements AlertViewImpl {

    private static final AlertViewCssResourceIOS CSS = (AlertViewCssResourceIOS)AlertView._CSS;

    @Override
    public final void initDialog(Dialog self) {
        /*empty*/
    }

    @Override
    public void initAlertView(AlertView self) {
        /*empty*/
    }

    @Override
    public String lookupShowAnimationName(AnimationEffect animateShowEffect) {
        if (animateShowEffect == null) return null;
        switch (animateShowEffect) {
            case FADE:
                return CSS.alertViewScreenCoverFadeInAnimationName();
            default:
                return CSS.alertViewFadeInAnimationName();
        }
    }

    @Override
    public String lookupHideAnimationName(AnimationEffect animateHideEffect) {
        if (animateHideEffect == null) return null;
        switch (animateHideEffect) {
            case FADE:
                return CSS.alertViewScreenCoverFadeOutAnimationName();
            default:
                return CSS.alertViewFadeOutAnimationName();
        }
    }

    @Override
    public int getShowAnimationDurationMillis(Window self) {
        return CSS.alertViewFadeInAnimationDurationMillis();
    }

    @Override
    public int getHideAnimationDurationMillis(Window self) {
        return CSS.alertViewFadeOutAnimationDurationMillis();
    }
}
