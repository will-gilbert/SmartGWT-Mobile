package com.smartgwt.mobile.client.types;

public enum ImageStyle implements ValueEnum {

    NORMAL("normal"),
    CENTER("center"),
    STRETCH("stretch");

    private final String value;

    private ImageStyle(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
