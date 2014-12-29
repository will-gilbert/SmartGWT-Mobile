package com.smartgwt.mobile.client.widgets.form.fields;

public class EmailItem extends TextItem {

    public EmailItem(String name) {
        super(name);
        init();
    }

    public EmailItem(String name, String title) {
        super(name, title);
        init();
    }

    public EmailItem(String name, String title, String placeholder) {
        super(name, title, placeholder);
        init();
    }

    private void init() {
        super.setBrowserInputType("email");
    }
}
