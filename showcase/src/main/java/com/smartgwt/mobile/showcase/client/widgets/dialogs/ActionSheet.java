package com.smartgwt.mobile.showcase.client.widgets.dialogs;

import com.smartgwt.mobile.client.widgets.Dialog;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.mobile.showcase.client.resources.AppResources;

public class ActionSheet extends ScrollablePanel {

    public ActionSheet(String title) {
        super(title);
        this.setWidth("100%");
        final Dialog dialog = new Dialog("Format sdcard?");
        dialog.setButtons(Dialog.YES, Dialog.NO, Dialog.CANCEL);
        ToolStrip toolbar = new ToolStrip();
        toolbar.setTintColor("#2d2d2d");
        ToolStripButton toolbarButton = new ToolStripButton("Show action sheet");
        toolbarButton.setIcon(AppResources.INSTANCE.contacts(), true);
        toolbarButton.setInheritTint(true);
        toolbarButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialog.show();
            }
        });
        toolbar.addButton(toolbarButton);
        addMember(toolbar);
    }
}
