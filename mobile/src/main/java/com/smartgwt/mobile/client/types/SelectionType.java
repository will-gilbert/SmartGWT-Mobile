package com.smartgwt.mobile.client.types;

public enum SelectionType implements ValueEnum {

    BUTTON("button"),
    CHECKBOX("checkbox"),
    RADIO("radio");

    private final String value;

    private SelectionType(String value) {
        this.value = value;
    }

    @Override
    public final String getValue() {
        return value;
    }
}
