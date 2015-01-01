package org.informagen.mobileeo.jso;

// GWT - Core
import com.google.gwt.core.client.JavaScriptObject;
   
public class RVPodcast extends JavaScriptObject {
        
    protected RVPodcast() {}
    
    public final native String title() /*-{
        return this.title;
    }-*/;
    
    public final native String pubDate() /*-{
        return this.pubDate;
    }-*/;
    
    public final native String image() /*-{
        return this.image;
    }-*/;
    
    public final native String mp3() /*-{
        return this.mp3;
    }-*/;
    
    public final native String topic() /*-{
        return this.topic;
    }-*/;
        
}
