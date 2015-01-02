package org.informagen.mobileeo.client.views;


import org.informagen.mobileeo.client.application.Callback;

import org.informagen.mobileeo.client.presenters.WordLookupPresenter;

// EOVortaro - Application
import org.informagen.mobileeo.client.application.Configuration;

// EOVortaro - Events
import org.informagen.mobileeo.client.events.SwitchToPageEvent;


// EOVortaro - JSO
import org.informagen.mobileeo.jso.Definition;

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
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;

// Alert Logging
import com.smartgwt.mobile.client.util.SC;


public class WordLookupView implements WordLookupPresenter.View {
    
    // final NavigationButton button = new RightButton().icon("icons/swap.png");

    final private Panel panel = new ScrollablePanel("");

    final private DynamicForm form = new DynamicForm();
    final private SearchItem searchItem = new SearchItem("search", "Search", "Search Term");
    final private Panel displayPanel = new Panel();


    Callback<String> searchTermCallback = null;
    
	public WordLookupView() {
        buildUI();
        wireUI();
        clear();
	}

    // WordLookupPresenter.View ---------------------------------------------------------------------

    @Override
    public Panel asPanel() {        
        return panel;
    }

    @Override
    public void setTitle(String title) {
        panel.setTitle(title);
    }


    @Override
    public void setSearchTermCallback(Callback<String> callback) {
        this.searchTermCallback = callback;
    }

    public void clear() {
        searchItem.setValue("");
        displayPanel.setContents("");
        displayPanel.setVisible(false);
    }
 
    @Override
    public void display(Definition definition) {  
                                       
        if(definition.success())
            displaySuccess(definition);
        else
            displayFailure();
        
        displayPanel.setVisible(true);
    }

    // public NavigationButton swapButton() {
    //     return button;
    // }
        
    //-----------------------------------------------------------------------------------------

    void buildUI() {

        form.setFormStyle(FormStyle.STYLE1);

        form.setFields(searchItem);
        panel.addMember(form);

        displayPanel.setStyleName("word-lookup-panel");
        displayPanel.setMargin(10);

        panel.addMember(displayPanel);
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

        // This handler does not work
        searchItem.addKeyPressHandler( new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {

                //SC.say(event.toDebugString());

                // if(event.getCharCode().equals( )) {
                //     if (searchItem.getValue() != null && searchTermCallback != null) {
                //         searchTermCallback.onSuccess(searchItem.getValueAsString());
                //     }
                // }
            }
        });

    }



    void displayFailure() {
        displayPanel.setContents("<div style='text-align:center;font-size:xx-large;color:red;'>0</div>");
    }
    
    void displaySuccess(Definition definition) {

        int count = definition.meanings().length();
        
        for(int i=0; i<count; i++) {
                        
            String found = new StringBuilder()
                .append("<span style='font-weight:bold;font-size:x-large;'>")
                .append(definition.meanings().get(i).found())
                .append("</span>")
                .toString()
            ;
            
            String meaning = new StringBuilder()
                .append("<div style='margin-left:3em;margin-top:1em;font-size:x-large;'>")
                .append(definition.meanings().get(i).meaning())
                .append("</div>")
                .toString()
            ;
            
            displayPanel.setContents(found + meaning);
        }
      
    }



}
