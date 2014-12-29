package org.informagen.eovortaro.client.di;

// EOVortaro - Controllers
import org.informagen.eovortaro.client.application.ApplicationController;

// EOVortaro - Presenters
import org.informagen.eovortaro.client.presenters.*;


// EOVortaro - Views
import org.informagen.eovortaro.client.views.*;

// EOVortaro - Models
import org.informagen.eovortaro.client.models.*;


// GWT - Event Bus
import com.google.gwt.event.shared.EventBus;

// Google GIN
import com.google.gwt.inject.client.AbstractGinModule;

// Google Injection
import com.google.inject.Singleton;

public class InjectorModule extends AbstractGinModule {

    @Override
    protected void configure() {

       //  // Classes with multiple interfaces ---------------------------------------------------
        
        bind(DictionaryModel.class).in(Singleton.class);
        bind(SettingsModel.class).in(Singleton.class);
        bind(PodcastModel.class).in(Singleton.class);
       
       //  // MVP Triads ------------------------------------------------------------------------

        // Front Panel
        bind(FrontPresenter.class).in(Singleton.class);
        bind(FrontPresenter.View.class).to(FrontView.class);
        // bind(FrontPresenter.Model.class).to(DictionaryModel.class);
 
         // WordOfTheDay Panel
        bind(WordOfTheDayPresenter.class).in(Singleton.class);
        bind(WordOfTheDayPresenter.View.class).to(WordOfTheDayView.class);
        bind(WordOfTheDayPresenter.Model.class).to(DictionaryModel.class);
       
         // WordLookup Panel
        bind(WordLookupPresenter.class).in(Singleton.class);
        bind(WordLookupPresenter.View.class).to(WordLookupView.class);
        bind(WordLookupPresenter.Model.class).to(DictionaryModel.class);

         // ESPDIC Panel
        bind(ESPDICPresenter.class).in(Singleton.class);
        bind(ESPDICPresenter.View.class).to(ESPDICView.class);
        bind(ESPDICPresenter.Model.class).to(DictionaryModel.class);

         // EOGlossary Panel
        bind(EOGlossaryPresenter.class).in(Singleton.class);
        bind(EOGlossaryPresenter.View.class).to(EOGlossaryView.class);
        bind(EOGlossaryPresenter.Model.class).to(DictionaryModel.class);
       
    }
 
}
