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

public class CloseClickEvent extends GwtEvent<CloseClickHandler> {

    private static GwtEvent.Type<CloseClickHandler> TYPE;

    public static <S extends HasCloseClickHandlers> void fire(S source) {
        if (TYPE != null) {
            final CloseClickEvent event = new CloseClickEvent();
            source.fireEvent(event);
        }
    }

    public static GwtEvent.Type<CloseClickHandler> getType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<CloseClickHandler>();
        }
        return TYPE;
    }

    private CloseClickEvent() {
        /*empty*/
    }

    @Override
    protected void dispatch(CloseClickHandler handler) {
        handler.onCloseClick(this);
    }

    @Override
    public final GwtEvent.Type<CloseClickHandler> getAssociatedType() {
        return TYPE;
    }
}
