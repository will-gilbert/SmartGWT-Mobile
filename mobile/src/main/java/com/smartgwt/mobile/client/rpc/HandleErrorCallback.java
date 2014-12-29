package com.smartgwt.mobile.client.rpc;

import com.smartgwt.mobile.client.data.DSRequest;
import com.smartgwt.mobile.client.data.DSResponse;

public interface HandleErrorCallback {

    public void handleError(DSResponse response, DSRequest request);
}
