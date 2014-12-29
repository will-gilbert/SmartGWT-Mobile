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

package com.smartgwt.mobile.client.data.events;

import com.google.gwt.event.shared.GwtEvent;

public class DataArrivedEvent extends GwtEvent<DataArrivedHandler> {

    private static Type<DataArrivedHandler> TYPE = null;

    public static Type<DataArrivedHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<DataArrivedHandler>();
        }
        return TYPE;
    }

    public static <S extends HasDataArrivedHandlers> void fire(S source, int startRow, int endRow) {
        if (TYPE != null) {
            final DataArrivedEvent event = new DataArrivedEvent(startRow, endRow);
            source.fireEvent(event);
        }
    }

    private int startRow, endRow;

    public DataArrivedEvent(int startRow, int endRow) {
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public final Type<DataArrivedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DataArrivedHandler handler) {
        handler.onDataArrived(this);
    }

    public final int getStartRow() {
        return startRow;
    }

    public int getEndRow() {
        return endRow;
    }
}