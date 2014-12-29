package org.informagen.eovortaro.service.jsonp;

import org.informagen.eovortaro.client.application.Callback;
import org.informagen.eovortaro.client.application.Configuration;

// EOVortaro - JSO
import org.informagen.eovortaro.jso.RVRSSFeed;
import org.informagen.eovortaro.jso.EsperantaRetradio;

// EOVortaro - Services
import org.informagen.eovortaro.service.RVPodcastService;

// GWT - User
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.UrlBuilder;
// GWT - JSONP
import com.google.gwt.jsonp.client.JsonpRequestBuilder;

// GWT - HTTP
import com.google.gwt.http.client.URL;

// GWT - JSO
import com.google.gwt.core.client.JavaScriptObject;

// Java - Collections
import java.util.Map;
import java.util.LinkedHashMap;
 
public class RVPodcastJSONPService implements RVPodcastService {

    private final static int TIMEOUT = 20000; // 20 seconds
    
    private final String rssURL = Configuration.getJsonpURL("radioVerdaRSS");
    private final String esperantaRetradioURL = Configuration.getJsonpURL("esperantaRetradio");

    public void fetchPodcastList(final Callback<RVRSSFeed> callback) {

        final String url = new UrlBuilder()
            .setPath(rssURL)
            .buildString();
        
        if(rssURL != null) {
            JsonpRequestBuilder request = new JsonpRequestBuilder();
            request.setTimeout(TIMEOUT);
            request.requestObject(url, callback);
        }
    }

    public void fetchEsperantaRetradio(Callback<EsperantaRetradio> callback) {
        
        final String url = new UrlBuilder()
            .setPath(esperantaRetradioURL)
            .buildString();
        
        if(esperantaRetradioURL != null) {
            JsonpRequestBuilder request = new JsonpRequestBuilder();
            request.setTimeout(TIMEOUT);
            request.requestObject(url, callback);
        }
    }

}



