package com.smartgwt.mobile.showcase.client.widgets.dialogs;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.Popover;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.mobile.showcase.client.resources.AppResources;

public class PopoverDialog extends ScrollablePanel {

    private Popover popover;
    private Button confirmButton;
    private HandlerRegistration confirmClickRegistration;

    public PopoverDialog(String title) {
        super(title);
        this.setWidth("100%");
        confirmButton = new Button("Confirm");
        confirmButton.setTintColor("#fdfdfd");
        popover = new Popover("Test Popover", confirmButton);
        confirmClickRegistration = confirmButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                popover.hide();
            }
        });
        ToolStrip toolbar2 = new ToolStrip();
        toolbar2.setTintColor("#2d2d2d");
        final ToolStripButton toolbarButton24 = new ToolStripButton("Show Popover");
        toolbar2.addButton(toolbarButton24);
        toolbarButton24.setIcon(AppResources.INSTANCE.contacts(), true);
        toolbarButton24.setInheritTint(true);
        toolbarButton24.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                popover.show(toolbarButton24);
            }
        });
        addMember(toolbar2);
    }

    @Override
    public void destroy() {
        if (confirmClickRegistration != null) {
            confirmClickRegistration.removeHandler();
            confirmClickRegistration = null;
        }
        popover.destroy();
        super.destroy();
    }
}
