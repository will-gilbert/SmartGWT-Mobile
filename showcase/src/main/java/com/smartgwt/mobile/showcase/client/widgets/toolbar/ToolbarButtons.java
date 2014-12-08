package com.smartgwt.mobile.showcase.client.widgets.toolbar;

import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.IconAlign;
import com.smartgwt.mobile.client.types.NavigationDirection;
import com.smartgwt.mobile.client.widgets.HRWidget;
import com.smartgwt.mobile.client.widgets.Header1;
import com.smartgwt.mobile.client.widgets.NavigationButton;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.VLayout;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.mobile.showcase.client.resources.AppResources;

public class ToolbarButtons extends ScrollablePanel {

    String text = "<p>A toolbar contains various widgets and is used for representing a navigation bar as well " +
    " as a list of buttons or icons that perform actions related to the other controls in the view " +
    " The samples below illustrate various toolbar and button styles.</p>";
    Panel infoPanel = new Panel();

    public ToolbarButtons(String title) {
        super(title);
        this.setWidth("100%");
        VLayout vlayout = new VLayout();
        vlayout.setMembersMargin(7);
        vlayout.setWidth("100%");
        infoPanel.setStyleName("sc-rounded-panel");
        infoPanel.setContents(text+"<p><br/></p>");
        infoPanel.setMargin(10);
        vlayout.addMember(infoPanel);
        vlayout.addMember(new HRWidget());
        ToolStrip toolbar2 = new ToolStrip();
        toolbar2.setTintColor("#2d2d2d");
        ToolStripButton add = new ToolStripButton();
        add.setTintColor("#ff0000");
        add.setIcon(AppResources.INSTANCE.add(), true);
        add.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Add button selected.</b></p>";
                infoPanel.setContents(newText);
            }            
        });
        toolbar2.addButton(add);
        ToolStripButton save = new ToolStripButton("Save");
        save.setButtonType(ToolStripButton.ButtonType.BORDERED_IMPORTANT);
        save.setTintColor("#008040");
        save.setIcon(AppResources.INSTANCE.attachment(), true);
        save.setIconAlign(IconAlign.RIGHT);
        toolbar2.addButton(save);
        save.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Save button selected.</b></p>";
                infoPanel.setContents(newText);
            }            
        });

        ToolStripButton profile = new ToolStripButton("Profile");
        profile.setIcon(AppResources.INSTANCE.contacts(), true);
        profile.setInheritTint(true);
        toolbar2.addButton(profile);
        profile.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Profile button selected.</b></p>";
                infoPanel.setContents(newText);
            }            
        });
        vlayout.addMember(toolbar2);
        vlayout.addMember(new HRWidget());
        ToolStrip toolbar4 = new ToolStrip();
        toolbar4.setAlign(Alignment.JUSTIFY);
        ToolStripButton open = new ToolStripButton("Open");
        open.setButtonType(ToolStripButton.ButtonType.BORDERED_IMPORTANT);
        open.setIcon(AppResources.INSTANCE.bookmarks(), true);
        open.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Open button selected.</b></p>";
                infoPanel.setContents(newText);
            }            
        });
        toolbar4.addButton(open);
        ToolStripButton deleteB = new ToolStripButton("Delete");
        deleteB.setButtonType(ToolStripButton.ButtonType.BORDERED_WARNING);
        deleteB.setIcon(AppResources.INSTANCE.stop(), true, IconAlign.RIGHT);
        deleteB.setDisabled(true);
        toolbar4.addButton(deleteB);
        vlayout.addMember(toolbar4);
        vlayout.addMember(new HRWidget());
        ToolStrip toolbar6 = new ToolStrip();
        toolbar6.setAlign(Alignment.JUSTIFY);
        NavigationButton back = new NavigationButton("Back");
        back.setDirection(NavigationDirection.BACK);
        back.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Back button selected.</b></p>";
                infoPanel.setContents(newText);
            }            
        });
        toolbar6.addButton(back);
        ToolStripButton title1 = new ToolStripButton("Title");
        title1.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Title button selected.</b></p>";
                infoPanel.setContents(newText);
            }            
        });
        toolbar6.addButton(title1);
        NavigationButton next = new NavigationButton("Next");
        next.setDirection(NavigationDirection.FORWARD);
        next.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Next button selected.</b></p>";
                infoPanel.setContents(newText);
            }            
        });
        toolbar6.addButton(next);
        vlayout.addMember(toolbar6);
        vlayout.addMember(new HRWidget());
        ToolStrip toolbar7 = new ToolStrip();
        toolbar7.setAlign(Alignment.RIGHT);
        Header1 title2 = new Header1("Configure");
        title2.getElement().addClassName("sc-toolbar-center");
        toolbar7.addMember(title2);
        ToolStripButton done = new ToolStripButton("Done");
        toolbar7.addButton(done);
        done.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Done button selected.</b></p>";
                infoPanel.setContents(newText);
            }            
        });
        toolbar7.addButton(done);
        vlayout.addMember(toolbar7);
        ToolStrip toolbar8 = new ToolStrip();  
        toolbar8.setAlign(Alignment.JUSTIFY);
        ToolStripButton cog = new ToolStripButton();
        cog.setButtonType(ToolStripButton.ButtonType.PLAIN);
        cog.setIcon(AppResources.INSTANCE.cog(), true);
        cog.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Cog button selected.</b></p>";
                infoPanel.setContents(newText);
            }            
        });
        toolbar8.addButton(cog);
        ToolStripButton computer = new ToolStripButton();
        computer.setButtonType(ToolStripButton.ButtonType.PLAIN);
        computer.setIcon(AppResources.INSTANCE.computer(), true);
        computer.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Computer button selected.</b></p>";
                infoPanel.setContents(newText);
            }            
        });
        toolbar8.addButton(computer);
        ToolStripButton download = new ToolStripButton();
        download.setButtonType(ToolStripButton.ButtonType.PLAIN);
        download.setIcon(AppResources.INSTANCE.download(), true);
        download.setIconColor("green");
        download.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Download button selected.</b></p>";
                infoPanel.setContents(newText);
            }            
        });
        toolbar8.addButton(download);
        vlayout.addMember(toolbar8);
        this.addMember(vlayout);
    }
}
