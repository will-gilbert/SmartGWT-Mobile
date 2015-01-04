package org.informagen.mobileeo.client.views;

import org.informagen.mobileeo.client.application.Callback;
import org.informagen.mobileeo.client.presenters.EsperantaRetradioPresenter;

import org.informagen.mobileeo.client.icons.Icons;

// EOVortaro - JSO
import org.informagen.mobileeo.jso.EsperantaRetradio;

// SmartGWT Mobile
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.types.IconAlign;

// GWT - UI
import com.google.gwt.user.client.ui.Anchor;


public class EsperantaRetradioView implements EsperantaRetradioPresenter.View {
    
    final Panel panel = new ScrollablePanel("Vorto de l' Tago");
    final Button goToWebsiteButton = new Button("Visit Esperanta Retradio", Button.ButtonType.ROUNDED_RECTANGLE);
    final Panel roundedPanel = new Panel();
    final Panel attibutionPanel = new Panel();
    final Anchor anchor = new Anchor();

    EsperantaRetradioPresenter delegate = null;

    
	public EsperantaRetradioView() {
        buildUI();
        wireUI();
        clear();

        attibutionPanel.setVisible(false);
	}

    // EsperantaRetradioPresenter.View ---------------------------------------------------------------------

    // WordOfTheDayView.View ---------------------------------------------------------------------

    @Override
    public void setDelegate(EsperantaRetradioPresenter delegate) {
        this.delegate = delegate;
    }

    @Override
    public void clear() {
        roundedPanel.setContents("");
        roundedPanel.setVisible(false);
    }


    @Override
    public void display(EsperantaRetradio esperantaRetradio) {  
                         
        if(esperantaRetradio.success())
            displaySuccess(esperantaRetradio);
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
        roundedPanel.setStyleName("esperanta-retradio-panel");
        roundedPanel.setMargin(10);
        attibutionPanel.setStyleName("footer-panel");

        panel.addMember(buildWebSitePanel());
        panel.addMember(roundedPanel);
        panel.addMember(attibutionPanel);
    }

   void wireUI() {

        goToWebsiteButton.addClickHandler(new com.smartgwt.mobile.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(com.smartgwt.mobile.client.widgets.events.ClickEvent event) {
                if(delegate != null)
                    delegate.visitWebPage();
            }
        });

        anchor.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
                if(delegate != null)
                    delegate.visitWebPage();
            }

        });

    }

    private Panel buildWebSitePanel() {
        Panel webSitePanel = new Panel();
        goToWebsiteButton.setIcon(Icons.INSTANCE.esperantaRetradio(), false);
        webSitePanel.addMember(goToWebsiteButton);

        // webSitePanel.add(createDownloadMenu("esperanta-retradio", "Esperanta Retradio", "http://aldone.de/retradio/", "Vizitu") );
        return webSitePanel;
    }

    void displayFailure() {
        roundedPanel.setContents("<div style='text-align:center;font-size:xx-large;color:red;'>Nenion</div>");
        roundedPanel.setVisible(true);
    }
    
    void displaySuccess(EsperantaRetradio esperantaRetradio) {
                    
                            String word = new StringBuilder()
            .append("<span style='font-weight:bold;font-size:x-large;'>")
            .append(esperantaRetradio.title())
            .append("</span>")
            .toString()
        ;
        
        String description = new StringBuilder()
            .append("<div style='margin:1em;font-size:large;'>")
            .append(esperantaRetradio.text())
            .append("</div>")
            .toString()
        ;
        
        roundedPanel.setContents(word + description);
      
    }


}
