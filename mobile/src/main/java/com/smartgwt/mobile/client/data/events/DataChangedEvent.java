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
import com.smartgwt.mobile.SGWTInternal;

public class DataChangedEvent extends GwtEvent<DataChangedHandler> {

    private static Type<DataChangedHandler> TYPE = null;

    public static Type<DataChangedHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<DataChangedHandler>();
        }
        return TYPE;
    }

    // Method signature obtained from ListGrid.js
    @SGWTInternal
    public static <S extends HasDataChangedHandlers> void _fire(S source, String type, Object originalRecord, Object rowNum, Boolean updateData, Boolean filterChanged) {
        if (TYPE != null) {
            final DataChangedEvent event = new DataChangedEvent(type);
            source.fireEvent(event);
        }
    }

    private String type;

    private DataChangedEvent(String type) {
        this.type = type;
    }

    @SGWTInternal
    public String _getType() {
        return type;
    }

    @Override
    public Type<DataChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DataChangedHandler handler) {
        handler.onDataChanged(this);
    }
}
