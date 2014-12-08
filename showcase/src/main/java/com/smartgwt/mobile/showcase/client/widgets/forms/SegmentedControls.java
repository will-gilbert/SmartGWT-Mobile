package com.smartgwt.mobile.showcase.client.widgets.forms;

import com.smartgwt.mobile.client.widgets.HRWidget;
import com.smartgwt.mobile.client.widgets.Header2;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.Segment;
import com.smartgwt.mobile.client.widgets.layout.SegmentedControl;
import com.smartgwt.mobile.showcase.client.resources.AppResources;

public class SegmentedControls extends ScrollablePanel {

    public SegmentedControls(String title) {
        super(title);
        this.setWidth("100%");
        addMember(new Header2("Segmented Buttons in a view"));
        SegmentedControl segmentedControl1 = new SegmentedControl();
        Segment segment11 = new Segment("");
        segment11.setIcon(AppResources.INSTANCE.arrow_left(), true);
        Segment segment12 = new Segment("");
        segment12.setIcon(AppResources.INSTANCE.arrow_right(), true);
        Segment segment13 = new Segment("");
        segment13.setIcon(AppResources.INSTANCE.arrow_up(), true);
        Segment segment14 = new Segment("");
        segment14.setIcon(AppResources.INSTANCE.arrow_down(), true);
        segmentedControl1.setSegments(segment11, segment12, segment13, segment14);
        addMember(segmentedControl1);
        addMember(new HRWidget());
        addMember(new Header2("Segmented Buttons in a view with stretch=true"));
        SegmentedControl segmentedButton2 = new SegmentedControl();
        segmentedButton2.setStretch(true);
        Segment segment21 = new Segment("<b>Bold</b>");
        segment21.setIcon(AppResources.INSTANCE.chart(), true);
        Segment segment22 = new Segment("<i>Italic</i>");
        segment22.setIcon(AppResources.INSTANCE.settings(), true);
        Segment segment23 = new Segment("Normal");
        segment23.setIcon(AppResources.INSTANCE.top_rated(), true);
        segmentedButton2.setSegments(segment21, segment22, segment23);
        addMember(segmentedButton2);
        addMember(new HRWidget());
        addMember(new Header2("Segmented Buttons in a view with tint color=blue"));
        SegmentedControl segmentedButton3 = new SegmentedControl();
        segmentedButton3.setTintColor("blue");
        Segment segment31 = new Segment("left");
        Segment segment32 = new Segment("right");
        Segment segment33 = new Segment("up");
        Segment segment34 = new Segment("down");
        segmentedButton3.setSegments(segment31, segment32, segment33, segment34);
        addMember(segmentedButton3);
        addMember(new HRWidget());
    }
}
