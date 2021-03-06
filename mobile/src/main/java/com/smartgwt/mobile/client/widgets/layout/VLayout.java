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

package com.smartgwt.mobile.client.widgets.layout;

import com.google.gwt.dom.client.Style.Unit;
import com.smartgwt.mobile.client.types.LayoutPolicy;
import com.smartgwt.mobile.client.widgets.Canvas;

/**
 * A subclass of Layout that applies a sizing policy along the vertical axis, interpreting  percent and "*" sizes as
 * proportions of the height of the layout. VLayouts will set any  members that do not have explicit widths to match the
 * layout.
 *
 * @see com.smartgwt.mobile.client.widgets.layout.Layout#getVPolicy
 */
public class VLayout extends Layout {
	
	public VLayout() {
		this._setClassName("sc-layout-vbox", false);
		this.setVertical(true);
		this.setVPolicy(LayoutPolicy.FILL);
	}
	
    /**
     * Add a canvas to the layout, optionally at a specific position.
     *
     * @param component the canvas object to be added to the layout
     */
	@Override
    public void addMember(Canvas component) {
        if(component != null) {
            if(members.size() != 0) {
                component.getElement().getStyle().setMarginTop(membersMargin, Unit.PX);
            } 
            super.addMember(component);
        }
    }

    /**
     * Add a canvas to the layout, optionally at a specific position.
     *
     * @param component the canvas object to be added to the layout
     * @param position  the position in the layout to place newMember (starts with 0);
     *                  if omitted, it will be added at the last position
     */
	@Override
    public void addMember(Canvas component, int position) {
        if(component != null) {
            if(position != 0) {
                component.getElement().getStyle().setMarginTop(membersMargin, Unit.PX);
            } 
            super.addMember(component, position);        
        }
    }
}
