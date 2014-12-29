package com.smartgwt.mobile.client.types;

public enum Encoding implements ValueEnum {

    MULTIPART("multipart"),
    NORMAL("normal");

    private final String value;

    private Encoding(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
