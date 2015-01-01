package org.informagen.mobileeo.client.application;

// EOVortaro - Application
import org.informagen.mobileeo.client.application.Analytics;
import org.informagen.mobileeo.client.application.Presenter;

// EOVortaro - DI
import org.informagen.mobileeo.client.di.Injector;

// EOVortaro - JSO

// EOVortaro - Pages

// EOVortaro - Models
import org.informagen.mobileeo.client.models.DictionaryModel;

// EOVortaro - Events
import org.informagen.mobileeo.client.events.HandlerFor;
import org.informagen.mobileeo.client.events.SwitchToPageEvent;
import org.informagen.mobileeo.client.events.PlayMP3Event;

// import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.Dialog;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.mobile.client.util.SC;


// GWT - Widgets
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.Window;

// GWT - EventBus
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Command;
import com.google.gwt.core.client.Scheduler;


// GWT - Simple Logging
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

// Java - util
import java.util.Map;
import java.util.HashMap;


public class ApplicationController  {
    
    private final Injector injector = GWT.create(Injector.class);

    private final EventBus eventBus;

    // Handles application pages history and transitions
    
    private NavStack navigationStack;

      
    public ApplicationController() {
        
        eventBus = injector.getEventBus();
        navigationStack = injector.getNavStack();
                	        
        bindEventBusHandlers();

    }

    // Which part of the browser to run in; Add the header singleton, the bring up
    //  the first page

    public void loadApplication(final HasWidgets container) {

        // this.container = container;
        // container.add(headerPresenter.asWidget());
        // switchToPage(injector.getSettingsModel().getStartupPage());

        RootLayoutPanel.get().add(navigationStack);

        navigationStack.push(injector.getFrontPresenter().getPanel());
    }


    private void bindEventBusHandlers() {

        eventBus.addHandler(SwitchToPageEvent.TYPE, 
            new HandlerFor.SwitchToPageEvent() {
                public void process(SwitchToPageEvent event) {
                   switchToPage(event.getPageName(), event.getEvent());
                }
            }
        );  

    }
    //-----------------------------------------------------------------------------------------

    void switchToPage(String pageName) {
        switchToPage(pageName, null);
    }

    void switchToPage(String pageName, final GwtEvent event) {
                
        Presenter presenter = getPresenter(pageName);
        if(presenter != null)
            navigationStack.push(presenter.getPanel());
        
    }

    //----------------------------------------------------------------------------------------

    Presenter getPresenter(String pageName) {

        Presenter presenter = null;
        
        if("Home".equals(pageName)) {
        
             ; // presenter = injector.getHomePresenter();
             
        // } else if("Home.Fullscreen".equals(pageName)) {
        
        //      presenter = injector.getFullScreenPresenter();
             
        } else if("EOGlossaryPage".equals(pageName)) {
        
             presenter = injector.getEOGlossaryPresenter();
             
        } else if("WordOfTheDay".equals(pageName)) {
        
             presenter = injector.getWordOfTheDayPresenter();
             
        } else if("WordLookup".equals(pageName)) {
        
             presenter = injector.getWordLookupPresenter();
             
        } else if("ESPDICLookup".equals(pageName)) {
        
             presenter = injector.getESPDICPresenter();
             
        // } else if("RVPodcastPage".equals(pageName)) {
        
        //      presenter = injector.getRVPodcastPresenter();
             
        // } else if("EsperantaRetradioPage".equals(pageName)) {
        
        //      presenter = injector.getEsperantaRetradioPresenter();
             
        // } else if("PlayMP3Page".equals(pageName)) {

        //      presenter = injector.getMP3PlayerPresenter();
             
        // } else if("SettingsPage".equals(pageName)) {
        
        //      Analytics.track(Analytics.GLOSSARY, "lernu", "definition");
        //      presenter = injector.getSettingsPresenter();
             
        // } else {
        //      presenter = injector.getFrontPresenter();
        }
        
        return presenter;
    }


}
