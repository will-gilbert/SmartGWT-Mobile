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

package com.smartgwt.mobile.client.util;

import com.google.gwt.core.client.GWT;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.i18n.SmartGwtMessages;
import com.smartgwt.mobile.client.internal.types.PromptType;
import com.smartgwt.mobile.client.internal.util.SCStaticImpl;
import com.smartgwt.mobile.client.internal.widgets.AlertView;
import com.smartgwt.mobile.client.types.DefaultDialogAction;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.Dialog;

/**
 * Various utility methods.
 */

//TODO INTERNAL NOTES
/*
1. See smartgwt-mobile-cvs/trunk/markup/popup/popup.html for css and sample markup that needs to be generated
   for the Alerts
2. Make sure you add the popup / alert css to smartgwt-mobile-cvs/trunk/main/src/com/smartgwt/mobile/client/theme/iOS.gwt.xml
3. Read the section on Alerts here : http://developer.apple.com/library/ios/#documentation/userexperience/conceptual/mobilehig/UIElementGuidelines/UIElementGuidelines.html
4. For now do not add icons for the various dialog boxes but design such that one can be added before the title region in the future
5. In the future when we support iPad UI components we'll add a more generic Dialog and Window class that allows users to build customized Dialogs / Windows.
   For the initial release we're only targeting mobile devices (and not tablets) and these specific helper methods will suffice
*/
public class SC {

    private static final SCStaticImpl IMPL = GWT.create(SCStaticImpl.class);

    private static Boolean promptAnimationsEnabled = null;

    static {
        IMPL.maybeInit();
    }

    @SGWTInternal
    public static void _maybeInit() {}

    @SGWTInternal
    public static boolean _getPromptAnimationsEnabled() {
        return Canvas._booleanValue(promptAnimationsEnabled, true);
    }

    /**
     * Sets whether to enable or disable showing/hiding animations on built-in prompt/say/warn
     * dialogs. Changes to this setting take effect with subsequent calls to say(), ask(),
     * confirm(), warn(), showPrompt(), and clearPrompt().
     *
     * @param enablePromptAnimations whether to enable or disable animations. If true or null,
     * then built-in prompt animations are enabled. Default value: <code>null</code>.
     */
    public static void enablePromptAnimations(Boolean enablePromptAnimations) {
        promptAnimationsEnabled = enablePromptAnimations;
    }

    /**
     * Show a modal dialog with a message, possibly an icon, and "OK" button. Intended for notifications which are not really warnings (default icon is less severe).
     *
     * @param message the message
     */
    public static void say(String message) {
        say(null, message);
    }

    /**
     * Show a modal dialog with a message, possibly an icon, and "OK" button. Intended for notifications which are not really warnings (default icon is less severe).
     *
     * @param title   the title of the message box
     * @param message the message
     */
    public static void say(String title, String message) {
        say(title, message, null);
    }


    /**
     * Show a modal dialog with a message, possibly an icon, and "OK" button. Intended for notifications which are not really warnings (default icon is less severe).
     * The callback will receive boolean true for an OK button click, or null if the Dialog is dismissed via the close button.
     *
     * @param message  the message
     * @param callback the callback to fire when the user dismisses the dialog.
     */
    public static void say(String message, BooleanCallback callback) {
        say(null, message, callback);
    }

    /**
     * Show a modal dialog with a message, possibly an icon, and "OK" button. Intended for notifications which are not really warnings (default icon is less severe).
     * The callback will receive boolean true for an OK button click, or null if the Dialog is dismissed via the close button.
     *
     * @param title    the title of the message box
     * @param message  the message
     * @param callback the callback to fire when the user dismisses the dialog.
     */
    public static void say(String title, String message, BooleanCallback callback) {
        final AlertView prompt = new AlertView(PromptType.NOTICE, callback);
        prompt.setTitle(title == null || (title = title.trim()).isEmpty() ? "&nbsp;" : title);
        prompt.setMessage(message);
        prompt.setButtons(Dialog.OK);
        prompt.setDefaultAction(DefaultDialogAction.DESTROY);
        prompt.show();
    }

    /**
     * Show a modal dialog with a message, possibly an icon, and "Yes" and "No" buttons. The callback will receive boolean true for an OK
     * button click, boolean false for a No button click, or null if the Dialog is dismissed via the close button.
     *
     * @param message  the message
     * @param callback the callback to fire when the user dismisses the dialog.
     */
    public static void ask(String message, BooleanCallback callback) {
        ask(null, message, callback);
    }

    /**
     * Show a modal dialog with a message, possibly an icon, and "Yes" and "No" buttons. The callback will receive boolean true for an OK
     * button click, boolean false for a No button click, or null if the Dialog is dismissed via the close button.
     *
     * @param title    the title of the message box
     * @param message  the message
     * @param callback the callback to fire when the user dismisses the dialog.
     */
    public static void ask(String title, String message, BooleanCallback callback) {
        final AlertView prompt = new AlertView(PromptType.YES_OR_NO, callback);
        prompt.setTitle(title == null || (title = title.trim()).isEmpty() ? "&nbsp;" : title);
        prompt.setMessage(message);
        prompt.setButtons(Dialog.YES, Dialog.NO);
        prompt.setDefaultAction(DefaultDialogAction.DESTROY);
        prompt.show();
    }


    /**
     * Show a modal dialog with a text entry box, asking the user to enter a value.
     * <p/>
     * As with other convenience methods that show Dialogs the dialog is shown
     * and the function immediately returns.  When the user responds, the provided callback is called.
     * <p/>
     * If the user clicks OK, the value typed in is passed to the callback (including the empty string ("") if nothing was entered.  If the
     * user clicks cancel, the value passed to the callback is null.
     * <p/>
     * <p/>
     * A default value for the text field can be passed via <code>properties.defaultValue</code>
     * <p/>
     * <p>Keyboard focus is automatically placed in the text entry field, and hitting the enter key is the equivalent of pressing OK.
     *
     * @param message  message to display
     * @param callback Callback to fire when the user clicks a button to dismiss the dialog. This has the single parameter 'value',
     *                 indicating the user entry, or null if cancel was pressed or the window closed
     */
    public static void askforValue(String message, ValueCallback callback) {
        askforValue(null, message, callback);
    }

    /**
     * Show a modal dialog with a text entry box, asking the user to enter a value.
     * <p/>
     * As with other convenience methods that show Dialogs the dialog is shown
     * and the function immediately returns.  When the user responds, the provided callback is called.
     * <p/>
     * If the user clicks OK, the value typed in is passed to the callback (including the empty string ("") if nothing was entered.  If the
     * user clicks cancel, the value passed to the callback is null.
     * <p/>
     * <p>Keyboard focus is automatically placed in the text entry field, and hitting the enter key is the equivalent of pressing OK.
     *
     * @param title    the title of the dialog
     * @param message  message to display
     * @param callback Callback to fire when the user clicks a button to dismiss the dialog. This has the single parameter 'value',
     *                 indicating the user entry, or null if cancel was pressed or the window closed
     */
    public static void askforValue(String title, String message, ValueCallback callback) {
        final AlertView prompt = new AlertView(PromptType.PROMPT_PLAIN_TEXT, callback);
        prompt.setTitle(title == null || (title = title.trim()).isEmpty() ? "&nbsp;" : title);
        prompt.setMessage(message);
        prompt.setButtons(Dialog.OK, Dialog.CANCEL);
        prompt.setDefaultAction(DefaultDialogAction.DESTROY);
        prompt.show();
    }


    /**
     * Show a modal prompt to the user.
     * <p>
     * <b>Note:</b> If this prompt is to be shown to the user during some slow logic, we advise
     * temporarily disabling prompt animations via {@link #enablePromptAnimations(Boolean) SC.enablePromptAnimations(false)},
     * calling this method, then using a {@link com.google.gwt.user.client.Timer} to kick off
     * the slow logic in a separate thread. In Mobile Safari, the timeout set on the <code>Timer</code>
     * should be more than 1ms (50ms recommended) to give the browser time to repaint the screen.
     * These steps ensure that the prompt is showing before the lengthy execution begins.
     *
     * @param message message to display
     * @see #clearPrompt()
     */
    public static void showPrompt(String message) {
        showPrompt(null, message);
    }

    /**
     * Show a modal prompt to the user.
     * <p>
     * <b>Note:</b> If this prompt is to be shown to the user during some slow logic, we advise
     * temporarily disabling prompt animations via {@link #enablePromptAnimations(Boolean) SC.enablePromptAnimations(false)},
     * calling this method, then using a {@link com.google.gwt.user.client.Timer} to kick off
     * the slow logic in a separate thread. In Mobile Safari, the timeout set on the <code>Timer</code>
     * should be more than 1ms (50ms recommended) to give the browser time to repaint the screen.
     * These steps ensure that the prompt is showing before the lengthy execution begins.
     *
     * @param title   the title of the dialog
     * @param message message to display
     * @see #clearPrompt()
     */
    private static AlertView prompt;
    public static void showPrompt(String title, String message) {
        prompt = new AlertView(PromptType.PROMPT, null);
        prompt.setTitle(title == null || (title = title.trim()).isEmpty() ? "&nbsp;" : title);
        prompt.setMessage(message);
        prompt.setDefaultAction(DefaultDialogAction.DESTROY);
        prompt.show();
    }

    /**
     * Clear the modal prompt being shown to the user.
     */
    public static void clearPrompt() {
        if (prompt != null) {
            prompt.destroy();
        }
    }

    /**
     * Show a modal dialog with a message, possibly an icon, and "OK" and "Cancel" buttons.
     * <p/>
     * The callback will receive boolean true for an OK button click, or null for a Cancel click or if the Dialog is dismissed via the close button.
     *
     * @param message  message to display
     * @param callback Callback to fire when the user clicks a button to dismiss the dialog.
     */
    public static void confirm(String message, BooleanCallback callback) {
        confirm(null, message, callback);
    }

    /**
     * Show a modal dialog with a message, possibly an icon, and "OK" and "Cancel" buttons.
     * <p/>
     * The callback will receive boolean true for an OK button click, or null for a Cancel click or if the Dialog is dismissed via the close button.
     *
     * @param title    the title of the dialog
     * @param message  message to display
     * @param callback Callback to fire when the user clicks a button to dismiss the dialog.
     */
    public static void confirm(String title, String message, BooleanCallback callback) {
        final AlertView prompt = new AlertView(PromptType.CONFIRM, callback);
        prompt.setTitle(title == null || (title = title.trim()).isEmpty() ? "&nbsp;" : title);
        prompt.setMessage(message);
        prompt.setButtons(Dialog.OK, Dialog.CANCEL);
        prompt.setDefaultAction(DefaultDialogAction.DESTROY);
        prompt.show();
    }

    /**
     * Show a modal dialog with a message, possibly an icon, and "OK" button.
     *
     * @param message the message
     */
    public static void warn(String message) {
        say(SmartGwtMessages.INSTANCE.dialog_WarnTitle(), message);
    }

    /**
     * Show a modal dialog with a message, possibly an icon, and "OK" button.
     * <p/>
     * The callback will receive boolean true for an OK button click, or null if the Dialog is dismissed via the close button.
     *
     * @param message  the message
     * @param callback Optional Callback to fire when the user dismisses the dialog.
     */
    public static void warn(String message, BooleanCallback callback) {
        say(SmartGwtMessages.INSTANCE.dialog_WarnTitle(), message, callback);
    }

    public static void logEcho(Object value) {
        IMPL.log("Log", value);
    }

    public static void logEcho(Object value, String message) {
        IMPL.log("Log", message, value);
    }

    public static void logWarn(String message) {
        logWarn(message, "Log");
    }

    public static void logWarn(String message, String category) {
        IMPL.warn(category, message);
    }
}
