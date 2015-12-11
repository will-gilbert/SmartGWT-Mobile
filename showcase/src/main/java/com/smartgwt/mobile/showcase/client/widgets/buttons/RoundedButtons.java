package com.smartgwt.mobile.showcase.client.widgets.buttons;

import com.smartgwt.mobile.client.types.IconAlign;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.HRWidget;
import com.smartgwt.mobile.client.widgets.Header2;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.layout.VLayout;
import com.smartgwt.mobile.showcase.client.resources.AppResources;

public class RoundedButtons extends ScrollablePanel {

    public RoundedButtons() {
        super("Rounded Buttons");
        this.setWidth("100%");
        VLayout vlayout = new VLayout();
        vlayout.setWidth("100%");
        Panel infoPanel = new Panel();
        infoPanel.setStyleName("sc-rounded-panel");
        infoPanel.setContents("<p>Rounded Rectangle Buttons are buttons that are used in the view region. "
                + " The samples below illustrate various Rounded Rectangular buttons.");
        infoPanel.setMargin(10);
        vlayout.addMember(infoPanel);
        vlayout.addMember(new Header2("Rounded Rectangular buttons"));
        HLayout hlayout1 = new HLayout();
        hlayout1.setLayoutMargin(10);
        Button button11 = new Button("Button", Button.ButtonType.ROUNDED_RECTANGLE);
        hlayout1.addMember(button11);
        Button button12 = new Button("Button with Icon", Button.ButtonType.ROUNDED_RECTANGLE);
        button12.setIcon(AppResources.INSTANCE.cube_green(), false);
        hlayout1.addMember(button12);
        Button button13 = new Button("Disabled", Button.ButtonType.ROUNDED_RECTANGLE);
        button13.setDisabled(true);
        hlayout1.addMember(button13);
        Button button14 = new Button("Disabled with Icon", Button.ButtonType.ROUNDED_RECTANGLE);
        button14.setDisabled(true);
        button14.setIcon(AppResources.INSTANCE.contacts(), false, IconAlign.RIGHT);
        hlayout1.addMember(button14);
        vlayout.addMember(hlayout1);
        vlayout.addMember(new HRWidget());
        vlayout.addMember(new Header2("'Important' style Rounded Rectangular buttons"));
        HLayout hlayout2 = new HLayout();
        hlayout2.setLayoutMargin(10);
        Button button21 = new Button("Button", Button.ButtonType.ROUNDED_RECTANGLE_IMPORTANT);
        hlayout2.addMember(button21);
        Button button22 = new Button("Button with Icon", Button.ButtonType.ROUNDED_RECTANGLE_IMPORTANT);
        button22.setIcon(AppResources.INSTANCE.cube_green(), false);
        hlayout2.addMember(button22);
        Button button23 = new Button("Disabled", Button.ButtonType.ROUNDED_RECTANGLE_IMPORTANT);
        button23.setDisabled(true);
        hlayout2.addMember(button23);
        Button button24 = new Button("Disabled with Icon", Button.ButtonType.ROUNDED_RECTANGLE_IMPORTANT);
        button24.setDisabled(true);
        button24.setIcon(AppResources.INSTANCE.contacts(), false, IconAlign.RIGHT);
        hlayout2.addMember(button24);
        vlayout.addMember(hlayout2);
        addMember(vlayout);
    }
}
