package com.smartgwt.mobile.showcase.client.widgets.badges;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.form.fields.SwitchItem;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.mobile.client.widgets.tab.TabSet;

public class Badges extends ScrollablePanel {
    DynamicForm dynamicForm = new DynamicForm();
    SwitchItem tab0 = new SwitchItem("tab0", "Set Tab 0");
    SwitchItem tab1 = new SwitchItem("tab1", "Set Tab 1");
    SwitchItem tab2 = new SwitchItem("tab2", "Set Tab 2");
    TabSet tabset = (TabSet) RootLayoutPanel.get().getWidget(0);

    public Badges(String title) {
        super(title);
        this.setWidth("100%");
        tab0.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if(tab0.isChecked()) {
                    tabset.getTab(0).setBadge("3");
                } else {
                    tabset.getTab(0).removeBadge();
                }
            }
        });
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
        dynamicForm.setFields(new FormItem[] { tab0, tab1, tab2 });
        addMember(dynamicForm);

    }
}
