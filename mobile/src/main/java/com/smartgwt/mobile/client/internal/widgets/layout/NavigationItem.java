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

package com.smartgwt.mobile.client.internal.widgets.layout;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.Header1;
import com.smartgwt.mobile.client.widgets.NavigationButton;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.layout.NavigationBar;

/**
 * This class encapsulates the various buttons and title information to be displayed for a given {@link NavigationBar}.
 */
@SGWTInternal
public class NavigationItem {

    private HandlerRegistration backClickRegistration;
    private NavigationButton backButton, leftButton;
    private Header1 titleView;
    private Canvas rightBarItem;

    public void destroy() {
        rightBarItem = null;
        if (backClickRegistration != null) {
            backClickRegistration.removeHandler();
            backClickRegistration = null;
        }
        if (backButton != null) {
            backButton.destroy();
            backButton = null;
        }
        leftButton = null;
    }

    public NavigationButton getLeftButton() {
        return leftButton;
    }

    public NavigationButton getBackButton() {
        return backButton;
    }

    public void setLeftButton(NavigationButton leftButton) {
        this.leftButton = leftButton;
        this.backButton = null;
        if (leftButton != null) {
            leftButton._setClassName("sc-toolbar-left", false);
        }
    }

    public void setBackButton(NavigationButton backButton, final NavStack navStack) {
        setLeftButton(backButton);
        if (backClickRegistration != null) {
            backClickRegistration.removeHandler();
        }
        if (backButton == null) {
            backClickRegistration = null;
        } else {
            this.backButton = backButton;
            backClickRegistration = backButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (!navStack._isAnimating()) navStack.pop();
                }
            });
        }
    }

    public final Header1 getTitleView() {
        return titleView;
    }

    public void setTitleView(Header1 titleView) {
        this.titleView = titleView;
        if (titleView != null) {
            titleView._setClassName("sc-toolbar-center", false);
        }
    }

    public final Canvas getRightBarItem() {
        return rightBarItem;
    }

    public void setRightBarItem(Canvas rightBarItem) {
        assert rightBarItem == null || (rightBarItem instanceof NavigationBarItem);
        this.rightBarItem = rightBarItem;
        if (rightBarItem != null) {
            rightBarItem._setClassName("sc-toolbar-right", false);
        }
    }
}
