package org.informagen.mobileeo.client.views;

import org.informagen.mobileeo.client.presenters.WordOfTheDayPresenter;

// EOVortaro - Application
import org.informagen.mobileeo.client.application.Configuration;

// EOVortaro - Events
import org.informagen.mobileeo.client.events.SwitchToPageEvent;


// EOVortaro - JSO
import org.informagen.mobileeo.jso.WordOfTheDay;

// SmartGWT Mobile
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;



public class WordOfTheDayView implements WordOfTheDayPresenter.View {

    final Panel panel = new ScrollablePanel("Vorto de l' Tago");
    final Panel roundedPanel = new Panel();
    
	public WordOfTheDayView() {

        roundedPanel.setStyleName("sc-rounded-panel");
        roundedPanel.setMargin(10);

        panel.addMember(roundedPanel);

        clear();

	}

    // WordOfTheDayView.View ---------------------------------------------------------------------

    @Override
    public void clear() {
        roundedPanel.setContents("");
        roundedPanel.setVisible(false);
    }

    @Override
    public void display(WordOfTheDay wordOfTheDay) {  
                         
        if(wordOfTheDay.success())
            displaySuccess(wordOfTheDay);
        else
            displayFailure();
        
        roundedPanel.setVisible(true);
    }

    @Override
    public Panel asPanel() {        
        return panel;
    }


    //-----------------------------------------------------------------------------------------


    void displayFailure() {
        roundedPanel.setContents("<div style='text-align:center;font-size:xx-large;color:red;'>Nenion</div>");
        roundedPanel.setVisible(true);
    }
    
    void displaySuccess(WordOfTheDay wordOfTheDay) {
            
        // Box box = new RoundedBox().backgroundColor("#f5f5dc");
        
        String word = new StringBuilder()
            .append("<span style='font-weight:bold;font-size:x-large;'>")
            .append(wordOfTheDay.word())
            .append("</span>")
            .toString()
        ;
        
        String description = new StringBuilder()
            .append("<div style='margin:1em;font-size:large;'>")
            .append(wordOfTheDay.description())
            .append("</div>")
            .toString()
        ;
        
        roundedPanel.setContents(word + description);
      
    }
   

}
