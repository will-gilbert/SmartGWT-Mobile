package org.informagen.mobileeo.client.presenters;

// Mobile EO - Application
import org.informagen.mobileeo.client.application.Presenter;
import org.informagen.mobileeo.client.application.Callback;
import org.informagen.mobileeo.client.events.VisitWebPageEvent;

// Mobile EO - JSO
// 
import org.informagen.mobileeo.jso.Definition;

// Mobile EO - Events
// import org.informagen.mobileeo.client.events.HandlerFor;
// import org.informagen.mobileeo.client.events.InstallHeaderButtonEvent;
// import org.informagen.mobileeo.client.events.DictionaryChangedEvent;
// import org.informagen.mobileeo.client.events.GlossariesChangedEvent;
// import org.informagen.mobileeo.client.events.PlayMP3Event;


// SmartGWT Mobile - Widgets
import com.smartgwt.mobile.client.widgets.Panel;
import com.google.gwt.resources.client.ImageResource;

// GWT - UI
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasText;

// GWT - EventBus
import com.google.gwt.event.shared.EventBus;

// Java - Util
import java.util.List;
import java.util.ArrayList;

// Alert Logging
import com.smartgwt.mobile.client.util.SC;


// Google Inject Annotation
import com.google.inject.Inject;


public class WordLookupPresenter implements Presenter {

    private static final String attributionText = "Vortaraj servoj provizatas per Lernu.net";
    private static final String attributionURL  = "http://lernu.net/";

//---------------------------------------------------------------------------------------------

    public interface View {
        void setDelegate(WordLookupPresenter delegate);
        void clear();
        void display(Definition definition);
        void setTitle(String title);

        void setAttribution(String text);

        // NavigationButton swapButton();

        Panel asPanel();
    }

    public interface Model {
        void setDictionary(String dictionary);
        void lookupWord(String word, final Callback<Definition> callback);
        String dictionary(String iso);
        String from();
        String to();
        void swap();
    }

//---------------------------------------------------------------------------------------------
    
    final EventBus eventBus;
    final View view;
    final Model model;
    
    @Inject
    public WordLookupPresenter(EventBus eventBus, View view, Model model) {
        this.eventBus = eventBus;
        this.view = view;
        this.model = model;        

        bindViewHandlers();
        bindEventBusHandlers(); 

        view.setTitle("EO -> EN");
        view.setDelegate(this);
        view.setAttribution(attributionText);
    
    }

    @Override
    public Panel getPanel() {
        view.clear();
        return view.asPanel();
    }


    void bindViewHandlers() {
         
        //  view.swapButton().addActionEventHandler(new ActionEventHandler<String>() {
        //     public void process(ActionEvent<String> event) {
        //         model.swap();
        //         //updateHeaderTitle(model.conversion());
        //         // eventBus.fireEvent(new GlossariesChangedEvent(model.from(), model.to()));
        //     }
        // });

    }

    void bindEventBusHandlers() {
         
        // eventBus.addHandler(GlossariesChangedEvent.TYPE, 
        //     new HandlerFor.GlossariesChangedEvent() {
        //         public void process(GlossariesChangedEvent event) {
        //             updateHeaderTitle(createHeaderTitle(event.from, event.to));
        //         }
        //     }
        // );  

    }

    public void visitWebPage() {
        eventBus.fireEvent(new VisitWebPageEvent(attributionURL));
    }

    public void lookupWord(String searchTerm) {
                
        if(searchTerm.trim().length() == 0)
            return;

        view.clear();
        
        model.lookupWord(searchTerm, new Callback<Definition>() {
            public void onSuccess(Definition definition) {
                view.display(definition);
            }
        });
        
    }

    String createHeaderTitle(String from, String to) {
       return new StringBuffer()
            .append(model.dictionary(from))
            .append(" \u2192 ")
            .append(model.dictionary(to))
            .toString()
        ;
    }



}
