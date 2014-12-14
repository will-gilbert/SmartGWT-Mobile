package com.smartgwt.mobile.showcase.client.overview;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.smartgwt.mobile.client.Version;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.icons.IconResources;
import com.smartgwt.mobile.client.widgets.layout.NavStack;

public class About extends NavStack {

    private static final DateTimeFormat DTF = DateTimeFormat.getFormat("yyyy-MM-dd");

    public About() {

        super("About", IconResources.INSTANCE.info());

        final ScrollablePanel about = new ScrollablePanel("About", IconResources.INSTANCE.info());

        final Panel roundedPanel = new Panel();

        roundedPanel.setContents("This app was built with:" +
                "<ul><li>SmartGWT.mobile 1.0d (beta) built on " + DTF.format(Version.getBuildDate(), TimeZone.createTimeZone(0)) + "</li>" +
                "<li>GWT version " + GWT.getVersion() + "</li></ul>");

        roundedPanel.setStyleName("sc-rounded-panel");
        roundedPanel.setMargin(10);
        
        about.addMember(roundedPanel);
        
        setSinglePanel(about);
    }
}
