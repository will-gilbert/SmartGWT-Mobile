/*
 * Smart GWT (GWT for SmartClient)
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * Smart GWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  Smart GWT is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package com.smartgwt.mobile.client.widgets.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasIconClickHandlers extends HasHandlers {
    /**
     * If this button is showing an {@link com.smartgwt.mobile.client.widgets.Button#getIcon icon}, a separate click handler for the
     * icon may be defined as <code>this.iconClick</code>. Returning false will suppress the standard button click handling
     * code.
     *
     * @param handler the iconClick handler
     * @return {@link com.google.gwt.event.shared.HandlerRegistration} used to remove this handler
     */
    HandlerRegistration addIconClickHandler(IconClickHandler handler);
}
