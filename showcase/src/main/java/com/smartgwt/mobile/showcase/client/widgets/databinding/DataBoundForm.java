package com.smartgwt.mobile.showcase.client.widgets.databinding;

import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.widgets.HRWidget;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.EmailItem;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;

public class DataBoundForm extends ScrollablePanel {
    DynamicForm dynamicForm = new DynamicForm();
    EmailItem emailItem;
    ToolStrip toolbar = new ToolStrip();

    public DataBoundForm(String title) {
        super(title);
        emailItem = new EmailItem("email","Email", "Enter email");
        emailItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (emailItem.getValue() != null) {
                }
            }
        });
        dynamicForm.setFields(new FormItem[]{emailItem});
        dynamicForm.setDataSource(new DataSource("accountDS"));
        dynamicForm.getDataSource().setDataURL("sampleResponses/validationError");
        toolbar.setAlign(Alignment.CENTER);
        ToolStripButton save = new ToolStripButton("Save");
        save.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dynamicForm.saveData();
            }
        });
        toolbar.addButton(save);
        addMember(dynamicForm);  
        addMember(new HRWidget());
        addMember(toolbar);       
    }
}
