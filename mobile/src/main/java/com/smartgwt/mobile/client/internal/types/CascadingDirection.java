package com.smartgwt.mobile.client.internal.types;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.types.ValueEnum;

@SGWTInternal
public enum CascadingDirection implements ValueEnum {

    UP("up"),
    DOWN("down");

    private final String value;

    private CascadingDirection(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
