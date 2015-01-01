package org.informagen.mobileeo.client.di.providers;

// EOVortaro - Application
import org.informagen.mobileeo.client.application.Configuration;

// EOVortaro - JSONP Service
import org.informagen.mobileeo.service.DictionaryService; 
import org.informagen.mobileeo.service.jsonp.DictionaryJSONPService; 

// Google Injection
import com.google.inject.Provider;

// GWT - Core,UI
import com.google.gwt.core.client.GWT;
  
public class DictionaryServiceProvider implements Provider<DictionaryService> {

    // Global application singleton
    private static final DictionaryService jsonService = new DictionaryJSONPService(); 

    @Override
    public DictionaryService get() {
        return jsonService;
    }
}
