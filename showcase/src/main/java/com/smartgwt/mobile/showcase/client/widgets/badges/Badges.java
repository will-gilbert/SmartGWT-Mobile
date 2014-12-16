package com.smartgwt.mobile.showcase.client.widgets.badges;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.form.fields.SwitchItem;

import com.smartgwt.mobile.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurHandler;

import com.smartgwt.mobile.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.mobile.client.widgets.form.fields.events.ChangedHandler;

import com.smartgwt.mobile.client.widgets.tab.TabSet;

public class Badges extends ScrollablePanel {

    final DynamicForm dynamicForm = new DynamicForm();

    final SwitchItem overviewSwitch = new SwitchItem("overviewSwitch", "Set Overview Badge");
    final SwitchItem tab1 = new SwitchItem("tab1", "Set Widgets Badge");
    final SwitchItem tab2 = new SwitchItem("tab2", "Set About Badge");

    // Access the tab set from the top level container i.e. the
    //   three tab at the bottom of the application: Overview, Widgets, About
    // This is better done with an event to the 'Showcase' class

    TabSet tabset = (TabSet) RootLayoutPanel.get().getWidget(0);

    public Badges(String title) {
        super(title);

        this.setWidth("100%");

        // Reset all badges
        tabset.getTab(0).removeBadge();
        tabset.getTab(1).removeBadge();
        tabset.getTab(2).removeBadge();


/*  Why doesn't this work in the 2014-12-06 release =====================

        // 'Overview' switch
        overviewSwitch.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                if(overviewSwitch.isChecked()) {
                    tabset.getTab(0).setBadge("3");
                } else {
                    tabset.getTab(0).removeBadge();
                }
            }
        });
*/


        // 'Overview' tab
        overviewSwitch.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if(overviewSwitch.isChecked()) {
                    tabset.getTab(0).setBadge("3");
                } else {
                    tabset.getTab(0).removeBadge();
                }
            }
        });

        // 'Widgets' tab
        tab1.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if(tab1.isChecked()) {
                    tabset.getTab(1).setBadge("2");
                } else {
                    tabset.getTab(1).removeBadge();
                }
            }
        });

        // 'About' tab
        tab2.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if(tab2.isChecked()) {
                    tabset.getTab(2).setBadge("10");
                } else {
                    tabset.getTab(2).removeBadge();
                }
            }
        });

        dynamicForm.setFields(new FormItem[] { overviewSwitch, tab1, tab2 });
        
        addMember(dynamicForm);

    }
}
