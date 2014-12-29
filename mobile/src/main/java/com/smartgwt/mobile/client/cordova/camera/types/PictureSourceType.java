package com.smartgwt.mobile.client.cordova.camera.types;

import com.smartgwt.mobile.client.internal.types.IntValueEnum;

public enum PictureSourceType implements IntValueEnum {

    PHOTOLIBRARY(getCode("PHOTOLIBRARY", 0)),
    CAMERA(getCode("CAMERA", 1)),
    SAVEDPHOTOALBUM(getCode("SAVEDPHOTOALBUM", 2));

    private static native int getCode(String value, int defaultCode) /*-{
        if ($wnd.Camera == null || $wnd.Camera.PictureSourceType == null) return defaultCode;
        var code = parseInt($wnd.Camera.PictureSourceType[value]);
        return (isNaN(code) ? defaultCode : code);
    }-*/;

    private final int code;

    private PictureSourceType(int code) {
        this.code = code;
    }

    @Override
    public final int _getCode() {
        return code;
    }
}
