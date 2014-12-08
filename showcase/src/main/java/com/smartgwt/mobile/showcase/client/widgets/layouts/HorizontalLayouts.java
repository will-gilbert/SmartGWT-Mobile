package com.smartgwt.mobile.showcase.client.widgets.layouts;

import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.Segment;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.layout.LayoutSpacer;
import com.smartgwt.mobile.client.widgets.layout.SegmentedControl;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.mobile.showcase.client.resources.AppResources;

public class HorizontalLayouts extends ScrollablePanel {

    public HorizontalLayouts(String title) {
        super(title);
        this.setWidth("100%");
        final HLayout hlayout = new HLayout();
        final HLayout hlayout2 = new HLayout();
        ToolStrip toolbar1 = new ToolStrip();
        toolbar1.setWidth("100%");
        toolbar1.setAlign(Alignment.CENTER);
        ToolStripButton toolbarButton12 = new ToolStripButton("Reverse");
        toolbarButton12.setTintColor("#ffc000");
        toolbarButton12.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hlayout.setReverseOrder(!hlayout.getReverseOrder());
            }
        });
        toolbar1.addButton(toolbarButton12);
        addMember(toolbar1);
        Button button1 = new Button("Button 1");
        button1.setTintColor("#ff6666");
        Button button2 = new Button("Button 2");
        button2.setTintColor("#8fc060");
        hlayout.addMember(new LayoutSpacer());
        hlayout.addMember(button1);
        hlayout.addMember(button2);
        hlayout.addMember(new LayoutSpacer());
        addMember(hlayout);
        ToolStrip toolbar2 = new ToolStrip();
        toolbar2.setTintColor("#339944");
        SegmentedControl control = new SegmentedControl();
        control.setInheritTint(true);
        Segment segment21 = new Segment("Center");
        segment21.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hlayout2.setAlign(Alignment.CENTER);
            }
        });
        Segment segment22 = new Segment("Justify");
        segment22.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hlayout2.setAlign(Alignment.JUSTIFY);
            }
        });
        Segment segment23 = new Segment("Align Left");
        segment23.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hlayout2.setAlign(Alignment.LEFT);
            }
        });
        Segment segment24 = new Segment("Align Right");
        segment24.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hlayout2.setAlign(Alignment.RIGHT);
            }
        });
        control.setSegments(segment21, segment22, segment23, segment24);
        control.selectSegment(0);
        toolbar2.addSpacer();
        toolbar2.addSegmentedControl(control);
        toolbar2.addSpacer();
        addMember(toolbar2);
        hlayout2.setAlign(Alignment.CENTER);
        ToolStripButton button21 = new ToolStripButton();
        button21.setIcon(AppResources.INSTANCE.refresh(), true);
        button21.setTintColor("#ff6666");
        ToolStripButton button22 = new ToolStripButton();
        button22.setIcon(AppResources.INSTANCE.bag(), true);
        button22.setTintColor("#8fc060");
        ToolStripButton button23 = new ToolStripButton();
        button23.setIcon(AppResources.INSTANCE.bullseye(), true);
        button23.setTintColor("#888800");
        ToolStripButton button24 = new ToolStripButton();
        button24.setIcon(AppResources.INSTANCE.chat(), true);
        button24.setTintColor("#880088");
        hlayout2.addMember(button21);
        hlayout2.addMember(button22);
        hlayout2.addMember(button23);
        hlayout2.addMember(button24);
        addMember(hlayout2);
    }
}
