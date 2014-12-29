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

import com.smartgwt.mobile.client.theme.ButtonsCssResource;
import com.smartgwt.mobile.client.types.SelectionType;
import com.smartgwt.mobile.client.widgets.BaseButton;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.ContainerFeatures;
import com.smartgwt.mobile.client.widgets.PanelContainer;
import com.smartgwt.mobile.client.widgets.Segment;

/**
 * A SegmentedControl is a horizontal control consisting of multiple {@link com.smartgwt.mobile.client.widgets.Segment}'s.
 * <br><br>
 */
public class SegmentedControl extends HLayout implements PanelContainer {

    private static final ButtonsCssResource BUTTONS_CSS = BaseButton._CSS;
    private static int nextRadioGroupNum = 1;

    private transient String radioGroup;

    /**
     * The segments in this control.
     */
    private Segment[] segments;

    /**
     * The tint color. Only applicable if the control is added to a {@link com.smartgwt.mobile.client.widgets.toolbar.ToolStrip}.
     */
    protected String tintColor;

    /**
     * Whether to inherit the tint from its containing {@link com.smartgwt.mobile.client.widgets.toolbar.ToolStrip}.
     * Does not apply to segmented controls added to views.
     */
    private boolean inheritTint;

    /**
     * whether the segments show the selected state
     */
    private boolean momentary = false;

    /**
     * Whether to stretch the entire width of the parent container, or auto-fit the width based on its contents.
     */
    private boolean stretch;

    public SegmentedControl() {
        radioGroup = "_segmentedControlRadioGroup" + Integer.toString(nextRadioGroupNum++);
        setStyleName(BUTTONS_CSS.segmentedControlClass());
        getElement().addClassName("sc-layout-box-direction-normal");
    }

    /**
     * Return the segments in the controller.
     *
     * @return the segments, or null if none set
     */
    public final Segment[] getSegments() {
        return segments;
    }

    /**
     * Sets the segments of this segment controller.
     *
     * @param segments the segments
     */
    public void setSegments(Segment... segments) {
        if (this.segments != null) {
            removeMembers(this.segments);
        }
        this.segments = segments;
        if (segments != null) {
            for (final Segment segment : segments) {
                if (momentary) {
                    segment.setActionType(SelectionType.BUTTON);
                    segment.setSelected(false);
                } else {
                    segment.setActionType(SelectionType.RADIO);
                    segment.setRadioGroup(radioGroup);
                }
                addMember(segment);
            }
            setInheritTint(inheritTint);
            if (!inheritTint) {
                setTintColor(tintColor);
            }
        }
    }

    /**
     * Return true if segments show selected state.
     *
     * @return momentary value. Defaults to false.
     */
    public final boolean isMomentary() {
        return momentary;
    }

    /**
     * Set true if the segmented control should not display the selected state of a segment.
     * Defaults to false.
     *
     * @param momentary the momentary value
     */
    public void setMomentary(boolean momentary) {
        if (this.momentary == momentary) return;

        this.momentary = momentary;
        if (segments != null) {
            for (final Segment segment : segments) {
                if (momentary) {
                    segment.setActionType(SelectionType.BUTTON);
                    segment.setSelected(false);
                } else {
                    segment.setActionType(SelectionType.RADIO);
                    segment.setRadioGroup(radioGroup);
                }
            }
        }
    }

    /**
     * Return the tint color of the control.
     *
     * @return the tintColor
     */
    public String getTintColor() {
        return tintColor;
    }

    /**
     * Set the button tintColor. Can pass in the color name, the hext value, or the rgb / rgba value as a String.
     * <br><br>
     * <b>Note:</b> Passing a rgba string with a value for opacity allows buttons to have an opacity / translucency.
     *
     * @param tintColor the tint color
     */
    public void setTintColor(String tintColor) {
        this.tintColor = tintColor;
        if (segments != null) {
            for (final Segment segment : segments) {
                if (tintColor != null) {
                    segment.getElement().addClassName(BUTTONS_CSS.customTintedButtonClass());
                    segment.getElement().getStyle().setBackgroundColor(tintColor);
                } else {
                    segment.getElement().removeClassName(BUTTONS_CSS.customTintedButtonClass());
                    segment.getElement().getStyle().clearBackgroundColor();
                }
            }
        }
    }

    /**
     * Whether the control should inherit the tint from the toolbar. Applies only to segmented controls added to a toolbar.
     *
     * @return true if inherits tint. Defaults to false
     */
    public boolean isInheritTint() {
        return inheritTint;
    }

    /**
     * Whether the control should inherit the tint from the toolbar. Applies only to segmented controls added to a toolbar.
     *
     * @param inheritTint true if inherits tint. Defaults to false
     */
    public void setInheritTint(boolean inheritTint) {
        this.inheritTint = inheritTint;
        if (segments != null) {
            for (final Segment segment : segments) {
                if (inheritTint) {
                    segment.getElement().addClassName(BUTTONS_CSS.customTintedButtonClass());
                } else {
                    segment.getElement().removeClassName(BUTTONS_CSS.customTintedButtonClass());
                }
            }
        }
    }

    /**
     * Should the button stretch in width?
     *
     * @return true for stretch, false for auto-width based on contents
     */
    public boolean isStretch() {
        return stretch;
    }

    /**
     * Should the button stretch in width?
     *
     * @param stretch true for stretch, false for auto-width based on contents
     */
    public void setStretch(boolean stretch) {
        this.stretch = stretch;
        if (stretch) {
            getElement().addClassName(BUTTONS_CSS.stretchButtonClass());
        } else {
            getElement().removeClassName(BUTTONS_CSS.stretchButtonClass());
        }
    }

    public boolean selectSegment(Segment otherSegment) {
        if (momentary) return false;
        if (segments != null) {
            for (final Segment segment : segments) {
                if (segment == otherSegment) {
                    segment.select();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return the index of the selected segment. Returns -1 if {@link #setMomentary(boolean) momentary} is true or if
     * no segments are selected.
     *
     * @return the selected segment index
     */
    public final int getSelectedIndex() {
        if (segments != null) {
            for (int i = 0; i < segments.length; ++i) {
                if (Canvas._booleanValue(segments[i].isSelected(), false)) return i;
            }
        }
        return -1;
    }

    public final Segment getSelectedSegment() {
        final int selectedIndex = getSelectedIndex();
        return (momentary || selectedIndex < 0) ? null : segments[selectedIndex];
    }

    /**
     * Sets the selected segment.
     *
     * @param selectedIndex the index of the segment to select
     */
    public boolean setSelectedIndex(int selectedIndex) {
        if (momentary) return false;
        return selectSegment(selectedIndex);
    }

    /**
     * Select the specified segment.
     *
     * @param index the index of the segment to select
     * @return true is selection was successful
     */
    public boolean selectSegment(int index) {
        if (momentary) return false;
        if (segments == null || segments.length <= index) {
            return false;
        }
        index = Math.max(-1, index);
        if (index != -1) segments[index].select();
        return true;
    }

    @Override
    public ContainerFeatures getContainerFeatures() {
        return new ContainerFeatures(this, true, false, false, true, 0);
    }
}
