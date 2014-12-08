package com.smartgwt.mobile.showcase.client.widgets.dialogs;

import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.BaseButton.ButtonType;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.Dialog;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;

public class CustomDialog extends ScrollablePanel {

    private Dialog customDialog;
    private Button myOkButton;
    private Button showDialogButton;

    public CustomDialog(String title) {
        super(title);

        customDialog = new Dialog("Buttons!");
        customDialog.setMessage("This dialog has a lot of buttons.");
        myOkButton = new Button("OK", ButtonType.ACTION_GREEN);
        myOkButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SC.say("You selected 'OK'.");
            }
        });
        customDialog.setButtons(myOkButton, Dialog.APPLY, Dialog.YES, Dialog.NO, Dialog.CANCEL);

        showDialogButton = new Button("Show Dialog");
        showDialogButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                customDialog.show();
            }
        });
        addMember(showDialogButton);
    }
}
