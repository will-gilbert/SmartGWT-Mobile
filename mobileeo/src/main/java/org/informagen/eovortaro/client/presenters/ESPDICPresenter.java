package org.informagen.eovortaro.client.presenters;

// EO Vortaro - Application
import org.informagen.eovortaro.client.application.Configuration;
import org.informagen.eovortaro.client.application.Callback;
import org.informagen.eovortaro.client.application.Presenter;

// EO Vortaro - JSO
import org.informagen.eovortaro.jso.Espdic;

// SmartGWT Mobile - Widgets
import com.smartgwt.mobile.client.widgets.Panel;


// Alert Logging
import com.smartgwt.mobile.client.util.SC;

// Google Inject Annotation
import com.google.inject.Inject;


public class ESPDICPresenter implements Presenter {

    private static final String attributionText = "ESPDIC - 28 October 2012 - Paul Denisowski";
    private static final String attributionURL = "http://www.denisowski.org/Esperanto/ESPDIC/espdic_readme.htm";

//---------------------------------------------------------------------------------------------

    public interface View {
        void clear();
        void display(Espdic espdic);
        void setSearchTermCallback(Callback<String> callback);

        void setAttribution(String text, String url);
        
        Panel asPanel();
   }

    public interface Model {
        void fetchESPDIC(String searchTerm, Callback<Espdic> callback);
    }

//---------------------------------------------------------------------------------------------
    
    final View view;
    final Model model;
    
    @Inject
     public ESPDICPresenter(View view, Model model) {
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

    void lookupWord(String searchTerm) {
                
        if(searchTerm.trim().length() == 0)
            return;
        
        model.fetchESPDIC(searchTerm, new Callback<Espdic>() {
            public void onSuccess(Espdic espdic) {
                view.display(espdic);
            }
            
        });
        
    }



}
