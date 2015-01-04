package org.informagen.mobileeo.client.views;

import org.informagen.mobileeo.client.presenters.WordOfTheDayPresenter;

// EOVortaro - Application
import org.informagen.mobileeo.client.application.Configuration;
import org.informagen.mobileeo.client.application.Callback;

// EOVortaro - Events
import org.informagen.mobileeo.client.events.SwitchToPageEvent;


// EOVortaro - JSO
import org.informagen.mobileeo.jso.WordOfTheDay;

// SmartGWT Mobile
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.Label;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;

// GWT - UI
import com.google.gwt.user.client.ui.Anchor;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class WordOfTheDayView implements WordOfTheDayPresenter.View {

    final Panel panel = new ScrollablePanel("Vorto de l' Tago");
    final Panel roundedPanel = new Panel();
    final Panel attibutionPanel = new Panel();
    final Anchor anchor = new Anchor();

    WordOfTheDayPresenter delegate;

	public WordOfTheDayView() {
        buildUI();
        wireUI();
        clear();

        attibutionPanel.setVisible(false);
	}

    // WordOfTheDayPresenter.View ---------------------------------------------------------------------

    @Override
    public void setDelegate(WordOfTheDayPresenter delegate) {
        this.delegate = delegate;
    }

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
    public void setAttribution(String text) {

        if ( text != null && text.trim().length() > 0 ) {
            anchor.setText(text != null ? text : "");

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

    void wireUI(){

        anchor.addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                if(delegate != null)
                    delegate.visitWebPage();
            }

        });

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
