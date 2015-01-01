package org.informagen.mobileeo.jso;

// GWT - Core
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
   
public class Espdic extends JavaScriptObject {
        
    protected Espdic() {}
    
    public final native boolean success() /*-{
        return this.success;
    }-*/;
    
    public final native String searchTerm() /*-{
        return this.searchTerm;
    }-*/;
        
    public final native int found() /*-{
        return this.found;
    }-*/;
    
    public final native JsArray<ESPDICPair> entries() /*-{
        return this.entries;
    }-*/;
    
}
