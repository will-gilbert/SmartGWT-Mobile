package org.informagen.mobileeo.client.presenters;

// EO Vortaro - Application
import org.informagen.mobileeo.client.application.Callback;
import org.informagen.mobileeo.client.application.Presenter;
import org.informagen.mobileeo.client.events.VisitWebPageEvent;

// EO Vortaro - JSO
import org.informagen.mobileeo.jso.Definition;

// SmartGWT Mobile - Widgets
import com.smartgwt.mobile.client.widgets.Panel;

// GWT - EventBus
import com.google.gwt.event.shared.EventBus;

// Google Inject Annotation
import com.google.inject.Inject;

public class EOGlossaryPresenter implements Presenter {  

    private static final String attributionText = "Vortaraj servoj provizatas per Lernu.net";
    private static final String attributionURL = "http://lernu.net/";

//---------------------------------------------------------------------------------------------

    public interface View {
        void setDelegate(EOGlossaryPresenter delegate);
        void clear();
        void display(Definition definition);
        void setAttribution(String text);
        Panel asPanel();
   }

    public interface Model {
        void eoGlossary(String word, final Callback<Definition> callback);
    }

//---------------------------------------------------------------------------------------------
    
    final EventBus eventBus;
    final View view;
    final Model model;
    
    @Inject
     public EOGlossaryPresenter(EventBus eventBus, View view, Model model) {
        this.eventBus = eventBus;
        this.view = view;
        this.model = model;

        view.setDelegate(this);
        view.setAttribution(attributionText);
    }


    @Override
    public Panel getPanel() {
        view.clear();
        return view.asPanel();
    }

    public void visitWebPage() {
        eventBus.fireEvent(new VisitWebPageEvent(attributionURL));
    }

    public void lookupWord(String word) {
                
        if(word.trim().length() == 0)
            return;
        
        model.eoGlossary(word, new Callback<Definition>() {
            public void onSuccess(Definition definition) {
                view.display(definition);
            }
            
        });
        
    }
}
