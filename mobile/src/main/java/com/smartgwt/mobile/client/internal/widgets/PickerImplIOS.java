package com.smartgwt.mobile.client.internal.widgets;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.i18n.SmartGwtMessages;
import com.smartgwt.mobile.client.internal.EventUtil;
import com.smartgwt.mobile.client.internal.test.AutoTestLocatable;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.theme.ios.PickerCssResourceIOS;
import com.smartgwt.mobile.client.internal.theme.ios.ToolStripCssResourceIOS;
import com.smartgwt.mobile.client.internal.widgets.events.PickerHiddenEvent;
import com.smartgwt.mobile.client.internal.widgets.events.ValuesSelectedEvent;
import com.smartgwt.mobile.client.widgets.BaseButton.ButtonType;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.HStack;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;
import com.smartgwt.mobile.internal.gwt.dom.client.event.AnimationEndEvent;

class PickerImplIOS extends PickerImpl implements ClickHandler {

    private static final PickerCssResourceIOS CSS = (PickerCssResourceIOS)Picker2.CSS;

    private Picker2 self;
    private transient PickerDial activeDial;
    private ToolStripButton cancelButton;
    private HandlerRegistration cancelClickRegistration;
    private HStack clearDoneStack;
    private ToolStripButton clearButton;
    private HandlerRegistration clearClickRegistration;
    private ToolStripButton doneButton;
    private HandlerRegistration doneClickRegistration;
    private Element pickerWrapperElem, pickerFrameWrapperElem, pickerFrameElem, topCoverElem, centerCoverElem, bottomCoverElem,
            pickerDialsWrapperElem;
    private Map<PickerDial, Record> selectedRecords;
    private ToolStrip toolbar;

    @Override
    void init(Picker2 self) {
        this.self = self;

        final Document document = Document.get();
        pickerWrapperElem = document.createDivElement();
        pickerWrapperElem.setClassName(CSS.pickerWrapperClass());

        toolbar = new ToolStrip();
        toolbar.getElement().addClassName(((ToolStripCssResourceIOS)ToolStrip._CSS).pickerToolbarClass());
        cancelButton = new ToolStripButton(SmartGwtMessages.INSTANCE.dialog_CancelButtonTitle());
        cancelButton.setButtonType(ButtonType.BORDERED);
        cancelButton.setTintColor("#333");
        cancelClickRegistration = cancelButton.addClickHandler(this);
        toolbar.addButton(cancelButton);
        clearDoneStack = new HStack();
        clearDoneStack.setMembersMargin(10);
        clearButton = new ToolStripButton(SmartGwtMessages.INSTANCE.dialog_ClearButtonTitle());
        clearButton.setVisible(false);
        clearButton.setButtonType(ButtonType.BORDERED);
        clearButton.setTintColor("#333");
        clearClickRegistration = clearButton.addClickHandler(this);
        clearDoneStack.addMember(clearButton);
        doneButton = new ToolStripButton(SmartGwtMessages.INSTANCE.dialog_DoneButtonTitle());
        doneButton.setButtonType(ButtonType.BORDERED_IMPORTANT);
        doneClickRegistration = doneButton.addClickHandler(this);
        clearDoneStack.addMember(doneButton);
        toolbar.addMember(clearDoneStack);

        pickerFrameElem = document.createDivElement();
        pickerFrameWrapperElem = document.createDivElement();
        pickerFrameWrapperElem.setClassName(CSS.pickerFrameWrapperClass());
        pickerFrameElem = document.createDivElement();
        pickerFrameElem.setClassName(CSS.pickerFrameClass());
        topCoverElem = document.createDivElement();
        topCoverElem.setClassName(CSS.topPickerCoverClass());
        pickerFrameElem.appendChild(topCoverElem);
        centerCoverElem = document.createDivElement();
        centerCoverElem.setClassName(CSS.centerPickerCoverClass());
        pickerFrameElem.appendChild(centerCoverElem);
        bottomCoverElem = document.createDivElement();
        bottomCoverElem.setClassName(CSS.bottomPickerCoverClass());
        pickerFrameElem.appendChild(bottomCoverElem);
        pickerDialsWrapperElem = document.createDivElement();
        pickerDialsWrapperElem.setClassName(CSS.pickerDialsWrapperClass());
        pickerFrameElem.appendChild(pickerDialsWrapperElem);
        pickerFrameWrapperElem.appendChild(pickerFrameElem);

        self.getElement().appendChild(pickerWrapperElem);
        self._add(toolbar, pickerWrapperElem);
        pickerWrapperElem.appendChild(pickerFrameWrapperElem);

        self._sinkAnimationEndEvent();
        self.sinkEvents((TouchEvent.isSupported() ? Event.ONTOUCHSTART : Event.ONMOUSEDOWN) | Event.ONCLICK);
    }

    @Override
    AutoTestLocatable getChildFromLocatorSubstring(Picker2 self, String substring, int index, List<String> locatorArray, GetAttributeConfiguration configuration) {
        if ("cancelButton".equals(substring)) return cancelButton;
        if ("clearButton".equals(substring)) return clearButton;
        if ("doneButton".equals(substring)) return doneButton;
        return super.getChildFromLocatorSubstring(self, substring, index, locatorArray, configuration);
    }

    void setDials(Picker2 self, PickerDial[] oldDials, PickerDial... dials) {
        if (oldDials != dials) {
            final Set<PickerDial> removedDials = new HashSet<PickerDial>();
            if (oldDials != null) removedDials.addAll(Arrays.asList(oldDials));
            final Set<PickerDial> newDials = new HashSet<PickerDial>();
            if (dials != null) newDials.addAll(Arrays.asList(dials));

            if (oldDials != null) {
                for (final PickerDial dial : oldDials) {
                    self.remove(dial);
                    newDials.remove(dial);
                }
            }
            if (dials != null) {
                for (final PickerDial dial : dials) {
                    self._add(dial, pickerDialsWrapperElem);
                    removedDials.remove(dial);
                }
            }
            if (selectedRecords == null) selectedRecords = new HashMap<PickerDial, Record>();
            else {
                for (final PickerDial removedDial : removedDials) {
                    selectedRecords.remove(removedDial);
                }
            }
            for (final PickerDial newDial : newDials) {
                selectedRecords.put(newDial, newDial.getSelectedRecord());
            }
        }
    }

    @Override
    void setShowClearButton(Picker2 self, Boolean showClearButton) {
        clearButton.setVisible(Canvas._booleanValue(showClearButton, true));
    }

    @Override
    void destroyPopup(Picker2 self) {
        if (doneClickRegistration != null) {
            doneClickRegistration.removeHandler();
            doneClickRegistration = null;
        }
        if (clearClickRegistration != null) {
            clearClickRegistration.removeHandler();
            clearClickRegistration = null;
        }
        if (cancelClickRegistration != null) {
            cancelClickRegistration.removeHandler();
            cancelClickRegistration = null;
        }
    }

    @Override
    void onBrowserEvent(Picker2 self, Event event) {
        final Element targetElem = EventUtil.getTargetElem(event);
        if (targetElem != null) {
            switch (event.getTypeInt()) {
                case Event.ONMOUSEDOWN:
                case Event.ONTOUCHSTART:
                    final PickerDial[] dials = self.getDials();
                    if (dials != null && dials.length != 0 && pickerFrameElem.isOrHasChild(targetElem)) {
                        final int clientX = (event.getTypeInt() == Event.ONTOUCHSTART ? event.getTouches().get(0).getClientX() : event.getClientX());

                        // Find the dial that the user has pressed/touched.
                        activeDial = null;
                        for (int i = 0; i < dials.length; ++i) {
                            final PickerDial dial = dials[i];
                            if (clientX <= dial.getElement().getAbsoluteRight()) {
                                activeDial = dial;
                                break;
                            }
                        }

                        if (activeDial != null) {
                            DOM.setCapture(activeDial.getElement());
                            activeDial.onBrowserEvent(event);
                        }
                    }
                    return;
            }

            final String eventType = event.getType();
            if (DOMConstants.INSTANCE.getAnimationEndEventType().equals(eventType)) {
                if (pickerWrapperElem.equals(targetElem)) {
                    final AnimationEndEvent aeEvent = event.cast();
                    final String animationName = aeEvent.getAnimationName();
                    if (CSS.pickerFadeInAnimationName().equals(animationName)) {
                        self._onShown();
                    } else if (CSS.pickerFadeOutAnimationName().equals(animationName)) {
                        self._onHidden();
                        if (selectedRecords != null) {
                            for (final Map.Entry<PickerDial, Record> e : selectedRecords.entrySet()) {
                                final PickerDial dial = e.getKey();
                                final Record priorSelectedRecord = e.getValue();
                                if (!dial.isSelected(priorSelectedRecord)) {
                                    dial.selectSingleRecord(priorSelectedRecord);
                                    assert dial.getSelectedRecord() == priorSelectedRecord;
                                    dial._refreshRows();
                                    assert dial.getSelectedRecord() == priorSelectedRecord;
                                }
                            }
                        }
                        if (self._isHidden()) {
                            PickerHiddenEvent.fire(self);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        final Object source = event.getSource();
        if (doneButton.equals(source)) {
            self.hide();
            if (selectedRecords != null) {
                final PickerDial[] dials = self.getDials();
                if (dials != null) {
                    for (final PickerDial dial : dials) {
                        selectedRecords.put(dial, dial.getSelectedRecord());
                    }
                }
            }
            ValuesSelectedEvent.fire(self, self.getSelectedValues());
        } else if (clearButton.equals(source)) {
            self.hide();
            ValuesSelectedEvent.fire(self, null);
        } else if (cancelButton.equals(source)) {
            self.hide();
        }
    }
}
