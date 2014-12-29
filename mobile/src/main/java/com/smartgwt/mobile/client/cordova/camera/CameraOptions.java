package com.smartgwt.mobile.client.cordova.camera;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.cordova.camera.types.DestinationType;
import com.smartgwt.mobile.client.cordova.camera.types.EncodingType;
import com.smartgwt.mobile.client.cordova.camera.types.MediaType;
import com.smartgwt.mobile.client.cordova.camera.types.PictureSourceType;
import com.smartgwt.mobile.client.util.EnumUtil;

public class CameraOptions extends JavaScriptObject {

    public static native CameraOptions create() /*-{
        return {
            quality: 75,
            destinationType: @com.smartgwt.mobile.client.cordova.camera.types.DestinationType::BASE64_ENCODED_STRING.@com.smartgwt.mobile.client.cordova.camera.types.DestinationType::_getCode()(),
            sourceType: @com.smartgwt.mobile.client.cordova.camera.types.PictureSourceType::CAMERA.@com.smartgwt.mobile.client.cordova.camera.types.PictureSourceType::_getCode()(),
            allowEdit: false,
            encodingType: @com.smartgwt.mobile.client.cordova.camera.types.EncodingType::JPEG.@com.smartgwt.mobile.client.cordova.camera.types.EncodingType::_getCode()(),
            mediaType: @com.smartgwt.mobile.client.cordova.camera.types.MediaType::PICTURE.@com.smartgwt.mobile.client.cordova.camera.types.MediaType::_getCode()(),
            popoverOptions: null,
            saveToPhotoAlbum: false
        };
    }-*/;

    protected CameraOptions() {}

    public final native int getQuality() /*-{
        return this.quality;
    }-*/;

    public final native void setQuality(int quality) /*-{
        this.quality = Math.max(0, Math.min(quality, 100));
    }-*/;

    @SGWTInternal
    public final native int _getDestinationTypeCode() /*-{
        return this.destinationType;
    }-*/;

    public final DestinationType getDestinationType() {
        return EnumUtil._getEnum(DestinationType.values(), _getDestinationTypeCode());
    }

    public final native void setDestinationType(DestinationType destinationType) /*-{
        if (destinationType == null) throw @java.lang.NullPointerException::new()();
        this.destinationType = destinationType.@com.smartgwt.mobile.client.cordova.camera.types.DestinationType::_getCode()();
    }-*/;

    @SGWTInternal
    public final native int _getSourceTypeCode() /*-{
        return this.sourceType;
    }-*/;

    public final PictureSourceType getSourceType() {
        return EnumUtil._getEnum(PictureSourceType.values(), _getSourceTypeCode());
    }

    public final native void setSourceType(PictureSourceType sourceType) /*-{
        if (sourceType == null) throw @java.lang.NullPointerException::new()();
        this.sourceType = sourceType.@com.smartgwt.mobile.client.cordova.camera.types.PictureSourceType::_getCode()();
    }-*/;

    public final native boolean getAllowEdit() /*-{
        return this.allowEdit;
    }-*/;

    public final native void setAllowEdit(boolean allowEdit) /*-{
        this.allowEdit = allowEdit;
    }-*/;

    @SGWTInternal
    public final native int _getEncodingTypeCode() /*-{
        return this.encodingType;
    }-*/;

    public final EncodingType getEncodingType() {
        return EnumUtil._getEnum(EncodingType.values(), _getEncodingTypeCode());
    }

    public final native void setEncodingType(EncodingType encodingType) /*-{
        if (encodingType == null) throw @java.lang.NullPointerException::new()();
        this.encodingType = encodingType.@com.smartgwt.mobile.client.cordova.camera.types.EncodingType::_getCode()();
    }-*/;

    @SGWTInternal
    public final native int _getMediaTypeCode() /*-{
        return this.mediaType;
    }-*/;

    public final MediaType getMediaType() {
        return EnumUtil._getEnum(MediaType.values(), _getMediaTypeCode());
    }

    public final native void setMediaType(MediaType mediaType) /*-{
        if (mediaType == null) throw @java.lang.NullPointerException::new()();
        this.mediaType = mediaType.@com.smartgwt.mobile.client.cordova.camera.types.MediaType::_getCode()();
    }-*/;

    public final native boolean getSaveToPhotoAlbum() /*-{
        return this.saveToPhotoAlbum;
    }-*/;

    public final native void setSaveToPhotoAlbum(boolean saveToPhotoAlbum) /*-{
        this.saveToPhotoAlbum = saveToPhotoAlbum;
    }-*/;
}
