package org.informagen.mobileeo.client.presenters;

// EO Vortaro - Application
import org.informagen.mobileeo.client.application.Callback;
import org.informagen.mobileeo.client.application.Presenter;
import org.informagen.mobileeo.client.application.Analytics;

import org.informagen.mobileeo.client.events.HandlerFor;
import org.informagen.mobileeo.client.events.VisitWebPageEvent;

// EO Vortaro - JSO
import org.informagen.mobileeo.jso.EsperantaRetradio;

// SmartGWT Mobile
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.util.SC;

// GWT
import com.google.gwt.event.shared.EventBus;


// Google Inject Annotation
import com.google.inject.Inject;


public class EsperantaRetradioPresenter implements Presenter {  

    private static final String attributionText = "Esperanta Retradio!";
    private static final String attributionURL  = "http://aldone.de/retradio";
    private static final String webSiteURL      = "http://aldone.de/retradio/";

//---------------------------------------------------------------------------------------------

    public interface View {
        void clear();
        void display(EsperantaRetradio esperantaRetradio);
        void setAttribution(String text, String url);
        void setGoToWebSiteCallback(Callback<Void> callback);
        Panel asPanel();
    }

    public interface Model {
        void fetchEsperantaRetradio(final Callback<EsperantaRetradio> callback);
    }

//---------------------------------------------------------------------------------------------
    
    final EventBus eventBus;
    final View view;
    final Model model;
    
    @Inject
     public EsperantaRetradioPresenter(EventBus eventBus, View view, Model model) {
        this.eventBus = eventBus;
        this.view = view;
        this.model = model;

        wireUI();

        fetchEsperantaRetradio();
    }

    void wireUI() {

        view.setAttribution(attributionText, attributionURL);

        view.setGoToWebSiteCallback(new Callback<Void>(){
            public void onSuccess(Void nothing) {
                eventBus.fireEvent(new VisitWebPageEvent(webSiteURL));
            } 
        });

    }


    @Override
    public Panel getPanel() {
        return view.asPanel();
    }

    void fetchEsperantaRetradio() {
        
        view.clear();
        
        model.fetchEsperantaRetradio(new Callback<EsperantaRetradio>() {
            public void onSuccess(EsperantaRetradio esperantaRetradio) {
                Analytics.track(Analytics.PODCAST, "esperanta retradio", esperantaRetradio.title());
                view.display(esperantaRetradio);
            }
            
        });
        
    }
}
