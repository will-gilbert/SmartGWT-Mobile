package org.informagen.eovortaro.client.models;

// EOVortaro - Application
import org.informagen.eovortaro.client.application.Callback;

// EOVortaro - Podcast Presenters

// EOVortaro - Services
import org.informagen.eovortaro.service.RVPodcastService;

// EOVortaro - DTO
import org.informagen.eovortaro.jso.RVRSSFeed;
import org.informagen.eovortaro.jso.EsperantaRetradio;

// GWT - Utility
import com.google.gwt.core.client.GWT;

// Java - Collections
import java.util.Map;
import java.util.LinkedHashMap;

// Google Inject Annotation
import com.google.inject.Inject;

public class PodcastModel  {

    // Podcast Services
    final RVPodcastService rvPodcastService;

    @Inject
    public PodcastModel(RVPodcastService rvPodcastService) {
        this.rvPodcastService = rvPodcastService;
    }

    public void fetchRadioVerda(final Callback<RVRSSFeed> callback) {
        rvPodcastService.fetchPodcastList(callback);
    }

    public void fetchEsperantaRetradio(final Callback<EsperantaRetradio> callback) {
        rvPodcastService.fetchEsperantaRetradio(callback);
    }

}
