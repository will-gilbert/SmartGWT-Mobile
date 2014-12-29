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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.test.AutoTest;
import com.smartgwt.mobile.client.internal.test.AutoTestLocatable;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.util.Pair;
import com.smartgwt.mobile.client.internal.widgets.DialogImpl;
import com.smartgwt.mobile.client.theme.DialogCssResource;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.types.DefaultDialogAction;
import com.smartgwt.mobile.client.widgets.BaseButton.ButtonType;
import com.smartgwt.mobile.client.widgets.events.ButtonClickEvent;
import com.smartgwt.mobile.client.widgets.events.ButtonClickHandler;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.events.HasButtonClickHandlers;

public class Dialog extends Window implements HasButtonClickHandlers {

    @SGWTInternal
    public static final DialogCssResource _CSS = ThemeResources.INSTANCE.dialogCSS();

    /**
     * OK  Button object to fire dialog's "okClick()" method on click.
     */
    public static final Button OK = new Button("OK");
    /**
     * APPLY Button object to fire dialog's "applyClick()" method on click.
     */
    public static final Button APPLY = new Button("Apply");
    /**
     * YES Button object to fire dialog's "yesClick()" method on click
     */
    public static final Button YES = new Button("Yes");
    /**
     * NO  Button object to fire dialog's "noClick()" method on click.
     */
    public static final Button NO = new Button("No");
    /**
     * CANCEL  Button object to fire dialog's "cancelClick()" method on click.
     */
    public static final Button CANCEL = new Button("Cancel", ButtonType.ACTION_CANCEL);
    /**
     * DONE  Button object to fire dialog's "doneClick()" method on click.
     */
    public static final Button DONE = new Button("Done");

    private transient Element buttonsContainerElem;
    private transient List<HandlerRegistration> handlerRegistrations = new ArrayList<HandlerRegistration>();

    private DefaultDialogAction defaultAction = DefaultDialogAction.HIDE;

    private Label messageLabel;

    private Button[] buttons;

    protected Dialog(DialogImpl impl) {
        super(impl);
        super.setIsModal(true);

        messageLabel = new Label();
        messageLabel.setStyleName(_CSS.dialogMessageLabelClass());
        add(messageLabel, _getInnerElement());

        buttonsContainerElem = Document.get().createDivElement();
        buttonsContainerElem.setClassName(_CSS.dialogButtonsContainerClass());
        _getInnerElement().appendChild(buttonsContainerElem);

        impl.initDialog(this);
    }

    public Dialog() {
        this(GWT.<DialogImpl>create(DialogImpl.class));
    }

    public Dialog(String title) {
        this();
        super.setTitle(title);
    }

    @SGWTInternal
    public final Element _getButtonsContainerElem() {
        return buttonsContainerElem;
    }

    /**
     * The standard action that is executed by {@link #doDefaultAction()} when a built-in
     * button (e.g. {@link #YES}, {@link #CANCEL}) on the dialog is clicked.
     * 
     * @return the standard action to perform.  Default value:
     * {@link com.smartgwt.mobile.client.types.DefaultDialogAction#HIDE}.
     */
    public DefaultDialogAction getDefaultAction() {
        return defaultAction;
    }

    public void setDefaultAction(DefaultDialogAction defaultAction) {
        this.defaultAction = defaultAction;
    }

    public final String getMessage() {
        return messageLabel.getContents();
    }

    public void setMessage(String newMessage) {
        messageLabel.setContents(newMessage);
    }

    private final void internalSetButtons(Button... buttons) {
        if (isAttached()) throw new IllegalStateException();

        removeHandlerRegistrations();

        if (this.buttons != null) {
            for (int i = 0; i < this.buttons.length; ++i) {
                remove(this.buttons[i]);
            }
        }

        if (buttons == null) {
            this.buttons = null;
        } else {
            this.buttons = new Button[buttons.length];
            for (int i = 0; i < buttons.length; ++i) {
                final int finalIndex = i;
                Button button = buttons[i];

                if (button == Dialog.OK) {
                    button = new Button(Dialog.OK);
                    final Button finalButton = button;
                    handlerRegistrations.add(button.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            okClick();
                            ButtonClickEvent.fire(Dialog.this, finalButton, finalIndex);
                        }
                    }));
                } else if (button == Dialog.APPLY) {
                    button = new Button(Dialog.APPLY);
                    final Button finalButton = button;
                    handlerRegistrations.add(button.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            applyClick();
                            ButtonClickEvent.fire(Dialog.this, finalButton, finalIndex);
                        }
                    }));
                } else if (button == Dialog.YES) {
                    button = new Button(Dialog.YES);
                    final Button finalButton = button;
                    handlerRegistrations.add(button.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            yesClick();
                            ButtonClickEvent.fire(Dialog.this, finalButton, finalIndex);
                        }
                    }));
                } else if (button == Dialog.NO) {
                    button = new Button(Dialog.NO);
                    final Button finalButton = button;
                    handlerRegistrations.add(button.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            noClick();
                            ButtonClickEvent.fire(Dialog.this, finalButton, finalIndex);
                        }
                    }));
                } else if (button == Dialog.CANCEL) {
                    button = new Button(Dialog.CANCEL);
                    final Button finalButton = button;
                    handlerRegistrations.add(button.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            cancelClick();
                            ButtonClickEvent.fire(Dialog.this, finalButton, finalIndex);
                        }
                    }));
                } else if (button == Dialog.DONE) {
                    button = new Button(Dialog.DONE);
                    final Button finalButton = button;
                    handlerRegistrations.add(button.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            doneClick();
                            ButtonClickEvent.fire(Dialog.this, finalButton, finalIndex);
                        }
                    }));
                } else {
                    final Button finalButton = button;
                    handlerRegistrations.add(button.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            ButtonClickEvent.fire(Dialog.this, finalButton, finalIndex);
                        }
                    }));
                }

                add(button, buttonsContainerElem.<com.google.gwt.user.client.Element>cast());
                this.buttons[i] = button;
            }
        }
    }

    /**
     * Array of Buttons to show in the Dialog
     * <P>
     * The set of buttons to use is typically set by calling one of the shortcuts such as
     * {@link com.smartgwt.mobile.client.widgets.Dialog#OK OK} or 
     * {@link com.smartgwt.mobile.client.widgets.Dialog#CANCEL CANCEL}. A custom set of
     * buttons can be passed to these shortcuts methods via the "properties" argument, or to a
     * directly created Dialog. 
     * <P>
     * In both cases, a mixture of built-in buttons, custom buttons, and other components can
     * be passed.  Built-in buttons can be referred to as <code>Dialog.OK</code>.
     * <P>
     * If you want to use one of the default button appearance use css styles like 'cancel',
     * 'green' or 'delete'.
     *
     * @param buttons buttons Default value is null
     */
    public void setButtons(Button... buttons) {
        internalSetButtons(buttons);
    }

    @Override
    public AutoTestLocatable _getChildFromLocatorSubstring(String substring, int index, List<String> locatorArray, GetAttributeConfiguration configuration) {
        if (buttons != null && buttons.length > 0 && substring.startsWith("button[")) {
            final Pair<String, Map<String, String>> p = AutoTest.parseLocatorFallbackPath(substring);
            if (p != null) {
                assert "button".equals(p.getFirst());
                final Map<String, String> configObj = p.getSecond();
                final String valueOnly = configObj.get(AutoTest.FALLBACK_VALUE_ONLY_FIELD);
                if (valueOnly != null) {
                    int i = -1;
                    try {
                        i = Integer.parseInt(valueOnly, 10);
                    } catch (NumberFormatException ex) {}
                    if (i >= 0 && i < buttons.length) {
                        return buttons[i];
                    }
                }
            }
        }
        return super._getChildFromLocatorSubstring(substring, index, locatorArray, configuration);
    }

    private void removeHandlerRegistrations() {
        for (HandlerRegistration reg : handlerRegistrations) {
            reg.removeHandler();
        }
        handlerRegistrations.clear();
    }

    @Override
    @SGWTInternal
    protected void _destroyPopup() {
        removeHandlerRegistrations();
        super._destroyPopup();
    }

    /**
     * Runs the default action.
     * 
     * @see #getDefaultAction()
     */
    protected void doDefaultAction() {
        switch (defaultAction) {
            case HIDE:
                hide();
                break;
            case DESTROY:
                hide();
                destroy();
                break;
            case DO_NOTHING:
                break;
        }
    }

    /**
     * Called when the {@link #OK} button is clicked.  By default, calls {@link #doDefaultAction()}.
     */
    protected void okClick() {
        doDefaultAction();
    }

    /**
     * Called when the {@link #APPLY} button is clicked.  By default, calls {@link #doDefaultAction()}.
     */
    protected void applyClick() {
        doDefaultAction();
    }

    /**
     * Called when the {@link #YES} button is clicked.  By default, calls {@link #doDefaultAction()}.
     */
    protected void yesClick() {
        doDefaultAction();
    }

    /**
     * Called when the {@link #NO} button is clicked.  By default, calls {@link #doDefaultAction()}.
     */
    protected void noClick() {
        doDefaultAction();
    }

    /**
     * Called when the {@link #CANCEL} button is clicked.  By default, calls {@link #doDefaultAction()}.
     */
    protected void cancelClick() {
        doDefaultAction();
    }

    /**
     * Called when the {@link #DONE} button is clicked.  By default, calls {@link #doDefaultAction()}.
     */
    protected void doneClick() {
        doDefaultAction();
    }

    @Override
    public HandlerRegistration addButtonClickHandler(ButtonClickHandler handler) {
        return addHandler(handler, ButtonClickEvent.getType());
    }
}
