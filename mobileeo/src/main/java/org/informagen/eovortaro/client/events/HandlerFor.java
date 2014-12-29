package org.informagen.eovortaro.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface HandlerFor {

    public interface SwitchToPageEvent extends EventHandler {
        void process(org.informagen.eovortaro.client.events.SwitchToPageEvent event);
    }
    
    public interface UpdateHeaderEvent extends EventHandler {
        void process(org.informagen.eovortaro.client.events.UpdateHeaderEvent event);
    }

    public interface DictionaryChangedEvent extends EventHandler {
        void process(org.informagen.eovortaro.client.events.DictionaryChangedEvent event);
    }

    public interface GlossariesChangedEvent extends EventHandler {
        void process(org.informagen.eovortaro.client.events.GlossariesChangedEvent event);
    }

    public interface PlayMP3Event extends EventHandler {
        void process(org.informagen.eovortaro.client.events.PlayMP3Event event);
    }
        
}
