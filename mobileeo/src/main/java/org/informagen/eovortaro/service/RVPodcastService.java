package org.informagen.eovortaro.service;

// EOVortaro - Models
import org.informagen.eovortaro.client.application.Callback;

// EOVortaro - JSO
import org.informagen.eovortaro.jso.RVRSSFeed;
import org.informagen.eovortaro.jso.EsperantaRetradio;


// Java - util
import java.util.List;
import java.util.Map;
 
public interface RVPodcastService  {
        
    void fetchPodcastList(Callback<RVRSSFeed> callback);

    void fetchEsperantaRetradio(Callback<EsperantaRetradio> callback);

}
