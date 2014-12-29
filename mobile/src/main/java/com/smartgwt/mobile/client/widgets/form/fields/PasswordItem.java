package com.smartgwt.mobile.client.widgets.form.fields;

public class PasswordItem extends TextItem {

    public PasswordItem(String name) {
        super(name);
        init();
    }

    public PasswordItem(String name, String title) {
        super(name, title);
        init();
    }

    public PasswordItem(String name, String title, String placeholder) {
        super(name, title, placeholder);
        init();
    }

    private void init() {
        super.setBrowserInputType("password");
    }
}
