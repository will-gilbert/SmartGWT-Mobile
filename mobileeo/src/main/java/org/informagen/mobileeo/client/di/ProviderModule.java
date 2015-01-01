package org.informagen.mobileeo.client.di;


// EOVortaro - Services
import org.informagen.mobileeo.service.DictionaryService;
import org.informagen.mobileeo.service.RVPodcastService;


// EOVortaro - GIN Providers
import org.informagen.mobileeo.client.di.providers.EventBusProvider;
import org.informagen.mobileeo.client.di.providers.NavStackProvider;
import org.informagen.mobileeo.client.di.providers.DictionaryServiceProvider;
import org.informagen.mobileeo.client.di.providers.RVPodcastServiceProvider;

//SmartGWT Mobile
import com.smartgwt.mobile.client.widgets.layout.NavStack;

// GWT - Event Bus
import com.google.gwt.event.shared.EventBus;

// Google - GIN
import com.google.gwt.inject.client.AbstractGinModule;

// Google - Injection
import com.google.inject.Singleton;

public class ProviderModule extends AbstractGinModule {
    
    @Override
    protected void configure() {
        
        // Application Event Bus --------------------------------------------------------------
        
        bind(EventBus.class)
            .toProvider(EventBusProvider.class)
            .in(Singleton.class);

        bind(NavStack.class)
            .toProvider(NavStackProvider.class)
            .in(Singleton.class);

 
        // Service Providers ------------------------------------------------------------------
        
        bind(DictionaryService.class)
            .toProvider(DictionaryServiceProvider.class)
            .in(Singleton.class);
        
        bind(RVPodcastService.class)
            .toProvider(RVPodcastServiceProvider.class)
            .in(Singleton.class);

    }
 
}
