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

package com.smartgwt.mobile.client.internal.widgets;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.theme.PopupCssResource;
import com.smartgwt.mobile.client.internal.util.ElementUtil;
import com.smartgwt.mobile.client.internal.util.PopupHiddenCallback;
import com.smartgwt.mobile.client.internal.util.PopupShownCallback;
import com.smartgwt.mobile.client.internal.widgets.events.HasPopupDismissedHandlers;
import com.smartgwt.mobile.client.internal.widgets.events.PopupDismissedEvent;
import com.smartgwt.mobile.client.internal.widgets.events.PopupDismissedHandler;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.widgets.Canvas;

/**
 * A <code>Popup</code> is a widget with four discernible states: showing, shown, hiding, and hidden.
 * This class manages these states for popup-type widgets.
 */
@SGWTInternal
public abstract class Popup extends Canvas implements HasPopupDismissedHandlers {

    @SGWTInternal
    public static final PopupCssResource _CSS = ThemeResources.INSTANCE.popupCSS();

    public enum PopupState {
        SHOWING,
        //SHOWN, // !SHOWING && !HIDING && isAttached()
        HIDING
        //HIDDEN // !isAttached()
    };


    // The current state of the Popup.
    private transient PopupState state;

    // If destroy() has been called, but this Popup was in transition at the time (either it was
    // showing or hiding itself). If this Popup shows itself and shouldDestroy is true, then it
    // should hide itself. If this Popup hides itself and shouldDestroy is true, then it should
    // call internalDestroy().
    private transient boolean shouldDestroy = false;
    private transient PopupShownCallback shownCallback;
    private transient PopupHiddenCallback hiddenCallback;

    // The previous history item at the time when this Popup was shown
    private transient String origHistoryItem;
    // The history item added by this Popup
    private transient String newHistoryItem;
    // The handler registration for listening to history changes.
    private transient HandlerRegistration valueChangeRegistration;


    // A string that is appended to the history item when this popup is showing or shown, and
    // removed when hiding.
    private String historyMarker = ";popup";

    private boolean isModal;

    private ScreenSpan modalMask;
    private String modalMaskStyle = _CSS._popupModalMaskClass();

    protected Popup() {
        super(Document.get().createDivElement());
        // The purpose of the popup container is two-fold:
        // 1. It allows us to set a z-index on the combined modalMask/background element such
        //    that the application does not need to worry about making sure to set a z-index
        //    on the custom-styled background element. It also means that the framework can
        //    change what z-index settings are used without impacting applications.
        // 2. When a widget is added to GWT's RootLayoutPanel, inline styling of:
        //        position: fixed; top: 0px; right: 0px; bottom: 0px; left: 0px;
        //    .. is applied, and this cannot be cleared without resorting to some hacks, including
        //    clearing these properties in a timeout and possibly also needing to respond to
        //    the window resize event.
        super.setStyleName(_CSS._popupContainerClass());
    }

    @Override
    public final void destroy() {
        if (_isShowing() || _isShown()) {
            PopupManager.requestHide(this);
            shouldDestroy = true;
            return;
        } else if (_isHiding()) {
            shouldDestroy = true;
            return;
        }
        internalDestroy();
    }

    @SGWTInternal
    protected abstract void _destroyPopup();

    private void internalDestroy() {
        assert _isHidden();
        shouldDestroy = false;
        _destroyPopup();
        if (modalMask != null) {
            modalMask.destroy();
        }
        super.destroy();
    }

    @SGWTInternal
    public void _setHistoryMarker(String historyMarker) {
        this.historyMarker = historyMarker;
    }

    @SGWTInternal
    public final boolean _getIsModal() {
        return isModal;
    }

    @SGWTInternal
    public void _setIsModal(boolean newIsModal) {
        this.isModal = newIsModal;
        if (!newIsModal && modalMask != null && modalMask.isAttached()) {
            modalMask.removeFromParent();
            modalMask.getElement().removeClassName(_CSS._showingClass());
            modalMask.getElement().removeClassName(_CSS._hidingClass());
        }
    }

    @SGWTInternal
    public ScreenSpan _getModalMask() {
        return modalMask;
    }

    @SGWTInternal
    public final String _getModalMaskStyle() {
        return modalMaskStyle;
    }

    @SGWTInternal
    public void _setModalMaskStyle(String newModalMaskStyle) {
        this.modalMaskStyle = newModalMaskStyle;
        if (modalMask != null) {
            modalMask.setStyleName(newModalMaskStyle);
        }
    }

    @Override
    public Object _getAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration) {
        switch (configuration.getAttribute()) {
            case IS_CLICKABLE:
                if (!_isShown()) return false;
                break;
            default:
                break;
        }
        return super._getAttributeFromSplitLocator(locatorArray, configuration);
    }

    @SGWTInternal
    public final boolean _isShowing() {
        final boolean ret = (state == PopupState.SHOWING);
        assert !ret || isAttached();
        return ret;
    }

    @SGWTInternal
    public final boolean _isShown() {
        final boolean ret = (!_isShowing() && !_isHiding() && isAttached());
        assert !ret || state == null;
        return ret;
    }

    @SGWTInternal
    public final boolean _isHiding() {
        final boolean ret = (state == PopupState.HIDING);
        assert !ret || isAttached();
        return ret;
    }

    @SGWTInternal
    public final boolean _isHidden() {
        final boolean ret = !isAttached();
        assert !ret || state == null;
        return ret;
    }

    @SGWTInternal
    public final String _getStateAsString() {
        if (_isShowing()) return "showing";
        else if (_isShown()) return "shown";
        else if (_isHiding()) return "hiding";
        else {
            assert _isHidden();
            return "hidden";
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (_HISTORY_ENABLED) {
            valueChangeRegistration = History.addValueChangeHandler(new ValueChangeHandler<String>() {
                @Override
                public void onValueChange(ValueChangeEvent<String> event) {
                    assert _HISTORY_ENABLED;
                    if (origHistoryItem != null &&
                        origHistoryItem.equals(event.getValue()))
                    {
                        //com.smartgwt.mobile.client.util.SC.logEcho(Popup.this, "valueChange handler called. origHistoryItem = '" + origHistoryItem + "', newHistoryItem = '" + newHistoryItem + "'");
                        assert !_isHidden();
                        origHistoryItem = null;
                        newHistoryItem = null;
                        PopupManager.requestHide(Popup.this);
                    }
                }
            });
        }
    }

    @Override
    public void onUnload() {
        if (_HISTORY_ENABLED) {
            if (valueChangeRegistration != null) {
                valueChangeRegistration.removeHandler();
                valueChangeRegistration = null;
            }
            origHistoryItem = null;
            newHistoryItem = null;
        }
        super.onUnload();
    }

    @SGWTInternal
    protected ScreenSpan _makeModalMask() {
        final ScreenSpan modalMask = new ScreenSpan();
        modalMask.setStyleName(modalMaskStyle);
        return modalMask;
    }

    // This should only be called by PopupManager.
    final void show(PopupShownCallback shownCallback) {
        checkRep();
        assert _isHidden();
        assert shownCallback != null;

        if (_HISTORY_ENABLED) {
            origHistoryItem = History.getToken();
            newHistoryItem = origHistoryItem + historyMarker;
            History.newItem(newHistoryItem, false);
        }

        //com.smartgwt.mobile.client.util.SC.logEcho(this, "Popup.show(PopupShownCallback) called. origHistoryItem = '" + origHistoryItem + "', newHistoryItem = '" + newHistoryItem + "'");

        state = PopupState.SHOWING;
        this.shownCallback = shownCallback;

        if (isModal) {
            if (modalMask == null) {
                modalMask = _makeModalMask();
            }
            insert(modalMask, getElement(), 0, true);
        }
        getElement().addClassName(_CSS._showingClass());
        RootLayoutPanel.get().add(this);
        checkRep();
        _doShow();
        checkRep();
    }

    /**
     * Called to allow the <code>Popup</code> implementation to perform its showing action.
     * After fully shown, the subclass must call {@link #_onShown()}.
     */
    @SGWTInternal
    protected abstract void _doShow();

    @SGWTInternal
    protected final void _onShown() {
        //com.smartgwt.mobile.client.util.SC.logEcho(this, "Popup._onShown() called. History.getToken() = '" + History.getToken() + "'");
        state = null;
        assert ElementUtil.hasClassName(getElement(), _CSS._showingClass());
        getElement().removeClassName(_CSS._showingClass());

        final PopupShownCallback shownCallback = this.shownCallback;
        this.shownCallback = null;
        checkRep();
        shownCallback.execute();
    }

    // This should only be called by PopupManager.
    final void hide(PopupHiddenCallback hiddenCallback) {
        checkRep();
        assert this._isShown();
        assert hiddenCallback != null;

        //com.smartgwt.mobile.client.util.SC.logEcho(this, "Popup.hide(PopupHiddenCallback) called. origHistoryItem = '" + origHistoryItem + "', newHistoryItem = '" + newHistoryItem + "', History.getToken() = '" + History.getToken() + "'");

        if (_HISTORY_ENABLED &&
            newHistoryItem != null &&
            newHistoryItem.equals(History.getToken()))
        {
            final String origHistoryItem = this.origHistoryItem;
            this.origHistoryItem = null;
            this.newHistoryItem = null;
            History.back();
        }

        state = PopupState.HIDING;
        this.hiddenCallback = hiddenCallback;

        getElement().addClassName(_CSS._hidingClass());
        checkRep();
        _doHide();
        checkRep();
    }

    /**
     * Called to allow the <code>Popup</code> implementation to perform its hiding action.
     * After fully hidden, the subclass must call {@link #_onHidden()}.
     */
    @SGWTInternal
    protected abstract void _doHide();

    @SGWTInternal
    protected final void _onHidden() {
        //com.smartgwt.mobile.client.util.SC.logEcho(this, "Popup._onHidden() called. History.getToken() = '" + History.getToken() + "'");
        state = null;
        RootLayoutPanel.get().remove(this);
        assert ElementUtil.hasClassName(getElement(), _CSS._hidingClass());
        getElement().removeClassName(_CSS._hidingClass());
        if (shouldDestroy) {
            internalDestroy();
        }

        final PopupHiddenCallback hiddenCallback = this.hiddenCallback;
        this.hiddenCallback = null;
        checkRep();
        hiddenCallback.execute();
    }

    @Override
    @SGWTInternal
    public HandlerRegistration _addPopupDismissedHandler(PopupDismissedHandler handler) {
        return addHandler(handler, PopupDismissedEvent.getType());
    }

    private void checkRep() {
        assert !isModal || _isHidden() || modalMask != null;

        assert _isShowing() == (shownCallback != null);
        assert _isShowing() == ElementUtil.hasClassName(getElement(), _CSS._showingClass());
        assert _isHiding() == (hiddenCallback != null);
        assert _isHiding() == ElementUtil.hasClassName(getElement(), _CSS._hidingClass());

        assert origHistoryItem == null || !origHistoryItem.equals(newHistoryItem);
        assert (valueChangeRegistration != null) == (_HISTORY_ENABLED && isAttached());
    }
}
