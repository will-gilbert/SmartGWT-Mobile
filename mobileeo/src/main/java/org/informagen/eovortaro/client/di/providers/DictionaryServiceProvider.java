package org.informagen.eovortaro.client.di.providers;

// EOVortaro - Application
import org.informagen.eovortaro.client.application.Configuration;

// EOVortaro - JSONP Service
import org.informagen.eovortaro.service.DictionaryService; 
import org.informagen.eovortaro.service.jsonp.DictionaryJSONPService; 

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
