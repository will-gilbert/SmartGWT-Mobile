package org.informagen.mobileeo.client.di.providers;

// EOVortaro - Application
import org.informagen.mobileeo.client.application.Configuration;

// EOVortaro - JSONP Service
import org.informagen.mobileeo.service.RVPodcastService; 
import org.informagen.mobileeo.service.jsonp.RVPodcastJSONPService; 

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
