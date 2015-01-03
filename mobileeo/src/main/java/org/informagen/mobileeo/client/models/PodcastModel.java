package org.informagen.mobileeo.client.models;

// EOVortaro - Application
import org.informagen.mobileeo.client.application.Callback;

// EOVortaro - Podcast Presenters
import org.informagen.mobileeo.client.presenters.EsperantaRetradioPresenter;

// EOVortaro - Services
import org.informagen.mobileeo.service.RVPodcastService;

// EOVortaro - DTO
import org.informagen.mobileeo.jso.RVRSSFeed;
import org.informagen.mobileeo.jso.EsperantaRetradio;

// GWT - Utility
import com.google.gwt.core.client.GWT;

// Java - Collections
import java.util.Map;
import java.util.LinkedHashMap;

// Google Inject Annotation
import com.google.inject.Inject;

public class PodcastModel implements  EsperantaRetradioPresenter.Model  {

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
