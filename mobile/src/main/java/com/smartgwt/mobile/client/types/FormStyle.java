package com.smartgwt.mobile.client.types;

public enum FormStyle implements ValueEnum {

    STYLE1("style1"),
    STYLE2("style2");

    private final String value;

    private FormStyle(String value) {
        this.value = value;
    }

    @Override
    public final String getValue() {
        return value;
    }
}
