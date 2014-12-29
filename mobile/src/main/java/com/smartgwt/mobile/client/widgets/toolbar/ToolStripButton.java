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

import com.smartgwt.mobile.client.widgets.BaseButton;

/**
 * This class represents a button that is placed on the {@link ToolStrip}.
 * It can either be a back / forward button, or an action button.
 *
 * @see com.smartgwt.mobile.client.widgets.toolbar.ToolStrip#addButton(ToolStripButton) 
 */
public class ToolStripButton extends BaseButton {

    private ButtonType buttonType = ButtonType.BORDERED;
    private boolean inheritTint;

    public ToolStripButton() {
        internalSetButtonType(buttonType);
    }

    public ToolStripButton(String title) {
        this();
        setTitle(title);
    }

    public ToolStripButton(String title, ButtonType buttonType) {
        this(title);
        internalSetButtonType(buttonType);
    }

    public final ButtonType getButtonType() {
        return buttonType;
    }

    private void internalSetButtonType(ButtonType buttonType) {
        if (this.buttonType != null) {
            getElement().removeClassName(this.buttonType._getClassNames());
        }
        this.buttonType = buttonType;
        if (buttonType != null) {
            getElement().addClassName(buttonType._getClassNames());
        }
    }

    public void setButtonType(ButtonType buttonType) {
        internalSetButtonType(buttonType);
    }

    public final boolean isInheritTint() {
        return inheritTint;
    }

    public void setInheritTint(boolean inheritTint) {
        this.inheritTint = inheritTint;
        if(inheritTint) {
            getElement().addClassName(_CSS.customTintedButtonClass());
        } else {
            getElement().removeClassName(_CSS.customTintedButtonClass());
        }
    }
}

