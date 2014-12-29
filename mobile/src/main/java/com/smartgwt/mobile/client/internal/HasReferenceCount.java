package com.smartgwt.mobile.client.internal;

import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public interface HasReferenceCount {

    @SGWTInternal
    public void _addRef();

    @SGWTInternal
    public void _unref();
}
