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

package com.smartgwt.mobile.client.widgets.events;


import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class KeyPressEvent extends GwtEvent<KeyPressHandler> implements Cancellable {
    private boolean cancel = false;

    /**
     * Handler type.
     */
    private static GwtEvent.Type<KeyPressHandler> TYPE;
    private String keyName;

    public KeyPressEvent(String keyName) {
        this.keyName = keyName;
    }

    /**
     * Fires a open event on all registered handlers in the handler manager.If no
     * such handlers exist, this method will do nothing.
     *
     * @param <S>    The event source
     * @param source the source of the handlers
     */
    public static <S extends HasKeyPressHandlers & HasHandlers> void fire(
            S source, String keyName) {
        if (TYPE != null) {
            KeyPressEvent event = new KeyPressEvent(keyName);
            source.fireEvent(event);
        }
    }

    /**
     * Gets the type associated with this event.
     *
     * @return returns the handler type
     */
    public static GwtEvent.Type<KeyPressHandler> getType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<KeyPressHandler>();
        }
        return TYPE;
    }


    @Override
    protected void dispatch(KeyPressHandler handler) {
        handler.onKeyPress(this);
    }

    // Because of type erasure, our static type is
    // wild carded, yet the "real" type should use our I param.
    @Override
    public final GwtEvent.Type<KeyPressHandler> getAssociatedType() {
        return TYPE;
    }


    /**
     * false to suppress native behavior in response to the keyPress, and prevent                    this event from bubbling
     * to this widget's parent, or true or undefined to bubble.
     */
    public void cancel() {
        cancel = true;
    }

    /**
     * @return true if cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }


    /**
     * Return the name of the key for the event passed in. Note that this is only set reliably for keyboard events.
     * <pre>
     * Strings to identify the various keys on the keyboard.
     *
     * For alpha keys, the single (uppercase) character value is used, such as "Q"
     * For Numeric keys, the number is used as in a single character string, like "1"
     * Function keys are identified as "f1" - "f12"
     * Non alpha-numeric character keys (such as the key for "[" / "{") are identified by their unmodified character value (the value obtained by hitting the key without holding shift down), by default - exceptions are listed below.
     * Additional key names:
     * - Space
     * - Tab
     * - Enter
     * - Escape
     * - Backspace
     * - Insert
     * - Delete
     * - Arrow_Up
     * - Arrow_Down
     * - Arrow_Left
     * - Arrow_Right
     * - Home
     * - End
     * - Page_Up
     * - Page_Down
     * - Shift
     * - Ctrl
     * - Alt
     * [Note: Some keys may not be available for capture on every platform]
     * </pre>
     *
     * @return the key name
     */
    public String getKeyName() {
        return keyName;
        //return com.smartgwt.client.util.EventHandler.getKey();
    }


}
