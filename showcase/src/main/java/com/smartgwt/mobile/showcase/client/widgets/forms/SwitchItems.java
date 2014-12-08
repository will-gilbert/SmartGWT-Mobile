package com.smartgwt.mobile.showcase.client.widgets.forms;

import com.smartgwt.mobile.client.internal.util.AnimationUtil;
import com.smartgwt.mobile.client.widgets.HRWidget;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.SwitchItem;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.mobile.client.widgets.layout.HLayout;

public class SwitchItems extends ScrollablePanel {
    DynamicForm dynamicForm = new DynamicForm();
    HLayout panelWrapper = new HLayout();
    SwitchItem switchItem = new SwitchItem("switchitem", "Switch Item");
    Panel output = new Panel();

    public SwitchItems(String title) {
        super(title);
        this.setWidth("100%");
        output.setStyleName("sc-rounded-panel");
        output.getElement().getStyle().setProperty("textAlign", "center");
        output.getElement().getStyle().setOpacity(0.0);
        panelWrapper.setLayoutMargin(20);
        panelWrapper.setPaddingAsLayoutMargin(true);
        panelWrapper.setMembersMargin(20);
        panelWrapper.addMember(output);
        switchItem.setOffText("No");
        switchItem.setOnText("Yes");
        switchItem.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                output.setContents("Switch Item is " + (switchItem.isChecked() ? "YES" : "NO"));
                AnimationUtil.fadeTransition(output, true);
            }
        });
        dynamicForm.setFields(switchItem);
        addMember(dynamicForm);
        addMember(new HRWidget());
        addMember(panelWrapper);
    }

    @Override
    public void reset() {
        super.reset();
        dynamicForm.clearValues();
        output.getElement().getStyle().setOpacity(0.0);
    }
}
