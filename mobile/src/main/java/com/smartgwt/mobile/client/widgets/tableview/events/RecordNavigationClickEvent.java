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

package com.smartgwt.mobile.client.widgets.tableview.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.widgets.tableview.TableView;

public class RecordNavigationClickEvent extends GwtEvent<RecordNavigationClickHandler> {

    private static Type<RecordNavigationClickHandler> TYPE = null;

    public static Type<RecordNavigationClickHandler> getType() {
        if (TYPE == null) TYPE = new Type<RecordNavigationClickHandler>();
        return TYPE;
    }

    @SGWTInternal
    public static void _fire(TableView source, Record record) {
        assert source != null : "`source' cannot be null.";
        if (TYPE != null) {
            final RecordNavigationClickEvent event = new RecordNavigationClickEvent(record);
            source.fireEvent(event);
        }
    }

    private Record record;

    private RecordNavigationClickEvent(Record record) {
        this.record = record;
    }

    public final TableView getTableView() {
        final Object source = getSource();
        assert source != null;
        assert source instanceof TableView;
        return (TableView)source;
    }

    public final Record getRecord() {
        return record;
    }

    @Override
    public final Type<RecordNavigationClickHandler> getAssociatedType() {
        assert TYPE != null;
        return TYPE;
    }

    @Override
    protected void dispatch(RecordNavigationClickHandler handler) {
        handler.onRecordNavigationClick(this);
    }
}
