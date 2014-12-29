package org.informagen.eovortaro.jso;

// GWT - Core
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
   
public class Definition extends JavaScriptObject {
        
    protected Definition() {}
    
    public final native boolean success() /*-{
        return this.success;
    }-*/;
    
    public final native String vorto() /*-{
        return this.vorto;
    }-*/;
    
    public final native String propono() /*-{
        return this.propono;
    }-*/;
    
    public final native JsArray<Meaning> meanings() /*-{
        return this.aroj;
    }-*/;
    
}
