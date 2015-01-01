package org.informagen.mobileeo.service;

// EOVortaro - Models
import org.informagen.mobileeo.client.application.Callback;

// EOVortaro - JSO
import org.informagen.mobileeo.jso.RVRSSFeed;
import org.informagen.mobileeo.jso.EsperantaRetradio;


// Java - util
import java.util.List;
import java.util.Map;
 
public interface RVPodcastService  {
        
    void fetchPodcastList(Callback<RVRSSFeed> callback);

    void fetchEsperantaRetradio(Callback<EsperantaRetradio> callback);

}
