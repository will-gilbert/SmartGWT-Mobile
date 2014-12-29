package com.smartgwt.mobile.client.internal;

import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public interface SelectionItem {

    public Object getAttributeAsObject(String property);

    public void setAttribute(String property, Object value);
}
