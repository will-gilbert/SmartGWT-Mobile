package org.informagen.mobileeo.client.presenters;

// EO Vortaro - Application
import org.informagen.mobileeo.client.application.Presenter;
import org.informagen.mobileeo.client.application.Callback;

// EO Vortaro - JSO
import org.informagen.mobileeo.jso.WordOfTheDay;

// SmartGWT Mobile
import com.smartgwt.mobile.client.widgets.Panel;


// Google Inject Annotation
import com.google.inject.Inject;


public class WordOfTheDayPresenter implements Presenter {

    private static final String attributionText = "Difinoj provizatas per Lernu.net";
    private static final String attributionURL = "http://lernu.net/lernado/vortoj/tagovortoj/";

//---------------------------------------------------------------------------------------------

    public interface View {
        void clear();
        void display(WordOfTheDay wordOfTheDay);
        void setAttribution(String text, String url);
        Panel asPanel();
    }

    public interface Model {
        void wordOfTheDay(final Callback<WordOfTheDay> callback);
    }

//---------------------------------------------------------------------------------------------
    
    final View view;
    final Model model;
    
    @Inject
     public WordOfTheDayPresenter(View view, Model model ) {
        this.view = view;
        this.model = model;

        view.setAttribution(attributionText, attributionURL);

        fetchWordOfTheDay();       
    }

    @Override
    public Panel getPanel() {
        return view.asPanel();
    }

    
    void fetchWordOfTheDay() {

        view.clear();
        
        model.wordOfTheDay(new Callback<WordOfTheDay>() {
            public void onSuccess(WordOfTheDay wordOfTheDay) {
                view.display(wordOfTheDay);
            }
            
        });
        
    }
}
