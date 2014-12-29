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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.i18n.SmartGwtMessages;
import com.smartgwt.mobile.client.internal.theme.AlertViewCssResource;
import com.smartgwt.mobile.client.internal.theme.InternalThemeResources;
import com.smartgwt.mobile.client.internal.types.PromptType;
import com.smartgwt.mobile.client.internal.util.UsernameAndPasswordCallback;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.types.DefaultDialogAction;
import com.smartgwt.mobile.client.util.BooleanCallback;
import com.smartgwt.mobile.client.util.IsCallback;
import com.smartgwt.mobile.client.util.ValueCallback;
import com.smartgwt.mobile.client.widgets.Dialog;
import com.smartgwt.mobile.client.widgets.events.CloseClickEvent;
import com.smartgwt.mobile.client.widgets.events.CloseClickHandler;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.PasswordItem;
import com.smartgwt.mobile.client.widgets.form.fields.TextItem;

@SGWTInternal
public class AlertView extends Dialog implements CloseClickHandler {

    public static AlertViewCssResource _CSS = ((InternalThemeResources)ThemeResources.INSTANCE).alertviewCSS();

    private transient HandlerRegistration closeClickRegistration;
    private transient Element outerWrapperElem;
    private transient Element wrapperElem;

    private final PromptType type;
    private final IsCallback userCallback;

    private DynamicForm form;
    private TextItem valueItem;
    private TextItem usernameItem;
    private PasswordItem passwordItem;

    protected AlertView(AlertViewImpl impl, PromptType type, IsCallback userCallback) {
        super(impl);
        super.setDismissOnOutsideClick(false);
        super._setHistoryMarker(";prompt");

        impl.initAlertView(this);

        closeClickRegistration = super.addCloseClickHandler(this);

        super._setModalMaskStyle(_CSS.alertViewScreenCoverClass());

        outerWrapperElem = Document.get().createDivElement();
        outerWrapperElem.setClassName(_CSS.alertViewOuterWrapperClass());
        getElement().appendChild(outerWrapperElem);

        wrapperElem = Document.get().createDivElement();
        wrapperElem.setClassName(_CSS.alertViewWrapperClass());
        outerWrapperElem.appendChild(wrapperElem);

        super.setBackgroundStyle(_CSS.alertViewClass());
        wrapperElem.appendChild(_getBackgroundElem());

        _getButtonsContainerElem().setClassName(_CSS.alertViewButtonsClass());


        assert type != null;
        this.type = type;
        this.userCallback = userCallback;

        if (type == PromptType.PROMPT_PLAIN_TEXT) {
            form = new DynamicForm();
            valueItem = new TextItem("value");
            valueItem.setShowTitle(false);
            form.setItems(valueItem);
        } else if (type == PromptType.PROMPT_SECURE_TEXT) {
            form = new DynamicForm();
            passwordItem = new PasswordItem("password");
            passwordItem.setShowTitle(false);
            form.setItems(passwordItem);
        } else if (type == PromptType.PROMPT_USERNAME_AND_PASSWORD) {
            form = new DynamicForm();
            usernameItem = new TextItem("username");
            usernameItem.setHint(SmartGwtMessages.INSTANCE.dialog_UserNameTitle());
            usernameItem.setShowTitle(false);
            passwordItem = new PasswordItem("password");
            passwordItem.setHint(SmartGwtMessages.INSTANCE.dialog_PasswordTitle());
            passwordItem.setShowTitle(false);
            form.setItems(usernameItem, passwordItem);
        }

        if (form != null) {
            add(form, _getInnerElement());
            _getInnerElement().appendChild(_getButtonsContainerElem());
        }
    }

    public AlertView(PromptType type, IsCallback userCallback) {
        this(GWT.<AlertViewImpl>create(AlertViewImpl.class), type, userCallback);
    }

    @Override
    @SGWTInternal
    protected void _destroyPopup() {
        if (closeClickRegistration != null) {
            closeClickRegistration.removeHandler();
            closeClickRegistration = null;
        }
        if (form != null) form.destroy();
        if (valueItem != null) {
            valueItem.setValue(null);
            valueItem.destroy();
        }
        if (usernameItem != null) {
            usernameItem.setValue(null);
            usernameItem.destroy();
        }
        if (passwordItem != null) {
            passwordItem.setValue(null);
            passwordItem.destroy();
        }
        super._destroyPopup();
    }

    @Override
    public void onCloseClick(CloseClickEvent event) {
        if (userCallback != null) {
            switch (type) {
                case NOTICE:
                case YES_OR_NO:
                    ((BooleanCallback)userCallback).execute(null);
                    break;
                case CONFIRM:
                    ((BooleanCallback)userCallback).execute(null);
                    break;
                case PROMPT:
                    break;
                case PROMPT_PLAIN_TEXT:
                case PROMPT_SECURE_TEXT:
                    ((ValueCallback)userCallback).execute(null);
                    break;
                case PROMPT_USERNAME_AND_PASSWORD:
                    ((UsernameAndPasswordCallback)userCallback).execute(null, null);
                    break;
            }
        }
        if (getDefaultAction() == DefaultDialogAction.DESTROY) {
            destroy();
        }
    }

    @Override
    protected void doDefaultAction() {
        // The overrides of okClick(), etc. should have called the user callback as appropriate.
        // Remove the closeClick registration so that the callback is not called twice.
        if (closeClickRegistration != null) {
            closeClickRegistration.removeHandler();
            closeClickRegistration = null;
        }
        super.doDefaultAction();
    }

    @Override
    @SuppressWarnings("incomplete-switch")
    protected void okClick() {
        if (userCallback != null) {
            switch (type) {
                case NOTICE:
                case CONFIRM:
                    ((BooleanCallback)userCallback).execute(Boolean.TRUE);
                    break;
                case PROMPT_PLAIN_TEXT:
                    ((ValueCallback)userCallback).execute(valueItem.getValueAsString());
                    break;
                case PROMPT_SECURE_TEXT:
                    ((ValueCallback)userCallback).execute(passwordItem.getValueAsString());
                    break;
                case PROMPT_USERNAME_AND_PASSWORD:
                    ((UsernameAndPasswordCallback)userCallback).execute(usernameItem.getValueAsString(), passwordItem.getValueAsString());
                    break;
            }
        }
        super.okClick();
    }

    @Override
    @SuppressWarnings("incomplete-switch")
    protected void cancelClick() {
        if (userCallback != null) {
            switch (type) {
                case CONFIRM:
                    ((BooleanCallback)userCallback).execute(Boolean.FALSE);
                    break;
                case PROMPT_PLAIN_TEXT:
                case PROMPT_SECURE_TEXT:
                    ((ValueCallback)userCallback).execute(null);
                    break;
                case PROMPT_USERNAME_AND_PASSWORD:
                    ((UsernameAndPasswordCallback)userCallback).execute(null, null);
                    break;
            }
        }
        super.cancelClick();
    }

    @Override
    @SuppressWarnings("incomplete-switch")
    protected void yesClick() {
        if (userCallback != null) {
            switch (type) {
                case YES_OR_NO:
                    ((BooleanCallback)userCallback).execute(Boolean.TRUE);
                    break;
            }
        }
        super.yesClick();
    }

    @Override
    @SuppressWarnings("incomplete-switch")
    protected void noClick() {
        if (userCallback != null) {
            switch (type) {
                case YES_OR_NO:
                    ((BooleanCallback)userCallback).execute(Boolean.FALSE);
                    break;
            }
        }
        super.yesClick();
    }
}
