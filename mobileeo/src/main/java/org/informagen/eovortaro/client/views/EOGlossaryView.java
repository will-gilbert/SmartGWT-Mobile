package org.informagen.eovortaro.client.views;

import org.informagen.eovortaro.client.presenters.EOGlossaryPresenter;

import org.informagen.eovortaro.client.application.Callback;

// EOVortaro - JSO
import org.informagen.eovortaro.jso.Definition;

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


public class EOGlossaryView implements EOGlossaryPresenter.View {
    
    final private Panel panel = new ScrollablePanel("EO Vortaro");

    final private DynamicForm form = new DynamicForm();
    final private SearchItem searchItem = new SearchItem("search", "Search", "Search Term");
    final private Panel displayPanel = new Panel();
    final private Panel attibutionPanel = new Panel();

    Callback<String> searchTermCallback = null;
    
	public EOGlossaryView() {
        buildUI();
        wireUI();

        displayPanel.setVisible(false);
        attibutionPanel.setVisible(false);
	}

    // WordLookupPresenter.View ---------------------------------------------------------------------

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
 
    public void display(Definition definition) {  
                      
        int count = 0;
        
        if(definition.meanings() != null)
            count = definition.meanings().length();
        
        if(count == 0 || (definition.propono() != null)) {
            
            String used = new StringBuilder()
                .append("<span style='font-weight:bold;font-size:larger;'>")
                .append(definition.propono() == null ? definition.vorto() : definition.propono() )
                .append("</span>")
                .toString()
            ;
            
            // Box box = new RoundedBox();
            // box.add(new TextBox(used));
            // resultsBox.add(box);
        }
                 
        if(definition.success())
            displaySuccess(definition);
        else
            displayFailure();
        
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

    void displayFailure() {
        displayPanel.setContents("<div style='text-align:center;font-size:xx-large;color:red;'>0</div>");
    }
    
    void displaySuccess(Definition definition) {

        int count = definition.meanings().length();

        StringBuilder builder = new StringBuilder();
        
        for(int i=0; i<count; i++) {
                        
            String found = new StringBuilder()
                .append("<span style='font-weight:bold;font-size:x-large;'>")
                .append(definition.meanings().get(i).found())
                .append("</span>")
                .toString()
            ;
 
             builder.append(found.toString());
           
            String meaning = new StringBuilder()
                .append("<div style='margin-left:3em;margin-top:0.5em;margin-bottom:1em;font-size:x-large;'>")
                .append(definition.meanings().get(i).meaning())
                .append("</div>")
                .toString()
            ;

            builder.append(meaning.toString()); 
        }

        displayPanel.setContents(builder.toString());
      
    }



}
