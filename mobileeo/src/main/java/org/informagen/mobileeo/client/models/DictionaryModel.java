package org.informagen.mobileeo.client.models;

// EOVortaro - Application
import org.informagen.mobileeo.client.application.Analytics;
import org.informagen.mobileeo.client.application.Callback;

// EOVortaro - Presenters
import org.informagen.mobileeo.client.presenters.WordOfTheDayPresenter;
import org.informagen.mobileeo.client.presenters.WordLookupPresenter;
import org.informagen.mobileeo.client.presenters.ESPDICPresenter;
import org.informagen.mobileeo.client.presenters.EOGlossaryPresenter;

// EO Vortaro - Events
import org.informagen.mobileeo.client.events.HandlerFor;
import org.informagen.mobileeo.client.events.DictionaryChangedEvent;
import org.informagen.mobileeo.client.events.GlossariesChangedEvent;

// EOVortaro - Services
import org.informagen.mobileeo.service.DictionaryService;

// EOVortaro - DTO
import org.informagen.mobileeo.jso.Definition;
import org.informagen.mobileeo.jso.WordOfTheDay;
import org.informagen.mobileeo.jso.Espdic;

// GWT - EventBus
import com.google.gwt.event.shared.EventBus;

// GWT - Utility
import com.google.gwt.core.client.GWT;

// GWT - Storage
import com.google.gwt.storage.client.Storage;

// Java - Collections
import java.util.Map;
import java.util.HashMap;

// Google Inject Annotation
import com.google.inject.Inject;

public class DictionaryModel implements WordOfTheDayPresenter.Model, 
                                        WordLookupPresenter.Model,
                                        ESPDICPresenter.Model,
                                        EOGlossaryPresenter.Model {


    Map<String,String> dictionaries = null;

    WordOfTheDay cachedWordOfTheDay = null;

    String from;
    String to;
    
    final EventBus eventBus;
    final DictionaryService service;
    final SettingsModel preferences;

    @Inject
    public DictionaryModel(EventBus eventBus, DictionaryService service, SettingsModel preferences) {
        this.eventBus = eventBus;
        this.service = service;
        this.preferences = preferences;
        
        bindEventBusHandlers();
        
        from = "eo";
        to = retrieveDictionary();
        fetchDictionaries(); 
        
        eventBus.fireEvent(new GlossariesChangedEvent(from, to));
    }


    void bindEventBusHandlers() {
         
        eventBus.addHandler(DictionaryChangedEvent.TYPE, 
            new HandlerFor.DictionaryChangedEvent() {
                public void process(DictionaryChangedEvent event) {
                   setDictionary(event.getDictionary());
                }
            }
        );  

    }
    
    public String from() { return from; }
    public String to() { return to; }
 
    public String dictionary(String iso) {
        return isSmallScreen() ? iso.toUpperCase() : dictionaries.get(iso);
    }
            
    public void eoGlossary(String word, final Callback<Definition> callback) {
        Analytics.track(Analytics.GLOSSARY, "lernu", "definition");
        service.lookupWord(word, "eo", "eo", callback);
    }

    public void lookupWord(String word, final Callback<Definition> callback) {
        Analytics.track(Analytics.GLOSSARY, "lernu", from + " " + to);
        service.lookupWord(word, from, to, callback);
    }

    public void fetchESPDIC(String model, final Callback<Espdic> callback) {
        Analytics.track(Analytics.GLOSSARY, "ESPDIC");
        service.fetchESPDIC(model, preferences.getESPDICMaxReturn(), callback);
    }

    public void wordOfTheDay(final Callback<WordOfTheDay> callback) {

        // If the 'WordOfTheDay' has been fetched, return it

        if ( cachedWordOfTheDay != null) {
            callback.onSuccess(cachedWordOfTheDay);
            return;
        }

        // Otherwise, fetch it, cache it and then return it
        Analytics.track(Analytics.GLOSSARY, "lernu", "word of the day");

        service.wordOfTheDay(new Callback<WordOfTheDay>() {
            public void onSuccess(WordOfTheDay wordOfTheDay) {
                cachedWordOfTheDay = wordOfTheDay;
                callback.onSuccess(cachedWordOfTheDay);
            }
        });
    }

    public void fetchDictionaries() {
        
        service.fetchDictionaries(new Callback<Map<String,String>>(){
            public void onSuccess(Map<String,String> map) {
                dictionaries = map;    
            }
        });
    }

    public void swap() {
        String temp = from;
        from = to;
        to = temp;

        eventBus.fireEvent(new GlossariesChangedEvent(from, to));
    }

    public void setDictionary(String language) {
        from = "eo";
        to = language;
        saveDictionary(language);
        
        eventBus.fireEvent(new GlossariesChangedEvent(from, to));
    }

    boolean isSmallScreen() {
        return true;
    }

    String retrieveDictionary() {
        return preferences.getCurrentDictionary();
    }

    void saveDictionary(String dictionary) {
        
        Storage storage = Storage.getLocalStorageIfSupported();
        
        if(storage != null)
            storage.setItem("mobile-eo.exchange.language", dictionary);

    }

}
