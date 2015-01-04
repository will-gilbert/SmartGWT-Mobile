package org.informagen.mobileeo.client.presenters;

// EO Vortaro - Application
import org.informagen.mobileeo.client.application.Callback;
import org.informagen.mobileeo.client.application.Presenter;
import org.informagen.mobileeo.client.events.VisitWebPageEvent;

// EO Vortaro - JSO
import org.informagen.mobileeo.jso.Espdic;

// SmartGWT Mobile - Widgets
import com.smartgwt.mobile.client.widgets.Panel;

// GWT - EventBus
import com.google.gwt.event.shared.EventBus;

// Google Inject Annotation
import com.google.inject.Inject;


public class ESPDICPresenter implements Presenter {

    private static final String attributionText = "ESPDIC - 28 October 2012 - Paul Denisowski";
    private static final String attributionURL = "http://www.denisowski.org/Esperanto/ESPDIC/espdic_readme.htm";

//---------------------------------------------------------------------------------------------

    public interface View {
        void setDelegate(ESPDICPresenter delegate);
        void clear();
        void display(Espdic espdic);
        void setAttribution(String text);
        Panel asPanel();
   }

    public interface Model {
        void fetchESPDIC(String searchTerm, Callback<Espdic> callback);
    }

//---------------------------------------------------------------------------------------------
    
    final EventBus eventBus;
    final View view;
    final Model model;
    
    @Inject
     public ESPDICPresenter(EventBus eventBus, View view, Model model) {
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

    public void lookupWord(String searchTerm) {
                
        if(searchTerm.trim().length() == 0)
            return;

        view.clear();
        
        model.fetchESPDIC(searchTerm, new Callback<Espdic>() {
            public void onSuccess(Espdic espdic) {
                view.display(espdic);
            }
            
        });
        
    }



}
