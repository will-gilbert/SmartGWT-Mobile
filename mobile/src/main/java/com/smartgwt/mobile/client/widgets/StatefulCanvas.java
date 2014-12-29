package com.smartgwt.mobile.client.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.EventUtil;
import com.smartgwt.mobile.client.internal.util.ElementUtil;
import com.smartgwt.mobile.client.types.SelectionType;
import com.smartgwt.mobile.client.types.State;

public class StatefulCanvas extends Canvas {

    private static final Map<String, ArrayList<StatefulCanvas>> radioGroups = new HashMap<String, ArrayList<StatefulCanvas>>();

    private transient State enabledState;

    private SelectionType actionType = SelectionType.BUTTON;
    private String baseStyle;
    private String radioGroup;
    private Boolean selected;
    private Boolean showDisabled = true;
    private Boolean showDown;
    private State state;

    public StatefulCanvas() {
        init();
    }

    private void init() {
        if (radioGroup != null) {
            final String rg = radioGroup;
            radioGroup = null;
            internalAddToRadioGroup(rg);
        }

        sinkEvents((TouchEvent.isSupported() ? Event.TOUCHEVENTS : Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEOUT) | Event.ONCLICK);
    }

    @Override
    public void destroy() {
        removeFromRadioGroup();
        super.destroy();
    }

    @Override
    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);
        if (!isEnabled()) {
            return;
        }
        final Element targetElem = EventUtil.getTargetElem(event);
        if (targetElem != null && getElement().isOrHasChild(targetElem)) {
            switch (event.getTypeInt()) {
                case Event.ONCLICK:
                    // If an <input> or <textarea> element has keyboard focus, tapping a button
                    // should hide the keyboard and clear any floating autocorrect suggestion.
                    final Element activeElement = ElementUtil.getActiveElement();
                    if (activeElement != null) activeElement.blur();

                    _handleActivate();
                    break;
                case Event.ONMOUSEDOWN:
                case Event.ONTOUCHSTART:
                    if (Canvas._booleanValue(showDown, false)) setState(State.STATE_DOWN);
                    break;
                case Event.ONMOUSEUP:
                case Event.ONMOUSEOUT:
                case Event.ONTOUCHEND:
                case Event.ONTOUCHCANCEL:
                    setState(State.STATE_UP);
                    break;
            }
        }
    }

    public final SelectionType getActionType() {
        return actionType;
    }

    public void setActionType(SelectionType actionType) {
        if (actionType == SelectionType.BUTTON) {
            setSelected(false);
        }
        this.actionType = actionType;
    }

    public final String getBaseStyle() {
        return baseStyle;
    }

    public void setBaseStyle(String style) {
        if (this.baseStyle == style) return;
        this.baseStyle = style;
        // fall through to stateChanged to actually update the appearance
        stateChanged();
    }

    @Override
    public void _setHandleDisabled(boolean disabled) {
        super._setHandleDisabled(disabled);

        if (!Canvas._booleanValue(showDisabled, false)) return;

        final State currentState = getState();
        final boolean handleIsDisabled = currentState == State.STATE_DISABLED;
        if (handleIsDisabled == disabled) return;

        if (disabled == false) {
            setState(enabledState == null ? State.STATE_UP : enabledState);
        } else {
            enabledState = currentState;
            setState(State.STATE_DISABLED);
        }
    }

    public final String getRadioGroup() {
        return radioGroup;
    }

    public void setRadioGroup(String radioGroup) {
        addToRadioGroup(radioGroup);
    }

    public final Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean newIsSelected) {
        if (!Canvas._isDifferent(this.selected, newIsSelected, false)) return;

        // handle mutually exclusive radioGroups
        if (newIsSelected && radioGroup != null) {
            final List<StatefulCanvas> groupArray = radioGroups.get(radioGroup);
            assert groupArray != null;
            for (final StatefulCanvas c : groupArray) {
                if (c != this && Canvas._booleanValue(c.isSelected(), false)) c.setSelected(false);
            }
        }

        this.selected = newIsSelected;

        stateChanged();
    }

    public final void deselect() {
        setSelected(false);
    }

    public final void select() {
        setSelected(true);
    }

    public final Boolean getShowDown() {
        return showDown;
    }

    public void setShowDown(Boolean showDown) {
        if (!Canvas._isDifferent(this.showDown, showDown, false)) return;
        this.showDown = showDown;
        stateChanged();
    }

    public final Boolean getShowDisabled() {
        return showDisabled;
    }

    public void setShowDisabled(Boolean showDisabled) {
        this.showDisabled = showDisabled;
        stateChanged();
    }

    public final State getState() {
        if (state == null) return State.STATE_UP;
        return state;
    }

    public void setState(State state) {
        final State previousState = getState();
        this.state = state;
        if (previousState != getState()) stateChanged();
    }

    @SGWTInternal
    public String _getStateName() {
        final String modifier = getStateSuffix();
        return (baseStyle == null ? "" : baseStyle) + modifier;
    }

    public final String getStateSuffix() {
        final String state = getState().getValue(),
                selected = Canvas._booleanValue(isSelected(), false) ? "Selected" : null;
        return _getStateSuffix(state, selected, null, null);
    }

    @SGWTInternal
    public final String _getStateSuffix(String state, String selected, String focused, String customState) {
        assert state != null;
        final String modifier;
        if (selected != null || focused != null) {
            modifier = (selected != null && focused != null)
                       ? "SelectedFocused"
                       : (selected != null ? selected : focused);
        } else {
            modifier = "";
        }
        return modifier + state + (customState == null ? "" : customState);
    }

    private void internalAddToRadioGroup(String groupID) {
        if (groupID == null || radioGroup == groupID) return;

        if (this.radioGroup != null) this.removeFromRadioGroup();

        this.radioGroup = groupID;

        ArrayList<StatefulCanvas> widgetArray = radioGroups.get(groupID);
        if (widgetArray == null) {
            widgetArray = new ArrayList<StatefulCanvas>();
            widgetArray.add(this);
            radioGroups.put(groupID, widgetArray);
        } else {
            assert !widgetArray.contains(this);
            widgetArray.add(this);
        }
    }

    public void addToRadioGroup(String groupID) {
        internalAddToRadioGroup(groupID);
    }

    @SGWTInternal
    public void _handleActivate() {
        final SelectionType actionType = getActionType();
        if (actionType == SelectionType.RADIO) {
            // if a radio button, select this button
            select();
        } else if (actionType == SelectionType.CHECKBOX) {
            // if a checkbox, toggle the selected state
            setSelected(!Canvas._booleanValue(isSelected(), false));
        }
    }

    public final void removeFromRadioGroup() {
        removeFromRadioGroup(null);
    }

    public void removeFromRadioGroup(String groupID) {
        if (radioGroup == null || (groupID != null && groupID != radioGroup)) return;

        final List<StatefulCanvas> widgetArray = radioGroups.get(radioGroup);
        assert widgetArray != null;
        final boolean wasRemoved = widgetArray.remove(this);
        assert wasRemoved && !widgetArray.contains(this);
        if (widgetArray.isEmpty()) radioGroups.remove(radioGroup);
        radioGroup = null;
    }

    protected void stateChanged() {
        setStyleName(_getStateName());
    }
}
