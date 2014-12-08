package com.smartgwt.mobile.showcase.client.widgets.forms;

import com.smartgwt.mobile.client.internal.util.AnimationUtil;
import com.smartgwt.mobile.client.widgets.HRWidget;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ValueChangedEvent;
import com.smartgwt.mobile.client.widgets.events.ValueChangedHandler;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.form.fields.SliderItem;
import com.smartgwt.mobile.client.widgets.layout.HLayout;

public class Sliders extends ScrollablePanel {
    DynamicForm dynamicForm = new DynamicForm();
    HLayout panelWrapper = new HLayout();
    SliderItem sliderItem = new SliderItem("slideritem", "SliderItem");
    Panel output = new Panel();

    public Sliders(String title) {
        super(title);
        this.setWidth("100%");
        output.setStyleName("sc-rounded-panel");
        output.getElement().getStyle().setProperty("textAlign", "center");
        output.getElement().getStyle().setOpacity(0.0);
        panelWrapper.setLayoutMargin(20);
        panelWrapper.setPaddingAsLayoutMargin(true);
        panelWrapper.setMembersMargin(20);
        panelWrapper.addMember(output);
        sliderItem.addValueChangedHandler(new ValueChangedHandler() {
            @Override
            public void onValueChanged(ValueChangedEvent event) {
                output.setContents("Slider value is "+sliderItem.getValue());
                AnimationUtil.fadeTransition(output, true);
            }
        });
        dynamicForm.setFields(new FormItem[] { sliderItem });
        addMember(dynamicForm);
        addMember(new HRWidget());
        addMember(panelWrapper);
    }
}
