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
import com.smartgwt.mobile.client.data.Record;

public class ImageClickEvent extends GwtEvent<ImageClickHandler> {

    public ImageClickEvent(Record record) {
        this.setRecord(record);
    }

    private static final Type<ImageClickHandler> TYPE = new Type<ImageClickHandler>();

    public static Type<ImageClickHandler> getType() {
        return TYPE;
    }

    private Record record;

    public Record getRecord() {
        return this.record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }


    @Override
    protected void dispatch(ImageClickHandler handler) {
        handler.onImageClick(this);
    }

    @Override
    public Type<ImageClickHandler> getAssociatedType() {
        return TYPE;
    }

}
