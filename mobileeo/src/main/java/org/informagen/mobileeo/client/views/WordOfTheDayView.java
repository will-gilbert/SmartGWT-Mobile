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

// GWT - UI
import com.google.gwt.user.client.ui.Anchor;


public class WordOfTheDayView implements WordOfTheDayPresenter.View {

    final Panel panel = new ScrollablePanel("Vorto de l' Tago");
    final Panel roundedPanel = new Panel();
    final Panel attibutionPanel = new Panel();
    
	public WordOfTheDayView() {

        buildUI();
        clear();

        attibutionPanel.setVisible(false);
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
    public void setAttribution(String text, String url) {

        if ( text != null && text.trim().length() > 0 ) {
            Anchor anchor = new Anchor();;
            anchor.setText(text != null ? text : "");

            if ( url != null && url.trim().length() > 0 )
                anchor.setHref(url);

            attibutionPanel.addMember(anchor);
            attibutionPanel.setVisible(true);
        } else
            attibutionPanel.setVisible(false);

    }

    @Override
    public Panel asPanel() {        
        return panel;
    }


    //-----------------------------------------------------------------------------------------

    void buildUI() {
        roundedPanel.setStyleName("word-of-the-day-panel");
        roundedPanel.setMargin(10);
        attibutionPanel.setStyleName("footer-panel");

        panel.addMember(roundedPanel);
        panel.addMember(attibutionPanel);
    }


    void displayFailure() {
        roundedPanel.setContents("<div style='text-align:center;font-size:xx-large;color:red;'>Nenion</div>");
        roundedPanel.setVisible(true);
    }
    
    void displaySuccess(WordOfTheDay wordOfTheDay) {
                    
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
