package com.smartgwt.mobile.client.internal.data;

import com.google.gwt.core.client.JavaScriptException;

public class StorageImplSafari extends StorageImpl {

    @Override
    public boolean isQuotaExceededException(JavaScriptException ex) {
        return "QUOTA_EXCEEDED_ERR".equals(ex.getName());
    }
}
