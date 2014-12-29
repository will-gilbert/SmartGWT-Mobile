package com.smartgwt.mobile.client.internal.types;

import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public enum PromptType {

    /**
     * Shows a message, possibly a title, and an "OK" button. Callback type: {@link com.smartgwt.mobile.client.util.BooleanCallback}.
     *
     * <p>If the OK button is clicked then the callback value is {@link java.lang.Boolean#TRUE
     * Boolean.TRUE}. Otherwise, the callback value is <code>null</code>.
     */
    NOTICE,

    /**
     * Shows a message, possibly a title, and "Yes" and "No" buttons. Callback type: {@link com.smartgwt.mobile.client.util.BooleanCallback}.
     *
     * <p>If the Yes button is clicked, then the callback value is {@link java.lang.Boolean#TRUE
     * Boolean.TRUE}. If the No button is clicked, then the callback value is {@link java.lang.Boolean#FALSE
     * Boolean.FALSE}. Otherwise, the callback value is <code>null</code>.
     */
    YES_OR_NO,

    /**
     * Shows a message, possibly a title, and "OK" and "Cancel" buttons. Callback type: {@link com.smartgwt.mobile.client.util.BooleanCallback}.
     *
     * <p>If the OK button is clicked, then the callback value is {@link java.lang.Boolean#TRUE
     * Boolean.TRUE}. If the Cancel button is clicked, then the callback value is
     * <code>null</code>. Otherwise, the callback value is <code>null</code>.
     */
    CONFIRM,

    /**
     * Shows a message and possibly a title. There are no buttons displayed.
     * The program is to control the display duration by calling {@link com.smartgwt.mobile.client.util.SC#clearPrompt()}
     * or {@link com.smartgwt.mobile.client.widgets.Dialog#hide() Dialog.Prompt.hide()}
     * when the prompt no longer needs to be displayed.
     *
     * <p>This can be used to show a "loading" message for an indeterminate length of time.
     */
    PROMPT,

    /**
     * Shows a message, possibly a title, a {@link com.smartgwt.mobile.client.widgets.form.fields.TextItem}
     * for entering a value, and "OK" and "Cancel" buttons. Callback type:
     * {@link com.smartgwt.mobile.client.util.ValueCallback}.
     *
     * <p>If the OK button is clicked, then the callback value is the value entered into
     * the <code>TextItem</code>. Otherwise, the callback value is <code>null</code>.
     */
    PROMPT_PLAIN_TEXT,

    /**
     * Shows a message, possibly a title, a {@link com.smartgwt.mobile.client.widgets.form.fields.PasswordItem}
     * for entering a sensitive value, and "OK" and "Cancel" buttons. Callback type:
     * {@link com.smartgwt.mobile.client.util.ValueCallback}.
     *
     * <p>If the OK button is clicked, then the callback value is the value entered into
     * the <code>PasswordItem</code>. Otherwise, the callback value is <code>null</code>.
     */
    PROMPT_SECURE_TEXT,

    /**
     * Shows a message, possibly a title, a {@link com.smartgwt.mobile.client.widgets.form.fields.TextItem}
     * for entering a username, a {@link com.smartgwt.mobile.client.widgets.form.fields.PasswordItem}
     * for entering a password, and "OK" and "Cancel" buttons. Callback type:
     * {@link com.smartgwt.mobile.client.internal.util.UsernameAndPasswordCallback}.
     *
     * <p>If the OK button is clicked, then the callback value is the value entered into
     * the <code>TextItem</code>. Otherwise, the callback value is <code>null</code>.
     */
    PROMPT_USERNAME_AND_PASSWORD
}
