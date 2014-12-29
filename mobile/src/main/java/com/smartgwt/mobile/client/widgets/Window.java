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

package com.smartgwt.mobile.client.widgets;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.test.AutoTestLocatable;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.widgets.Popup;
import com.smartgwt.mobile.client.internal.widgets.PopupManager;
import com.smartgwt.mobile.client.internal.widgets.ScreenSpan;
import com.smartgwt.mobile.client.internal.widgets.WindowImpl;
import com.smartgwt.mobile.client.internal.widgets.events.PopupDismissedEvent;
import com.smartgwt.mobile.client.internal.widgets.events.PopupDismissedHandler;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.theme.WindowCssResource;
import com.smartgwt.mobile.client.types.AnimationEffect;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.events.CloseClickEvent;
import com.smartgwt.mobile.client.widgets.events.CloseClickHandler;
import com.smartgwt.mobile.client.widgets.events.HasCloseClickHandlers;

public class Window extends Popup implements HasCloseClickHandlers {

    @SGWTInternal
    public static final WindowCssResource _CSS = ThemeResources.INSTANCE.windowCSS();

    private transient HandlerRegistration modalMaskClickRegistration;
    private transient Element backgroundElem;
    private transient Element headerElem;
    private transient Element bodyElem;
    private transient HandlerRegistration popupDismissedRegistration;

    private transient final WindowImpl impl;

    private AnimationEffect animateShowEffect;
    private String showAnimationName;
    private AnimationEffect animateHideEffect;
    private String hideAnimationName;

    private boolean dismissOnOutsideClick;

    private String backgroundStyle = _CSS.windowBackgroundClass();

    private boolean showHeader;
    private Label headerLabel;

    private String bodyStyle;

    protected Window(WindowImpl impl) {
        this.impl = impl;

        super._setModalMaskStyle(_CSS.modalMaskClass());
        super._setHistoryMarker(";window");
        internalSetDismissOnOutsideClick(true);

        backgroundElem = Document.get().createDivElement();
        backgroundElem.setClassName(backgroundStyle);
        getElement().appendChild(backgroundElem);
        internalSetAnimateShowEffect(impl.getDefaultAnimateShowEffect(this));
        internalSetAnimateHideEffect(impl.getDefaultAnimateHideEffect(this));

        headerElem = Document.get().createDivElement();
        headerElem.setClassName(_CSS.windowHeaderClass());
        backgroundElem.appendChild(headerElem);
        headerLabel = new Label();
        headerLabel.setStyleName(_CSS.windowHeaderLabelClass());
        add(headerLabel, headerElem.<com.google.gwt.user.client.Element>cast());
        internalSetShowHeader(true);

        bodyElem = Document.get().createDivElement();
        bodyElem.setClassName(_CSS.windowBodyClass());
        backgroundElem.appendChild(bodyElem);

        impl.init(this);
    }

    public Window() {
        this(GWT.<WindowImpl>create(WindowImpl.class));
    }

    @Override
    protected void _destroyPopup() {
        if (modalMaskClickRegistration != null) {
            modalMaskClickRegistration.removeHandler();
            modalMaskClickRegistration = null;
        }
        if (popupDismissedRegistration != null) {
            popupDismissedRegistration.removeHandler();
            popupDismissedRegistration = null;
        }
        impl.destroyImpl(this);
    }

    public final AnimationEffect getAnimateShowEffect() {
        return animateShowEffect;
    }

    private void internalSetAnimateShowEffect(AnimationEffect newAnimateShowEffect) {
        if (this.animateShowEffect != newAnimateShowEffect) {
            this.animateShowEffect = newAnimateShowEffect;
            impl.animateShowEffectChanged(this, newAnimateShowEffect);
        }
    }
    public void setAnimateShowEffect(AnimationEffect newAnimateShowEffect) {
        internalSetAnimateShowEffect(newAnimateShowEffect);
    }

    public final AnimationEffect getAnimateHideEffect() {
        return animateHideEffect;
    }

    private void internalSetAnimateHideEffect(AnimationEffect newAnimateHideEffect) {
        if (this.animateHideEffect != newAnimateHideEffect) {
            this.animateHideEffect = newAnimateHideEffect;
            impl.animateHideEffectChanged(this, newAnimateHideEffect);
        }
    }
    public void setAnimateHideEffect(AnimationEffect newAnimateHideEffect) {
        internalSetAnimateHideEffect(newAnimateHideEffect);
    }

    @SGWTInternal
    public final Element _getBackgroundElem() {
        return backgroundElem;
    }

    public final String getBackgroundStyle() {
        return backgroundStyle;
    }

    public void setBackgroundStyle(String newBackgroundStyle) {
        if (this.backgroundStyle != null) {
            backgroundElem.removeClassName(this.backgroundStyle);
        }
        this.backgroundStyle = newBackgroundStyle;
        if (newBackgroundStyle != null) {
            backgroundElem.addClassName(newBackgroundStyle);
        }
    }

    public final String getBodyStyle() {
        return bodyStyle;
    }

    public void setBodyStyle(String newBodyStyle) {
        if (this.bodyStyle != null) {
            bodyElem.removeClassName(this.bodyStyle);
        }
        this.bodyStyle = newBodyStyle;
        if (newBodyStyle != null) {
            bodyElem.addClassName(newBodyStyle);
        }
    }

    public final boolean getDismissOnOutsideClick() {
        return dismissOnOutsideClick;
    }

    private void setupModalMask(ScreenSpan modalMask) {
        if (modalMaskClickRegistration == null) {
            modalMaskClickRegistration = modalMask.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    assert Window.this.dismissOnOutsideClick;
                    Window.this.hide();
                }
            });
        }
    }
    private void internalSetDismissOnOutsideClick(boolean newDismissOnOutsideClick) {
        this.dismissOnOutsideClick = newDismissOnOutsideClick;
        if (newDismissOnOutsideClick) {
            final ScreenSpan modalMask = _getModalMask();
            if (modalMask != null) setupModalMask(modalMask);
        } else if (modalMaskClickRegistration != null) {
            modalMaskClickRegistration.removeHandler();
            modalMaskClickRegistration = null;
        }
    }
    public void setDismissOnOutsideClick(boolean newDismissOnOutsideClick) {
        internalSetDismissOnOutsideClick(newDismissOnOutsideClick);
    }

    @SGWTInternal
    public Element _getHeaderElem() {
        return headerElem;
    }

    @SuppressWarnings("unchecked")
    @SGWTInternal
    public final <I extends WindowImpl> I _getImpl() {
        return (I)impl;
    }

    @Override
    @SGWTInternal
    public com.google.gwt.user.client.Element _getInnerElement() {
        return bodyElem.cast();
    }

    public final boolean getIsModal() {
        return _getIsModal();
    }

    public void setIsModal(boolean newIsModal) {
        _setIsModal(newIsModal);
    }

    public final Canvas getModalMask() {
        return _getModalMask();
    }

    public final String getModalMaskStyle() {
        return _getModalMaskStyle();
    }

    public void setModalMaskStyle(String newModalMaskStyle) {
        _setModalMaskStyle(newModalMaskStyle);
    }

    @SGWTInternal
    public final String _getShowAnimationName() {
        return showAnimationName;
    }

    @SGWTInternal
    public void _setShowAnimationName(String newShowAnimationName) {
        assert newShowAnimationName == null || (!newShowAnimationName.isEmpty() && !newShowAnimationName.equals(this.hideAnimationName));
        final boolean changed = (this.showAnimationName == null ? newShowAnimationName != null : !this.showAnimationName.equals(newShowAnimationName));
        if (changed) {
            this.showAnimationName = newShowAnimationName;
            impl.showAnimationNameChanged(this, newShowAnimationName);
        }
    }

    @SGWTInternal
    public final String _getHideAnimationName() {
        return hideAnimationName;
    }

    @SGWTInternal
    public void _setHideAnimationName(String newHideAnimationName) {
        assert newHideAnimationName == null || (!newHideAnimationName.isEmpty() && !newHideAnimationName.equals(this.showAnimationName));
        final boolean changed = (this.hideAnimationName == null ? newHideAnimationName != null : !this.hideAnimationName.equals(newHideAnimationName));
        if (changed) {
            this.hideAnimationName = newHideAnimationName;
            impl.hideAnimationNameChanged(this, newHideAnimationName);
        }
    }

    public final boolean getShowHeader() {
        return showHeader;
    }

    private void internalSetShowHeader(boolean newShowHeader) {
        this.showHeader = newShowHeader;
        if (newShowHeader) _getBackgroundElem().removeClassName(_CSS.headerlessClass());
        else _getBackgroundElem().addClassName(_CSS.headerlessClass());
    }
    public void setShowHeader(boolean newShowHeader) {
        internalSetShowHeader(newShowHeader);
    }

    public final String getTitle() {
        return headerLabel.getContents();
    }

    public void setTitle(String newTitle) {
        headerLabel.setContents(newTitle);
    }

    @Override
    public Object _getAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration) {
        return super._getAttributeFromSplitLocator(locatorArray, configuration);
    }

    @Override
    public Object _getInnerAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration) {
        return super._getInnerAttributeFromSplitLocator(locatorArray, configuration);
    }

    @Override
    public AutoTestLocatable _getChildFromLocatorSubstring(String substring, int index, List<String> locatorArray, GetAttributeConfiguration configuration) {
        if ("title".equals(substring)) return headerLabel;
        return super._getChildFromLocatorSubstring(substring, index, locatorArray, configuration);
    }

    @Override
    @SGWTInternal
    protected ScreenSpan _makeModalMask() {
        final ScreenSpan modalMask = super._makeModalMask();
        if (dismissOnOutsideClick) {
            setupModalMask(modalMask);
        }
        return modalMask;
    }

    public void show() {
        PopupManager.requestShow(this);
    }

    @Override
    @SGWTInternal
    protected void _doShow() {
        impl.doShow(this);
    }

    public void hide() {
        PopupManager.requestHide(this);
    }

    @Override
    @SGWTInternal
    protected void _doHide() {
        impl.doHide(this);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        impl.onBrowserEvent(this, event);
    }

    @Override
    public HandlerRegistration addCloseClickHandler(CloseClickHandler handler) {
        if (popupDismissedRegistration == null) {
            popupDismissedRegistration = _addPopupDismissedHandler(new PopupDismissedHandler() {
                @Override
                public void _onPopupDismissed(PopupDismissedEvent event) {
                    assert Window.this == event.getSource();
                    final Window source = (Window)event.getSource();
                    assert source._isShown() || source._isHiding() || source._isHidden();
                    CloseClickEvent.fire(source);
                }
            });
        }
        return addHandler(handler, CloseClickEvent.getType());
    }
}
