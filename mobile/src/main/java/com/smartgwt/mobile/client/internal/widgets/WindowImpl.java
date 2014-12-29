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

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.types.AnimationEffect;
import com.smartgwt.mobile.client.widgets.Window;

@SGWTInternal
public interface WindowImpl {

    public void init(Window self);

    public void destroyImpl(Window self);

    public AnimationEffect getDefaultAnimateShowEffect(Window self);

    public String lookupShowAnimationName(AnimationEffect animateShowEffect);

    public void animateShowEffectChanged(Window self, AnimationEffect newAnimateShowEffect);

    public AnimationEffect getDefaultAnimateHideEffect(Window self);

    public String lookupHideAnimationName(AnimationEffect animateHideEffect);

    public void animateHideEffectChanged(Window self, AnimationEffect newAnimateHideEffect);

    public Element getAnimatedElem(Window self);

    public int getShowAnimationDurationMillis(Window self);

    public void doShow(Window self);

    public int getHideAnimationDurationMillis(Window self);

    public void doHide(Window self);

    public void showAnimationNameChanged(Window self, String newShowAnimationName);

    public void hideAnimationNameChanged(Window self, String newHideAnimationName);

    public void onBrowserEvent(Window self, Event event);
}
