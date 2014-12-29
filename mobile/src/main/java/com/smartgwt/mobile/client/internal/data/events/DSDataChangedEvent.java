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

package com.smartgwt.mobile.client.internal.data.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.DSRequest;
import com.smartgwt.mobile.client.data.DSResponse;

@SGWTInternal
public class DSDataChangedEvent extends GwtEvent<DSDataChangedHandler> {

    private static Type<DSDataChangedHandler> TYPE = null;

    public static Type<DSDataChangedHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<DSDataChangedHandler>();
        }
        return TYPE;
    }

    public static <S extends HasDSDataChangedHandlers> void fire(S source, DSResponse dsResponse, DSRequest dsRequest) {
        if (TYPE != null) {
            final DSDataChangedEvent event = new DSDataChangedEvent(dsResponse, dsRequest);
            source.fireEvent(event);
        }
    }

    private DSResponse dsResponse;
    private DSRequest dsRequest;

    private DSDataChangedEvent(DSResponse dsResponse, DSRequest dsRequest) {
        this.dsResponse = dsResponse;
        this.dsRequest = dsRequest;
    }

    @Override
    public Type<DSDataChangedHandler> getAssociatedType() {
        return TYPE;
    }

    public final DSResponse getDSResponse() {
        return dsResponse;
    }

    public final DSRequest getDSRequest() {
        return dsRequest;
    }

    @Override
    protected void dispatch(DSDataChangedHandler handler) {
        handler._onDSDataChanged(this);
    }
}