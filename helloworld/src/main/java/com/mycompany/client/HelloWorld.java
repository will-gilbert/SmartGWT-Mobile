package com.mycompany.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.Dialog;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */


public class HelloWorld implements EntryPoint {

    // handles application pages history and transitions
    
    private NavStack navigationStack;

    /**
     * This is the entry point method.
     */
    
    public void onModuleLoad() {
        navigationStack = new NavStack(getColorsView());
        RootLayoutPanel.get().add(navigationStack);
    }

    public Panel getColorsView() {

        Panel panel = new ScrollablePanel("Colors");

        String[] colors = new String[]{ "blue", "red", "yellow", "green", "gray", "white", "black", "pink", "brown" };

        for (String color : colors) {
            Button button = new Button(color);
            button.setTintColor(color);
            button.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    navigationStack.push(getSportsView(((Button)event.getSource()).getTitle()));
                }
            });
            panel.addMember(button);
        }

        return panel;
    }

    public Panel getSportsView(String color) {

        Panel panel = new ScrollablePanel("Sports");

        String[] sports = new String[]{ "Baseball", "Basketball", "Football", "Hockey", "Volleyball" };

        for (String sport : sports) {

            ToolStripButton button = new ToolStripButton(sport);

            button.setInheritTint(true);
            button.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    String sportName = ((ToolStripButton)event.getSource()).getTitle();
                    Dialog dialog = new Dialog("Do you like " + sportName + "?");
                    dialog.setButtons(Dialog.YES, Dialog.NO);
                    dialog.show();
                }
            }); 
            
            ToolStrip toolbar = new ToolStrip();
            toolbar.setTintColor(color);
            toolbar.addMember(button);
            panel.addMember(toolbar);
        }
        return panel;
    }
}

