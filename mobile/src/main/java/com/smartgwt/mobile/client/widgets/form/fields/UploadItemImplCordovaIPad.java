package com.smartgwt.mobile.client.widgets.form.fields;

import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.Dialog;
import com.smartgwt.mobile.client.widgets.Popover;
import com.smartgwt.mobile.client.widgets.layout.VLayout;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperElement;

class UploadItemImplCordovaIPad extends UploadItemImplCordova {

    @Override
    Dialog createPictureSourceDialog() {
        takePhotoButton.getElement().removeClassName(Button._CSS.actionButtonClass());
        takePhotoButton.getElement().addClassName(Button._CSS.popoverActionButtonClass());
        chooseExistingButton.getElement().removeClassName(Button._CSS.actionButtonClass());
        chooseExistingButton.getElement().addClassName(Button._CSS.popoverActionButtonClass());

        final VLayout vlayout = new VLayout();
        vlayout.addMember(takePhotoButton);
        vlayout.addMember(chooseExistingButton);
        final Popover popover = new Popover() {
            @Override
            protected void _destroyPopup() {
                setChild(null);
                vlayout.destroy();
                super._destroyPopup();
            }
        };
        popover.setSmallFormFactor(true);
        popover.setChild(vlayout);
        return popover;
    }

    @Override
    void showPictureSourceDialog() {
        ((Popover)pictureSourceDialog).showForArea(buttonElem.<SuperElement>cast().getBoundingClientRect().asRectangle());
    }
}
