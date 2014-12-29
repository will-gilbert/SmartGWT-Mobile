package org.informagen.eovortaro.client.presenters;

// EO Vortaro - Application
import org.informagen.eovortaro.client.application.Presenter;
import org.informagen.eovortaro.client.application.Callback;

// EO Vortaro - JSO
import org.informagen.eovortaro.jso.WordOfTheDay;

// SmartGWT Mobile
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.util.SC;


// Google Inject Annotation
import com.google.inject.Inject;


public class WordOfTheDayPresenter implements Presenter {  

//---------------------------------------------------------------------------------------------

    public interface View {
        void clear();
        void display(WordOfTheDay wordOfTheDay);
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
