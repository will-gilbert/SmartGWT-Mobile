package org.informagen.mobileeo.jso;

// GWT - Core
import com.google.gwt.core.client.JavaScriptObject;
   
public class ESPDICPair extends JavaScriptObject {
        
    protected ESPDICPair() {}
    
    public final native String word() /*-{
        return this.eo;
    }-*/;
    
    public final native String meaning() /*-{
        return this.en;
    }-*/;
    
        
}
