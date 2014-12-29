package org.informagen.eovortaro.service;

// EOVortaro - Models
import org.informagen.eovortaro.client.application.Callback;

// EOVortaro - JSO
import org.informagen.eovortaro.jso.Definition;
import org.informagen.eovortaro.jso.WordOfTheDay;
import org.informagen.eovortaro.jso.Espdic;

// Java - util
import java.util.List;
import java.util.Map;
 
public interface DictionaryService  {
        
    void lookupWord(String word, String from, String to, Callback<Definition> callback);

    void wordOfTheDay(Callback<WordOfTheDay> callback);
    
    void fetchESPDIC(String model, int maximumReturn, Callback<Espdic> callback);

    void fetchDictionaries(Callback<Map<String,String>> callback);
}
