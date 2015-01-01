package org.informagen.mobileeo.client.application;

// GWT - Core
import com.google.gwt.core.client.GWT;

public class Analytics {

    // Google Analytics Event 'category' constants
    public final static String APPLICATION = "Application";
    public final static String GLOSSARY = "Glossary";
    public final static String PODCAST = "Podcast";

    private static Analytics instance = null;

    private String key = null;

    /**
     * constructor - this is a static class
     */

    private Analytics(String key) {
        
        if(key != null && key.length() > 0)
            this.key = key;
        else
            this.key = null;
    }
    
    public static void createInstance(String key) {
        assert (Analytics.checkAnalytics()) : "Google Analytics javascript file was not found";
        Analytics.instance = new Analytics(key);
    }

    public static Analytics getInstance() {
        assert (Analytics.instance != null) : "'Analytics' singleton has not been initialized";
        return Analytics.instance;
    }

    public static void track(String category) {
        track(category, null, null);
    }

    public static void track(String category, String action) {
        track(category, action, null);
    }

    public static void track(String category, String action, String label) {
        log(category, action, label);
        Analytics analytics = Analytics.getInstance();
        analytics.jsniTrack(category, action, label);
    }
 
    private static void log(String category, String action, String label) {
        
        StringBuilder builder = new StringBuilder()
            .append("Category: ").append(category)
            .append(", Action: ").append(action);
            
        if(label != null)    
            builder.append(", Label: ").append(label);
            
        GWT.log(builder.toString());
    }
   
    // Check to see if the Google Analytics javascript file in the bootstrap HTML file
    private static native boolean checkAnalytics() /*-{
        return(typeof $wnd._gaq !== 'undefined');
        // return(typeof $wnd._gat !== 'undefined');
    }-*/;


    // Invoke the 'trackEvent' method of the JavaScript 'gat' class
    private native void jsniTrack(String category, String action, String label) /*-{

        try {
            
            var key = this.@org.informagen.mobileeo.client.application.Analytics::key;
            //var pageTracker = $wnd._gat._getTracker(key);

            $wnd._gaq.push(['_setAccount', key]);
            
            if(label !== null) {
                //pageTracker._trackEvent(category, action, label);
                $wnd._gaq.push(['_trackEvent', category, action, label]);
            } else {
                //pageTracker._trackEvent(category, action);
                $wnd._gaq.push(['_trackEvent', category, action]);
           }

            // alert("jsniTrack: " + key + ' -> ' + category + ', ' + action);

            
        } catch(err) {
            alert('FAILURE: Sending an event to Google Analytics: ' + err);
        }

    }-*/;

  
  
}
