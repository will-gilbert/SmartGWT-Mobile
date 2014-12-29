package com.smartgwt.mobile.client.widgets.form.fields;

public class AutoFitTextAreaItem extends TextAreaItem {

    public AutoFitTextAreaItem(String name, String title) {
        super(name, title);
        setAutoFit(true);
    }

    public AutoFitTextAreaItem(String name, String title, String placeholder) {
        super(name, title, placeholder);
        setAutoFit(true);
    }

    @Override
    public void setAutoFit(Boolean autoFit) {
        assert autoFit == null || autoFit.booleanValue() : "Auto fit shouldn't be disabled when using `AutoFitTextAreaItem'.";
        if (autoFit == null || autoFit.booleanValue()) super.setAutoFit(autoFit);
    }
}
