package com.smartgwt.mobile.client.cordova;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public abstract class CordovaEntryPoint implements EntryPoint {

    private final CordovaEntryPointImpl impl = GWT.create(CordovaEntryPointImpl.class);

    @Override
    public final void onModuleLoad() {
        impl.onModuleLoad(this);
    }

    protected abstract void onDeviceReady();
}
