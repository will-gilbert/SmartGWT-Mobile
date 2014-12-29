package com.smartgwt.mobile.client.types;

public enum SelectionStyle implements ValueEnum {

    NONE("none"),
    SINGLE("single"),
    MULTIPLE("multiple");

    private String value;

    private SelectionStyle(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
