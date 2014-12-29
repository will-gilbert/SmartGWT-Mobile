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

/**
 *
 */
package com.smartgwt.mobile.client.widgets.tab;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.widgets.tab.TabSetItem;
import com.smartgwt.mobile.client.widgets.Canvas;

public class Tab {

    private TabSetItem tabSetItem;

    private TabSet tabSet;
    private Canvas pane;

    private String id;
    private boolean disabled;
    private String badge;
    private Image icon;
    private Boolean iconPrerendered;
    private String title;

    public Tab() {}

    public Tab(String title) {
        this.title = title;
    }

    public Tab(String title, String icon) {
        this(title, icon == null ? null : new Image(icon));
    }

    public Tab(String title, ImageResource iconResource) {
        this(title, iconResource == null ? null : new Image(iconResource));
    }

    public Tab(String title, Image icon) {
        this(title);
        this.icon = icon;
    }

    @SGWTInternal
    public TabSetItem _ensureTabSetItem(TabSet tabSet) {
        if (tabSet == null) throw new NullPointerException("`tabSet' cannot be null.");
        if (tabSetItem == null) {
            tabSetItem = new TabSetItem(this);
            tabSetItem.setBadge(badge);
            tabSetItem.setIcon(icon, iconPrerendered != null && iconPrerendered.booleanValue());
            tabSetItem.setTitle(title);
            tabSetItem.setDisabled(disabled);
            tabSetItem.setTabSet(tabSet);
            this.tabSet = tabSet;
        } else {
            _setTabSet(tabSet);
        }
        assert tabSetItem != null;
        return tabSetItem;
    }

    @SGWTInternal
    public TabSetItem _getTabSetItem() {
        assert tabSetItem != null;
        return tabSetItem;
    }

    @SGWTInternal
    public void _destroyTabSetItem() {
        if (tabSetItem != null) {
            tabSetItem.destroy();
            tabSetItem = null;
        }
    }

    public final TabSet getTabSet() {
        return tabSet;
    }

    @SGWTInternal
    public void _setTabSet(TabSet tabSet) {
        if (this.tabSet != tabSet) {
            this.tabSet = tabSet;
            if (tabSetItem != null) tabSetItem.setTabSet(tabSet);
        }
    }

    @SGWTInternal
    public void _setPane(Canvas pane) {
        this.pane = pane;
    }

    public final Canvas getPane() {
        return pane;
    }

    public void setPane(Canvas pane) {
        if (this.pane != pane) {
            if (tabSet != null) tabSet.updateTab(this, pane);
            else _setPane(pane);
        }
    }

    public final String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public final boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        setDisabled(disabled == null ? false : disabled.booleanValue());
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        if (tabSetItem != null) tabSetItem.setDisabled(disabled);
    }

    public final String getBadge() {
        return badge;
    }

    public void setBadge(String value) {
        this.badge = value;
        if (tabSetItem != null) tabSetItem.setBadge(value);
    }

    public void clearBadge() {
        setBadge(null);
    }

    public void removeBadge() {
        setBadge(null);
    }

    public final Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
        if (tabSetItem != null) tabSetItem.setIcon(icon, iconPrerendered != null && iconPrerendered.booleanValue());
    }

    /**
     * Whether the tab icon is prerendered.
     * 
     * <p>SmartGWT.mobile makes use of <a href="https://developer.mozilla.org/en-US/docs/CSS/-webkit-mask-image">WebKit image masking</a>
     * to provide a basic tab icon masking effect; however, it is currently not possible in HTML and CSS only to
     * replicate the sophisticated image processing operations that mobile operating systems
     * apply when rendering tab icons. For a better user experience, it may be preferable to prerender
     * the icon for a platform and use the prerendered icon instead.
     * 
     * <p>Setting the iconPrerendered attribute to {@link java.lang.Boolean#TRUE} makes SmartGWT.mobile
     * assume that the icon is a prerendered sprite image. SmartGWT.mobile will not apply any effect to the image,
     * but will instead resize the image to 32px x 64px. The top 32px x 32px area is used for
     * the nonselected icon and the bottom 32px x 32px area is used for the selected icon.
     * 
     * @return whether the icon is prerendered. Default value: <code>null</code>
     */
    public final Boolean getIconPrerendered() {
        return iconPrerendered;
    }

    public void setIconPrerendered(Boolean iconPrerendered) {
        // Because `null' and `FALSE' are considered equivalent, the truth table for
        // "did the property effectively change" is:
        //
        //                               +---------------------+
        //                               |   iconPrerendered   |
        //                               | null | TRUE | FALSE |
        // +-----------------------------+------+------+-------+
        // |                        null |  F   |  T   |   F   |
        // |                      -------+------+------+-------+
        // | this.iconPrerendered   TRUE |  T   |  F   |   T   |
        // |                      -------+------+------+-------+
        // |                       FALSE |  F   |  T   |   F   |
        // +-----------------------------+------+------+-------+
        if (tabSetItem != null && 
            ((this.iconPrerendered == null && iconPrerendered != null && iconPrerendered.booleanValue()) ||
             (this.iconPrerendered != null && ((iconPrerendered == null && this.iconPrerendered.booleanValue()) ||
                                               (iconPrerendered != null &&
                                                this.iconPrerendered.booleanValue() != iconPrerendered.booleanValue())))))
        {
            tabSetItem.setIcon(icon, iconPrerendered != null && iconPrerendered.booleanValue());
        }
        this.iconPrerendered = iconPrerendered;
    }

    public final String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (tabSetItem != null) tabSetItem.setTitle(title);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tab)) return false;
        final Tab other = (Tab)obj;
        if (id != null) {
            return id.equals(other.id);
        } else {
            return (other.id == null && this == other);
        }
    }
}
