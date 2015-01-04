package org.informagen.mobileeo.client.presenters;

// EO Vortaro - Application
import org.informagen.mobileeo.client.application.Presenter;
import org.informagen.mobileeo.client.application.Callback;
import org.informagen.mobileeo.client.events.VisitWebPageEvent;

// EO Vortaro - JSO
import org.informagen.mobileeo.jso.WordOfTheDay;

// SmartGWT Mobile
import com.smartgwt.mobile.client.widgets.Panel;

// GWT
import com.google.gwt.event.shared.EventBus;

// Google Inject Annotation
import com.google.inject.Inject;


public class WordOfTheDayPresenter implements Presenter {

    private static final String attributionText = "Difinoj provizatas per Lernu.net";
    private static final String attributionURL = "http://lernu.net/lernado/vortoj/tagovortoj/";

    //---------------------------------------------------------------------------------------------

    public interface View {
        void setDelegate(WordOfTheDayPresenter delegate);
        void setAttribution(String text);
        void clear();
        void display(WordOfTheDay wordOfTheDay);
        Panel asPanel();
    }

    public interface Model {
        void wordOfTheDay(final Callback<WordOfTheDay> callback);
    }

    //---------------------------------------------------------------------------------------------
    
    final EventBus eventBus;
    final View view;
    final Model model;
    
    @Inject
     public WordOfTheDayPresenter(EventBus eventBus, View view, Model model ) {
        this.eventBus = eventBus;
        this.view = view;
        this.model = model;

        view.setAttribution(attributionText);
        view.setDelegate(this);

        fetchWordOfTheDay();       
    }

    // Presenter ---------------------------------------------------------------------

    @Override
    public Panel getPanel() {
        return view.asPanel();
    }

    // -------------------------------------------------------------------------------

    public void visitWebPage() {
        eventBus.fireEvent(new VisitWebPageEvent(attributionURL));
    }
    
    private void fetchWordOfTheDay() {

        view.clear();
        
        model.wordOfTheDay(new Callback<WordOfTheDay>() {
            public void onSuccess(WordOfTheDay wordOfTheDay) {
                view.display(wordOfTheDay);
            }
            
        });
        
    }
}
