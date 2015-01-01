package org.informagen.mobileeo.jso;

// GWT - Core
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
   
public class RVRSSFeed extends JavaScriptObject {
        
    protected RVRSSFeed() {}
    
    public final native boolean success() /*-{
        return this.success;
    }-*/;
    
    public final native String rssTitle() /*-{
        return this.vorto;
    }-*/;
    
    public final native int count() /*-{
        return this.count;
    }-*/;
    
    public final native JsArray<RVPodcast> podcasts() /*-{
        return this.podcasts;
    }-*/;
    
}
