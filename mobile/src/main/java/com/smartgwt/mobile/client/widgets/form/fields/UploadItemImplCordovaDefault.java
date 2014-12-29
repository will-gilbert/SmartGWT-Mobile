package com.smartgwt.mobile.client.widgets.form.fields;

import com.smartgwt.mobile.client.widgets.Dialog;

class UploadItemImplCordovaDefault extends UploadItemImplCordova {

    @Override
    Dialog createPictureSourceDialog() {
        final Dialog dialog = new Dialog();
        dialog.setButtons(takePhotoButton, chooseExistingButton, Dialog.CANCEL);
        return dialog;
    }

    @Override
    void showPictureSourceDialog() {
        pictureSourceDialog.show();
    }
}
