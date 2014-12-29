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

package com.smartgwt.mobile.client.widgets.toolbar;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.theme.ToolStripCssResource;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.layout.SegmentedControl;

/**
 * A toolbar contains various widgets and is used for representing a navigation bar as well
 * as a list of buttons or icons that perform actions related to the other controls in the view.
 * Uses css stylesheet toolbar.css
 */
public class ToolStrip extends HLayout {

    @SGWTInternal
    public static final ToolStripCssResource _CSS = ThemeResources.INSTANCE.toolbarCSS();

    /**
     * The tintColor of the toolbar. Defaults to null which displays the default toolbar color declared in the skin.
     */
    private String tintColor;

    /**
     * The toolbar type. Default's to DEFAULT. To display icon-only {@link com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton}'s,
     * set to PLAIN
     */
    private ToolbarType toolbarType = ToolbarType.DEFAULT;

    /**
     * The default Toolbar constructor.
     */
    public ToolStrip() {
        super.setStyleName(_CSS.toolStripClass());
    }

    /**
     * Return the toolbar type.
     *
     * @return the toolbar type. Defaults to ToolbarType.DEFAULT
     */
    public ToolbarType getToolbarType() {
        return toolbarType;
    }
    
    /**
     * Set the toolbar type. Defaults to {@link com.smartgwt.mobile.client.widgets.toolbar.ToolStrip.ToolbarType#DEFAULT}.
     * To display icon-only {@link com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton}'s, set the toolbar type to {@link com.smartgwt.mobile.client.widgets.toolbar.ToolStrip.ToolbarType#PLAIN_ICON}.
     *
     * @param toolbarType the toolbar type. Defaults to ToolbarType.DEFAULT
     */
    public void setToolbarType(ToolbarType toolbarType) {
        if (this.toolbarType != null && this.toolbarType != ToolbarType.DEFAULT) {
            getElement().removeClassName(this.toolbarType._getClassName());
        }
        this.toolbarType = toolbarType;
        if (toolbarType != null && toolbarType != ToolbarType.DEFAULT) {
            getElement().addClassName(toolbarType._getClassName());
        }
    }

    /**
     * Add a toolbar button.
     *
     * @param toolbarButton the toolbar button
     */
    public void addButton(ToolStripButton toolbarButton) {
        this.addMember(toolbarButton);
    }

    /**
     * Add a segmented control to the toolbar.
     *
     * @param segmentedControl the segmented control
     */
    public void addSegmentedControl(SegmentedControl segmentedControl) {
        this.addMember(segmentedControl);
    }

    /**
     * Return the tint color. Defaults to null (the skin defined style)
     *
     * @return the tint color
     */
    public String getTintColor() {
        return tintColor;
    }

    /**
     * Set the button tintColor. Can pass in the color name, the hext value, or the rgb / rgba value as a String.
     * <br><br>
     * <b>Note:</b> Passing a rgba string with a value for opacity allows buttons to have an opacity / translucency.
     *
     * @param tintColor the tint color
     */
    public void setTintColor(String tintColor) {
        if (this.tintColor != null) {
            getElement().removeClassName(_CSS.customTintedToolStripClass());
        }
        this.tintColor = tintColor;
        if (this.tintColor != null) {
            getElement().addClassName(_CSS.customTintedToolStripClass());
            getElement().getStyle().setBackgroundColor(tintColor);
        }
    }

    /**
     * Add a spacer to the toolbar. A spacer pushes the "next" toolbar item to the right.
     */
    public void addSpacer() {
        getElement().appendChild(new ToolStripSpacer().getElement());
    }


    /**
     * Toolbar type styles.
     */
    public static enum ToolbarType {
        /**
         * The default type allows toolbar buttons, segmented controls and other widgets to be
         * added to the toolbar.
         */
        DEFAULT,

        /**
         * This type of toolbar is used specifically for displaying icon toolbar buttons with a "plain" appearance
         * without any borders. Such plain buttons when clicked display a glow around the icon
         */
        PLAIN_ICON;

        private ToolbarType() {}

        @SGWTInternal
        public String _getClassName() {
            switch (this) {
                case DEFAULT:
                    return null;
                case PLAIN_ICON:
                    return _CSS.plainIconToolStripClass();
            }
            assert false : "ToolbarType._getClassName(): '" + this + "' unhandled.";
            throw new RuntimeException();
        }
    }
}
