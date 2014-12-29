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

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.events.AbstractCancellableEvent;

public class ClickEvent extends AbstractCancellableEvent<ClickHandler> {

    private static Type<ClickHandler> TYPE = null;

    public static Type<ClickHandler> getType() {
        if (TYPE == null) TYPE = new Type<ClickHandler>();
        return TYPE;
    }

    // Returns whether the event was cancelled.
    @SGWTInternal
    public static <S extends HasClickHandlers> boolean _fire(S source, int x, int y) {
        if (TYPE == null) {
            return false;
        } else {
            final ClickEvent event = new ClickEvent(x, y);
            source.fireEvent(event);
            return event.isCancelled();
        }
    }

    private int x;
    private int y;

    private ClickEvent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    @Override
    public final Type<ClickHandler> getAssociatedType() {
        assert TYPE != null;
        return TYPE;
    }

    @Override
    protected void dispatch(ClickHandler handler) {
        handler.onClick(this);
    }
}
