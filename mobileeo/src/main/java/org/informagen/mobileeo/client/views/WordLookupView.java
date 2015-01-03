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
import com.google.gwt.user.client.ui.Anchor;


public class WordLookupView implements WordLookupPresenter.View {
    
    // final NavigationButton button = new RightButton().icon("icons/swap.png");

    final private Panel panel = new ScrollablePanel("");

    final private DynamicForm form = new DynamicForm();
    final private SearchItem searchItem = new SearchItem("search", "Search", "Search Term");
    final private Panel displayPanel = new Panel();
    final private Panel attibutionPanel = new Panel();


    Callback<String> searchTermCallback = null;
    
	public WordLookupView() {
        buildUI();
        wireUI();
        clear();

        attibutionPanel.setVisible(false);

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

        displayPanel.setStyleName("word-lookup-panel");
        displayPanel.setMargin(10);

        attibutionPanel.setStyleName("footer-panel");

        // Assemble UI
        panel.addMember(form);
        panel.addMember(displayPanel);
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
