package org.informagen.eovortaro.client.di.providers;

// EOVortaro - Application
import org.informagen.eovortaro.client.application.Configuration;

// EOVortaro - JSONP Service
import org.informagen.eovortaro.service.RVPodcastService; 
import org.informagen.eovortaro.service.jsonp.RVPodcastJSONPService; 

// Google Injection
import com.google.inject.Provider;

// GWT - Core,UI
import com.google.gwt.core.client.GWT;
  
public class RVPodcastServiceProvider implements Provider<RVPodcastService> {

    // Global application singleton
    private static final RVPodcastService jsonService = new RVPodcastJSONPService(); 

    @Override
    public RVPodcastService get() {
        return jsonService;
    }
}
