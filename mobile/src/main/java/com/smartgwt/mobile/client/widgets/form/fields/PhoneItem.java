package com.smartgwt.mobile.client.widgets.form.fields;

public class PhoneItem extends TextItem {

    public PhoneItem(String name) {
        super(name);
        init();
    }

    public PhoneItem(String name, String title) {
        super(name, title);
        init();
    }

    public PhoneItem(String name, String title, String placeholder) {
        super(name, title, placeholder);
        init();
    }

    private void init() {
        super.setBrowserInputType("tel");
    }
}
