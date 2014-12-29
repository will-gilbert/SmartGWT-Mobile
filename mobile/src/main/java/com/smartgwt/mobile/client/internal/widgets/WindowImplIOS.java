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
import com.google.gwt.user.client.Timer;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.EventUtil;
import com.smartgwt.mobile.client.types.AnimationEffect;
import com.smartgwt.mobile.client.widgets.Window;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;
import com.smartgwt.mobile.internal.gwt.dom.client.event.AnimationEndEvent;

@SGWTInternal
public abstract class WindowImplIOS implements WindowImpl {

    private transient Timer delayedOnShownTimer;
    private transient Timer delayedOnHiddenTimer;

    @Override
    public void init(Window self) {
        self._sinkAnimationEndEvent();
    }

    @Override
    public void destroyImpl(Window self) {
        /*empty*/
    }

    @Override
    public AnimationEffect getDefaultAnimateShowEffect(Window self) {
        return AnimationEffect.SLIDE;
    }

    @Override
    public void animateShowEffectChanged(Window self, AnimationEffect newAnimateShowEffect) {
        self._setShowAnimationName(lookupShowAnimationName(newAnimateShowEffect));
    }

    @Override
    public AnimationEffect getDefaultAnimateHideEffect(Window self) {
        return AnimationEffect.SLIDE;
    }

    @Override
    public void animateHideEffectChanged(Window self, AnimationEffect newAnimateHideEffect) {
        self._setHideAnimationName(lookupHideAnimationName(newAnimateHideEffect));
    }

    @Override
    public Element getAnimatedElem(Window self) {
        return self._getBackgroundElem();
    }

    @Override
    public void doShow(final Window self) {
        assert delayedOnShownTimer == null;
        assert delayedOnHiddenTimer == null;
        final String showAnimationName = self._getShowAnimationName();
        if (showAnimationName != null) {
            getAnimatedElem(self).getStyle().setProperty(DOMConstants.INSTANCE.getAnimationShorthandPropertyName(), showAnimationName + " " + getShowAnimationDurationMillis(self) + "ms both");
        } else {
            getAnimatedElem(self).getStyle().setProperty(DOMConstants.INSTANCE.getAnimationShorthandPropertyName(), "none");
            delayedOnShownTimer = new Timer() {
                @Override
                public void run() {
                    assert delayedOnShownTimer == this;
                    delayedOnShownTimer = null;
                    self._onShown();
                }
            };
            delayedOnShownTimer.schedule(1);
        }
    }

    @Override
    public void doHide(final Window self) {
        assert delayedOnShownTimer == null;
        assert delayedOnHiddenTimer == null;
        final String hideAnimationName = self._getHideAnimationName();
        if (hideAnimationName != null) {
            getAnimatedElem(self).getStyle().setProperty(DOMConstants.INSTANCE.getAnimationShorthandPropertyName(), hideAnimationName + " " + getHideAnimationDurationMillis(self) + "ms both");
        } else {
            getAnimatedElem(self).getStyle().setProperty(DOMConstants.INSTANCE.getAnimationShorthandPropertyName(), "none");
            delayedOnHiddenTimer = new Timer() {
                @Override
                public void run() {
                    assert delayedOnHiddenTimer == this;
                    delayedOnHiddenTimer = null;
                    self._onHidden();
                }
            };
            delayedOnHiddenTimer.schedule(1);
        }
    }

    public void showAnimationNameChanged(final Window self, String newShowAnimationName) {
        if (self._isShowing()) {
            if (delayedOnShownTimer != null) {
                delayedOnShownTimer.cancel();
                delayedOnShownTimer = null;
            }
            if (newShowAnimationName != null) {
                getAnimatedElem(self).getStyle().setProperty(DOMConstants.INSTANCE.getAnimationShorthandPropertyName(), newShowAnimationName + " " + getShowAnimationDurationMillis(self) + "ms both");
            } else {
                getAnimatedElem(self).getStyle().setProperty(DOMConstants.INSTANCE.getAnimationShorthandPropertyName(), "none");
                delayedOnShownTimer = new Timer() {
                    @Override
                    public void run() {
                        assert delayedOnShownTimer == this;
                        delayedOnShownTimer = null;
                        self._onShown();
                    }
                };
                delayedOnShownTimer.schedule(1);
            }
        }
    }

    public void hideAnimationNameChanged(final Window self, String newHideAnimationName) {
        if (self._isHiding()) {
            if (delayedOnHiddenTimer != null) {
                delayedOnHiddenTimer.cancel();
                delayedOnHiddenTimer = null;
            }
            if (newHideAnimationName != null) {
                getAnimatedElem(self).getStyle().setProperty(DOMConstants.INSTANCE.getAnimationShorthandPropertyName(), newHideAnimationName + " " + getHideAnimationDurationMillis(self) + "ms both");
            } else {
                getAnimatedElem(self).getStyle().setProperty(DOMConstants.INSTANCE.getAnimationShorthandPropertyName(), "none");
                delayedOnHiddenTimer = new Timer() {
                    @Override
                    public void run() {
                        assert delayedOnHiddenTimer == this;
                        delayedOnHiddenTimer = null;
                        self._onHidden();
                    }
                };
                delayedOnHiddenTimer.schedule(1);
            }
        }
    }

    @Override
    public void onBrowserEvent(Window self, Event event) {
        final Element targetElem = EventUtil.getTargetElem(event);
        if (targetElem != null) {
            final String eventType = event.getType();
            if (DOMConstants.INSTANCE.getAnimationEndEventType().equals(eventType)) {
                if (getAnimatedElem(self).equals(targetElem)) {
                    final AnimationEndEvent aeEvent = event.cast();
                    final String animationName = aeEvent.getAnimationName();
                    if (animationName != null) {
                        if (animationName.equals(self._getShowAnimationName())) {
                            self._onShown();
                        } else if (animationName.equals(self._getHideAnimationName())) {
                            self._onHidden();
                        }
                    }
                }
            }
        }
    }
}
