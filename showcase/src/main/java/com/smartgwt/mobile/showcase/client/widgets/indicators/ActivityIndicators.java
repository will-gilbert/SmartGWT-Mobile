package com.smartgwt.mobile.showcase.client.widgets.indicators;

import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.widgets.ActivityIndicator;
import com.smartgwt.mobile.client.widgets.Header2;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;

public class ActivityIndicators extends ScrollablePanel {

    public ActivityIndicators(String title) {
        super(title);

        this.setWidth("100%");

        HLayout hlayout = new HLayout();
        hlayout.setWidth("100%");
        hlayout.setAlign(Alignment.CENTER);
        hlayout.addMember(new ActivityIndicator());
        addMember(hlayout);
        
        addMember(new Header2("Activity Indicator in Toolbar"));
        
        ToolStrip toolbar = new ToolStrip();
        toolbar.addMember(new ActivityIndicator());
        addMember(toolbar);
    }
}
