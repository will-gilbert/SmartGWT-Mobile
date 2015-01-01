package org.informagen.mobileeo.client.application;

public class Configuration  {
    
    private Configuration() {}
    
    public static String getProperty(String key) {
        return getProperty(key, "");
    }
    
    public static String getProperty(String key, String theDefault) {
        
        String value = getLocalityProperty(key);
        
        if(value == null)
            value = theDefault;
            
        return value;
    }
    
    public static String getJsonpURL(String method) {
        
        String value = getJSONPForMethod(method);
        
        // if(value == null)
        //     throw an error;
            
        return value;
    }


    
    final native static String getLocalityProperty(String key) /*-{
        return $wnd.Configuration[key];
    }-*/;
    
    final native static String getJSONPForMethod(String method) /*-{
        return $wnd.Configuration.jsonp[method];
    }-*/;

}
