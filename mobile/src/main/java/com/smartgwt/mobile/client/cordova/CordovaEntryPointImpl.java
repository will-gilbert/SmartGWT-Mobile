package com.smartgwt.mobile.client.cordova;

import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
class CordovaEntryPointImpl {

    void onModuleLoad(CordovaEntryPoint self) {
        self.onDeviceReady();
    }
}
