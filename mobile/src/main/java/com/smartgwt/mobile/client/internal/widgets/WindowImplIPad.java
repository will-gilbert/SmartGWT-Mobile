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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.smartgwt.mobile.client.internal.theme.ipad.WindowCssResourceIPad;
import com.smartgwt.mobile.client.types.AnimationEffect;
import com.smartgwt.mobile.client.widgets.Window;

public class WindowImplIPad extends WindowImplIOS {

    private static final WindowCssResourceIPad CSS = (WindowCssResourceIPad)Window._CSS;

    private transient Element outerWrapperElem;
    private transient Element wrapperElem;

    @Override
    public void init(Window self) {
        super.init(self);

        outerWrapperElem = Document.get().createDivElement();
        outerWrapperElem.setClassName(CSS.alertViewOuterWrapperClass());
        self.getElement().appendChild(outerWrapperElem);

        wrapperElem = Document.get().createDivElement();
        wrapperElem.setClassName(CSS.alertViewWrapperClass());
        outerWrapperElem.appendChild(wrapperElem);

        wrapperElem.appendChild(self._getBackgroundElem());
    }

    @Override
    public AnimationEffect getDefaultAnimateShowEffect(Window self) {
        return AnimationEffect.FADE;
    }

    @Override
    public String lookupShowAnimationName(AnimationEffect animateShowEffect) {
        if (animateShowEffect == null) return null;
        switch (animateShowEffect) {
            case FADE:
                return CSS.windowBackgroundFadeInAnimationName();
            default:
                return null;
        }
    }

    @Override
    public AnimationEffect getDefaultAnimateHideEffect(Window self) {
        return AnimationEffect.FADE;
    }

    @Override
    public String lookupHideAnimationName(AnimationEffect animateHideEffect) {
        if (animateHideEffect == null) return null;
        switch (animateHideEffect) {
            case FADE:
                return CSS.windowBackgroundFadeOutAnimationName();
            default:
                return null;
        }
    }

    @Override
    public int getShowAnimationDurationMillis(Window self) {
        return CSS.windowBackgroundFadeInAnimationDurationMillis();
    }

    @Override
    public int getHideAnimationDurationMillis(Window self) {
        return CSS.windowBackgroundFadeOutAnimationDurationMillis();
    }
}
