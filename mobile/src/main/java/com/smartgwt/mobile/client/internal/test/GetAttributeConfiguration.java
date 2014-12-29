package com.smartgwt.mobile.client.internal.test;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.types.AttributeType;

@SGWTInternal
public class GetAttributeConfiguration {
    private AttributeType attribute;

    public final AttributeType getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeType attribute) {
        this.attribute = attribute;
    }
}