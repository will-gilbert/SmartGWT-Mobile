package com.smartgwt.mobile.client.types;

public enum JSONCircularReferenceMode implements ValueEnum {

    MARKER("marker"),
    OMIT("omit"),
    PATH("path");

    private final String value;

    private JSONCircularReferenceMode(String value) {
        this.value = value;
    }

    @Override
    public final String getValue() {
        return value;
    }
}
