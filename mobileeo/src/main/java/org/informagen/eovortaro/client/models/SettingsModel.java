package org.informagen.eovortaro.client.models;

// EOVortaro - Application
import org.informagen.eovortaro.client.application.Callback;

// EOVortaro - Presenters

// EOVortaro - Services
import org.informagen.eovortaro.service.DictionaryService;

// EOVortaro - DTO
import org.informagen.eovortaro.jso.Definition;

// GWT - Storage
import com.google.gwt.storage.client.Storage;

// GWT - Utility
import com.google.gwt.core.client.GWT;

// Java - Collections
import java.util.Map;
import java.util.LinkedHashMap;

// Google Inject Annotation
import com.google.inject.Inject;

public class SettingsModel {

    Map<String,String> dictionaries = null;
    
    final DictionaryService dictionaryService;

    @Inject
    public SettingsModel(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    public void fetchDictionaries(final Callback<Map<String,String>> callback) {
        
        if(dictionaries != null)
            callback.onSuccess(dictionaries);
        
        dictionaryService.fetchDictionaries(new Callback<Map<String,String>>(){
            public void onSuccess(Map<String,String> map) {
                dictionaries = new LinkedHashMap<String,String>(map);
                // Remove Esperanto from list
                dictionaries.remove("eo");
                callback.onSuccess(dictionaries); 
            }
            
        });

    }

    public String getCurrentDictionary() {
        
        Storage storage = Storage.getLocalStorageIfSupported();
        
        String dictionary = "en";
        if(storage != null) {
            dictionary = storage.getItem("mobile-eo.exchange.language");
            dictionary = (dictionary == null) ? "en" : dictionary;
        }
 
        return dictionary;
    }


    public void saveFavoriteStartupPage(String page) {

        Storage storage = Storage.getLocalStorageIfSupported();
        
        if(storage != null)
            storage.setItem("mobile-eo.startupPage", page);
    }


    public String getStartupPage() {
        
        Storage storage = Storage.getLocalStorageIfSupported();
        
        String page = "Home";
        if(storage != null) {
            page = storage.getItem("mobile-eo.startupPage");
            page = (page == null) ? "Home" : page;
        }
 
        return page;
    }


    public int getESPDICMaxReturn() {
        
        Storage storage = Storage.getLocalStorageIfSupported();
        
        int maxReturn = 50;
        if(storage != null) {
            String value = storage.getItem("mobile-eo.espdic.maxReturn");
            maxReturn = (value == null) ? maxReturn : Integer.valueOf(value).intValue();
        }
 
        return maxReturn;
    }

    public void saveESPDICMaxReturn(int maxReturn) {

        Storage storage = Storage.getLocalStorageIfSupported();
        
        if(storage != null)
            storage.setItem("mobile-eo.espdic.maxReturn", Integer.toString(maxReturn));
    }



}
