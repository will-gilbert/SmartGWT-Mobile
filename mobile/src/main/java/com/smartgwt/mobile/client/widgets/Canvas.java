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

package com.smartgwt.mobile.client.widgets;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.EventHandler;
import com.smartgwt.mobile.client.internal.EventUtil;
import com.smartgwt.mobile.client.internal.test.AutoTestLocatable;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.types.AndroidWindowSoftInputMode;
import com.smartgwt.mobile.client.internal.types.AttributeType;
import com.smartgwt.mobile.client.internal.util.ElementUtil;
import com.smartgwt.mobile.client.internal.widgets.CanvasStaticImpl;
import com.smartgwt.mobile.client.theme.BaseCssResource;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.types.PageOrientation;
import com.smartgwt.mobile.client.util.Page;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.util.events.OrientationChangeEvent;
import com.smartgwt.mobile.client.util.events.OrientationChangeHandler;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.events.HasClickHandlers;
import com.smartgwt.mobile.client.widgets.events.HasShowContextMenuHandlers;
import com.smartgwt.mobile.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.mobile.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.mobile.client.widgets.form.fields.SubmitItem;
import com.smartgwt.mobile.client.widgets.grid.ListGrid;
import com.smartgwt.mobile.client.widgets.layout.Layout;
import com.smartgwt.mobile.client.widgets.menu.Menu;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;

/**
 * Canvas is the base class for SmartGWT.mobile widgets.
 */
public class Canvas extends ComplexPanel implements AutoTestLocatable, HasClickHandlers, HasShowContextMenuHandlers {

    private static final CanvasStaticImpl IMPL = GWT.create(CanvasStaticImpl.class);

    static {
        EventHandler.maybeInit();
        SC._maybeInit();

        RootLayoutPanel.get().addDomHandler(new TouchMoveHandler(){
            @Override
            public void onTouchMove(TouchMoveEvent event) {
                if (Canvas.useIOSNativeScrolling()) {
                    final Element targetElem = EventUtil.getElement(event.getNativeEvent().getEventTarget());
                    for (Element el = targetElem; el != null; el = el.getParentElement()) {
                        if (ElementUtil.hasClassName(el, "sc-scrollable")) return;
                    }
                }

                event.preventDefault();
            }
        }, TouchMoveEvent.getType());
        if (isIPad() || isIPhone()) {
            Page.addOrientationChangeHandler(new OrientationChangeHandler() {
                @Override
                public void onOrientationChange(OrientationChangeEvent event) {
                    Canvas._hideAddressBarNow();
                }
            });
        } else {
            Window.addResizeHandler(new ResizeHandler() {
                @Override
                public void onResize(ResizeEvent event) {
                    Canvas._hideAddressBar();
                }
            });
        }

        _hideAddressBarNow();

        nativeStaticInitialize();
    }

    private static native void clearGlobal(String name) /*-{
        if (name) delete $wnd[name];
    }-*/;

    @SGWTInternal
    public static native double _nativeParseFloat(String str) /*-{
        return parseFloat(str);
    }-*/;
    @SGWTInternal
    public static native void _triggerDebugger() /*-{
        debugger;
    }-*/;

    @SGWTInternal
    public static final BaseCssResource _CSS = ThemeResources.INSTANCE.baseCSS();

    @SGWTInternal
    public static final String _CONTENT_CHANGED_EVENT_TYPE = "scContentChanged";
    @SGWTInternal
    public static final String _REQUEST_SCROLL_TO_EVENT_TYPE = "scRequestScrollTo";

    private static native boolean dispatchCapturedEvent(com.google.gwt.user.client.Event evt) /*-{
        var ret = @com.google.gwt.user.client.DOM::previewEvent(Lcom/google/gwt/user/client/Event;)(evt);
        if (ret == false) {
            if (evt != null) {
                evt.stopPropagation();
                evt.preventDefault();
            }
            return false;
        }
    }-*/;

    private static native void nativeStaticInitialize() /*-{
        // See DOMImplStandard.initEventSystem() for the model.
        // This allows 'animationend', 'input', and 'transitionend' events to be previewed.

        var domConstantsInstance = @com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants::INSTANCE;

        var animationEndEventType = domConstantsInstance.@com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants::getAnimationEndEventType()();
        $wnd.addEventListener(animationEndEventType, @com.smartgwt.mobile.client.widgets.Canvas::dispatchCapturedEvent(Lcom/google/gwt/user/client/Event;), true);

        $wnd.addEventListener("input", @com.smartgwt.mobile.client.widgets.Canvas::dispatchCapturedEvent(Lcom/google/gwt/user/client/Event;), true);

        var transitionEndEventType = domConstantsInstance.@com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants::getTransitionEndEventType()();
        $wnd.addEventListener(transitionEndEventType, @com.smartgwt.mobile.client.widgets.Canvas::dispatchCapturedEvent(Lcom/google/gwt/user/client/Event;), true);
    }-*/;

    private static native void sinkBitlessEvent(Element element, String eventType) /*-{
        // See DOMImplStandard.sinkBitlessEventImpl() for the model.

        // First call removeEventListener() so that DOMImplStandard.dispatchEvent() is not
        // added as a listener more than once.
        element.removeEventListener(eventType, @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent, false);
        element.addEventListener(eventType, @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent, false);
    }-*/;

    @SGWTInternal
    public static boolean _booleanValue(Boolean b, boolean nullEquivalent) {
        return (b == null ? nullEquivalent : b.booleanValue());
    }

    @SGWTInternal
    public static boolean _isDifferent(Boolean prevB, Boolean b, boolean nullEquivalent) {
        //               +--------------------+
        //               |         b          |
        //  null == true |                    |
        //               | null | true | false|
        // +-------------+------+------+------+
        // |        null |  F   |  F   |  T   |
        // |        -----+------+------+------+
        // | prevB  true |  F   |  F   |  T   |
        // |        -----+------+------+------+
        // |        false|  T   |  T   |  F   |
        // +-------------+------+------+------+
        //
        //
        //               +--------------------+
        //               |         b          |
        //  null == false|                    |
        //               | null | true | false|
        // +-------------+------+------+------+
        // |        null |  F   |  T   |  F   |
        // |        -----+------+------+------+
        // | prevB  true |  T   |  F   |  T   |
        // |        -----+------+------+------+
        // |        false|  F   |  T   |  F   |
        // +-------------+------+------+------+
        return ((prevB == null && b != null && b.booleanValue() != nullEquivalent) ||
                (prevB != null && ((b == null && prevB.booleanValue() != nullEquivalent) ||
                                   (b != null && prevB.booleanValue() != b.booleanValue()))));
    }


    @SGWTInternal
    public static final boolean _HISTORY_ENABLED = IMPL.isHistoryEnabled();

    @SGWTInternal
    protected static boolean touched = false;

    protected String id;
    private String styleName;
    private boolean disabled = false;
    private Menu contextMenu;

	protected native int parseDimension(String dim) /*-{
        if (dim == "auto" || dim == "inherit" || dim == "") {
          return 0;
        } else {
          dim = dim.replace(/^(\s*[+-]?((\d+\.?\d*)|(\.\d+))([eE][+-]?\d+)?)(.*)$/, "$1");
          return parseInt(dim);
        }
    }-*/;
	protected native String split(String path, String token, int part) /*-{
        var parts = path.split(token);
        if(parts && parts.length && (parts.length > part)) {
            return unescape(parts[part]);
        }
        return "";
    }-*/;
    public static native boolean isStandAlone() /*-{
        return $wnd.navigator.standalone || false;
    }-*/;
    @SGWTInternal
    protected static void _hideAddressBarNow() {
        // In iOS 7, the scrollTo() hack to hide the address bar does not work anymore.
        // http://www.mobilexweb.com/blog/safari-ios7-html5-problems-apis-review
        // We still run some code in _maybeHideAddressBar() on iPad, however, to work around
        // the issue that 20px is cut off from the top or bottom in landscape mode.
        // Also, on iPhone running in a UIWebView, we need to move up the RootLayoutPanel's
        // bottom coord.
        final Style rootLayoutPanelElementStyle = RootLayoutPanel.get().getElement().getStyle();
        if (IMPL.isIOSMin7_0()) {
            if (!Canvas.isUIWebView()) {
                // Work around an issue with iOS 7 Mobile Safari on an iPad in landscape mode.
                // http://stackoverflow.com/questions/19012135/ios-7-ipad-safari-landscape-innerheight-outerheight-layout-issue
                // http://stackoverflow.com/questions/18855642/ios-7-css-html-height-100-692px
                if (Canvas.isIPad()) {
                    if (Page.getOrientation() == PageOrientation.LANDSCAPE) {
                        rootLayoutPanelElementStyle.setHeight(Window.getClientHeight() - 20, Style.Unit.PX);
                    } else {
                        rootLayoutPanelElementStyle.setHeight(100, Style.Unit.PCT);
                    }

                // On iPhone running iOS 7.1 using the 'minimal-ui' viewport parameter, the top 20 CSS pixels
                // in landscape mode should not be used because tapping within this area causes the browser
                // chrome to be revealed: http://www.mobilexweb.com/blog/ios-7-1-safari-minimal-ui-bugs
                } else if (Canvas.isIPhone() && IMPL.isIOSMin7_1()) {
                    if (Page.getOrientation() == PageOrientation.LANDSCAPE) {
                        rootLayoutPanelElementStyle.clearHeight();
                        rootLayoutPanelElementStyle.setTop(20, Style.Unit.PX);
                    } else {
                        rootLayoutPanelElementStyle.setHeight(100, Style.Unit.PCT);
                        rootLayoutPanelElementStyle.setTop(0, Style.Unit.PX);
                    }
                }

            // Since, when running in a UIWebView, the device-width/device-height
            // includes the height of the status bar, set the bottom of the RootLayoutPanel's
            // element to 20px (the standard height of the status bar).
            } else if (Canvas.isUIWebView()) {
                rootLayoutPanelElementStyle.clearHeight();
                rootLayoutPanelElementStyle.setBottom(20.0, Style.Unit.PX);
            }

            Window.scrollTo(0, 0);
        } else if (isIPhone() && !isStandAlone() && !isUIWebView()) {
            rootLayoutPanelElementStyle.setHeight(Window.getClientHeight() + 100 - 40, Style.Unit.PX);
            Window.scrollTo(0, 0);
        } else {
            rootLayoutPanelElementStyle.setHeight(Window.getClientHeight(), Style.Unit.PX);
        }
    }
    @SGWTInternal
    protected static void _hideAddressBar() {
        new Timer() {
            public void run() {
                _hideAddressBarNow();
            }
        }.schedule(100);
    }

    @SGWTInternal
    public static native boolean _isHDPIDisplay() /*-{
        // http://stackoverflow.com/questions/5457913/why-does-ios-simulator-render-my-images-at-lower-resolution-in-mobile-safari-for#comment6319656_5516767
        return ("devicePixelRatio" in $wnd && $wnd.devicePixelRatio >= 1.5);
    }-*/;
    public static boolean isAndroid() {
        return IMPL.isAndroid();
    }
    public static boolean isIPad() {
        return IMPL.isIPad();
    }
    public static boolean isIPhone() {
        return IMPL.isIPhone();
    }
    public static boolean isSafari() {
        return IMPL.isSafari();
    }
    @SGWTInternal
    public static AndroidWindowSoftInputMode _getAndroidWindowSoftInputMode() {
        return IMPL.getAndroidWindowSoftInputMode();
    }
    @SGWTInternal
    public static boolean _getHideTabBarDuringKeyboardFocus() {
        return IMPL.getHideTabBarDuringKeyboardFocus();
    }
    @SGWTInternal
    public static boolean _getFixNavigationBarPositionDuringKeyboardFocus() {
        return IMPL.getFixNavigationBarPositionDuringKeyboardFocus();
    }
    public static boolean isUIWebView() {
        return IMPL.isUIWebView();
    }
    @SGWTInternal
    public static boolean _isIOS4OrOlder() {
        return (Canvas.isIPad() || Canvas.isIPhone()) && !Canvas._isIOS5() && !Canvas._isIOSMin6_0();
    }
    @SGWTInternal
    public static boolean _isIOS5() {
        return IMPL.isIOS5();
    }
    @SGWTInternal
    public static boolean _isIOSMin6_0() {
        return IMPL.isIOSMin6_0();
    }
    @SGWTInternal
    public static boolean _isStandaloneMode() {
        return IMPL.isStandaloneMode();
    }
    public static boolean useIOSNativeScrolling() {
        return IMPL.useIOSNativeScrolling();
    }

    public Canvas() {}

    public Canvas(Element element) {
        this();
        setElement(element);
    }

    @Override
    public Object _getAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration) {
        @SuppressWarnings("unused")
        final AttributeType attribute = configuration.getAttribute();

        if (!locatorArray.isEmpty()) {
            final AutoTestLocatable child = _getChildFromLocatorSubstring(locatorArray.get(0), 0, locatorArray, configuration);
            if (child != null) {
                locatorArray.remove(0);
                return child._getAttributeFromSplitLocator(locatorArray, configuration);
            }
        }

        return _getInnerAttributeFromSplitLocator(locatorArray, configuration);
    }

    @Override
    public AutoTestLocatable _getChildFromLocatorSubstring(String substring, int index, List<String> locatorArray, GetAttributeConfiguration configuration) {
        return null;
    }

    @Override
    protected void setElement(com.google.gwt.user.client.Element elem) {
        super.setElement(elem);
        assert elem == null || elem.getId() == null || elem.getId().isEmpty();
    }

    @Override
    public Object _getInnerAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration) {
        if (locatorArray.isEmpty()) {
            switch (configuration.getAttribute()) {
                case ELEMENT:
                    return getElement();
                case IS_CLICKABLE:
                    return Boolean.valueOf(isAttached() && isEnabled() && isVisible());
                case COMPONENT:
                    return this;
                default:
                    break;
            }
        }
        return null;
    }

    @SGWTInternal
    public void _sinkAnimationEndEvent() {
        sinkBitlessEvent(getElement(), DOMConstants.INSTANCE.getAnimationEndEventType());
    }

    @SGWTInternal
    public void _sinkContentChangedEvent() {
        sinkBitlessEvent(getElement(), _CONTENT_CHANGED_EVENT_TYPE);
    }

    @SGWTInternal
    public void _sinkFocusInEvent() {
        sinkBitlessEvent(getElement(), "focusin");
    }

    @SGWTInternal
    public void _sinkFocusOutEvent() {
        sinkBitlessEvent(getElement(), "focusout");
    }

    @SGWTInternal
    public void _sinkInputEvent() {
        sinkBitlessEvent(getElement(), "input");
    }

    @SGWTInternal
    public void _sinkRequestScrollToEvent() {
        sinkBitlessEvent(getElement(), _REQUEST_SCROLL_TO_EVENT_TYPE);
    }

    @SGWTInternal
    public void _sinkTransitionEndEvent() {
        sinkBitlessEvent(getElement(), DOMConstants.INSTANCE.getTransitionEndEventType());
    }

    @Override
    public void onBrowserEvent(Event event) {
        switch (event.getTypeInt()) {
            case Event.ONCLICK:
                if (disabled) return;

                // A click event might have fired on a SubmitItem as a result of the user tapping
                // the return key ("Go"/"Search" button) on the soft keyboard. If this is the case,
                // then do not fire a SmartGWT.mobile ClickEvent.
                if (!(this instanceof SubmitItem) || EventHandler.isFastClickEvent(event)) {
                    final boolean cancelled = ClickEvent._fire(this, event.getClientX(), event.getClientY());
                    if (cancelled) event.stopPropagation();
                }
                break;

            case Event.ONCONTEXTMENU:
                if (disabled) return;

                // If this is a ListGrid, then exit early because ListGrid implementations
                // have more specialized context menu support that incorporates information
                // about which row and possibly which cell was clicked.
                if (this instanceof ListGrid) return;

                if (contextMenu != null) {
                    final boolean showContextMenuCancelled = ShowContextMenuEvent._fire(this);
                    if (!showContextMenuCancelled) {
                        final int x = event.getClientX(),
                                y = event.getClientY();
                        contextMenu._showAt(this, x, y, x, x, y, y);
                    }
                }
                break;
        }
    }

    @SGWTInternal
    public com.google.gwt.user.client.Element _getInnerElement() {
        return getElement();
    }

    @SGWTInternal
    public final void _add(Widget child, Element container) {
        add(child, container.<com.google.gwt.user.client.Element>cast());
    }

    public void addChild(Canvas canvas) {
        removeChild(canvas);
        add(canvas, _getInnerElement());
    }
    
    public void addChild(int index, Canvas canvas) {
        removeChild(canvas);
        insert(canvas, getElement(), index, true);
    }

    public void addChild(Widget widget) {
        removeChild(widget);
        add(new WidgetCanvas(widget), _getInnerElement());
    }

    public final boolean hasChild(Canvas canvas) {
        return getChildren().contains(canvas);
    }

    public final boolean hasChild(Widget widget) {
        WidgetCollection children = getChildren();
        Iterator<Widget> it = children.iterator();
        while (it.hasNext()) {
            Widget child = it.next();
            if (child instanceof WidgetCanvas) {
                if (((WidgetCanvas) child)._getWidget().equals(widget)) {
                    return true;
                }
            }
        }
        return false;
    }

    @SGWTInternal
    public final void _insert(Widget child, Element container, int beforeIndex, boolean domInsert) {
        insert(child, container.<com.google.gwt.user.client.Element>cast(), beforeIndex, domInsert);
    }

    public void removeChild(Canvas canvas) {
        remove(canvas);
    }

    public void removeChild(Widget widget) {
        WidgetCollection children = getChildren();
        Iterator<Widget> it = children.iterator();
        while (it.hasNext()) {
            Widget child = it.next();
            if (child instanceof WidgetCanvas) {
                if (((WidgetCanvas) child)._getWidget().equals(widget)) {
                    it.remove();
                    return;
                }
            }
        }
    }

    public void setStyleName(String styleName) {
        final Element elem = getElement();
        if (this.styleName != null && !this.styleName.isEmpty()) elem.removeClassName(this.styleName);
        this.styleName = styleName;
        if (styleName != null && !styleName.isEmpty()) elem.addClassName(styleName);
    }

    @SGWTInternal
    public void _setClassName(String className, boolean replace) {
        if (replace) {
            setStyleName(getElement(), className);
        } else {
            setStyleName(getElement(), className, true);
        }
    }

    @SGWTInternal
    public void _removeClassName(String className) {
        getElement().removeClassName(className);
    }

    public final String getID() {
        return id;
    }

    public native void setID(String id) /*-{
        this.@com.smartgwt.mobile.client.widgets.Canvas::id = id;
        var elem = this.@com.google.gwt.user.client.ui.UIObject::element;
        if (elem != null) elem.id = id;
        $wnd[id] = this;
    }-*/;

    public String getInnerHTML() {
        return _getInnerElement().getInnerHTML();
    }

    public void setContents(String html) {
        _getInnerElement().setInnerHTML(html);
    }
    

    public final Boolean isDisabled() {
        return disabled;
    }

    public final boolean isEnabled() {
        return disabled == false;
    }

    public void setDisabled(boolean newState) {
        // We can no-op if we're already explicitly set to the appropriate state.
        if (this.disabled == newState) return;

        final boolean wasDisabled = Canvas._booleanValue(isDisabled(), false);
        this.disabled = newState;
        final boolean isDisabled = Canvas._booleanValue(isDisabled(), false);

        if (wasDisabled != isDisabled) {
            _setHandleDisabled(isDisabled);
        }
    }

    @SGWTInternal
    public void _setHandleDisabled(boolean disabled) {
        final Element elem = getElement();
        if (disabled) {
            elem.addClassName(_CSS.disabledClass());
        } else {
            elem.removeClassName(_CSS.disabledClass());
        }
        elem.setPropertyBoolean("disabled", disabled);
    }

    public void destroy() {
		try {
			Widget parent = getParent();
			if(parent instanceof Layout) {
				((Layout)parent).removeMember(this);
			}
            removeFromParent(); // Automatically calls onDetach().
		} catch(Exception e) {
			e.printStackTrace();
		}
        clearGlobal(id);
	}

    public final void enable() {
        setDisabled(false);
    }

    public final void disable() {
        setDisabled(true);
    }

    public void setMargin(int margin) {
    	getElement().getStyle().setMargin(margin, Style.Unit.PX);
    }

    public int getMargin() {
    	return new Integer(getElement().getStyle().getMargin());
    }

    public final Menu getContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(Menu contextMenu) {
        if (this.contextMenu != contextMenu) {
            if (this.contextMenu != null) this.contextMenu._hide();
            this.contextMenu = contextMenu;
            sinkEvents(Event.ONCONTEXTMENU);
        }
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        sinkEvents(Event.ONCLICK);
        return addHandler(handler, ClickEvent.getType());
    }

    @Override
    public HandlerRegistration addShowContextMenuHandler(ShowContextMenuHandler handler) {
        return addHandler(handler, ShowContextMenuEvent.getType());
    }

    @SGWTInternal
    public final void _fireContentChangedEvent() {
        _fireContentChangedEvent(getElement());
    }

    @SGWTInternal
    public static native void _fireContentChangedEvent(Element targetElement) /*-{
        if (targetElement != null) {
            // This used to be createEvent("CustomEvent"), but some Android 2.2 and 2.3 devices
            // do not support the `CustomEvent' interface. The issue seems to be that the JSC
            // JavaScript engine does not support `CustomEvent' whereas the v8 engine does.
            var event = $doc.createEvent("UIEvents");
            event.initUIEvent(@com.smartgwt.mobile.client.widgets.Canvas::_CONTENT_CHANGED_EVENT_TYPE, true, true, $wnd, 0);
            targetElement.dispatchEvent(event);
        }
    }-*/;

    @SGWTInternal
    public static final native void _fireRequestScrollToEvent(Element targetElement) /*-{
        if (targetElement != null) {
            var event = $doc.createEvent("UIEvents");
            event.initUIEvent(@com.smartgwt.mobile.client.widgets.Canvas::_REQUEST_SCROLL_TO_EVENT_TYPE, true, true, $wnd, 0);
            targetElement.dispatchEvent(event);
        }
    }-*/;
}
