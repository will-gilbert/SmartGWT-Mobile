package org.informagen.mobileeo.client.presenters;

// Mobile EO  - Application
import org.informagen.mobileeo.client.application.Presenter;

// Mobile EO - Events
import org.informagen.mobileeo.client.events.SwitchToPageEvent;
import org.informagen.mobileeo.client.events.VisitWebPageEvent;

// SmartGWT Mobile
import com.smartgwt.mobile.client.widgets.Panel;

// GWT - EventBus
import com.google.gwt.event.shared.EventBus;

// Google Inject Annotation
import com.google.inject.Inject;

public class FrontPresenter implements Presenter {  

    private static final String attributionText = "Konstruita kun SmartGWT Mobile®";
    private static final String attributionURL  = "http://www.smartclient.com/product/smartgwtMobile.jsp";

    //---------------------------------------------------------------------------------------------

    public interface View {
        // void setInterchangeTitle(String title);       
        void setDelegate(FrontPresenter delegate);
        void setAttribution(String text);
        Panel asPanel();
    }

    public interface Model {
        String from();
        String to();
        void setDictionary(String iso);
        String dictionary(String iso);
    }

    //---------------------------------------------------------------------------------------------
   
    final EventBus eventBus;
    final View view;
    // final Model model;

    @Inject
    public FrontPresenter(EventBus eventBus, View view /*, Model model*/) {
        this.eventBus = eventBus;
        this.view = view;
        
        view.setDelegate(this);
        view.setAttribution(attributionText);
        bindEventBusHandlers();
                
        // view.setInterchangeTitle(createInterchangeTitle(model.from(), model.to()));
    }

    @Override
    public Panel getPanel() {
        return view.asPanel();
    }

    public void switchToPage(String pageName) {
        eventBus.fireEvent(new SwitchToPageEvent(pageName));
    }

    public void visitWebPage() {
        visitWebPage(attributionURL);
    }

    public void visitWebPage(String websiteURL) {
        eventBus.fireEvent(new VisitWebPageEvent(websiteURL));
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
