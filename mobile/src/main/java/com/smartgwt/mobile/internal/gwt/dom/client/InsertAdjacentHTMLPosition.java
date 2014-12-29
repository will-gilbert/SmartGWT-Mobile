package com.smartgwt.mobile.internal.gwt.dom.client;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.types.ValueEnum;

@SGWTInternal
public enum InsertAdjacentHTMLPosition implements ValueEnum {

    BEFORE_BEGIN("beforebegin"),
    AFTER_BEGIN("afterbegin"),
    BEFORE_END("beforeend"),
    AFTER_END("afterend");

    private final String value;

    private InsertAdjacentHTMLPosition(String value) {
        this.value = value;
    }

    public final String getValue() {
        return value;
    }
}
