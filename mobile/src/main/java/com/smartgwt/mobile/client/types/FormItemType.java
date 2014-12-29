package com.smartgwt.mobile.client.types;


public enum FormItemType implements ValueEnum {

    TEXT("text"),
    BOOLEAN("boolean"),
    FLOAT("float"),
    DATE("date");

    public final String value;

    private FormItemType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
