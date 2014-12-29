package com.smartgwt.mobile.client.widgets.form.fields;

public class UrlItem extends TextItem {

    public UrlItem(String name) {
        super(name);
        init();
    }

    public UrlItem(String name, String title) {
        super(name, title);
        init();
    }

    public UrlItem(String name, String title, String placeholder) {
        super(name, title, placeholder);
        init();
    }

    private void init() {
        super.setBrowserInputType("url");
    }
}
