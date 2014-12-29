package org.informagen.eovortaro.jso;

// GWT - Core
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
   
public class EsperantaRetradio extends JavaScriptObject {
        
    protected EsperantaRetradio() {}
    
    public final native boolean success() /*-{
        return this.success;
    }-*/;
    
    public final native String title() /*-{
        return this.title;
    }-*/;
    
    public final native String pubDate() /*-{
        return this.pubDate;
    }-*/;
    
    public final native String mp3() /*-{
        return this.mp3;
    }-*/;
    
    public final native String text() /*-{
        return this.text;
    }-*/;
    
}
