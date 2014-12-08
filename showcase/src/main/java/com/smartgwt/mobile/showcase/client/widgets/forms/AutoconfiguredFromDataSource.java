package com.smartgwt.mobile.showcase.client.widgets.forms;

import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.form.fields.PasswordItem;
import com.smartgwt.mobile.client.widgets.form.fields.TextItem;

public class AutoconfiguredFromDataSource extends ScrollablePanel {

    private DataSource boundFormDS;
    private DataSourceField givenNameField, familyNameField, emailField, passwordField, acceptToSField;

    private DynamicForm form;
    private TextItem givenNameItem, familyNameItem;
    private PasswordItem confirmPasswordItem;

    public AutoconfiguredFromDataSource(String title) {
        super(title);

        boundFormDS = new DataSource();
        givenNameField = new DataSourceField("givenName", "text");
        givenNameField.setTitle("Given Name");
        givenNameField.setHint("Given Name");
        familyNameField = new DataSourceField("familyName", "text");
        familyNameField.setTitle("Family Name");
        familyNameField.setHint("Family Name");
        emailField = new DataSourceField("email", "text");
        emailField.setEditorType("EmailItem");
        emailField.setTitle("Email Address");
        emailField.setHint("Email Address");
        passwordField = new DataSourceField("password", "password");
        passwordField.setTitle("Password");
        passwordField.setHint("Password");
        acceptToSField = new DataSourceField("acceptToS", "boolean");
        acceptToSField.setTitle("Accept Terms?");
        acceptToSField.setOnText("Yes");
        acceptToSField.setOffText("No");
        boundFormDS.setFields(givenNameField, familyNameField, emailField, passwordField, acceptToSField);

        form = new DynamicForm();
        form.setDataSource(boundFormDS);
        // Override "Given Name" to "First Name" and "Family Name" to "Last Name" because
        // English speakers are more accustomed to "First Name" and "Last Name".
        givenNameItem = new TextItem("givenName", "First Name");
        givenNameItem.setHint("First Name");
        familyNameItem = new TextItem("familyName", "Last Name");
        familyNameItem.setHint("Last Name");
        confirmPasswordItem = new PasswordItem("-confirmPassword", "Password (Confirm)");
        confirmPasswordItem.setHint("Password (Confirm)");
        form.setFields(
                // Use the overridden 'givenName' and 'familyName' form fields.
                givenNameItem,
                familyNameItem,

                // Autoconfiguration of the 'email' and 'password' form fields based on the corresponding
                // `DataSourceField's is fine.
                new FormItem("email"),
                new FormItem("password"),

                // An extra field that is only used client-side.
                confirmPasswordItem,

                // Autoconfiguration of the 'acceptToS' field is also fine.
                new FormItem("acceptToS"));
        addMember(form);
    }

    @Override
    public void reset() {
        super.reset();
        form.clearValues();
    }
}
