package org.informagen.mobileeo.client.views;

import org.informagen.mobileeo.client.presenters.ESPDICPresenter;

import org.informagen.mobileeo.client.application.Callback;

// EOVortaro - JSO
import org.informagen.mobileeo.jso.Espdic;
import org.informagen.mobileeo.jso.ESPDICPair;

// SmartGWT Mobile - Widgets
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;

// SmartGWT Mobile - Input Form
import com.smartgwt.mobile.client.types.FormStyle;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.SearchItem;

import com.smartgwt.mobile.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurHandler;

// GWT - UI
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasText;

// GWT - Core
import com.google.gwt.core.client.JsArray;

// GWT - Simple Logging
import com.google.gwt.core.client.GWT;


public class ESPDICView implements ESPDICPresenter.View {
    
    final private Panel panel = new ScrollablePanel("ESPDIC Glosaro");

    final private DynamicForm form = new DynamicForm();
    final private SearchItem searchItem = new SearchItem("search", "Search", "Search Term");
    final private Panel displayPanel = new Panel();
    final private Panel attibutionPanel = new Panel();

    Callback<String> searchTermCallback = null;
    
	public ESPDICView() {
        buildUI();
        wireUI();

        displayPanel.setVisible(false);
        attibutionPanel.setVisible(false);
	}

    // ESPDICPresenter.View ---------------------------------------------------------------------

    @Override
    public Panel asPanel() {        
        return panel;
    }

    public void clear() {
        searchItem.setValue("");
        displayPanel.setContents("");
    }

    @Override
    public void setAttribution(String text, String url) {
        attibutionPanel.setContents(text);
        attibutionPanel.setVisible(true);
    }

    @Override
    public void setSearchTermCallback(Callback<String> callback) {
        this.searchTermCallback = callback;
    }
 
    public void display(Espdic espdic) {  

        // Exception handling here

        if(espdic.success() == false)
            displayFailure();
        else if(espdic.found() == 0)
            displayNoResults();
        else if(espdic.found() > 0 &&  espdic.entries().length() == 0)
            displayTooManyResults(espdic.found());
        else
            displaySuccess(espdic.entries());
        
        displayPanel.setVisible(true);
    }        

    //-----------------------------------------------------------------------------------------

    void buildUI() {

        form.setFormStyle(FormStyle.STYLE1);
        form.setFields(searchItem);
        panel.addMember(form);

        displayPanel.setStyleName("sc-rounded-panel");
        displayPanel.setMargin(10);
        panel.addMember(displayPanel);

        attibutionPanel.setStyleName("sc-rounded-panel");
        panel.addMember(attibutionPanel);
        
    }


    void wireUI() {

        searchItem.addBlurHandler( new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (searchItem.getValue() != null && searchTermCallback != null) {
                    searchTermCallback.onSuccess(searchItem.getValueAsString());
                }
            }
        });
    }

   void displayNoResults() {

        String message = new StringBuilder()
            .append("<div style='text-align:center;font-size:x-large;color:red;'>")
            .append("Nenio Trovita")
            .append("</div>")
        .toString();

        displayPanel.setContents(message);
    }


    void displayTooManyResults(int found) {

        String message = new StringBuilder()
            .append("<div style='text-align:center;font-size:x-large;color:red;'>")
            .append("Tro Multaj Trovita: ")
            .append(found)
            .append("</div>")
        .toString();

        displayPanel.setContents(message);
    }


    void displayFailure() {
        displayPanel.setContents("<div style='text-align:center;font-size:x-large;color:red;'>Server Failed</div>");
    }
    
    void displaySuccess(JsArray<ESPDICPair> entries) {

        int count = entries.length();

        StringBuilder builder = new StringBuilder();
        
        for(int i=0; i<count; i++) {
                        
            String word = new StringBuilder()
                .append("<span style='font-weight:bold;font-size:x-large;'>")
                .append(entries.get(i).word())
                .append("</span>")
                .toString()
            ;

            builder.append(word.toString());
            
            String meaning = new StringBuilder()
                .append("<div style='margin-left:3em;margin-top:0.5em;margin-bottom:1em;font-size:x-large;'>")
                .append(entries.get(i).meaning())
                .append("</div>")
                .toString()
            ;

            builder.append(meaning.toString());

        }

        displayPanel.setContents(builder.toString());

      
    }



}
