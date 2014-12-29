package com.smartgwt.mobile.client.types;

public enum JSONDateFormat implements ValueEnum {

    DATE_CONSTRUCTOR("dateConstructor"),
    XML_SCHEMA("xmlSchema");

    private final String value;

    private JSONDateFormat(String value) {
        this.value = value;
    }

    @Override
    public final String getValue() {
        return value;
    }
}
