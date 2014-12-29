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

public class ValueChangedEvent extends GwtEvent<ValueChangedHandler> {

    /**
     * Handler type.
     */
    private static GwtEvent.Type<ValueChangedHandler> TYPE;
    private Number value;

    public ValueChangedEvent(Number value) {
        this.value = value;
    }

    /**
     * Fires a open event on all registered handlers in the handler manager.If no
     * such handlers exist, this method will do nothing.
     *
     * @param <S>    The event source
     * @param source the source of the handlers
     */
    public static <S extends HasValueChangedHandlers & HasHandlers> void fire(
            S source, Number value) {
        if (TYPE != null) {
            ValueChangedEvent event = new ValueChangedEvent(value);
            source.fireEvent(event);
        }
    }

    /**
     * Gets the type associated with this event.
     *
     * @return returns the handler type
     */
    public static GwtEvent.Type<ValueChangedHandler> getType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<ValueChangedHandler>();
        }
        return TYPE;
    }


    @Override
    protected void dispatch(ValueChangedHandler handler) {
        handler.onValueChanged(this);
    }

    // Because of type erasure, our static type is
    // wild carded, yet the "real" type should use our I param.

    @Override
    public final GwtEvent.Type<ValueChangedHandler> getAssociatedType() {
        return TYPE;
    }


    /**
     * the new value
     *
     * @return the new value
     */
    public Number getValue() {
        return value;
    }


}
