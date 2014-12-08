package com.smartgwt.mobile.showcase.client.widgets.buttons;

import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.Header2;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.VLayout;
import com.smartgwt.mobile.showcase.client.resources.AppResources;

public class ActionButtons extends ScrollablePanel {
    
    String text = "<p>Action Buttons are more distinct buttons compared to Rounded Rectangular Buttons "
        + "and are used for actions that are more critical or destructive. Action Buttons are also commonly used in modal Action Sheets.";
    Panel infoPanel = new Panel();

    public ActionButtons() {
        super("Action Buttons");
        this.setWidth("100%");
        VLayout vlayout = new VLayout();
        vlayout.setWidth("100%");
        infoPanel.setStyleName("sc-rounded-panel");
        infoPanel.setContents(text+"<p><br/></p>");
        infoPanel.setMargin(10);
        vlayout.addMember(infoPanel);
        vlayout.addMember(new Header2("Various kinds of Action buttons"));
        VLayout actionButtonLayout = new VLayout();
        actionButtonLayout.setLayoutMargin(10);
        actionButtonLayout.setAlign(Alignment.CENTER);
        Button button = new Button("Default");
        button.setIcon(AppResources.INSTANCE.action(), false);
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Default button selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        actionButtonLayout.addMember(button);
        Button cancelButton = new Button("Cancel", Button.ButtonType.ACTION_CANCEL);
        cancelButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Cancel button selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        actionButtonLayout.addMember(cancelButton);
        Button deleteButton = new Button("Delete", Button.ButtonType.ACTION_DELETE);
        deleteButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Delete button selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        actionButtonLayout.addMember(deleteButton);
        Button greenButton = new Button("Green", Button.ButtonType.ACTION_GREEN);
        greenButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Green button selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        actionButtonLayout.addMember(greenButton);
        Button importantButton = new Button("Important", Button.ButtonType.ACTION_IMPORTANT);
        importantButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Important button selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        actionButtonLayout.addMember(importantButton);
        Button customTintButton1 = new Button("Custom Tint (#ff6666)");
        customTintButton1.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Custom Tint (#ff6666) button selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        customTintButton1.setButtonType(Button.ButtonType.ACTION_DEFAULT);
        customTintButton1.setIcon(AppResources.INSTANCE.location_pin(), true);
        customTintButton1.setTintColor("#ff6666");
        customTintButton1.setStretch(true);
        actionButtonLayout.addMember(customTintButton1);
        Button customTintButton2 = new Button("Custom Tint (#8fc060)");
        customTintButton2.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Custom Tint (#8fc060) button selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        customTintButton2.setButtonType(Button.ButtonType.ACTION_DEFAULT);
        customTintButton2.setIcon(AppResources.INSTANCE.location_pin(), true);
        customTintButton2.setStretch(true);
        customTintButton2.setTintColor("#8fc060");
        actionButtonLayout.addMember(customTintButton2);
        vlayout.addMember(actionButtonLayout);
        addMember(vlayout);
    }
}
