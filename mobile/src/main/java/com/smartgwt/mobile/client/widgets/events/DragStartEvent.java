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

public class DragStartEvent extends GwtEvent<DragStartHandler> implements Cancellable {
    private boolean cancel = false;

    /**
     * Handler type.
     */
    private static GwtEvent.Type<DragStartHandler> TYPE;

    /**
     * Fires a open event on all registered handlers in the handler manager.If no
     * such handlers exist, this method will do nothing.
     *
     * @param <S>    The event source
     * @param source the source of the handlers
     */
    public static <S extends HasDragStartHandlers & HasHandlers> void fire(
            S source) {
        if (TYPE != null) {
            DragStartEvent event = new DragStartEvent();
            source.fireEvent(event);
        }
    }

    /**
     * Gets the type associated with this event.
     *
     * @return returns the handler type
     */
    public static GwtEvent.Type<DragStartHandler> getType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<DragStartHandler>();
        }
        return TYPE;
    }


    @Override
    protected void dispatch(DragStartHandler handler) {
        handler.onDragStart(this);
    }

    // Because of type erasure, our static type is
    // wild carded, yet the "real" type should use our I param.

    @Override
    public final GwtEvent.Type<DragStartHandler> getAssociatedType() {
        return TYPE;
    }


    /**
     * false to cancel drag action.
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


}
