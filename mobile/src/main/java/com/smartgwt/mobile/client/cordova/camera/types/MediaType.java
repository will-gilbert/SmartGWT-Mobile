package com.smartgwt.mobile.client.cordova.camera.types;

import com.smartgwt.mobile.client.internal.types.IntValueEnum;

public enum MediaType implements IntValueEnum {

    PICTURE(getCode("PICTURE", 0)),
    VIDEO(getCode("VIDEO", 1)),
    ALLMEDIA(getCode("ALLMEDIA", 2));

    private static native int getCode(String value, int defaultCode) /*-{
        if ($wnd.Camera == null || $wnd.Camera.MediaType == null) return defaultCode;
        var code = parseInt($wnd.Camera.MediaType[value]);
        return (isNaN(code) ? defaultCode : code);
    }-*/;

    private final int code;

    private MediaType(int code) {
        this.code = code;
    }

    @Override
    public final int _getCode() {
        return code;
    }
}
