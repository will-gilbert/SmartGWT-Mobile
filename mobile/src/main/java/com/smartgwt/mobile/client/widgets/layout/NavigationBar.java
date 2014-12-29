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

package com.smartgwt.mobile.client.widgets.layout;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.EventHandler;
import com.smartgwt.mobile.client.internal.test.AutoTestLocatable;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.theme.ios.NavigationBarCssResourceIOS;
import com.smartgwt.mobile.client.internal.util.AnimationUtil.Direction;
import com.smartgwt.mobile.client.internal.util.ElementUtil;
import com.smartgwt.mobile.client.internal.widgets.NavigationButtons;
import com.smartgwt.mobile.client.internal.widgets.layout.NavigationBarItem;
import com.smartgwt.mobile.client.internal.widgets.layout.NavigationItem;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.types.NavigationDirection;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.ContainerFeatures;
import com.smartgwt.mobile.client.widgets.Header1;
import com.smartgwt.mobile.client.widgets.NavigationButton;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.PanelContainer;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.internal.gwt.dom.client.CSSStyleDeclaration;
import com.smartgwt.mobile.internal.gwt.dom.client.ClientRect;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperDocument;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperElement;

public class NavigationBar extends Canvas implements PanelContainer {

    @SGWTInternal
    public static final NavigationBarCssResourceIOS _CSS = (NavigationBarCssResourceIOS)ThemeResources.INSTANCE.navigationBarCSS();

    private static final RegExp COMMA_SPACE_REG_EXP = RegExp.compile(",\\s*");

    private transient final NavStack navStack;
    private transient boolean isPositionFixed = false;
    private transient HandlerRegistration windowScrollRegistration;
    private transient HandlerRegistration windowResizeRegistration;
    private transient Timer mockScrollTimer;
    private transient Timer unfixPositionTimer;
    private transient Timer layoutTimer;

    private final List<NavigationItem> items = new ArrayList<NavigationItem>();

    NavigationBar(NavStack stack) {
        super.setElement(Document.get().createDivElement());
        super.setStyleName(_CSS.navigationBarClass());
        this.navStack = stack;

        _sinkContentChangedEvent();
    }

    @Override
    public AutoTestLocatable _getChildFromLocatorSubstring(String substring, int index, List<String> locatorArray, GetAttributeConfiguration configuration) {
        if (substring.equals("backButton")) return ensureItem().getBackButton();
        if (substring.equals("leftItem")) return getLeftButton();
        if (substring.equals("rightItem")) return (Canvas)_getRightBarItem();
        return super._getChildFromLocatorSubstring(substring, index, locatorArray, configuration);
    }

    private void removeHandlers() {
    }

    @Override
    public void destroy() {
        removeHandlers();
        super.destroy();
    }

    @SGWTInternal
    public void _fixPosition() {
        assert Canvas._getFixNavigationBarPositionDuringKeyboardFocus();
        if (unfixPositionTimer != null) {
            unfixPositionTimer.cancel();
            unfixPositionTimer = null;
        }
        if (!isPositionFixed) {
            final SuperElement elem = getElement().<SuperElement>cast();
            final Style elemStyle = elem.getStyle();
            isPositionFixed = true;
            elemStyle.setPosition(Style.Position.FIXED);
            elemStyle.setProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName(), DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + " 200ms");
            //elemStyle.setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "none");
        }

        // In a UIWebView, the 'scroll' event is not fired on the window when the Previous/Next
        // buttons are used to move from <input> to <input>. It is when the app is run in
        // Mobile Safari, however.
        if (Canvas.isUIWebView()) {
            if (mockScrollTimer != null) mockScrollTimer.cancel();
            mockScrollTimer = new Timer() {
                @Override
                public void run() {
                    assert this == mockScrollTimer;
                    mockScrollTimer = null;
                    _updateFixedPosition();
                }
            };
            mockScrollTimer.schedule(1);
        }
    }

    @SGWTInternal
    public void _updateFixedPosition() {
        assert Canvas._getFixNavigationBarPositionDuringKeyboardFocus();
        if (isPositionFixed) {
            final SuperElement elem = NavigationBar.this.getElement().<SuperElement>cast();
            final CSSStyleDeclaration computedStyle = elem.getComputedStyle(null);
            final String computedTransform = computedStyle.get(DOMConstants.INSTANCE.getTransformPropertyName());
            final double currentYTranslation;
            if ("none".equals(computedTransform)) {
                currentYTranslation = 0.0;
            } else {
                final SplitResult parts = COMMA_SPACE_REG_EXP.split(computedTransform);
                currentYTranslation = _nativeParseFloat(parts.get(5));
            }
            final ClientRect bcr = elem.getBoundingClientRect();
            elem.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translateY(" + Math.max(0.0, -bcr.getTop() + currentYTranslation) + "px)");
        }
    }

    @SGWTInternal
    public void _scheduleUnfixPosition() {
        assert Canvas._getFixNavigationBarPositionDuringKeyboardFocus();
        if (unfixPositionTimer != null) unfixPositionTimer.cancel();
        unfixPositionTimer = new Timer() {
            @Override
            public void run() {
                assert this == unfixPositionTimer;
                unfixPositionTimer = null;
                _unfixPosition();
            }
        };
        unfixPositionTimer.schedule(1);
    }

    @SGWTInternal
    public void _unfixPosition() {
        assert Canvas._getFixNavigationBarPositionDuringKeyboardFocus();
        isPositionFixed = false;
        if (mockScrollTimer != null) {
            mockScrollTimer.cancel();
            mockScrollTimer = null;
        }
        if (unfixPositionTimer != null) {
            unfixPositionTimer.cancel();
            unfixPositionTimer = null;
        }
        final Element elem = getElement();
        final Style elemStyle = elem.getStyle();
        elemStyle.clearPosition();
        elemStyle.clearProperty(DOMConstants.INSTANCE.getTransitionShorthandPropertyName());
        elemStyle.clearProperty(DOMConstants.INSTANCE.getTransformPropertyName());
    }

    private NavigationItem ensureItem() {
        final NavigationItem item;
        final int items_size = items.size();
        if (items_size == 0) {
            item = new NavigationItem();
            items.add(item);
        } else {
            item = items.get(items_size - 1);
        }
        return item;
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        final String eventType = event.getType();
        if (_CONTENT_CHANGED_EVENT_TYPE.equals(eventType)) {
            _layOutMembers();
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (Canvas._getFixNavigationBarPositionDuringKeyboardFocus()) {
            if (EventHandler.couldShowSoftKeyboard(Document.get().<SuperDocument>cast().getActiveElement())) {
                _fixPosition();
            }
            // In a UIWebView, the 'scroll' event is not fired on the window when the Previous/Next
            // buttons are used to move from <input> to <input>. It is when the app is run in
            // Mobile Safari, however.
            if (!Canvas.isUIWebView()) {
                windowScrollRegistration = Window.addWindowScrollHandler(new Window.ScrollHandler() {
                    @Override
                    public void onWindowScroll(ScrollEvent event) {
                        _updateFixedPosition();
                    }
                });
            }
        }
        windowResizeRegistration = Window.addResizeHandler(new ResizeHandler() {

            private Timer timer;

            @Override
            public void onResize(ResizeEvent event) {
                // A timeout is used to prevent severe choppiness when the user is resizing
                // a desktop browser window.
                if (timer == null) {
                    timer = new Timer() {
                        @Override
                        public void run() {
                            if (timer != this) return;
                            _layOutMembers();
                            timer = null;
                        }
                    };
                    timer.schedule(200);
                }
            }
        });
        _layOutMembers();
    }

    @Override
    protected void onUnload() {
        if (windowResizeRegistration != null) {
            windowResizeRegistration.removeHandler();
            windowResizeRegistration = null;
        }
        if (windowScrollRegistration != null) {
            windowScrollRegistration.removeHandler();
            windowScrollRegistration = null;
        }
        if (Canvas._getFixNavigationBarPositionDuringKeyboardFocus()) {
            _unfixPosition();
        }
        super.onUnload();
    }

    @SGWTInternal
    void _clear() {
        removeHandlers();
        if (items != null) {
            while (!items.isEmpty()) {
                final NavigationItem topItem = items.remove(items.size() - 1);
                if (topItem.getLeftButton() != null) {
                    remove(topItem.getLeftButton());
                }
                if (topItem.getTitleView() != null) {
                    remove(topItem.getTitleView());
                }
                if (topItem.getRightBarItem() != null) {
                    remove(topItem.getRightBarItem());
                }
            }
        }
    }

    public final NavigationButton getRightButton() {
        final Canvas rightBarItem = ensureItem().getRightBarItem();
        if (rightBarItem instanceof NavigationButton) {
            return (NavigationButton)rightBarItem;
        } else if (rightBarItem instanceof NavigationButtons) {
            return ((NavigationButtons)rightBarItem).getFirstButton();
        }
        return null;
    }

    public final NavigationButton getRightButton(int index) {
        final Canvas rightBarItem = ensureItem().getRightBarItem();
        if (rightBarItem instanceof NavigationButton) {
            return (index == 0 ? (NavigationButton)rightBarItem : null);
        } else if (rightBarItem instanceof NavigationButtons) {
            return ((NavigationButtons)rightBarItem).getButton(index);
        }
        return null;
    }

    public final NavigationButton[] getRightButtons() {
        final Canvas rightBarItem = ensureItem().getRightBarItem();
        if (rightBarItem instanceof NavigationButton) {
            return new NavigationButton[] { (NavigationButton)rightBarItem };
        } else if (rightBarItem instanceof NavigationButtons) {
            return ((NavigationButtons)rightBarItem).getButtons();
        }
        return null;
    }

    /**
     * Sets whether a box shadow on the <code>NavigationBar</code> element is not allowed.
     * 
     * <p>Some default skinning (e.g. for iOS 6 on iPhone) applies a slight box shadow to the
     * <code>NavigationBar</code> element that overlaps the <code>NavStack</code> content. In
     * cases where a box shadow is not desired, the <code>NavigationBar</code> can be made
     * shadowless by setting the NavigationBar.shadowless attribute to {@link java.lang.Boolean#TRUE}.
     * 
     * @param shadowless {@link java.lang.Boolean#TRUE} to disable any box shadow; <code>null</code>
     * or {@link java.lang.Boolean#FALSE} to permit a box shadow. Default value: <code>null</code>.
     */
    public void setShadowless(Boolean shadowless) {
        if (shadowless != null && shadowless.booleanValue()) {
            getElement().addClassName(_CSS.shadowlessNavigationBarClass());
        } else {
            getElement().removeClassName(_CSS.shadowlessNavigationBarClass());
        }
    }

    public void setRightButton(NavigationButton rightButton) {
        _setRightBarItem(rightButton);
    }

    public void setRightButtons(NavigationButton... rightButtons) {
        if (rightButtons == null || rightButtons.length == 0) {
            _setRightBarItem((NavigationButton)null);
        } else if (rightButtons.length == 1) {
            _setRightBarItem(rightButtons[0]);
        } else {
            _setRightBarItem(new NavigationButtons(rightButtons));
        }
    }

    @SGWTInternal
    public final NavigationBarItem _getRightBarItem() {
        return (NavigationBarItem)ensureItem().getRightBarItem();
    }

    @SGWTInternal
    public <T extends Canvas & NavigationBarItem> void _setRightBarItem(T rightBarItem) {
        final NavigationItem item = ensureItem();
        setRightBarItem(item, item, rightBarItem, true, Direction.NONE);
    }

    @SGWTInternal
    public <T extends Canvas & NavigationBarItem> void _setRightBarItem(int numSlotsFromTop, T rightBarItem) {
        if (numSlotsFromTop == 0) {
            _setRightBarItem(rightBarItem);
        } else {
            final NavigationItem item = items.get(items.size() - numSlotsFromTop - 1);
            item.setRightBarItem(rightBarItem);
        }
    }

    private void setRightBarItem(NavigationItem oldItem, NavigationItem item, Canvas rightBarItem, boolean doLayOut, Direction direction) {
        assert rightBarItem == null || (rightBarItem instanceof NavigationBarItem);
        Canvas button = oldItem.getRightBarItem();
        if (button == rightBarItem) return;
        if (button != null) {
            deferredRemove(this, button, button.getElement());
            switch(direction) {
            case RIGHT:
                button._setClassName(_CSS.navbarSlideOutLeftClass(), false);
                break;
            case LEFT:
                button._setClassName(_CSS.navbarSlideOutRightClass(), false);
                break;
            case UP:
            case DOWN:
            case NONE:
                button._setClassName(_CSS.navbarFadeOutClass(), false);
                break;
            }
        }
        item.setRightBarItem(rightBarItem);
        if (rightBarItem != null) {
            postAnimate(rightBarItem.getElement());
            if (button != null) {
                switch (direction) {
                    case RIGHT:
                        rightBarItem._setClassName(_CSS.navbarSlideInFromRightClass(), false);
                        break;
                    case LEFT:
                        rightBarItem._setClassName(_CSS.navbarSlideInFromLeftClass(), false);
                        break;
                    case UP:
                    case DOWN:
                    case NONE:
                        rightBarItem._setClassName(_CSS.navbarFadeInClass(), false);
                        break;
                }
            } else {
                rightBarItem._setClassName(_CSS.navbarFadeInClass(), false);
            }
            add(rightBarItem, getElement().<com.google.gwt.user.client.Element>cast());
        }
        if (doLayOut) _layOutMembers();
    }

    public final NavigationButton getLeftButton() {
        return ensureItem().getLeftButton();
    }

    public void setLeftButton(NavigationButton leftButton) {
        final NavigationItem item = ensureItem();
        setLeftButton(item, item, leftButton, null, true, Direction.NONE);
    }

    private static void finishDeferredRemove(Element element) {
        element.removeClassName(_CSS.navbarFadeOutClass());
        element.removeClassName(_CSS.navbarSlideOutRightClass());
        element.removeClassName(_CSS.navbarSlideOutLeftClass());
    }
    private static native void deferredRemove(NavigationBar bar, Canvas item, Element element) /*-{
        var listener = $entry(function (event) {
            if (element != event.target) return;
            bar.@com.smartgwt.mobile.client.widgets.layout.NavigationBar::remove(Lcom/smartgwt/mobile/client/widgets/Canvas;)(item);
            element.removeEventListener(@com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants::INSTANCE.@com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants::getAnimationEndEventType()(), listener, false);
            listener = null;
            @com.smartgwt.mobile.client.widgets.layout.NavigationBar::finishDeferredRemove(Lcom/google/gwt/dom/client/Element;)(element);
        });
        element.addEventListener(@com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants::INSTANCE.@com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants::getAnimationEndEventType()(), listener, false);
    }-*/;

    private static void finishPostAnimate(Element element) {
        element.removeClassName(_CSS.navbarFadeInClass());
        element.removeClassName(_CSS.navbarSlideInFromRightClass());
        element.removeClassName(_CSS.navbarSlideInFromLeftClass());
    }
    private static native void postAnimate(Element element) /*-{
        var listener = $entry(function (event) {
            if (element != event.target) return;
            element.removeEventListener(@com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants::INSTANCE.@com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants::getAnimationEndEventType()(), listener, false);
            listener = null;
            @com.smartgwt.mobile.client.widgets.layout.NavigationBar::finishPostAnimate(Lcom/google/gwt/dom/client/Element;)(element);
        });
        element.addEventListener(@com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants::INSTANCE.@com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants::getAnimationEndEventType()(), listener, false);
    }-*/;

    private void remove(Canvas w) {
        super.remove((Widget)w);
    }

    private void insert(Canvas w, Element container, int beforeIndex) {
        super.insert((Widget)w, (com.google.gwt.user.client.Element) container, beforeIndex, true);
    }

    private void setLeftButton(NavigationItem oldItem, NavigationItem item, NavigationButton leftButton, NavigationButton backButton, boolean doLayOut, Direction direction) {
        NavigationButton button = oldItem.getLeftButton();
        if (button == leftButton) return;
        if (button != null) {
            deferredRemove(this, button, button.getElement());
            switch(direction) {
            case RIGHT:
                button._setClassName(_CSS.navbarSlideOutLeftClass(), false);
                break;
            case LEFT:
                button._setClassName(_CSS.navbarSlideOutRightClass(), false);
                break;
            case UP:
            case DOWN:
            case NONE:
                button._setClassName(_CSS.navbarFadeOutClass(), false);
                break; 
            }
        }
        if (oldItem == item || (oldItem != item && backButton == null)) {
            item.setLeftButton(leftButton);
        } else {
            item.setBackButton(leftButton, navStack);
        }
        if (leftButton != null) {
            postAnimate(leftButton.getElement());
            switch (direction) {
                case RIGHT:
                    leftButton._setClassName(_CSS.navbarSlideInFromRightClass(), false);
                    break;
                case LEFT:
                    leftButton._setClassName(_CSS.navbarSlideInFromLeftClass(), false);
                    break;
                case UP:
                case DOWN:
                case NONE:
                    leftButton._setClassName(_CSS.navbarFadeInClass(), false);
                    break;
            }
            insert(leftButton, getElement().<com.google.gwt.user.client.Element>cast(), 0, true);
        }
        if (doLayOut) _layOutMembers();
    }

    public final Header1 getTitleLabel() {
        return ensureItem().getTitleView();
    }

    @SGWTInternal
    public void _layOutMembers() {
        if (! isAttached()) return;
        if (! isVisible() || ! navStack.isVisible()) return;
        if (layoutTimer == null) {
            layoutTimer = new Timer() {

                @Override
                public void run() {
                    final float _1vw = Window.getClientWidth(); // 1 viewport width
                    final NavigationItem item = ensureItem();
                    final Header1 titleView = item.getTitleView();
                    final float minTitleViewWidth, titleViewContentWidth;
                    if (titleView == null) {
                        minTitleViewWidth = 0.0f;
                        titleViewContentWidth = 0.0f;
                    } else {
                        final SpanElement span = titleView._getSpan();
                        span.getStyle().clearPadding();
                        final float titleViewWidth = ElementUtil.getOuterWidth(titleView.getElement(), true);
                        minTitleViewWidth = titleViewWidth - titleView.getElement().getClientWidth();
                        titleViewContentWidth = span.getOffsetWidth();
                    }
                    final float titleViewAutoWidth = minTitleViewWidth + titleViewContentWidth;
                    final NavigationButton leftButton = item.getLeftButton();
                    final Canvas rightBarItem = item.getRightBarItem();
                    final float leftButtonHMargin, absoluteMinLeftButtonWidth, leftButtonContentWidth;
                    if (leftButton == null) {
                        leftButtonHMargin = 0.0f;
                        absoluteMinLeftButtonWidth = 0.0f;
                        leftButtonContentWidth = 0.0f;
                    } else {
                        final Element element = leftButton.getElement();
                        leftButtonHMargin = ElementUtil.getHMarginWidth(element);
                        absoluteMinLeftButtonWidth = ElementUtil.getMinMarginBoxWidth(element);
                        leftButtonContentWidth = leftButton._getContentWidth();
                    }
                    final float rightBarItemHMargin, absoluteMinRightBarItemWidth, rightBarItemContentWidth;
                    if (rightBarItem == null) {
                        rightBarItemHMargin = 0.0f;
                        absoluteMinRightBarItemWidth = 0.0f;
                        rightBarItemContentWidth = 0.0f;
                    } else {
                        final Element element = rightBarItem.getElement();
                        rightBarItemHMargin = ElementUtil.getHMarginWidth(element);
                        absoluteMinRightBarItemWidth = ElementUtil.getMinMarginBoxWidth(element);
                        rightBarItemContentWidth = ((NavigationBarItem)rightBarItem)._getContentWidth();
                    }
                    final float leftButtonAutoWidth = absoluteMinLeftButtonWidth + leftButtonContentWidth,
                            rightBarItemAutoWidth = absoluteMinRightBarItemWidth + rightBarItemContentWidth;

                    final float totalWidth = titleViewAutoWidth + leftButtonAutoWidth + rightBarItemAutoWidth;

                    float newLeftButtonMarginBoxWidth = 0.0f, newRightButtonMarginBoxWidth = 0.0f;
                    if (totalWidth + 0.000001f >= _1vw) {
                        final float minLeftButtonWidth = leftButton == null ? 0.0f : absoluteMinLeftButtonWidth + Math.min(53.0f, leftButtonContentWidth),
                                minRightBarItemWidth = rightBarItem == null ? 0.0f : absoluteMinRightBarItemWidth + (rightBarItem instanceof NavigationButtons ? rightBarItemContentWidth : Math.min(53.0f, rightBarItemContentWidth));
                        assert minLeftButtonWidth <= leftButtonAutoWidth + 0.000001f;
                        assert minRightBarItemWidth <= rightBarItemAutoWidth + 0.000001f;

                        final float r = _1vw - titleViewAutoWidth - minLeftButtonWidth - minRightBarItemWidth;
                        if (r < 0.000001f) {
                            newLeftButtonMarginBoxWidth = minLeftButtonWidth;
                            newRightButtonMarginBoxWidth = minRightBarItemWidth;
                        } else { // The title gets as much width as it needs while still giving the left and/or right
                                 // buttons a minimum client width (at most 53px).  The remaining width is distributed to
                                 // the left and right buttons.
                            if (leftButton != null || rightBarItem != null) {
                                final float denominator = leftButtonAutoWidth - minLeftButtonWidth + rightBarItemAutoWidth - minRightBarItemWidth;
                                if (denominator < 2.0f + 0.000001f) {
                                    newLeftButtonMarginBoxWidth = minLeftButtonWidth;
                                    newRightButtonMarginBoxWidth = minRightBarItemWidth;
                                } else {
                                    newLeftButtonMarginBoxWidth = minLeftButtonWidth + (leftButtonAutoWidth - minLeftButtonWidth) / denominator * r;
                                    newRightButtonMarginBoxWidth = minRightBarItemWidth + (rightBarItemAutoWidth - minRightBarItemWidth) / denominator * r;
                                }
                            }
                        }
                    } else {
                        newLeftButtonMarginBoxWidth = leftButtonAutoWidth;
                        newRightButtonMarginBoxWidth = rightBarItemAutoWidth;

                        final float leftButtonExtra = Math.max(0.0f, leftButtonAutoWidth - rightBarItemAutoWidth),
                                rightButtonExtra = Math.max(0.0f, rightBarItemAutoWidth - leftButtonAutoWidth);
                        float r = _1vw - totalWidth;
                        assert r + 0.000001f >= 0.0f;
                        if (titleView != null) {
                            if (r + 0.000001f >= leftButtonExtra + rightButtonExtra) {
                                final float rightPadding = leftButtonExtra,
                                        leftPadding = rightButtonExtra;
                                final SpanElement span = titleView._getSpan();
                                final Style spanStyle = span.getStyle();
                                spanStyle.setPaddingRight(rightPadding, Unit.PX);
                                spanStyle.setPaddingLeft(leftPadding, Unit.PX);
                            } else {
                                if (leftButtonExtra + 0.000001f >= rightButtonExtra) {
                                    final float rightPadding = r;
                                    final SpanElement span = titleView._getSpan();
                                    span.getStyle().setPaddingRight(rightPadding, Unit.PX);
                                } else {
                                    final float leftPadding = r;
                                    final SpanElement span = titleView._getSpan();
                                    span.getStyle().setPaddingLeft(leftPadding, Unit.PX);
                                }
                            }
                        }
                    }
                    if (leftButton != null) {
                        ElementUtil.setBorderBoxWidth(leftButton.getElement(), newLeftButtonMarginBoxWidth - leftButtonHMargin);
                    }
                    if (rightBarItem != null) {
                        ElementUtil.setBorderBoxWidth(rightBarItem.getElement(), newRightButtonMarginBoxWidth - rightBarItemHMargin);
                    }
                    if (titleView != null) {
                        // Instead of setting the left and right padding, set the `left' and `right'
                        // CSS properties.
                        // There is a WebKit bug that affects iOS 6 where left and right padding
                        // is incorrectly factored into the calculation of how wide the <h1>
                        // element is for the purpose of centering the text.
                        // https://bugs.webkit.org/show_bug.cgi?id=75277
                        titleView.getElement().getStyle().setLeft(newLeftButtonMarginBoxWidth, Unit.PX);
                        titleView.getElement().getStyle().setRight(newRightButtonMarginBoxWidth, Unit.PX);
                        titleView.getElement().getStyle().clearVisibility();
                    }

                    layoutTimer = null;
                } // run()
            };
            layoutTimer.schedule(1);
        }
    }

    public void setTitleLabel(String titleLabelHtml) {
        setTitleLabel(new Header1(titleLabelHtml));
    }

    public void setTitleLabel(Header1 titleLabel) {
        final NavigationItem item = ensureItem();
        setTitleLabel(item, item, titleLabel, true, Direction.LEFT);
    }

    private void setTitleLabel(NavigationItem oldItem, NavigationItem item, Header1 titleLabel, boolean doLayOut, Direction direction) {
        final Header1 title = oldItem.getTitleView();
        if (title == titleLabel) return;
        if (title != null) {
            deferredRemove(this, title, title.getElement());
            if(direction == Direction.RIGHT) { 
                title._setClassName(_CSS.navbarSlideOutLeftClass(), false);
            } else {
                title._setClassName(_CSS.navbarSlideOutRightClass(), false);
            }
        }
        item.setTitleView(titleLabel);

        if (titleLabel != null) {
            final int beforeIndex;
            if (item.getLeftButton() != null) {
                beforeIndex = 1;
            } else {
                beforeIndex = 0;
            }

            // Add a dummy handler and remove it.
            // The reason for this is that in iOS 6 Mobile Safari, but not iOS 5.0 or 5.1,
            // a SmartGWT.mobile app can fail to load unless the Web Inspector is open prior to
            // loading the page. Adding and removing any `EventHandler' fixes the problem.
            final HandlerRegistration clickRegistration = titleLabel.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {}
            });
            clickRegistration.removeHandler();

            if (navStack.size() > 0) {
                postAnimate(titleLabel.getElement());
                if (direction == Direction.RIGHT) {
                    titleLabel._setClassName(_CSS.navbarSlideInFromRightClass(), false);
                } else {
                    titleLabel._setClassName(_CSS.navbarSlideInFromLeftClass(), false);
                }
            }
            if (title != null) {
                insert(titleLabel, getElement().<com.google.gwt.user.client.Element>cast(), 2, true);
            } else {
                insert(titleLabel, getElement().<com.google.gwt.user.client.Element>cast(), beforeIndex, true);
            }
        }
        if (doLayOut) _layOutMembers();
    }

    void _push(Panel panel) {
        final Panel oldPanel = navStack.get(navStack.size() - 2);
        final NavigationItem oldItem = ensureItem();
        final NavigationItem item = new NavigationItem();
        items.add(item);
        final NavigationButton backButton = new NavigationButton(oldPanel.getTitle(), NavigationDirection.BACK);
        setLeftButton(oldItem, item, backButton, backButton, false, Direction.RIGHT);
        this.setRightBarItem(oldItem, item, null, false, Direction.RIGHT);
        setTitleLabel(oldItem, item, new Header1(panel.getTitle()), false, Direction.RIGHT);
        _layOutMembers();
    }

    void _pop() {
        final NavigationItem oldItem = items.remove(items.size() - 1);
        NavigationItem item = ensureItem();
        final Panel panel = navStack.top();
        final String newTitle = panel.getTitle();
        final Header1 titleLabel = new Header1(newTitle);
        setTitleLabel(oldItem, item, titleLabel, false, Direction.LEFT);
        setLeftButton(oldItem, item, item.getLeftButton(), item.getBackButton(), false, Direction.LEFT);
        Canvas rightBarItem = item.getRightBarItem();
        setRightBarItem(oldItem, item, rightBarItem, false, Direction.LEFT);
        _layOutMembers();
        oldItem.destroy();
	}

	public void setSinglePanel(Panel panel) {
        navStack.setSinglePanel(panel);
	}

    @Override
    public final ContainerFeatures getContainerFeatures() {
        return new ContainerFeatures(this, false, false, false, true, 0);
    }
}
