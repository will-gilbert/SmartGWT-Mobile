package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.client.cordova.camera.Camera;
import com.smartgwt.mobile.client.cordova.camera.CameraErrorFunction;
import com.smartgwt.mobile.client.cordova.camera.CameraOptions;
import com.smartgwt.mobile.client.cordova.camera.CameraSuccessFunction;
import com.smartgwt.mobile.client.cordova.camera.types.DestinationType;
import com.smartgwt.mobile.client.cordova.camera.types.PictureSourceType;
import com.smartgwt.mobile.client.internal.EventUtil;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.Dialog;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;

abstract class UploadItemImplCordova extends UploadItemImpl {

    private InputElement hiddenFileInputElem;
    protected InputElement buttonElem;
    private ImageElement previewImgElem;

    protected Dialog pictureSourceDialog;
    protected Button takePhotoButton, chooseExistingButton;
    private HandlerRegistration takePhotoClickRegistration, chooseExistingClickRegistration;

    @Override
    Element createElement(UploadItem self) {
        final Document document = Document.get();
        final DivElement elem = document.createDivElement();
        elem.setClassName(DynamicForm._CSS.uploadItemClass());
        hiddenFileInputElem = document.createFileInputElement();
        hiddenFileInputElem.getStyle().setDisplay(Style.Display.NONE);
        elem.appendChild(hiddenFileInputElem);
        buttonElem = document.createButtonInputElement();
        buttonElem.setValue("Choose File");
        elem.appendChild(buttonElem);
        previewImgElem = document.createImageElement();
        previewImgElem.setClassName(DynamicForm._CSS.uploadPreviewImageClass());
        previewImgElem.getStyle().setDisplay(Style.Display.NONE);
        elem.appendChild(previewImgElem);
        return elem;
    }

    @Override
    void create(final UploadItem self) {
        self.sinkEvents(Event.ONCLICK);

        final CameraSuccessFunction cameraSuccess = new CameraSuccessFunction() {
            @Override
            public void execute(String fileURI) {
                previewImgElem.getStyle().clearDisplay();
                previewImgElem.setSrc(fileURI);
                hiddenFileInputElem.setValue(fileURI);
            }
        };

        takePhotoButton = new Button("Take Photo");
        takePhotoClickRegistration = takePhotoButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                pictureSourceDialog.hide();
                final CameraErrorFunction cameraError = new CameraErrorFunction() {
                    @Override
                    public void execute(String errorMessage) {
                        String message = "An error occurred while trying to take a photo";
                        if (errorMessage == null || errorMessage.isEmpty()) message += ".";
                        else if (errorMessage.endsWith(".")) message += ": " + errorMessage;
                        else message += ": " + errorMessage + ".";
                        SC.say("Error", message);
                    }
                };
                final CameraOptions options = CameraOptions.create();
                options.setSourceType(PictureSourceType.CAMERA);
                options.setDestinationType(DestinationType.FILE_URI);
                try {
                    Camera.getPicture(cameraSuccess, cameraError, options);
                } catch (RuntimeException ex) {
                    SC.logWarn("Camera.getPicture() threw an exception: " + ex);
                }
            }
        });

        chooseExistingButton = new Button("Choose Existing");
        chooseExistingClickRegistration = chooseExistingButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                pictureSourceDialog.hide();
                final CameraErrorFunction cameraError = new CameraErrorFunction() {
                    @Override
                    public void execute(String errorMessage) {
                        if ("no image selected".equals(errorMessage)) return;

                        String message = "An error occurred while choosing an existing photo";
                        if (errorMessage == null || errorMessage.isEmpty()) message += ".";
                        else if (errorMessage.endsWith(".")) message += ": " + errorMessage;
                        else message += ": " + errorMessage + ".";
                        SC.say("Error", message);
                    }
                };
                final CameraOptions options = CameraOptions.create();
                options.setSourceType(PictureSourceType.PHOTOLIBRARY);
                options.setDestinationType(DestinationType.FILE_URI);
                try {
                    Camera.getPicture(cameraSuccess, cameraError, options);
                } catch (RuntimeException ex) {
                    SC.logWarn("Camera.getPicture() threw an exception: " + ex);
                }
            }
        });

        pictureSourceDialog = createPictureSourceDialog();
    }

    abstract Dialog createPictureSourceDialog();

    @Override
    void destroyImpl(UploadItem self) {
        pictureSourceDialog.destroy();
        if (chooseExistingClickRegistration != null) {
            chooseExistingClickRegistration.removeHandler();
            chooseExistingClickRegistration = null;
        }
        chooseExistingButton.destroy();
        if (takePhotoClickRegistration != null) {
            takePhotoClickRegistration.removeHandler();
            takePhotoClickRegistration = null;
        }
        takePhotoButton.destroy();
        super.destroyImpl(self);
    }

    @Override
    void setElement(UploadItem self, com.google.gwt.user.client.Element elem) {
        assert "DIV".equals(elem.getTagName());
        hiddenFileInputElem.setName(self.getFieldName());
    }

    @Override
    void clearElementValue(UploadItem self) {
        hiddenFileInputElem.setValue("");
        previewImgElem.getStyle().setDisplay(Style.Display.NONE);
        previewImgElem.setSrc("");
    }

    abstract void showPictureSourceDialog();

    @Override
    void onBrowserEvent(Event event) {
        final Element targetElem = EventUtil.getTargetElem(event);
        if (event.getTypeInt() == Event.ONCLICK) {
            if (targetElem != null && buttonElem.isOrHasChild(targetElem)) {
                if (pictureSourceDialog != null) showPictureSourceDialog();
            }
        }
    }
}
