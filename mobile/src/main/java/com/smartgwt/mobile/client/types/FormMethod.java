package com.smartgwt.mobile.client.types;

public enum FormMethod implements ValueEnum {

    GET("GET"),
    POST("POST");

    private final String value;

    private FormMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
