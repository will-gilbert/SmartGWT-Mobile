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

package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.theme.ios.SliderCssResourceIOS;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.widgets.events.HasValueChangedHandlers;
import com.smartgwt.mobile.client.widgets.events.ValueChangedEvent;
import com.smartgwt.mobile.client.widgets.events.ValueChangedHandler;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;

/**
 * The Slider class implements a GUI slider widget allowing the user to select a numeric  value from within a range by
 * dragging a visual indicator up and down a track. <p>  The slider will generate events as the user interacts with it and
 * changes its value.
 */

public class SliderItem extends FormItem implements HasValueChangedHandlers {

    public static final SliderCssResourceIOS _CSS = (SliderCssResourceIOS)ThemeResources.INSTANCE.sliderCSS();

    private float minValue = 0.0F, maxValue = 100.0F, value = 0.0F;
	private int length = 40;
	private int offsetX, lastX = 0, internalWidth, roundPrecision = 1, numValues;
	private boolean active = false, roundValues = true;
    private Element knobElem;

    public SliderItem(String name) {
        super(name, Document.get().createDivElement());
        final Element element = getElement();
        element.addClassName(_CSS.sliderItemClass());
        knobElem = Document.get().createDivElement();
        knobElem.setClassName(_CSS.sliderKnobClass());
        element.appendChild(knobElem);
        sinkEvents(Event.GESTUREEVENTS);
    }

    public SliderItem(String name, String title) {
        this(name);
        setTitle(title);
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (!isEnabled() || _isReadOnly()) {
            return;
        }
        Element target = event.getEventTarget().cast();
        if (target != null && getElement().isOrHasChild(target)) {
	        switch (event.getTypeInt()) {
	        	case Event.ONMOUSEDOWN:
	        	    if(!touched) {
	        	        _onStart(event, null);
	        	    }
	        	    break;
	        	case Event.ONTOUCHSTART:
	        	case Event.ONGESTURESTART:
	        	    touched = true;
	        		_onStart(event, null);
	        		break;
	        	case Event.ONMOUSEMOVE:
	        	    if(!touched) {
	        	        onMove(event);
	        	    }
	        	    break;
	        	case Event.ONTOUCHMOVE:
	        	case Event.ONGESTURECHANGE:
	        		onMove(event);
	        		break;
	            case Event.ONMOUSEUP:
	                if(!touched) {
	                    _onEnd(event);
	                }
	                break;
	        	case Event.ONTOUCHEND:
	        	case Event.ONTOUCHCANCEL:
	        	case Event.ONGESTUREEND:
	        		_onEnd(event);
	        		break;
	        }
        }
    }

    private void calcInternalWidth() {
        internalWidth = getElement().getOffsetWidth();
    }

    @Override
    public void _onStart(Event event, Touch touch) {
    	if(!active) {
            event.preventDefault();
            if (_isReadOnly()) return;
        	active = true;
        	offsetX = (event.getTypeInt() == Event.ONTOUCHSTART ? event.getTouches().get(0).getClientX() : event.getClientX()) - lastX;
            calcInternalWidth();
    	}
    }

    protected void onMove(Event event) {
    	event.preventDefault();
    	if(active) {
    		lastX = (event.getTypeInt() == Event.ONTOUCHMOVE ? event.getTouches().get(0).getClientX() : event.getClientX()) - offsetX;
    		if(lastX < 0) {
    			lastX = 0;
    		}
    		if(lastX > internalWidth) {
    			lastX = internalWidth;
    		}
            value = lastX / ((float)internalWidth) * (maxValue - minValue);
            _updateValue(getRoundedValue(value));
            getElement().getStyle().setProperty(DOMConstants.INSTANCE.getBackgroundSizePropertyName(), lastX + "px " + _CSS.sliderBarHeightPx() + "px, 100% " + _CSS.sliderBarHeightPx() + "px");
            knobElem.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "translate(" + lastX + "px)");
    	}
    }

    protected void _onEnd(Event event) {
    	if(active) {
    	  active = false;
          fireEvent(new com.smartgwt.mobile.client.widgets.events.ValueChangedEvent((Float)value));
    	}
    }

	/**
     * Used to set slider height if vertical, slider width if horizontal. Applied to the slider track, not necessarily the
     * entire widget. Overridden by an explicit width/height specification for the widget.
     *
     * @param length length Default value is 50
     */
    public void setLength(int length) {
    	this.length = length;
    }

    /**
     * Used to set slider height if vertical, slider width if horizontal. Applied to the slider track, not necessarily the
     * entire widget. Overridden by an explicit width/height specification for the widget.
     *
     * @return int
     */
    public int getLength() {
        return length;
    }

    /**
     * The maximum slider value. The slider value is equal to maxValue when the thumb is at the top or right of the slider
     * (unless flipValues is true, in which case the maximum value is at the bottom/left of the slider)
     * Sets the {@link com.smartgwt.mobile.client.widgets.form.fields.SliderItem#getMaxValue maximum value} of the slider
     *
     * @param maxValue the new maximum value. Default value is 100
     */
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * The maximum slider value. The slider value is equal to maxValue when the thumb is at the top or right of the slider
     * (unless flipValues is true, in which case the maximum value is at the bottom/left of the slider)
     *
     * @return float
     */
    public float getMaxValue() {
        return this.maxValue;
    }


    /**
     * The minimum slider value. The slider value is equal to minValue when the thumb is at the bottom or left of the slider
     * (unless flipValues is true, in which case the minimum value is at the top/right of the slider)
     * Sets the {@link com.smartgwt.mobile.client.widgets.form.fields.SliderItem#getMinValue minimum value} of the slider
     *
     * @param minValue the new minimum value. Default value is 1
     */
    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    /**
     * The minimum slider value. The slider value is equal to minValue when the thumb is at the bottom or left of the slider
     * (unless flipValues is true, in which case the minimum value is at the top/right of the slider)
     *
     * @return float
     */
    public float getMinValue() {
        return this.minValue;
    }

    /**
     * The number of discrete values represented by slider. If specified, the range of valid values (between
     * <code>minValue</code> and <code>maxValue</code>) will be divided into this many steps. As the thumb is moved along the
     * track it will only select these values and appear to jump between the steps.
     * Sets the {@link com.smartgwt.mobile.client.widgets.form.fields.SliderItem#getNumValues number of values} for the slider
     *
     * @param numValues the new number of values. Default value is null
     */
    public void setNumValues(Integer numValues) {
        this.numValues = numValues;
    }

    /**
     * The number of discrete values represented by slider. If specified, the range of valid values (between
     * <code>minValue</code> and <code>maxValue</code>) will be divided into this many steps. As the thumb is moved along the
     * track it will only select these values and appear to jump between the steps.
     *
     * @return Integer
     */
    public Integer getNumValues() {
        return numValues;
    }

    /**
     * If {@link com.smartgwt.mobile.client.widgets.form.fields.SliderItem#getRoundValues roundValues} is false, the slider value will be rounded to
     * this number of decimal places. If set to null the value will not be rounded
     * Sets the {@link com.smartgwt.mobile.client.widgets.form.fields.SliderItem#getRoundPrecision roundPrecision} property of the slider
     *
     * @param roundPrecision new round precision. Default value is 1
     */
    public void setRoundPrecision(int roundPrecision) {
        this.roundPrecision = roundPrecision;
    }

    /**
     * If {@link com.smartgwt.mobile.client.widgets.form.fields.SliderItem#getRoundValues roundValues} is false, the slider value will be rounded to
     * this number of decimal places. If set to null the value will not be rounded
     *
     * @return int
     */
    public int getRoundPrecision() {
        return roundPrecision;
    }

    /**
     * Specifies whether the slider value should be rounded to the nearest integer.  If set to false, values will be rounded to
     * a fixed number of decimal places controlled by {@link com.smartgwt.mobile.client.widgets.form.fields.SliderItem#getRoundPrecision
     * roundPrecision}.
     *
     * @param roundValues roundValues Default value is true
     */
    public void setRoundValues(boolean roundValues) {
        this.roundValues = roundValues;
    }

    /**
     * Specifies whether the slider value should be rounded to the nearest integer.  If set to false, values will be rounded to
     * a fixed number of decimal places controlled by {@link com.smartgwt.mobile.client.widgets.form.fields.SliderItem#getRoundPrecision
     * roundPrecision}.
     *
     * @return boolean
     */
    public final boolean getRoundValues() {
        return roundValues;
    }

    private float getRoundedValue(float value) {
        if (roundValues) value = Math.round(value);
        else {
            double multiplier = Math.pow(10.0, roundPrecision);
            value = (float) (Math.round(value * multiplier) / multiplier);
        }
        return value;
    }

    @Override
    Float _getElementValue() {
        return getRoundedValue(value);
    }

    @Override
    public void _setElementValue(Object displayValue, Object newValue) {
        if (validate()) {
            calcInternalWidth();
            float valueF = displayValue == null ? minValue : ((Number)displayValue).floatValue();
            lastX = Math.round((maxValue - minValue) / valueF * internalWidth);
        }
    }

    /**
     * The percentage of the total slider that constitutes one discrete step. The slider will move one step when the
     * appropriate arrow key is pressed.
     * Sets the {@link com.smartgwt.mobile.client.widgets.form.fields.SliderItem#getStepPercent stepPercent} property of the slider
     *
     * @param stepPercent new slider step percent. Default value is 5
     */
    public void setStepPercent(float stepPercent) {

    }

    /**
     * The percentage of the total slider that constitutes one discrete step. The slider will move one step when the
     * appropriate arrow key is pressed.
     *
     * @return float
     */
    public float getStepPercent() {
        return 0;
    }

    /**
     * Indicates whether this is a vertical or horizontal slider.
     * Sets the {@link com.smartgwt.mobile.client.widgets.form.fields.SliderItem#getVertical vertical} property of the slider
     *
     * @param vertical is the slider vertical. Default value is true
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#controls_category_slider" target="examples">Slider Example</a>
     */
    public void setVertical(boolean vertical) {

    }

    /**
     * Indicates whether this is a vertical or horizontal slider.
     *
     * @return boolean
     */
    public boolean getVertical() {
        return false;
    }

    /**
     * Add a valueChanged handler.
     * <p/>
     * This method is called when the slider value changes. This occurs when the setValue method is called, or when the slider
     * is moved. Observe this method to be notified when the slider value changes.
     *
     * @param handler the valueChanged handler
     * @return {@link HandlerRegistration} used to remove this handler
     */
    public HandlerRegistration addValueChangedHandler(ValueChangedHandler handler) {
        return addHandler(handler, ValueChangedEvent.getType());
    }

    /**
     * Call this method in your {@link com.smartgwt.mobile.client.widgets.form.fields.SliderItem#addValueChangedHandler Slider.valueChanged} handler
     * to determine whether the value change is due to an ongoing drag interaction (true) or due to a thumb-release, mouse
     * click, keypress, or programmatic event (false). You may choose to execute temporary or partial updates while the slider
     * thumb is dragged, and final updates or persistence of the value in response to the other events.
     *
     * @return true if user is still dragging the slider thumb, false otherwise
     */
    @SGWTInternal
    public boolean _valueIsChanging() {
        return active;
    }

    @Override
    public boolean validate() {
        return minValue <= value && value <= maxValue;
    }
}
