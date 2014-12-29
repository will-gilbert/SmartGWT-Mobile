package com.smartgwt.mobile.client.cordova.camera;

public final class Camera {

    public static native void getPicture(CameraSuccessFunction cameraSuccess, CameraErrorFunction cameraError, CameraOptions options) /*-{
        if ($wnd.navigator.camera == null) throw @java.lang.RuntimeException::new(Ljava/lang/String;)("The Cordova Camera API is not installed.");
        $wnd.navigator.camera.getPicture($entry(function (data) {
            if (cameraSuccess != null) {
                // Call the success function in a timeout to allow the image picker or popover
                // to fully close.
                // http://docs.phonegap.com/en/edge/cordova_camera_camera.md.html#camera.getPicture_ios_quirks
                setTimeout(function () {
                    cameraSuccess.@com.smartgwt.mobile.client.cordova.camera.CameraSuccessFunction::execute(Ljava/lang/String;)(data);
                }, 1);
            }
        }), $entry(function (errorMessage) {
            if (cameraError == null) {
                @com.smartgwt.mobile.client.util.SC::logWarn(Ljava/lang/String;)("navigator.camera.getPicture() failed: " + errorMessage);
            } else {
                setTimeout(function () {
                    cameraError.@com.smartgwt.mobile.client.cordova.camera.CameraErrorFunction::execute(Ljava/lang/String;)(errorMessage);
                }, 1);
            }
        }), options);
    }-*/;

    private Camera() {}
}
