package com.smartgwt.mobile.internal.gwt.dom.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;

public class DOMParser extends JavaScriptObject {

    public static native DOMParser create() /*-{
        return new $wnd.DOMParser();
    }-*/;

    protected DOMParser() {}

    public final native Document parseHTMLFromString(String html) /*-{
        return this.parseFromString(html, "text/html");
    }-*/;

    public final native Document parseXHTMLFromString(String xhtml) /*-{
        return this.parseFromString(xhtml, "application/xhtml+xml");
    }-*/;

    // parseXMLFromString() intentionally omitted. Use GWT's com.google.gwt.xml.client.XMLParser class instead.
}
