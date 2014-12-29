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

public class DropEvent extends GwtEvent<DropHandler> implements Cancellable {
    private boolean cancel = false;
    private Object data = null;

    /**
     * Handler type.
     */
    private static GwtEvent.Type<DropHandler> TYPE;

    /**
     * Fires a open event on all registered handlers in the handler manager.If no
     * such handlers exist, this method will do nothing.
     *
     * @param <S>    The event source
     * @param source the source of the handlers
     */
    public static <S extends HasDropHandlers & HasHandlers> void fire(
            S source) {
        if (TYPE != null) {
            DropEvent event = new DropEvent();
            source.fireEvent(event);
        }
    }

    /**
     * Gets the type associated with this event.
     *
     * @return returns the handler type
     */
    public static GwtEvent.Type<DropHandler> getType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<DropHandler>();
        }
        return TYPE;
    }
    
    public DropEvent() {
    }
    
    public DropEvent(Object data) {
        this.data = data;
    }


    @Override
    protected void dispatch(DropHandler handler) {
        handler.onDrop(this);
    }

    // Because of type erasure, our static type is
    // wild carded, yet the "real" type should use our I param.

    @Override
    public final GwtEvent.Type<DropHandler> getAssociatedType() {
        return TYPE;
    }


    /**
     * return false to cancel default drop handling
     */
    public void cancel() {
        cancel = true;
    }

    public Object getData() {
        return data;
    }

    /**
     * @return true if cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }


}
