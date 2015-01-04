package org.informagen.mobileeo.client.views;

import org.informagen.mobileeo.client.presenters.EOGlossaryPresenter;

import org.informagen.mobileeo.client.application.Callback;

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

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;


public class EOGlossaryView implements EOGlossaryPresenter.View {
    
    final private Panel panel = new ScrollablePanel("EO Vortaro");

    final private DynamicForm form = new DynamicForm();
    final private SearchItem searchItem = new SearchItem("search", "Search", "Search Term");
    final private Panel displayPanel = new Panel();
    final private Panel attibutionPanel = new Panel();
    final private Anchor anchor = new Anchor();

    EOGlossaryPresenter delegate = null;
    
	public EOGlossaryView() {
        buildUI();
        wireUI();

        displayPanel.setVisible(false);
        attibutionPanel.setVisible(false);
	}

    // EOGlossaryPresenter.View ---------------------------------------------------------------------

    @Override
    public void setDelegate(EOGlossaryPresenter delegate) {
        this.delegate = delegate;
    }

    @Override
    public Panel asPanel() {        
        return panel;
    }

    public void clear() {
        searchItem.setValue("");
        displayPanel.setContents("");
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

        displayPanel.setStyleName("eo-glossary-panel");
        displayPanel.setMargin(10);
        panel.addMember(displayPanel);

        attibutionPanel.setStyleName("footer-panel");
        panel.addMember(attibutionPanel);
        
    }


    void wireUI() {

        searchItem.addBlurHandler( new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (searchItem.getValue() != null && delegate != null) {
                    delegate.lookupWord(searchItem.getValueAsString());
                }
            }
        });

        anchor.addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                if(delegate != null)
                    delegate.visitWebPage();
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
