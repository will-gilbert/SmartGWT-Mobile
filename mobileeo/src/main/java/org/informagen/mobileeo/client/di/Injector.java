package org.informagen.mobileeo.client.di;

// EOVortaro - Controllers
import org.informagen.mobileeo.client.application.ApplicationController;

// EOVortaro - Presenters
import org.informagen.mobileeo.client.application.Presenter;
import org.informagen.mobileeo.client.presenters.*;

// EOVortaro - Models
import org.informagen.mobileeo.client.models.*;

//SmartGWT Mobile
import com.smartgwt.mobile.client.widgets.layout.NavStack;


// GWT - EventBus
import com.google.gwt.event.shared.EventBus;

// GWT - DI
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.inject.client.GinModules;


@GinModules({InjectorModule.class, ProviderModule.class})
public interface Injector extends Ginjector {

    // Application wide objects
    EventBus getEventBus();
    NavStack getNavStack();

    // Presenters
    
    FrontPresenter getFrontPresenter();
    // FullScreenPresenter getFullScreenPresenter();
    // SettingsPresenter getSettingsPresenter();
    WordLookupPresenter getWordLookupPresenter();
    EOGlossaryPresenter getEOGlossaryPresenter();
    // RVPodcastPresenter getRVPodcastPresenter();
    // MP3PlayerPresenter getMP3PlayerPresenter();
    WordOfTheDayPresenter getWordOfTheDayPresenter();
    ESPDICPresenter getESPDICPresenter();
    EsperantaRetradioPresenter getEsperantaRetradioPresenter();

    // Models
    DictionaryModel getDictionaryModel();
    SettingsModel getSettingsModel();
    PodcastModel getPodcastModel();

}
