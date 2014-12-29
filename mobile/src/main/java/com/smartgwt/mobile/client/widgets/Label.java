package com.smartgwt.mobile.client.widgets;

import com.google.gwt.dom.client.Document;

public class Label extends Canvas {

    public static final String COMPONENT_CLASS_NAME = "sc-label";

    private String contents;

    public Label() {
        super(Document.get().createDivElement());
        super.setStyleName(COMPONENT_CLASS_NAME);
    }

    public Label(String contents) {
        this();
        internalSetContents(contents); 
    }

    public final String getContents() {
        return contents;
    }

    private void internalSetContents(String newContents) {
        this.contents = newContents;
        getElement().setInnerHTML(newContents);
    }
    @Override
    public void setContents(String newContents) {
        internalSetContents(newContents);
    }
}
