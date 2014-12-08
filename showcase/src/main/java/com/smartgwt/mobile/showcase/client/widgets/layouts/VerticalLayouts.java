package com.smartgwt.mobile.showcase.client.widgets.layouts;

import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.VerticalAlignment;
import com.smartgwt.mobile.client.widgets.Header1;
import com.smartgwt.mobile.client.widgets.Header2;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.Segment;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.layout.Layout;
import com.smartgwt.mobile.client.widgets.layout.SegmentedControl;
import com.smartgwt.mobile.client.widgets.layout.VLayout;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.mobile.showcase.client.resources.AppResources;

public class VerticalLayouts extends ScrollablePanel {

    public VerticalLayouts(String title) {
        super(title);
        this.setWidth("100%");
        final VLayout vlayout = new VLayout();
        ToolStrip toolbar1 = new ToolStrip();
        toolbar1.setWidth("100%");
        toolbar1.setAlign(Alignment.CENTER);
        ToolStripButton toolbarButton12 = new ToolStripButton("Reverse");
        toolbarButton12.setTintColor("#ffc000");
        toolbarButton12.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                vlayout.setReverseOrder(!vlayout.getReverseOrder());
            }
        });
        toolbar1.addButton(toolbarButton12);
        addMember(toolbar1);
        HLayout hlayout = new HLayout();
        hlayout.setAlign(Alignment.CENTER);
        SegmentedControl segmentedControl1 = new SegmentedControl();
        Segment segment11 = new Segment("1");
        segment11.setIcon(AppResources.INSTANCE.chart(), true);
        Segment segment12 = new Segment("2");
        segment12.setIcon(AppResources.INSTANCE.settings(), true);
        segmentedControl1.setSegments(segment11, segment12);
        hlayout.addMember(segmentedControl1);
        vlayout.addMember(hlayout);
        vlayout.addMember(new Header1("child 1"));
        vlayout.addMember(new Header2("child 2"));
        addMember(vlayout);
        final Layout layout2 = new HLayout();
        ToolStrip toolbar2 = new ToolStrip();
        toolbar2.setTintColor("#339944");
        SegmentedControl control = new SegmentedControl();
        control.setInheritTint(true);
        Segment segment22 = new Segment("Top");
        segment22.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                layout2.setAlign(VerticalAlignment.TOP);
            }
        });
        Segment segment23 = new Segment("Center");
        segment23.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                layout2.setAlign(VerticalAlignment.CENTER);
            }
        });
        Segment segment24 = new Segment("Bottom");
        segment24.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                layout2.setAlign(VerticalAlignment.BOTTOM);
            }
        });
        control.setSegments(segment22, segment23, segment24);
        control.selectSegment(0);
        toolbar2.addSpacer();
        toolbar2.addSegmentedControl(control);
        toolbar2.addSpacer();
        addMember(toolbar2);
        layout2.setWidth("100%");
        layout2.setHeight("140px");
        layout2.setAlign(Alignment.CENTER);
        layout2.getElement().getStyle().setProperty("marginLeft", "auto");
        layout2.getElement().getStyle().setProperty("marginRight", "auto");
        layout2.setAlign(VerticalAlignment.TOP);
        Panel info1 = new Panel();
        info1.setStyleName("sc-rounded-panel");
        info1.setContents("<p>line 1</p>");
        info1.setMargin(10);
        Panel info2 = new Panel();
        info2.setStyleName("sc-rounded-panel");
        info2.setContents("<p>line 1<br/>line 2</p>");
        info2.setMargin(10);
        Panel info3 = new Panel();
        info3.setStyleName("sc-rounded-panel");
        info3.setContents("<p>line 1<br/>line 2<br/>line 3</p>");
        info3.setMargin(10);
        layout2.addMember(info1);
        layout2.addMember(info2);
        layout2.addMember(info3);
        addMember(layout2);
    }
}
