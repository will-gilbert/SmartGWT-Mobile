package com.smartgwt.mobile.client.types;

public enum ValidationMode implements ValueEnum {

    FULL("full"),

    PARTIAL("partial");

    private final String value;

    private ValidationMode(String value) {
        this.value = value;
    }

    @Override
    public final String getValue() {
        return value;
    }
}
