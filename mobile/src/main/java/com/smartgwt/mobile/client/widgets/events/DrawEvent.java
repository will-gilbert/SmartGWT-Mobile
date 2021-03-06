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


public class DrawEvent extends GwtEvent<DrawHandler> {

    private String ID;

    public DrawEvent(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    /**
     * Handler type.
     */
    private static Type<DrawHandler> TYPE;


    /**
     * Fires a close event on all registered handlers in the handler manager.
     *
     * @param source     the source of the handlers
     * @param ID         the target ID
     */
    public static <S extends HasDrawHandlers<?> & HasHandlers> void fire(
            S source, String ID) {
        if (TYPE != null) {
            DrawEvent event = new DrawEvent(ID);
            source.fireEvent(event);
        }
    }

    /**
     * Gets the type associated with this event.
     *
     * @return returns the handler type
     */
    public static Type<DrawHandler> getType() {
        return TYPE != null ? TYPE : (TYPE = new Type<DrawHandler>());
    }

    @Override
    protected void dispatch(DrawHandler handler) {
        handler.onDraw(this);
    }

    // The instance knows its of type T, but the TYPE
    // field itself does not, so we have to do an unsafe cast here.
    @Override
    public final Type<DrawHandler> getAssociatedType() {
        return (Type<DrawHandler>) TYPE;
    }


}
