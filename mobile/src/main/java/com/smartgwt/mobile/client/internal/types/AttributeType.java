package com.smartgwt.mobile.client.internal.types;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.types.ValueEnum;

@SGWTInternal
public enum AttributeType implements ValueEnum {
    COMPONENT("component"),
    ELEMENT("element"),
    VALUE("value"),
    IS_CLICKABLE("isClickable");

    private final String value;

    private AttributeType(String value) {
        this.value = value;
    }

    @Override
    public final String getValue() {
        return value;
    }
}