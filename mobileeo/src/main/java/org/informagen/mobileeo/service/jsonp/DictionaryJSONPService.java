package org.informagen.mobileeo.service.jsonp;

import org.informagen.mobileeo.client.application.Callback;
import org.informagen.mobileeo.client.application.Configuration;

// EOVortaro - JSO
import org.informagen.mobileeo.jso.Definition;
import org.informagen.mobileeo.jso.WordOfTheDay;
import org.informagen.mobileeo.jso.Espdic;

// EOVortaro - Services
import org.informagen.mobileeo.service.DictionaryService;

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
 
public class DictionaryJSONPService implements DictionaryService {
    
    private final static int TIMEOUT = 20000; // 20 seconds
    
    private final String lookupWordURL = Configuration.getJsonpURL("lookupWord");
    private final String wordOfTheDayURL = Configuration.getJsonpURL("wordOfTheDay");
    private final String fetchESPDICURL = Configuration.getJsonpURL("fetchESPDIC");

    Map<String,String> dictionaries = null;

    public DictionaryJSONPService() {
            
        dictionaries = new LinkedHashMap<String,String>();
        
        dictionaries.put("eo","Esperanto");
        dictionaries.put("en","English");
        dictionaries.put("ar","عربي");
        dictionaries.put("bg","Български");
        dictionaries.put("cs","Čeština");
        dictionaries.put("zh","中文");
        dictionaries.put("da","Dansk");
        dictionaries.put("fi","Suomi");
        dictionaries.put("fr","Français");
        dictionaries.put("de","Deutsch");
        dictionaries.put("el","Ελληνικά");
        dictionaries.put("he","עברית");
        dictionaries.put("hi","हिन्दी");
        dictionaries.put("es","Español");
        dictionaries.put("hu","Magyar");
        dictionaries.put("ga","Gaeilge");
        dictionaries.put("it","Italiano");
        dictionaries.put("ja","日本語");
        dictionaries.put("ca","Català");
        dictionaries.put("ko","한국어");
        dictionaries.put("hr","Hrvatski");
        dictionaries.put("lt","Lietuvių");
        dictionaries.put("nl","Nederlands");
        dictionaries.put("no","Norsk");
        dictionaries.put("fa","فارسی");
        dictionaries.put("pl","Polski");
        dictionaries.put("pt","Português");
        dictionaries.put("ro","Română");
        dictionaries.put("ru","Русский");
        dictionaries.put("sr","Српски");
        dictionaries.put("sk","Slovenčina");
        dictionaries.put("sl","Slovensko");
        dictionaries.put("hsb","Hornjoserbsce");
        dictionaries.put("sw","Kiswahili");
        dictionaries.put("sv","Svenska");
        dictionaries.put("th","ไทย");
        dictionaries.put("tr","Türkçe");
        dictionaries.put("uk","Українська");
        dictionaries.put("vi","Việt nam");

    }


    public void lookupWord(String word, String from, String to, final Callback<Definition> callback) {

        final String url = new UrlBuilder()
            .setPath(lookupWordURL)
            .setParameter("modelo", word)
            .setParameter("delingvo", from)
            .setParameter("allingvo", to)
            .buildString();
            
        JsonpRequestBuilder request = new JsonpRequestBuilder();
        request.setTimeout(TIMEOUT);
        request.requestObject(url, callback);
    }


    public void fetchDictionaries(Callback<Map<String,String>> callback) {
        if(dictionaries != null)
            callback.onSuccess(dictionaries);
    }

    public void wordOfTheDay( Callback<WordOfTheDay> callback) {
        
        final String url = new UrlBuilder()
            .setPath(wordOfTheDayURL)
            .buildString();
            
        JsonpRequestBuilder request = new JsonpRequestBuilder();
        request.setTimeout(TIMEOUT);
        request.requestObject(url, callback);
        
    }

    public void fetchESPDIC(String model, int maxReturn, final Callback<Espdic> callback) {

        final String url = new UrlBuilder()
            .setPath(fetchESPDICURL)
            .setParameter("searchTerm", model)
            .setParameter("maxReturn", Integer.toString(maxReturn))
            .buildString();
            
        JsonpRequestBuilder request = new JsonpRequestBuilder();
        request.setTimeout(TIMEOUT);
        request.requestObject(url, callback);
    }


}



