package com.smartgwt.mobile.showcase.client.widgets.toolbar;

import com.google.gwt.dom.client.Style.Unit;
import com.smartgwt.mobile.client.internal.util.AnimationUtil;
import com.smartgwt.mobile.client.widgets.HRWidget;
import com.smartgwt.mobile.client.widgets.Header1;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.form.fields.EmailItem;
import com.smartgwt.mobile.client.widgets.form.fields.PhoneItem;
import com.smartgwt.mobile.client.widgets.form.fields.SearchItem;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;

public class ToolbarInputs extends ScrollablePanel {

    public ToolbarInputs(String title) {
        super(title);
        this.setWidth("100%");
        final Panel output = new Panel();
        output.setStyleName("sc-rounded-panel");
        output.getElement().getStyle().setProperty("textAlign", "center");
        output.getElement().getStyle().setOpacity(0.0);
        HLayout panelWrapper = new HLayout();
        panelWrapper.setLayoutMargin(20);
        panelWrapper.setPaddingAsLayoutMargin(true);
        panelWrapper.setMembersMargin(20);
        panelWrapper.addMember(output);

        ToolStrip toolbar3 = new ToolStrip();
        toolbar3.setTintColor("#40AA40");
        Header1 header3 = new Header1("Phone");
        header3.getElement().getStyle().setMarginLeft(10, Unit.PX);
        header3.getElement().getStyle().setProperty("textAlign", "left");
        toolbar3.addMember(header3);
        toolbar3.addSpacer();
        final PhoneItem phoneItem = new PhoneItem("phone");
        phoneItem.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (phoneItem.getValue() != null) {
                    output.setContents("Phone input is " + phoneItem.getValue() + " " + (phoneItem.validate() ? "VALID" : "INVALID"));
                    AnimationUtil.fadeTransition(output, true);
                }
            }
        });
        toolbar3.addMember(phoneItem);
        ToolStrip toolbar4 = new ToolStrip();
        toolbar4.setTintColor("#40AA40");
        Header1 header4 = new Header1("Email");
        header4.getElement().getStyle().setMarginLeft(10, Unit.PX);
        header4.getElement().getStyle().setProperty("textAlign", "left");
        toolbar4.addMember(header4);
        toolbar4.addSpacer();
        final EmailItem emailItem = new EmailItem("email");
        emailItem.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (emailItem.getValue() != null) {
                    output.setContents("Email input is " + emailItem.getValue() + " " + (emailItem.validate() ? "VALID" : "INVALID"));
                    AnimationUtil.fadeTransition(output, true);
                }
            }
        });
        toolbar4.addMember(emailItem);
        ToolStrip toolbar5 = new ToolStrip();
        toolbar5.setTintColor("#40AA40");
        Header1 header5 = new Header1("Search");
        header5.getElement().getStyle().setMarginLeft(10, Unit.PX);
        header5.getElement().getStyle().setProperty("textAlign", "left");
        toolbar5.addMember(header5);
        toolbar5.addSpacer();
        final SearchItem searchItem = new SearchItem("search");
        searchItem.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (searchItem.getValue() != null) {
                    output.setContents("Search input is " + searchItem.getValue());
                    AnimationUtil.fadeTransition(output, true);
                }
            }
        });
        toolbar5.addMember(searchItem);
        addMember(toolbar3);
        addMember(toolbar4);
        addMember(toolbar5);
        addMember(new HRWidget());
        addMember(panelWrapper);
    }
}

