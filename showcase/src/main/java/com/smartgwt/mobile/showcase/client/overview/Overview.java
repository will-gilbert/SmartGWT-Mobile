package com.smartgwt.mobile.showcase.client.overview;

import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.icons.IconResources;
import com.smartgwt.mobile.client.widgets.layout.NavStack;

public class Overview extends NavStack {

    public Overview() {
        super("Overview", IconResources.INSTANCE.home());
        final ScrollablePanel overview = new ScrollablePanel("Overview", IconResources.INSTANCE.home());
        final Panel roundedPanel = new Panel();
        roundedPanel.setContents("SmartGWT.mobile lets you use Java to build ultra-lightweight " +
                "mobile applications with native look-and-feel, leveraging the same powerful server framework as Smart&nbsp;GWT." +

                "<h2>Develop in Java &amp; Eclipse</h2>" +
                "SmartGWT.mobile is based on GWT (Google Web Toolkit), which means you can write your " +
                "application in Java using Eclipse, even using step-through debugging to troubleshoot.  You " +
                "never have to learn the idiosyncrasies of HTML and JavaScript behavior on different devices " +
                "because that's handled by the framework - just write Java." +

                "<h2>Single Codebase</h2>" +
                "SmartGWT.mobile lets you deliver to a variety of smartphone devices from a single codebase, " +
                "instead of using 4-5 different sets of tools and technologies to build mobile apps for each " +
                "major platform." +

                "<h2>The API you already know</h2>" +
                "SmartGWT.mobile and Smart GWT use the same server technology. DataSources and server business " +
                "logic defined in either technology can be immediately reused with the other, with no new coding " +
                "required." +
                "<p>" +
                "The UI components of SmartGWT.mobile have the same Java API as Smart GWT's UI components, " +
                "resulting in quicker learning as well as both conceptual and code re-use between your desktop, " +
                "tablet and mobile applications." +

                "<h2>Ultra-lightweight</h2>" +
                "Through advanced use of CSS3 and HTML5 techniques, SmartGWT.mobile applications look and feel " +
                "exactly like native mobile applications, even replicating smooth 3D animations and screen " +
                "transitions." +

                "<h2>Get Started</h2>" +
                "<ul><li>You are viewing the Showcase app, containing live examples of a number of SmartGWT.mobile features. Try it out in a mobile device.</li>" +
                "<li>The latest SDK can be downloaded from: <a href='http://www.smartclient.com/builds/SmartGWT.mobile/1.0d/LGPL' target='_new'>http://smartclient.com/builds</a></li>" +
                "<li><a href='http://forums.smartclient.com/showthread.php?t=8159#aSmartGWTMobile' target='_new'>Read the FAQ</a> - Covers common questions such as when to use SmartGWT.mobile vs our other technologies</li>" +
                "<li>Ask questions on the <a href='http://forums.smartclient.com/forumdisplay.php?f=14' target='_new'>Smart&nbsp;GWT Technical Q&amp;A forum</a></li></ul>");
        roundedPanel.setStyleName("sc-rounded-panel");
        roundedPanel.setMargin(10);
        overview.addMember(roundedPanel);
        setSinglePanel(overview);
    }
}
