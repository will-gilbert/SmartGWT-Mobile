package org.informagen.mobileeo.client.presenters;

// EO Vortaro - UI
import org.informagen.mobileeo.client.application.Presenter;
import org.informagen.mobileeo.client.application.Callback;

import org.informagen.mobileeo.client.events.SwitchToPageEvent;


// SmartGWT Mobile
import com.smartgwt.mobile.client.widgets.Panel;


// GWT - EventBus
import com.google.gwt.event.shared.EventBus;

// GWT - Simple Logging
import com.google.gwt.core.client.GWT;

// Java - Util
import java.util.List;
import java.util.ArrayList;

// Google Inject Annotation
import com.google.inject.Inject;

public class FrontPresenter implements Presenter {  

//---------------------------------------------------------------------------------------------


    public interface View {
        // void setInterchangeTitle(String title);       
        void setRecordClickedCallback(Callback<String> callback);
        Panel asPanel();
    }

    public interface Model {
        String from();
        String to();
        void setDictionary(String iso);
        String dictionary(String iso);
    }

//---------------------------------------------------------------------------------------------
   
    final View view;
    // final Model model;
    final EventBus eventBus;

    @Inject
    public FrontPresenter(EventBus eventBus, View view /*, Model model*/) {
        this.eventBus = eventBus;
        this.view = view;
        // this.model = model;
        
        bindCallbacks();
        bindEventBusHandlers();
                
        // view.setInterchangeTitle(createInterchangeTitle(model.from(), model.to()));
    }

    @Override
    public Panel getPanel() {
        // updateHeader(Configuration.getProperty("windowTitle","Esperanto"), false);
        return view.asPanel();
    }

    void bindCallbacks() {
        
        view.setRecordClickedCallback(new Callback<String>() {
            public void onSuccess(String pageName) {
                eventBus.fireEvent(new SwitchToPageEvent(pageName));
            }
        });

    }

    void bindEventBusHandlers() {
         
        // eventBus.addHandler(GlossariesChangedEvent.TYPE, 
        //     new HandlerFor.GlossariesChangedEvent() {
        //         public void process(GlossariesChangedEvent event) {
        //            view.setInterchangeTitle(createInterchangeTitle(event.from, event.to));
        //         }
        //     }
        // );  

    }

    
    // String createInterchangeTitle(String from, String to) {
    //   return new StringBuffer()
    //         .append(model.dictionary(from))
    //         .append(" \u2192 ")
    //         .append(model.dictionary(to))
    //         .append(" Interŝanĝilo")
    //         .toString()
    //     ;
    // }

}
