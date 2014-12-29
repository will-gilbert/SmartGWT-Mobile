package org.informagen.eovortaro.jso;

// GWT - Core
import com.google.gwt.core.client.JavaScriptObject;
   
public class Meaning extends JavaScriptObject {
        
    protected Meaning() {}
    
    public final native String found() /*-{
        return this.fondata;
    }-*/;
    
    public final native String parts() /*-{
        return this.partoj;
    }-*/;
    
    public final native String root() /*-{
        return this.radiko;
    }-*/;
    
    public final native String meaning() /*-{
        return this.signifo;
    }-*/;
        
}
