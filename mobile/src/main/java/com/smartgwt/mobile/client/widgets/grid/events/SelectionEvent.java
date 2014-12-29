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

package com.smartgwt.mobile.client.widgets.grid.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.Record;

public class SelectionEvent extends GwtEvent<SelectionChangedHandler> {

    private static Type<SelectionChangedHandler> TYPE = null;

    public static Type<SelectionChangedHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<SelectionChangedHandler>();
        }
        return TYPE;
    }

    public static <S extends HasSelectionChangedHandlers> void fire(S source, Record record, boolean state) {
        _fire(source, record, state, null);
    }

    @SGWTInternal
    public static <S extends HasSelectionChangedHandlers> void _fire(S source, Record record, boolean state, Boolean previousState) {
        if (TYPE != null) {
            final SelectionEvent event = new SelectionEvent(record, state, previousState);
            source.fireEvent(event);
        }
    }

    private Record record;
    private boolean state;
    private Boolean previousState;

    private SelectionEvent(Record record, boolean state, Boolean previousState) {
        this.record = record;
        this.state = state;
        this.previousState = previousState;
    }

    @Override
    public Type<SelectionChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SelectionChangedHandler handler) {
        handler.onSelectionChanged(this);
    }

    public Record getRecord() {
        return this.record;
    }

    public boolean getState() {
        return state;
    }

    @SGWTInternal
    public Boolean _getPreviousState() {
        return previousState;
    }
}
