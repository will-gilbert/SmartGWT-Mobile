package com.smartgwt.mobile.showcase.client.widgets.toolbar;

import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.IconAlign;
import com.smartgwt.mobile.client.widgets.HRWidget;
import com.smartgwt.mobile.client.widgets.Header2;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.Segment;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.SegmentedControl;
import com.smartgwt.mobile.client.widgets.layout.VLayout;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.showcase.client.resources.AppResources;

public class ToolbarSegmentedControls extends ScrollablePanel {
    String text = "<p>A toolbar may contain segmented controls which group buttons within a type of radiobox." +
    " The samples below illustrate toolbars with segmented controls.</p>";
    Panel infoPanel = new Panel();

    public ToolbarSegmentedControls(String title) {
        super(title);
        this.setWidth("100%");
        final VLayout vlayout = new VLayout();
        vlayout.setWidth("100%");
        infoPanel.setStyleName("sc-rounded-panel");
        infoPanel.setContents(text+"<p><br/></p>");
        infoPanel.setMargin(10);
        vlayout.addMember(infoPanel);
        vlayout.addMember(new HRWidget());
        ToolStrip toolbar10 = new ToolStrip();
        toolbar10.setTintColor("#FFCC33");
        SegmentedControl control1 = new SegmentedControl();
        control1.setInheritTint(true);
        Segment segment11 = new Segment("Open");
        segment11.setIcon(AppResources.INSTANCE.bookmarks(), true);
        segment11.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Open segment selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        Segment segment12 = new Segment("Edit");
        segment12.setIcon(AppResources.INSTANCE.compose(), true);
        segment12.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Edit segment selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        Segment segment13 = new Segment("Save");
        segment13.setIcon(AppResources.INSTANCE.downloads(), true);
        segment13.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Save segment selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        control1.setSegments(segment11, segment12, segment13);
        toolbar10.addSegmentedControl(control1);
        ToolStrip toolbar11 = new ToolStrip();
        toolbar11.setAlign(Alignment.CENTER);
        toolbar11.setTintColor("#7fcdff");
        SegmentedControl control2 = new SegmentedControl();
        control2.setInheritTint(true);
        Segment segment21 = new Segment("Send");
        segment21.setIcon(AppResources.INSTANCE.action(), true);
        segment21.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Send segment selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        Segment segment22 = new Segment("Flag");
        segment22.setIcon(AppResources.INSTANCE.flag(), true);
        segment22.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Flag segment selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        Segment segment23 = new Segment("Cancel");
        segment23.setIcon(AppResources.INSTANCE.stop(), true);
        segment23.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Cancel segment selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        control2.setSegments(segment21, segment22, segment23);
        toolbar11.addSpacer();
        toolbar11.addSegmentedControl(control2);
        toolbar11.addSpacer();
        ToolStrip toolbar12 = new ToolStrip();
        toolbar12.setAlign(Alignment.RIGHT);
        toolbar12.setTintColor("#CC6633");
        SegmentedControl control3 = new SegmentedControl();
        control3.setInheritTint(true);
        Segment segment31 = new Segment();
        segment31.setIcon(AppResources.INSTANCE.reload(), true);
        segment31.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Reload segment selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        Segment segment32 = new Segment();
        segment32.setIcon(AppResources.INSTANCE.settings(), true, IconAlign.RIGHT);
        segment32.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Settings segment selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        Segment segment33 = new Segment();
        segment33.setIcon(AppResources.INSTANCE.top_rated(), true);
        segment33.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Rate segment selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        control3.setSegments(segment31, segment32, segment33);
        toolbar12.addSpacer();
        toolbar12.addSegmentedControl(control3);
        ToolStrip toolbar13 = new ToolStrip();
        toolbar13.setTintColor("#009999");
        SegmentedControl control4 = new SegmentedControl();
        control4.setInheritTint(true);
        control4.setStretch(true);
        Segment segment41 = new Segment("Rate");
        segment41.setIcon(AppResources.INSTANCE.top_rated(), true);
        segment41.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Rate segment selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        Segment segment42 = new Segment("Discuss");
        segment42.setIcon(AppResources.INSTANCE.chat(), true);
        segment42.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Discuss segment selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        Segment segment43 = new Segment("Other");
        segment43.setIcon(AppResources.INSTANCE.more(), true, IconAlign.RIGHT);
        segment43.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String newText = text + "<p><b>Other segment selected.</b></p>";
                infoPanel.setContents(newText);
            }
        });
        control4.setSegments(segment41, segment42, segment43);
        toolbar13.addSegmentedControl(control4);
        vlayout.addMember(new Header2("Segmented Buttons left justified."));
        vlayout.addMember(toolbar10);
        vlayout.addMember(new Header2("Segmented Buttons centered."));
        vlayout.addMember(toolbar11);
        vlayout.addMember(new Header2("Segmented Buttons icons only, right justified."));
        vlayout.addMember(toolbar12);
        vlayout.addMember(new Header2("Segmented Buttons with stretch=true."));
        vlayout.addMember(toolbar13);
        this.addMember(vlayout);
    }
}
