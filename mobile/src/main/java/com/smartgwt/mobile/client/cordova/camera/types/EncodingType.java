package com.smartgwt.mobile.client.cordova.camera.types;

import com.smartgwt.mobile.client.internal.types.IntValueEnum;

public enum EncodingType implements IntValueEnum {

    JPEG(getCode("JPEG", 0)),
    PNG(getCode("PNG", 1));

    private static native int getCode(String value, int defaultCode) /*-{
        if ($wnd.Camera == null || $wnd.Camera.EncodingType == null) return defaultCode;
        var code = parseInt($wnd.Camera.EncodingType[value]);
        return (isNaN(code) ? defaultCode : code);
    }-*/;

    private final int code;

    private EncodingType(int code) {
        this.code = code;
    }

    @Override
    public final int _getCode() {
        return code;
    }
}
