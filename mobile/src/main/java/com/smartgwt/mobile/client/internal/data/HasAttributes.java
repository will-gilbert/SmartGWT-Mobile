package com.smartgwt.mobile.client.internal.data;

import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public interface HasAttributes {

    public Object getAttributeAsObject(String property);

    public void setAttribute(String property, Object value);
}
