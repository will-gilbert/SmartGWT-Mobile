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

package com.smartgwt.mobile.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.MetaElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.EventListener;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.theme.ios.ActivityIndicatorCssResourceIOS;
import com.smartgwt.mobile.client.internal.theme.ios.AlertViewCssResourceIOS;
import com.smartgwt.mobile.client.internal.theme.ios.NavigationBarCssResourceIOS;
import com.smartgwt.mobile.client.internal.theme.ios.PickerCssResourceIOS;
import com.smartgwt.mobile.client.internal.theme.ios.TabSetCssResourceIOS;
import com.smartgwt.mobile.client.internal.theme.ios.WindowCssResourceIOS;
import com.smartgwt.mobile.client.internal.theme.ipad.WindowCssResourceIPad;
import com.smartgwt.mobile.client.internal.theme.iphone.MenuCssResourceIPhone;
import com.smartgwt.mobile.client.internal.theme.iphone.PopoverCssResourceIPhone;
import com.smartgwt.mobile.client.internal.theme.iphone.WindowCssResourceIPhone;
import com.smartgwt.mobile.client.internal.util.CSSUtil;
import com.smartgwt.mobile.client.internal.widgets.AlertView;
import com.smartgwt.mobile.client.internal.widgets.CanvasStaticImpl;
import com.smartgwt.mobile.client.internal.widgets.Picker2;
import com.smartgwt.mobile.client.internal.widgets.Popup;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.types.PageOrientation;
import com.smartgwt.mobile.client.util.Page;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.util.events.OrientationChangeEvent;
import com.smartgwt.mobile.client.util.events.OrientationChangeHandler;
import com.smartgwt.mobile.client.widgets.ActivityIndicator;
import com.smartgwt.mobile.client.widgets.BaseButton;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.Dialog;
import com.smartgwt.mobile.client.widgets.Header1;
import com.smartgwt.mobile.client.widgets.Popover;
import com.smartgwt.mobile.client.widgets.Progressbar;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.Window;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.SliderItem;
import com.smartgwt.mobile.client.widgets.form.fields.SwitchItem;
import com.smartgwt.mobile.client.widgets.layout.NavigationBar;
import com.smartgwt.mobile.client.widgets.menu.Menu;
import com.smartgwt.mobile.client.widgets.tab.TabSet;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;

/**
 * Internal SmartGWT Mobile entry point class where framework level initialization code executes
 * before the user's EntryPoint is run.
 */
public class SmartGwtMobileEntryPoint implements EntryPoint {

    private static final CanvasStaticImpl CANVAS_STATIC_IMPL = GWT.create(CanvasStaticImpl.class);
    private static boolean initialized = false;

    private static EventListener getEventListener(com.google.gwt.user.client.Element elem) {
        return DOM.getEventListener(elem);
    }

    @SGWTInternal
    public static native void _init() /*-{

        // In JSNI, we may be passed GWT Java Object references.
        // These typically can not be directly manipulated -- this check will test for such objects.
        $wnd.SmartGWTMobile = {
            isNativeJavaObject : function (object) {
                // From observation "typeof" reports "function" for native Java objects in Firefox in development mode
                // and in OmniWeb, in development mode
                // In all other browsers, typeof reports as "object"
                var type = typeof object;
                if (type != "function" && type != "object") return false;
                if (@com.smartgwt.mobile.client.util.JSOHelper::isJSO(Ljava/lang/Object;)(object)) return false;
                return true;
            }
        };

        // Define isc.DataSource.create to run the static DataSource.fromConfig() method.
        // This allows eval() to parse JavaScript sent from the server's data source loader.
        // E.g.:
        //     isc.DataSource.create({
        //         ID: "someName",
        //         inheritsFrom: isc.DataSource.create({
        //             ID: "someName_inheritsFrom",
        //             fields:[
        //                 {name:"whatever", type:"text"}
        //             ]
        //         })
        //     });
        //
        // In compiled mode, we can't eval() in the code frame because we would have to overwrite
        // `window.isc', which could be an obfuscated symbol. However, the strange syntax for
        // eval()ing in the host frame does not work in hosted mode, which is used when running
        // the GWT JUnit tests. So, in hosted mode, we eval() in the code frame, but in compiled
        // mode, we eval() in the host frame. We need to install an `isc' global in whichever
        // frame is used to eval() JavaScript source.
        var isHosted = !@com.google.gwt.core.client.GWT::isScript()();
        var evalWnd = (isHosted ? window : $wnd);
        evalWnd.isc = {
            DataSource: {
                create : $entry(@com.smartgwt.mobile.client.data.DataSource::fromConfig(Lcom/google/gwt/core/client/JavaScriptObject;))
            },
            Date: evalWnd.Date
        };

        $wnd.Date.parseServerDate = Date.parseServerDate = $entry(function (year, month, day) {
            var logicalDate = @com.smartgwt.mobile.client.util.LogicalDate::new(III)(year, month, day);
            return logicalDate.@com.smartgwt.mobile.client.util.LogicalDate::toString()();
        });
        $wnd.Date.parseServerTime = Date.parseServerTime = $entry(function (hour, minute, second) {
            var logicalTime = @com.smartgwt.mobile.client.util.LogicalTime::new(III)(hour, minute, second);
            return logicalTime.@com.smartgwt.mobile.client.util.LogicalTime::toString()();
        });

        // Install a hook for DOM.getEventListener() to allow the GWT widget hierarchy to be
        // inspected.
        $wnd.GWT = {
            getEventListener : $entry(@com.smartgwt.mobile.client.SmartGwtMobileEntryPoint::getEventListener(Lcom/google/gwt/user/client/Element;))
        };
    }-*/;

    private boolean assertionsEnabled = false;
    public void onModuleLoad() {
        // Added boolean init check flag because GWT for some reason invokes this entry point class twice in hosted mode
        // even though it appears only once in the load hierarchy. Check with GWT team.
        if (initialized) return;
        initialized = true;

        assert (assertionsEnabled = true) == true; // Intentional side effect.
        if (!assertionsEnabled) {
            SC.logWarn("WARNING: Assertions are not enabled. It is recommended to develop with assertions " +
                    "enabled because both GWT and SmartGWT.mobile use them to help find bugs in application code. " +
                    "To enable assertions, if using GWT 2.6.0 or later, recompile the application with the " +
                    "-checkAssertions option passed to the GWT compiler. If using GWT 2.5.1 or earlier, recompile with the " +
                    "-ea option passed to the GWT compiler.");
        }

        _init();

        // Delete all current viewport <meta> tags. We will create a new one to work around two
        // issues:
        // - In an iOS 7 UIWebView, we need to set the viewport height to fix the issue that the
        //   TabSet tabBar stays above the virtual keyboard when a text <input> has keyboard focus
        //   (http://forums.smartclient.com/showthread.php?t=29005).
        //   - On iPhone and iPad (iOS v7.0.4), we need to set the height to device-width to work
        //     around a bug. If height is set to device-height, then in landscape orientation,
        //     the <body>'s dimensions are 1024x1024, causing pickers to fail to appear.
        //     - iOS 6.1 also has this problem, but if we use height = device-width, then there
        //       are severe display issues when the orientation is changed, so we don't specify the
        //       height.
        //   - Don't want to do this unless in a UIWebView, however, because if we set the
        //     viewport height then pickers get cut off when Showcase is viewed in Mobile Safari.
        // - In an iOS 7 UIWebView, we also need a viewport height to fix a different issue, namely
        //   that if an input is tapped, but the virtual keyboard appears on top of the input, then
        //   that input element is not focused, and the ScrollablePanel fails to scroll the tapped
        //   input into view:
        //   http://stackoverflow.com/questions/19110144/ios7-issues-with-webview-focus-when-using-keyboard-html
        //   - This is not an issue on iOS 6.1.
        // - Firefox for Android does not support updating the content of an existing viewport
        //   <meta> tag. However, if a new viewport <meta> tag is added, then the viewport settings
        //   are updated: https://bugzilla.mozilla.org/show_bug.cgi?id=714737
        final NodeList<Element> metaElems = Document.get().getElementsByTagName("meta");
        for (int ri = metaElems.getLength(); ri > 0; --ri) {
            final MetaElement metaElem = metaElems.getItem(ri - 1).cast();
            if ("viewport".equals(metaElem.getName())) {
                metaElem.removeFromParent();
            }
        }
        String width = "device-width";
        String height = null;
        if (CANVAS_STATIC_IMPL.isIOSMin7_0() && CANVAS_STATIC_IMPL.isUIWebView()) {
            height = Page.getOrientation() == PageOrientation.LANDSCAPE ? "device-width" : "device-height";
            Page.addOrientationChangeHandler(new OrientationChangeHandler() {
                @Override
                public void onOrientationChange(OrientationChangeEvent event) {
                    final String newHeight = Page.getOrientation() == PageOrientation.LANDSCAPE ? "device-width" : "device-height";
                    Page._updateViewport(Float.valueOf(1.0f), "device-width", newHeight, Boolean.FALSE, "minimal-ui");
                }
            });
        }
        Page._updateViewport(Float.valueOf(1.0f), width, height, Boolean.FALSE, "minimal-ui");

        // Inject default styles.
        //
        // Because `CssResource' does not have support for at rules, CSS3 animations @keyframes
        // and @media rules must be added separately.
        // http://code.google.com/p/google-web-toolkit/issues/detail?id=4911
        final StringBuilder cssText = new StringBuilder();
        final String translateXStart = Canvas.isAndroid() ? "translateX(" : "translate3d(",
                translateXEnd = Canvas.isAndroid() ? ")" : ",0,0)",
                translateYStart = Canvas.isAndroid() ? "translateY(" : "translate3d(0,",
                translateYEnd = Canvas.isAndroid() ? ")" : ",0)";

        // base
        cssText.append(Canvas._CSS.getText());
        cssText.append(ThemeResources.INSTANCE.otherBaseCSS().getText());
        cssText.append("@media all and (orientation:landscape){" +
                ThemeResources.INSTANCE.baseCSSLandscape().getText() +
            "}");

        // activityindicator
        cssText.append(ActivityIndicator._CSS.getText());
        if (ActivityIndicator._CSS instanceof ActivityIndicatorCssResourceIOS) {
            final ActivityIndicatorCssResourceIOS CSS = (ActivityIndicatorCssResourceIOS)ActivityIndicator._CSS;
            // Android 2.3.3 Browser has a bug where attempting to animate from transform:rotate(0deg)
            // to transform:rotate(360deg) results in no animation because its angle calculations
            // are mod 360, so 0 = 360 mod 360 and it thinks that there is no change in the transform
            // from start to end. A work-around is to add a 50% keyframe of transform:rotate(180deg).
            cssText.append(DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.spinAnimationName() + "{" +
                    "0%{" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":rotate(0deg)}" +
                    "50%{" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":rotate(180deg)}" +
                    "to{" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":rotate(360deg)}" +
                "}" +
                "@keyframes " + CSS.spinAnimationName() + "{" +
                    "0%{transform:rotate(0deg)}" +
                    "50%{transform:rotate(180deg)}" +
                    "to{transform:rotate(360deg)}" +
                "}");
        }

        // layout
        cssText.append(ThemeResources.INSTANCE.layoutCSS().getText());

        // panel
        cssText.append(ThemeResources.INSTANCE.panelCSS().getText());

        // headings
        cssText.append(Header1._CSS.getText());

        // buttons
        cssText.append(BaseButton._CSS.getText());

        // form
        cssText.append(DynamicForm._CSS.getText());
        // Note: GWT's CssResource does not currently support CSS3 @media queries.
        // https://code.google.com/p/google-web-toolkit/issues/detail?id=8162
        // One work-around is to add the CSS text within the media query:
        // https://code.google.com/p/google-web-toolkit/issues/detail?id=4911#c6
        cssText.append("@media all and (orientation:landscape){" + // The space after the 'and' keyword is important. Without this space, the styles are not applied on iPad in landscape orientation.
                    ThemeResources.INSTANCE.formCSSLandscape().getText() +
                "}");

        // menu
        cssText.append(Menu._CSS.getText());
        if (Menu._CSS instanceof MenuCssResourceIPhone) {
            final MenuCssResourceIPhone CSS = (MenuCssResourceIPhone)Menu._CSS;
            cssText.append(DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.menuFadeInAnimationName() + "{" +
                        "0%{opacity:0}" +
                        "to{opacity:1}" +
                    "}" +
                    "@keyframes " + CSS.menuFadeInAnimationName() + "{" +
                        "0%{opacity:0}" +
                        "to{opacity:1}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.menuFadeOutAnimationName() + "{" +
                        "0%{opacity:1}" +
                        "50%{opacity:1}" +
                        "to{opacity:0}" +
                    "}" +
                    "@keyframes " + CSS.menuFadeOutAnimationName() + "{" +
                        "0%{opacity:1}" +
                        "50%{opacity:1}" +
                        "to{opacity:0}" +
                    "}");
        }

        // navigationbar
        cssText.append(NavigationBar._CSS.getText());
        if (NavigationBar._CSS instanceof NavigationBarCssResourceIOS) {
            final NavigationBarCssResourceIOS CSS = (NavigationBarCssResourceIOS)NavigationBar._CSS;
            cssText.append(DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.navbarFadeInAnimationName() + "{" +
                        "0%{opacity:0}" +
                        "to{opacity:1}" +
                    "}" +
                    "@keyframes " + CSS.navbarFadeInAnimationName() + "{" +
                        "0%{opacity:0}" +
                        "to{opacity:1}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.navbarFadeOutAnimationName() + "{" +
                        "0%{opacity:1}" +
                        "to{opacity:0}" +
                    "}" +
                    "@keyframes " + CSS.navbarFadeOutAnimationName() + "{" +
                        "0%{opacity:1}" +
                        "to{opacity:0}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.navbarSlideInFromRightAnimationName() + "{" +
                        "0%{opacity:0;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "90px" + translateXEnd + "}" +
                        "to{opacity:1;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "0" + translateXEnd + "}" +
                    "}" +
                    "@keyframes " + CSS.navbarSlideInFromRightAnimationName() + "{" +
                        "0%{opacity:0;transform:translateX(90px)}" +
                        "to{opacity:1;transform:translateX(0)}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.navbarHeadingSlideInFromRightAnimationName() + "{" +
                        "0%{opacity:0;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "60%" + translateXEnd + "}" +
                        "to{opacity:1;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "0" + translateXEnd + "}" +
                    "}" +
                    "@keyframes " + CSS.navbarHeadingSlideInFromRightAnimationName() + "{" +
                        "0%{opacity:0;transform:translateX(60%)}" +
                        "to{opacity:1;transform:translateX(0)}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.navbarSlideInFromLeftAnimationName() + "{" +
                        "0%{opacity:0;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "-90px" + translateXEnd + "}" +
                        "to{opacity:1;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "0" + translateXEnd + "}" +
                    "}" +
                    "@keyframes " + CSS.navbarSlideInFromLeftAnimationName() + "{" +
                        "0%{opacity:0;transform:translateX(-90px)}" +
                        "to{opacity:1;transform:translateX(0)}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.navbarHeadingSlideInFromLeftAnimationName() + "{" +
                        "0%{opacity:0;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "-60%" + translateXEnd + "}" +
                        "to{opacity:1;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "0" + translateXEnd + "}" +
                    "}" +
                    "@keyframes " + CSS.navbarHeadingSlideInFromLeftAnimationName() + "{" +
                        "0%{opacity:0;transform:translateX(-60%)}" +
                        "to{opacity:1;transform:translateX(0)}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.navbarSlideOutRightAnimationName() + "{" +
                        "0%{opacity:1;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "0" + translateXEnd + "}" +
                        "to{opacity:0;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "90px" + translateXEnd + "}" +
                    "}" +
                    "@keyframes " + CSS.navbarSlideOutRightAnimationName() + "{" +
                        "0%{opacity:1;transform:translateX(0)}" +
                        "to{opacity:0;transform:translateX(90px)}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.navbarHeadingSlideOutRightAnimationName() + "{" +
                        "0%{opacity:1;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "0" + translateXEnd + "}" +
                        "to{opacity:0;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "60%" + translateXEnd + "}" +
                    "}" +
                    "@keyframes " + CSS.navbarHeadingSlideOutRightAnimationName() + "{" +
                        "0%{opacity:1;transform:translateX(0)}" +
                        "to{opacity:0;transform:translateX(60%)}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.navbarSlideOutLeftAnimationName() + "{" +
                        "0%{opacity:1;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "0" + translateXEnd + "}" +
                        "to{opacity:0;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "-90px" + translateXEnd + "}" +
                    "}" +
                    "@keyframes " + CSS.navbarSlideOutLeftAnimationName() + "{" +
                        "0%{opacity:1;transform:translateX(0)}" +
                        "to{opacity:0;transform:translateX(-90px)}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.navbarHeadingSlideOutLeftAnimationName() + "{" +
                        "0%{opacity:1;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "0" + translateXEnd + "}" +
                        "to{opacity:0;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateXStart + "-60%" + translateXEnd + "}" +
                    "}" +
                    "@keyframes " + CSS.navbarHeadingSlideOutLeftAnimationName() + "{" +
                        "0%{opacity:1;transform:translateX(0)}" +
                        "to{opacity:0;transform:translateX(-60%)}" +
                    "}");
        }

        // popup
        cssText.append(Popup._CSS.getText());

        // picker2
        cssText.append(Picker2.CSS.getText());
        if (Picker2.CSS instanceof PickerCssResourceIOS) {
            final PickerCssResourceIOS CSS = (PickerCssResourceIOS)Picker2.CSS;
            cssText.append(DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.pickerFadeInAnimationName() + "{" +
                        "0%{" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateYStart + "100%" + translateYEnd + "}" +
                        "to{" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateYStart + "0" + translateYEnd + "}" +
                    "}" +
                    "@keyframes " + CSS.pickerFadeInAnimationName() + "{" +
                        "0%{transform:translateY(100%)}" +
                        "to{transform:translateY(0)}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.pickerFadeOutAnimationName() + "{" +
                        "0%{" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateYStart + "0" + translateYEnd + "}" +
                        "to{" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateYStart + "100%" + translateYEnd + "}" +
                    "}" +
                    "@keyframes " + CSS.pickerFadeOutAnimationName() + "{" +
                        "0%{transform:translateY(0)}" +
                        "to{transform:translateY(100%)}" +
                    "}");
        }

        // progressbar
        cssText.append(Progressbar._CSS.getText());

        // scrollable
        cssText.append(ScrollablePanel._CSS.getText());

        // slider
        cssText.append(SliderItem._CSS.getText());

        // switchitem
        cssText.append(SwitchItem._CSS.getText());

        // tableview
        // Don't use `TableView._CSS.ensureInjected()' because the way that CssResource.ensureInjected()
        // is implemented, the CSS rules are scheduled to be added in a timeout, which may mean
        // that the DOM elements for the first `TableView' are added to the document before
        // the `TableView' CSS styles are added. If this first `TableView' has a parent `NavStack',
        // then the browser considers the background color of the LIs to have changed when
        // the `TableView' CSS is added, meaning that the background-color transition effect
        // kicks in.
        // http://jsfiddle.net/A4QDd/
        //
        // GWT 2.5 introduces the StyleInjector.flush() function which would solve this problem,
        // but for GWT 2.4, we can simulate the same effect by always using StyleInjector.injectAtEnd()
        // (immediate = true) at the small price of more <style> elements being added to the document.
        cssText.append(TableView._CSS.getText());
        cssText.append("@media all and (orientation:landscape){" +
                    ThemeResources.INSTANCE.tableViewCSSLandscape().getText() +
                "}");

        // tabs
        cssText.append(TabSet._CSS.getText());
        if (TabSet._CSS instanceof TabSetCssResourceIOS) {
            final TabSetCssResourceIOS CSS = (TabSetCssResourceIOS)TabSet._CSS;
            cssText.append(DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.tabBarSlideOutAnimationName() + "{" +
                        "0%{" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateYStart + "0" + translateYEnd + "}" +
                        "to{" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateYStart + "100%" + translateYEnd + "}" +
                    "}" +
                    "@keyframes " + CSS.tabBarSlideOutAnimationName() + "{" +
                        "0%{transform:translateY(0)}" +
                        "to{transform:translateY(100%)}" +
                    "}");
        }
        cssText.append("@media all and (orientation:landscape){" +
                    ThemeResources.INSTANCE.tabsCSSLandscape().getText() +
                "}");

        // toolbar
        cssText.append(ToolStrip._CSS.getText());

        // window
        if (Window._CSS instanceof WindowCssResourceIOS) {
            final WindowCssResourceIOS CSS = (WindowCssResourceIOS)Window._CSS;
            cssText.append(DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.modalMaskFadeInAnimationName() + "{" +
                        "0%{opacity:0}" +
                        "to{opacity:1}" +
                    "}" +
                    "@keyframes " + CSS.modalMaskFadeInAnimationName() + "{" +
                        "0%{opacity:0}" +
                        "to{opacity:1}" +
                    "}" +
                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.modalMaskFadeOutAnimationName() + "{" +
                        "0%{opacity:1}" +
                        "to{opacity:0}" +
                    "}" +
                    "@keyframes " + CSS.modalMaskFadeOutAnimationName() + "{" +
                        "0%{opacity:1}" +
                        "to{opacity:0}" +
                    "}");
        }
        // On iPad, windows look like alertviews.
        if (Window._CSS instanceof WindowCssResourceIPad) {
            final WindowCssResourceIPad CSS = (WindowCssResourceIPad)Window._CSS;
            cssText.append(DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.windowBackgroundFadeInAnimationName() + "{" +
                        "0%{opacity:0;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":scale(1)}" +
                        "50%{opacity:1;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":scale(.95)}" +
                        "to{" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":scale(1)}" +
                    "}" +
                    "@keyframes " + CSS.windowBackgroundFadeInAnimationName() + "{" +
                        "0%{opacity:0;transform:scale(1)}" +
                        "50%{opacity:1;transform:scale(.95)}" +
                        "to{transform:scale(1)}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.windowBackgroundFadeOutAnimationName() + "{" +
                        "0%{opacity:1;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":scale(1)}" +
                        "to{opacity:0;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":scale(.95)}" +
                    "}" +
                    "@keyframes " + CSS.windowBackgroundFadeOutAnimationName() + "{" +
                        "0%{opacity:1;transform:scale(1)}" +
                        "to{opacity:0;transform:scale(.95)}" +
                    "}");

        // Otherwise, windows look like action sheets.
        } else {
            assert Window._CSS instanceof WindowCssResourceIPhone;
            final WindowCssResourceIPhone CSS = (WindowCssResourceIPhone)Window._CSS;
            cssText.append(DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.windowBackgroundSlideInAnimationName() + "{" +
                        "0%{" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateYStart + "100%" + translateYEnd + "}" +
                        "to{" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateYStart + "0" + translateYEnd + "}" +
                    "}" +
                    "@keyframes " + CSS.windowBackgroundSlideInAnimationName() + "{" +
                        "0%{transform:translateY(100%)}" +
                        "to{transform:translateY(0)}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.windowBackgroundSlideOutAnimationName() + "{" +
                        "0%{opacity:1;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateYStart + "0" + translateYEnd + "}" +
                        "to{opacity:1;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":" + translateYStart + "100%" + translateYEnd + "}" +
                    "}" +
                    "@keyframes " + CSS.windowBackgroundSlideOutAnimationName() + "{" +
                        "0%{opacity:1;transform:translateY(0)}" +
                        "to{opacity:1;transform:translateY(100%)}" +
                    "}");
        }
        cssText.append(Window._CSS.getText());
        cssText.append("@media all and (orientation:landscape){" +
                    ThemeResources.INSTANCE.windowCSSLandscape().getText() +
                "}");

        // dialog
        cssText.append(Dialog._CSS.getText());

        // alertview
        if (AlertView._CSS instanceof AlertViewCssResourceIOS) {
            final AlertViewCssResourceIOS CSS = (AlertViewCssResourceIOS)AlertView._CSS;
            cssText.append(DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.alertViewScreenCoverFadeInAnimationName() + "{" +
                        "0%{opacity:0}" +
                        "to{opacity:1}" +
                    "}" +
                    "@keyframes " + CSS.alertViewScreenCoverFadeInAnimationName() + "{" +
                        "0%{opacity:0}" +
                        "to{opacity:1}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.alertViewScreenCoverFadeOutAnimationName() + "{" +
                        "0%{opacity:1}" +
                        "to{opacity:0}" +
                    "}" +
                    "@keyframes " + CSS.alertViewScreenCoverFadeOutAnimationName() + "{" +
                        "0%{opacity:1}" +
                        "to{opacity:0}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.alertViewFadeInAnimationName() + "{" +
                        "0%{opacity:0;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":scale(1)}" +
                        "50%{opacity:1;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":scale(.95)}" +
                        "to{" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":scale(1)}" +
                    "}" +
                    "@keyframes " + CSS.alertViewFadeInAnimationName() + "{" +
                        "0%{opacity:0;transform:scale(1)}" +
                        "50%{opacity:1;transform:scale(.95)}" +
                        "to{transform:scale(1)}" +
                    "}" +

                    DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.alertViewFadeOutAnimationName() + "{" +
                        "0%{opacity:1;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":scale(1)}" +
                        "to{opacity:0;" + DOMConstants.INSTANCE.getTransformPropertyNameForCSSText() + ":scale(.95)}" +
                    "}" +
                    "@keyframes " + CSS.alertViewFadeOutAnimationName() + "{" +
                        "0%{opacity:1;transform:scale(1)}" +
                        "to{opacity:0;transform:scale(.95)}" +
                    "}");
        }
        cssText.append(AlertView._CSS.getText());

        // popover
        cssText.append(Popover._CSS.getText());
        if (Popover._CSS instanceof PopoverCssResourceIPhone) {
            final PopoverCssResourceIPhone CSS = (PopoverCssResourceIPhone)Popover._CSS;
            cssText.append(DOMConstants.INSTANCE.getAtKeyframesText() + " " + CSS.popoverFadeOutAnimationName() + "{" +
                        "0%{opacity:1}" +
                        "to{opacity:0}" +
                    "}" +
                    "@keyframes " + CSS.popoverFadeOutAnimationName() + "{" +
                        "0%{opacity:1}" +
                        "to{opacity:0}" +
                    "}");
        }

        StyleInjector.injectAtEnd(CSSUtil.fixNotSelectors(cssText.toString()), true);
    }
}