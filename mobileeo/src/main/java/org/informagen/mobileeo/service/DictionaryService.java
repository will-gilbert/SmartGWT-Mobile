package org.informagen.mobileeo.service;

// EOVortaro - Models
import org.informagen.mobileeo.client.application.Callback;

// EOVortaro - JSO
import org.informagen.mobileeo.jso.Definition;
import org.informagen.mobileeo.jso.WordOfTheDay;
import org.informagen.mobileeo.jso.Espdic;

// Java - util
import java.util.List;
import java.util.Map;
 
public interface DictionaryService  {
        
    void lookupWord(String word, String from, String to, Callback<Definition> callback);

    void wordOfTheDay(Callback<WordOfTheDay> callback);
    
    void fetchESPDIC(String model, int maximumReturn, Callback<Espdic> callback);

    void fetchDictionaries(Callback<Map<String,String>> callback);
}
