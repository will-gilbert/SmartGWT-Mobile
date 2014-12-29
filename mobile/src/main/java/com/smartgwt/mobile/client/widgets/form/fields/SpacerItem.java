package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.smartgwt.mobile.client.theme.FormCssResource;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;

public class SpacerItem extends FormItem {

    private static final FormCssResource CSS = DynamicForm._CSS;
    private static final int DEFAULT_HEIGHT = 15;
    private static int nextNum = 1;

    private Integer height;

    public SpacerItem() {
        super("$36z-" + Integer.toString(nextNum++), Document.get().createDivElement());
        getElement().addClassName(CSS.spacerItemClass());
        internalSetHeight(DEFAULT_HEIGHT);
    }

    public final Integer getHeight() {
        return height;
    }

    private void internalSetHeight(int height) {
        final int newHeight = Math.min(0, height);
        this.height = newHeight;
        getElement().getStyle().setHeight(newHeight, Style.Unit.PX);
    }

    public void setHeight(int height) {
        internalSetHeight(height);
    }

    @Override
    public boolean validate() {
        return true;
    }
}
