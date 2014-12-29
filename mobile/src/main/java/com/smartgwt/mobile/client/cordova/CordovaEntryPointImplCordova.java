package com.smartgwt.mobile.client.cordova;

import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
class CordovaEntryPointImplCordova extends CordovaEntryPointImpl {

    @Override
    native void onModuleLoad(CordovaEntryPoint self) /*-{
        if ($wnd.isDeviceReady) self.@com.smartgwt.mobile.client.cordova.CordovaEntryPoint::onDeviceReady()();
        else {
            var listener = $entry(function () {
                $doc.removeEventListener("deviceready", listener, false);
                self.@com.smartgwt.mobile.client.cordova.CordovaEntryPoint::onDeviceReady()();
            });
            $doc.addEventListener("deviceready", listener, false);
        }
    }-*/;
}
