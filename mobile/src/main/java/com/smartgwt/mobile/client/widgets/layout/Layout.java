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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.mobile.client.core.Function;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.LayoutPolicy;
import com.smartgwt.mobile.client.types.Overflow;
import com.smartgwt.mobile.client.types.VerticalAlignment;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.WidgetCanvas;
import com.smartgwt.mobile.client.widgets.events.DragMoveHandler;
import com.smartgwt.mobile.client.widgets.events.HasDragMoveHandlers;

/**
 * A subclass of Canvas that automatically arranges other Canvases according to a layout policy. <br><br> A Layout manages
 * a set of "member" Canvases initialized via the "members" property.  Layouts can have both "members", which are managed
 * by the Layout, and normal Canvas children, which are unmanaged. <br><br> Rather than using the Layout class directly,
 * use the HLayout, VLayout, HStack and VStack classes, which are subclasses of Layout preconfigured for horizontal or
 * vertical stacking, with the "fill" (VLayout) or "none" (VStack) {@link com.smartgwt.mobile.client.types.LayoutPolicy policies}
 * already set. <br><br> Layouts and Stacks may be nested to create arbitrarily complex layouts. Like other Canvas subclasses, Layout and
 * Stack components may have % width and height values. To create a dynamically-resizing layout that occupies the entire
 * page (or entire parent component), set width and height to "100%".
 *
 * @see com.smartgwt.mobile.client.types.LayoutPolicy
 * @see com.smartgwt.mobile.client.widgets.layout.VLayout
 * @see com.smartgwt.mobile.client.widgets.layout.HLayout
 * @see com.smartgwt.mobile.client.widgets.layout.VStack
 * @see com.smartgwt.mobile.client.widgets.layout.HStack
 * @see com.smartgwt.mobile.client.widgets.layout.LayoutSpacer
 */

//TODO INTERNAL NOTES
/*
1. VLayout and HLayout are the core layout classes that allows various layout by combining nested VLayout and HLayout instanced.
   See the samples http://www.smartclient.com/smartgwt/showcase/#layout_layout
    http://www.smartclient.com/smartgwt/showcase/#layout_stack and
2. For the initial implementation we need the main layout functionality working. Later on we'll add API's like setAnimateMembers(..) so that when
   this property is true and members are added / removed, they should be animated as they are shown or hidden in position. Also DnD API's will
   be added subsequently.
3. See the SmartClient Layout.js source file if need clarification on behavior / implementation.
4. We need to incorporate momentum scroll behaviour into Layout's (boolean momentumScroll property?) that
 utilizes iscroll. See http://cubiq.org/iscroll-4
*/

public class Layout extends Canvas implements HasDragMoveHandlers {

	protected Alignment alignment;
	protected Boolean enforcePolicy = true;
	protected LayoutPolicy hPolicy = LayoutPolicy.FILL;
	protected List<Canvas> members = new ArrayList<Canvas>();
	protected int membersMargin = 0, minMemberSize = 0;
	protected boolean momentumScroll = false;
	protected Overflow overflow = Overflow.VISIBLE;
	protected boolean paddingAsLayoutMargin = true;
	protected Boolean reverseOrder = false;
	protected VerticalAlignment valign;
	protected Boolean vertical;

    public Layout() {
        setElement(Document.get().createDivElement());
        sinkEvents(Event.ONCLICK | Event.GESTUREEVENTS | Event.MOUSEEVENTS | Event.TOUCHEVENTS | Event.FOCUSEVENTS | Event.KEYEVENTS);
    }

    /**
     * Whether the layout policy is continuously enforced as new members are added or removed and as members are resized. <p>
     * This setting implies that any member that resizes larger, or any added member, will take space from other members in
     * order to allow the overall layout to stay the same size.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param enforcePolicy enforcePolicy Default value is true
     */
    public void setEnforcePolicy(Boolean enforcePolicy) {
    	this.enforcePolicy = enforcePolicy;
    }

    /**
     * Whether the layout policy is continuously enforced as new members are added or removed and as members are resized. <p>
     * This setting implies that any member that resizes larger, or any added member, will take space from other members in
     * order to allow the overall layout to stay the same size.
     *
     * @return Boolean
     */
    public Boolean getEnforcePolicy() {
        return enforcePolicy;
    }

    /**
     * Sizing policy applied to members on horizontal axis
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param hPolicy hPolicy Default value is "fill"
     */
    public void setHPolicy(LayoutPolicy hPolicy) {
    	this.hPolicy = hPolicy;
    }

    /**
     * Sizing policy applied to members on horizontal axis
     *
     * @return LayoutPolicy
     */
    public LayoutPolicy getHPolicy() {
        return hPolicy;
    }

    /**
     * Space outside of all members, on the bottom side.  Defaults to {@link
     * com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutMargin layoutMargin}. <P> Requires a manual call to
     * <code>setLayoutMargin()</code> if changed on the fly.
     *
     * @param layoutBottomMargin layoutBottomMargin Default value is null
     */
    public void setLayoutBottomMargin(Integer layoutBottomMargin) {
        getElement().getStyle().setBottom(layoutBottomMargin, Unit.PX);
    }

    /**
     * Space outside of all members, on the bottom side.  Defaults to {@link
     * com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutMargin layoutMargin}. <P> Requires a manual call to
     * <code>setLayoutMargin()</code> if changed on the fly.
     *
     * @return Integer
     */
    public Integer getLayoutBottomMargin() {
    	return parseDimension(getElement().getStyle().getMarginBottom());
    }

    /**
     * Space outside of all members, on the left-hand side.  Defaults to {@link
     * com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutMargin layoutMargin}. <P> Requires a manual call to
     * <code>setLayoutMargin()</code> if changed on the fly.
     *
     * @param layoutLeftMargin layoutLeftMargin Default value is null
     */
    public void setLayoutLeftMargin(Integer layoutLeftMargin) {
    	getElement().getStyle().setMarginLeft(layoutLeftMargin.doubleValue(), Style.Unit.PX);
    }

    /**
     * Space outside of all members, on the left-hand side.  Defaults to {@link
     * com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutMargin layoutMargin}. <P> Requires a manual call to
     * <code>setLayoutMargin()</code> if changed on the fly.
     *
     * @return Integer
     */
    public Integer getLayoutLeftMargin() {
    	return parseDimension(getElement().getStyle().getMarginLeft());
    }

    /**
     * Space outside of all members. This attribute, along with {@link
     * com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutLeftMargin layoutLeftMargin}  and related properties does not have a
     * true setter method.<br> It may be assigned directly at runtime. After setting the property,  {@link
     * com.smartgwt.mobile.client.widgets.layout.Layout#setLayoutMargin Layout.setLayoutMargin} may be called with no arguments to
     * reflow the layout.
     * Method to force a reflow of the layout after directly assigning a value to any of the layout*Margin properties. Takes no arguments.
     *
     * @param layoutMargin layoutMargin Default value is null
     * @see com.smartgwt.mobile.client.widgets.layout.Layout#setLayoutLeftMargin
     * @see com.smartgwt.mobile.client.widgets.layout.Layout#setLayoutRightMargin
     * @see com.smartgwt.mobile.client.widgets.layout.Layout#setLayoutBottomMargin
     * @see com.smartgwt.mobile.client.widgets.layout.Layout#setLayoutTopMargin
     * @see com.smartgwt.mobile.client.widgets.layout.Layout#setPaddingAsLayoutMargin
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#layout_user_sizing" target="examples">User Sizing Example</a>
     */
    public void setLayoutMargin(Integer layoutMargin) {
    	if(paddingAsLayoutMargin) {
    		getElement().getStyle().setPadding(layoutMargin.doubleValue(), Style.Unit.PX);
    	} else {
    		getElement().getStyle().setMargin(layoutMargin.doubleValue(), Style.Unit.PX);	
    	}
    }

    /**
     * Space outside of all members. This attribute, along with {@link
     * com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutLeftMargin layoutLeftMargin}  and related properties does not have a
     * true setter method.<br> It may be assigned directly at runtime. After setting the property,  {@link
     * com.smartgwt.mobile.client.widgets.layout.Layout#setLayoutMargin Layout.setLayoutMargin} may be called with no arguments to
     * reflow the layout.
     *
     * @return Integer
     * @see com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutLeftMargin
     * @see com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutRightMargin
     * @see com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutBottomMargin
     * @see com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutTopMargin
     * @see com.smartgwt.mobile.client.widgets.layout.Layout#getPaddingAsLayoutMargin
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#layout_user_sizing" target="examples">User Sizing Example</a>
     */
    public Integer getLayoutMargin() {
    	return parseDimension(getElement().getStyle().getMargin());    }

    /**
     * Space outside of all members, on the right-hand side.  Defaults to {@link
     * com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutMargin layoutMargin}. <P> Requires a manual call to
     * <code>setLayoutMargin()</code> if changed on the fly.
     *
     * @param layoutRightMargin layoutRightMargin Default value is null
     */
    public void setLayoutRightMargin(Integer layoutRightMargin) {
    	getElement().getStyle().setMarginRight(layoutRightMargin.doubleValue(), Style.Unit.PX);
    }

    /**
     * Space outside of all members, on the right-hand side.  Defaults to {@link
     * com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutMargin layoutMargin}. <P> Requires a manual call to
     * <code>setLayoutMargin()</code> if changed on the fly.
     *
     * @return Integer
     */
    public Integer getLayoutRightMargin() {
    	return parseDimension(getElement().getStyle().getMarginRight());    
    }

    /**
     * Space outside of all members, on the top side.  Defaults to {@link
     * com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutMargin layoutMargin}. <P> Requires a manual call to
     * <code>setLayoutMargin()</code> if changed on the fly.
     *
     * @param layoutTopMargin layoutTopMargin Default value is null
     */
    public void setLayoutTopMargin(Integer layoutTopMargin) {
    	getElement().getStyle().setMarginTop(layoutTopMargin.doubleValue(), Style.Unit.PX);
    }

    /**
     * Space outside of all members, on the top side.  Defaults to {@link
     * com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutMargin layoutMargin}. <P> Requires a manual call to
     * <code>setLayoutMargin()</code> if changed on the fly.
     *
     * @return Integer
     */
    public Integer getLayoutTopMargin() {
    	return parseDimension(getElement().getStyle().getMarginTop());
    }


    /**
     * If set, a Layout with breadthPolicy:"fill" will specially interpret a percentage breadth on a member as a percentage of
     * available space excluding the {@link com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutMargin layoutMargin}.  If false,
     * percentages work exactly as for a non-member, with layoutMargins, if any, ignored.
     *
     * @param managePercentBreadth managePercentBreadth Default value is true
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setManagePercentBreadth(Boolean managePercentBreadth) throws IllegalStateException {

    }

    /**
     * If set, a Layout with breadthPolicy:"fill" will specially interpret a percentage breadth on a member as a percentage of
     * available space excluding the {@link com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutMargin layoutMargin}.  If false,
     * percentages work exactly as for a non-member, with layoutMargins, if any, ignored.
     *
     * @return Boolean
     */
    public Boolean getManagePercentBreadth() {
        return false;
    }


    /**
     * Space between each member of the layout. <P> Requires a manual call to <code>reflow()</code> if changed on the fly.
     *
     * @param membersMargin membersMargin Default value is 0
     */
    public void setMembersMargin(int membersMargin) {
    	this.membersMargin = membersMargin;
    }

    /**
     * Space between each member of the layout. <P> Requires a manual call to <code>reflow()</code> if changed on the fly.
     *
     * @return int
     */
    public int getMembersMargin() {
        return membersMargin;
    }

    /**
     * Minimum size, in pixels, below which members should never be shrunk, even if this requires the Layout to overflow.
     *
     * @param minMemberSize minMemberSize Default value is 1
     */
    public void setMinMemberSize(int minMemberSize) {
        this.minMemberSize = minMemberSize;
    }

    /**
     * Minimum size, in pixels, below which members should never be shrunk, even if this requires the Layout to overflow.
     *
     * @return int
     */
    public int getMinMemberSize() {
        return minMemberSize;
    }

    /**
     * Normal {@link com.smartgwt.mobile.client.types.Overflow} settings can be used on layouts, for example, an overflow:auto Layout
     * will scroll if members exceed its specified size, whereas an overflow:visible Layout will grow to accommodate members.
     *
     * @param overflow overflow Default value is "visible"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setOverflow(Overflow overflow) throws IllegalStateException {
    	if(overflow == null) {
    		throw new IllegalStateException("overflow should not be null");
    	}
    	this.overflow = overflow;
    }

    /**
     * Normal {@link com.smartgwt.mobile.client.types.Overflow} settings can be used on layouts, for example, an overflow:auto Layout
     * will scroll if members exceed its specified size, whereas an overflow:visible Layout will grow to accommodate members.
     *
     * @return Overflow
     */
    public Overflow getOverflow() {
        return overflow;
    }

    /**
     * If this widget has padding specified (or in the
     * CSS style applied to this layout), should it show up as space outside the members, similar to layoutMargin? <P> If this
     * setting is false, padding will not affect member positioning (as CSS padding normally does not affect absolutely
     * positioned children).  Leaving this setting true allows a designer to more effectively control layout purely from CSS.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param paddingAsLayoutMargin paddingAsLayoutMargin Default value is true
     */
    public void setPaddingAsLayoutMargin(Boolean paddingAsLayoutMargin) {
    	this.paddingAsLayoutMargin = paddingAsLayoutMargin;
    }

    /**
     * If this widget has padding specified or in the
     * CSS style applied to this layout), should it show up as space outside the members, similar to layoutMargin? <P> If this
     * setting is false, padding will not affect member positioning (as CSS padding normally does not affect absolutely
     * positioned children).  Leaving this setting true allows a designer to more effectively control layout purely from CSS.
     * <P> Note that {@link com.smartgwt.mobile.client.widgets.layout.Layout#getLayoutMargin layoutMargin} if specified, takes
     * precedence over this value.
     *
     * @return Boolean
     */
    public Boolean getPaddingAsLayoutMargin() {
        return paddingAsLayoutMargin;
    }


    /**
     * Reverse the order of stacking for this Layout, so that the last member is shown first. <P> Requires a manual call to
     * <code>reflow()</code> if changed on the fly. <P> In RTL mode, for horizontal Layouts the value of this flag will be
     * flipped during initialization.
     *
     * @param reverseOrder reverseOrder Default value is false
     */
    public void setReverseOrder(Boolean reverseOrder) {
    	this.reverseOrder = reverseOrder;
    	if(reverseOrder) {
    		getElement().addClassName("sc-layout-box-direction-reverse");
    	} else {
    		getElement().removeClassName("sc-layout-box-direction-reverse");
    	}
    }

    /**
     * Reverse the order of stacking for this Layout, so that the last member is shown first. <P> Requires a manual call to
     * <code>reflow()</code> if changed on the fly. <P> In RTL mode, for horizontal Layouts the value of this flag will be
     * flipped during initialization.
     *
     * @return Boolean
     */
    public Boolean getReverseOrder() {
        return reverseOrder;
    }


    /**
     * Should this layout appear with members stacked vertically or horizontally. Defaults to  <code>false</code> if
     * unspecified.
     *
     * @param vertical vertical Default value is null
     */
    public void setVertical(Boolean vertical) {
    	this.vertical = vertical;
    }

    /**
     * Should this layout appear with members stacked vertically or horizontally. Defaults to  <code>false</code> if
     * unspecified.
     *
     * @return Boolean
     */
    public Boolean getVertical() {
        return vertical;
    }

    /**
     * Sizing policy applied to members on vertical axis
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param vPolicy vPolicy Default value is "fill"
     */
    public void setVPolicy(LayoutPolicy vPolicy) {

    }

    /**
     * Sizing policy applied to members on vertical axis
     *
     * @return LayoutPolicy
     */
    public LayoutPolicy getVPolicy() {
        return null;
    }


    /**
     * Returns true if the layout includes the specified canvas.
     *
     * @param canvas the canvas to check for
     * @return true if the layout includes the specified canvas
     */
    public Boolean hasMember(Canvas canvas) {
        return members.contains(canvas);
    }


    /**
     * Layout members according to current settings. <P> Members will reflow automatically when the layout is resized, members
     * resize, the list of members changes or members change visibility.  It is only necessary to manually call
     * <code>reflow()</code> after changing settings on the layout, for example, <code>layout.reverseOrder</code>.
     */
    public void reflow() {
        List<Canvas> clone = new ArrayList<Canvas>(members);
        clear();
        for(Canvas canvas: clone) {
            addMember(canvas);
        }
    }


    /**
     * Removes the specified member from the layout. If it has a resize bar, the bar will be destroyed.
     *
     * @param member the canvas to be removed from the layout
     */
    public void removeMember(Canvas member) {
        try {
            remove(member);
        } finally {
            members.remove(member);
        }
    }

    public void removeMember(Widget widget) {
        for (Canvas member : members) {
            if (member instanceof WidgetCanvas) {
                if (((WidgetCanvas) member)._getWidget().equals(widget)) {
                    removeMember(member);
                    return;
                }
            }
        }
    }

    public void removeMembers(Canvas... members) {
        for (Canvas member : members) {
            removeMember(member);
        }
    }

    public void removeMembers(Iterable<? extends Canvas> iterable) {
        for (Canvas member : iterable) {
            removeMember(member);
        }
    }

    /**
     * Shift a member of the layout to a new position
     *
     * @param memberNum   current position of the member to move to a new position
     * @param newPosition new position to move the member to
     */
    public void reorderMember(int memberNum, int newPosition) {
        if (memberNum == newPosition) return;
        Canvas member = members.get(memberNum);
        removeMember(member);
        addMember(member, newPosition);
    }

    /**
     * Move a range of members to a new position
     *
     * @param start       beginning of range of members to move
     * @param end         end of range of members to move, non-inclusive
     * @param newPosition new position to move the members to
     */
    public void reorderMembers(int start, int end, int newPosition) {
        if (start == newPosition) return;
        List<Canvas> sublist = members.subList(start, end);
        removeMembers(sublist);
        addMembers(sublist, newPosition);
    }

    /**
     * Hide all other members and make the single parameter member visible.
     *
     * @param member member to show
     */
    public void setVisibleMember(Canvas member) {

    }


    /**
     * An array of canvases that will be contained within this layout. You can set the following properties on these
     * canvases (in addition to the standard component properties): <ul>  <li>layoutAlign--specifies the member's
     * alignment along the breadth axis; valid  values are "top", "center" and "bottom" for a horizontal layout and
     * "left", "center"  and "right" for a vertical layout  <li>showResizeBar--set to true to show a resize bar (default
     * is false) </ul> Height and width settings found on members are interpreted by the Layout according to the getVPolicy
     *
     * @param members members Default value is null
     */
    public void setMembers(Canvas... members) {
        final Canvas[] membersToRemove = getMembers();
        removeMembers(membersToRemove);
        addMembers(members);
    }

    /**
     * Add one or more canvases to the layout
     * @param members - canvases to be added
     */
    public void addMembers(Canvas... members) {
        if (members == null || members.length == 0) return;
        for(Canvas member : members) {
            addMember(member);
        }
    }

    /**
     * Add one or more canvases to the layout at specific positions.
     * @param members - array of canvases to be added
     * @param position - position to add newMembers
     */
    public void addMembers(Canvas[] members, int position) {
        if (members == null || members.length == 0) return;
        if (position < this.members.size()) position = this.members.size();
        for (int i = 0; i < members.length; i++) {
            addMember(members[i], position + i);
        }
    }

    public void addMembers(Iterable<? extends Canvas> members, int position) {
        int i = 0;
        for (Canvas canvas : members) {
            addMember(canvas, position + i++);
        }
    }

    /**
     * Add a canvas to the layout.
     *
     * @param component the canvas object to be added to the layout
     */
    public void addMember(Canvas component) {
        if (component != null) {
            int indexOfComponent = members.indexOf(component);
            if (indexOfComponent >= 0) {
                // TODO Log a warning that this Layout already contains `component`.
                assert members.contains(component);
                if (indexOfComponent == members.size() - 1) return;
                removeMember(component);
            }
            add(component, _getInnerElement());
            members.add(component);
        }
    }

    public void addMember(Widget widget) {
        final WidgetCanvas canvas = new WidgetCanvas(widget);
        addMember(canvas);
    }

    /**
     * Adds a component to the layout at the specified position.
     *
     * @param component the <code>Canvas</code> object to be added to the layout
     * @param position  the position in the layout at which to place <code>component</code> (starts with 0)
     */
    public void addMember(Canvas component, int position) {
        if (position < 0 || members.size() < position) throw new IndexOutOfBoundsException("`component' cannot be added to this Layout at index " + position);
        if (component != null) {
            int indexOfComponent = members.indexOf(component);
            if (indexOfComponent >= 0) {
                // TODO Log a warning that this Layout already contains `component`.
                assert members.contains(component);
                if (indexOfComponent == position) return;
                removeMember(component);
                if (indexOfComponent < position) --position;
            }
            final com.google.gwt.user.client.Element layoutElement = _getInnerElement();
            if (position >= members.size()) {
                add(component, layoutElement);
            } else {
                int childIndex = DOM.getChildIndex(layoutElement, members.get(position).getElement());
                assert childIndex >= 0;
                insert(component, layoutElement, childIndex, true);
            }
            members.add(position, component);
        }
    }

    public void addMember(Widget widget, int position) {
        final WidgetCanvas canvas = new WidgetCanvas(widget);
        addMember(canvas, position);
    }

    public void clear() {
        removeMembers(getMembers());
    }


    /**
     * Specifies the default alignment for layout members on the breadth axis. If unset, default member
     * layout alignment will be "top" for a horizontal layout, and left for a vertical layout.
     *
     * @param alignment defaultLayoutAlign Default value is null
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setDefaultLayoutAlign(Alignment alignment) throws IllegalStateException {
        this.alignment = alignment;
    }

    /**
     * Specifies the default alignment for layout members on the breadth axis. If unset, default member
     * layout alignment will be "top" for a horizontal layout, and left for a vertical layout.
     *
     * @param alignment defaultLayoutAlign Default value is null
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setDefaultLayoutAlign(VerticalAlignment alignment) throws IllegalStateException {
    	this.valign = alignment;
    }


    /**
     * Alignment of all members in this Layout on the length axis.  Defaults to "top" for vertical Layouts, and "left"
     * for horizontal Layouts.
     *
     * @param alignment alignment Default value is null
     */
    public void setAlign(Alignment alignment) {
        if(this.alignment != alignment) {
            if(this.alignment != null) {
            	switch(this.alignment){
            	case CENTER:
            	    _removeClassName("sc-layout-box-pack-center");
            	    break;
                case JUSTIFY:
                    _removeClassName("sc-layout-box-pack-justify");
                    break;
            	case LEFT:
            	    _removeClassName("sc-layout-box-pack-start");
            	    break;
            	case RIGHT:
            	    _removeClassName("sc-layout-box-pack-end");
            	    break;
            	}
            }
            if(alignment != null) {
                this.alignment = alignment;
                switch(this.alignment){
                case CENTER:
                    _setClassName("sc-layout-box-pack-center", false);
                    break;
                case JUSTIFY:
                    _setClassName("sc-layout-box-pack-justify", false);
                    break;
                case LEFT:
                    _setClassName("sc-layout-box-pack-start", false);
                    break;
                case RIGHT:
                    _setClassName("sc-layout-box-pack-end", false);
                    break;
                }
            }
        }
    }

    /**
     * Alignment of all members in this Layout on the length axis.  Defaults to "top" for vertical Layouts, and "left"
     * for horizontal Layouts.
     *
     * @param valign alignment Default value is null
     */
    public void setAlign(VerticalAlignment valign) {
        if(this.valign != valign) {
            if(this.valign != null) {
            	switch(this.valign) {
            	case TOP:
            	    _removeClassName("sc-layout-box-align-start");
            	    break;
            	case CENTER:
            	    _removeClassName("sc-layout-box-align-center");
            	    break;
            	case BOTTOM:
            	    _removeClassName("sc-layout-box-align-end");
            	    break;
            	}
            }
            if(valign != null) {
            this.valign = valign;
                switch(this.valign) {
                case TOP:
                    _setClassName("sc-layout-box-align-start", false);
                    break;
                case CENTER:
                    _setClassName("sc-layout-box-align-center", false);
                    break;
                case BOTTOM:
                    _setClassName("sc-layout-box-align-end", false);
                    break;
                }
            }
        }
    }

    /**
     * Return the members in the Layout.
     *
     * @return the members
     */
    public Canvas[] getMembers() {
        return members.toArray(new Canvas[members.size()]);
    }
    
    /**
     * Return if momentum scroll is enabled.
     *
     * @return boolean
     */
    public boolean getMomentumScroll() {
        return momentumScroll;
    }
    
    /**
     * Enable/disable momentum scroll
     *
     */
    public void setMomentumScroll(boolean onoff) {
         momentumScroll = onoff;;
    }

    /**
     * Show the specified member, firing the specified callback when the hide is complete. <P> Members can always be
     * directly shown via <code>member.show()</code>
     * @param member Member to show
     */
    public final void showMember(Canvas member) {
        member.setVisible(true);
    }

    /**
     * Show the specified member, firing the specified callback when show is complete. <P> Members can always be
     * directly shown via <code>member.show()</code>
     * @param member   Member to show
     * @param callback action to fire when the member has been shown
     */
    public final void showMember(Canvas member, Function callback) {
        showMember(member);
        if (callback != null) {
            callback.execute();
        }
    }


    /**
     * Hide the specified member, firing the specified callback when the hide is complete. <P> Members can always be
     * directly hidden via <code>member.hide()</code>
     *
     * @param member Member to hide
     */
    public final void hideMember(Canvas member) {
        member.setVisible(false);
    }

    /**
     * Hide the specified member, firing the specified callback when the hide is complete. <P> Members can always be
     * directly hidden via <code>member.hide()</code>
     *
     * @param member   Member to hide
     * @param callback callback to fire when the member is hidden.
     */
    public final void hideMember(Canvas member, Function callback) {
        hideMember(member);
        if (callback != null) {
            callback.execute();
        }
    }

    /**
     * Given a numerical index or a member ID, return a pointer to the appropriate member. <p> If passed a member
     * Canvas, just returns it.
     *
     * @param index index for the member
     * @return member widget
     */
    public Canvas getMember(int index) {
        return members.get(index);
    }

    /**
     * Given a numerical index or a member ID, return a pointer to the appropriate member. <p> If passed a member
     * Canvas, just returns it.
     *
     * @param memberID identifier for the required member
     * @return member widget
     */
    public Canvas getMember(String memberID) {
        return null;
    }

    /**
     * Given a member Canvas or member ID, return the index of that member within this layout's members array <p> If
     * passed a number, just returns it.
     *
     * @param member the member
     * @return index of the member canvas (or -1 if not found)
     */
    public int getMemberNumber(Canvas member) {
        return members.indexOf(member);
    }

    /**
     * Given a member Canvas or member ID, return the index of that member within this layout's members array <p> If
     * passed a number, just returns it.
     *
     * @param memberID identifier for the required member
     * @return index of the member canvas (or -1 if not found)
     */
    public int getMemberNumber(String memberID) {
        return 0;
    }

    public HandlerRegistration addDragMoveHandler(DragMoveHandler dragMoveHandler) {
        return null;
    }
}
