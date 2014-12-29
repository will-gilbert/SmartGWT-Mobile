package org.informagen.eovortaro.client.presenters;

// EO Vortaro - Application
import org.informagen.eovortaro.client.application.Configuration;
import org.informagen.eovortaro.client.application.Callback;
import org.informagen.eovortaro.client.application.Presenter;

// EO Vortaro - JSO
// 
import org.informagen.eovortaro.jso.Definition;

// EO Vortaro - Events
// import org.informagen.eovortaro.client.events.HandlerFor;
// import org.informagen.eovortaro.client.events.InstallHeaderButtonEvent;
// import org.informagen.eovortaro.client.events.DictionaryChangedEvent;
// import org.informagen.eovortaro.client.events.GlossariesChangedEvent;
// import org.informagen.eovortaro.client.events.PlayMP3Event;


// SmartGWT Mobile - Widgets
import com.smartgwt.mobile.client.widgets.Panel;

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

//---------------------------------------------------------------------------------------------

    public interface View {
        void clear();
        void display(Definition definition);
        void setTitle(String title);

        void setSearchTermCallback(Callback<String> callback);

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

        bindViewCallbacks();
        bindViewHandlers();
        bindEventBusHandlers(); 

        view.setTitle("EO -> EN");
      
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


    void lookupWord(String searchTerm) {
                
        if(searchTerm.trim().length() == 0)
            return;
        
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
