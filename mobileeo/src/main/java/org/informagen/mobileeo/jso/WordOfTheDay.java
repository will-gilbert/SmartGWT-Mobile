package org.informagen.mobileeo.jso;

// GWT - Core
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
   
public class WordOfTheDay extends JavaScriptObject {
        
    protected WordOfTheDay() {}
    
    public final native boolean success() /*-{
        return this.success;
    }-*/;
    
    public final native String word() /*-{
        return this.word;
    }-*/;
    
    public final native String link() /*-{
        return this.link;
    }-*/;
    
    public final native String description() /*-{
        return this.description;
    }-*/;
    
}
