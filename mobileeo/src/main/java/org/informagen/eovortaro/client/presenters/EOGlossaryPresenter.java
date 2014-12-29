package org.informagen.eovortaro.client.presenters;

// EO Vortaro - Application
import org.informagen.eovortaro.client.application.Configuration;
import org.informagen.eovortaro.client.application.Callback;
import org.informagen.eovortaro.client.application.Presenter;

// EO Vortaro - JSO
import org.informagen.eovortaro.jso.Definition;

// SmartGWT Mobile - Widgets
import com.smartgwt.mobile.client.widgets.Panel;


// Alert Logging
import com.smartgwt.mobile.client.util.SC;

// Google Inject Annotation
import com.google.inject.Inject;

public class EOGlossaryPresenter implements Presenter {  

    private static final String attributionText = "Vortaraj servoj provizatas per Lernu.net";
    private static final String attributionURL = "http://lernu.net/";

//---------------------------------------------------------------------------------------------

    public interface View {
        void clear();
        void display(Definition definition);
        void setSearchTermCallback(Callback<String> callback);

        void setAttribution(String text, String url);
        
        Panel asPanel();
   }

    public interface Model {
        void eoGlossary(String word, final Callback<Definition> callback);
    }

//---------------------------------------------------------------------------------------------
    
    final View view;
    final Model model;
    
    @Inject
     public EOGlossaryPresenter(View view, Model model) {
        this.view = view;
        this.model = model;

        view.setAttribution(attributionText, attributionURL);

        bindViewCallbacks();
    }


    @Override
    public Panel getPanel() {
        view.clear();
        return view.asPanel();
    }

    void bindViewCallbacks() {

        view.setSearchTermCallback(new Callback<String>() {
            public void onSuccess(String searchTerm) {
                view.clear();
                lookupWord(searchTerm);
            }
        });

    }


    void lookupWord(String word) {
                
        if(word.trim().length() == 0)
            return;
        
        model.eoGlossary(word, new Callback<Definition>() {
            public void onSuccess(Definition definition) {
                view.display(definition);
            }
            
        });
        
    }
}
