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
import com.smartgwt.mobile.client.data.Criteria;
import com.smartgwt.mobile.client.data.DSRequest;

public class FetchDataEvent extends GwtEvent<FetchDataHandler> {

    private Criteria criteria;
    private DSRequest requestProperties;


    /**
     * Handler type.
     */
    private static GwtEvent.Type<FetchDataHandler> TYPE;


    public FetchDataEvent(Criteria criteria, DSRequest requestProperties) {
        this.criteria = criteria;
        this.requestProperties = requestProperties;
    }

    /**
     * Fires a open event on all registered handlers in the handler manager.If no
     * such handlers exist, this method will do nothing.
     *
     * @param <S>    The event source
     * @param source the source of the handlers
     */
    public static <S extends HasFetchDataHandlers & HasHandlers> void fire(
            S source, Criteria criteria, DSRequest requestProperties) {
        if (TYPE != null) {
            FetchDataEvent event = new FetchDataEvent(criteria, requestProperties);
            source.fireEvent(event);
        }
    }

    /**
     * Gets the type associated with this event.
     *
     * @return returns the handler type
     */
    public static GwtEvent.Type<FetchDataHandler> getType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<FetchDataHandler>();
        }
        return TYPE;
    }


    @Override
    protected void dispatch(FetchDataHandler handler) {
        handler.onFilterData(this);
    }

    // Because of type erasure, our static type is
    // wild carded, yet the "real" type should use our I param.

    @Override
    public final GwtEvent.Type<FetchDataHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * The criteria derived from the filter editor values.
     *
     * @return criteria derived from the filter editor values
     */
    public Criteria getCriteria() {
        return criteria;
    }

    /**
     * The criteria derived from the filter editor values.
     *
     * @return criteria derived from the filter editor values
     */
    public DSRequest getRequestProperties() {
        return requestProperties;
    }
}
