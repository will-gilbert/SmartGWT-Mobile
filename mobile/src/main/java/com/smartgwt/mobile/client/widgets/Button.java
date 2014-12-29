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

/**
 * A mobile Button class representation. In addition to being able to set an icon and title,
 * a Button have different appearances based on its {@link com.smartgwt.mobile.client.widgets.BaseButton.ButtonType}.
 *
 */
public class Button extends BaseButton {

    /**
     * The button type. Defaults to ACTION_DEFAULT.
     */
    private ButtonType buttonType = ButtonType.ACTION_DEFAULT;


    /**
     * The default constructor.
     */
    public Button() {
        internalSetButtonType(buttonType);
    }

    /**
     * Button constructor that sets the title label.
     *
     * @param title the title label
     */
    public Button(String title) {
        this();
        setTitle(title);

    }

    /**
     * Button constructor that sets the title label and button type.
     *
     * @param title the title label
     * @param buttonType the button type
     */
    public Button(String title, ButtonType buttonType) {
        super(title);
        setButtonType(buttonType);
    }

    public Button(Button button) {
        super(button);
        setButtonType(button.buttonType);
    }

    /**
     * Return the button type.
     *
     * @return the button type
     */
    public ButtonType getButtonType() {
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

    /**
     * Set the button type.
     *
     * @param buttonType the button type
     */
    public void setButtonType(ButtonType buttonType) {
        internalSetButtonType(buttonType);
    }
}
