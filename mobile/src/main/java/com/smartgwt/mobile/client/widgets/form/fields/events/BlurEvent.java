package com.smartgwt.mobile.client.widgets.form.fields.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;

public class BlurEvent extends GwtEvent<BlurHandler> {

    private static GwtEvent.Type<BlurHandler> TYPE;

    public static GwtEvent.Type<BlurHandler> getType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<BlurHandler>();
        }
        return TYPE;
    }

    public static <S extends HasChangedHandlers> void fire(S source, DynamicForm form, FormItem item) {
        if (TYPE != null) {
            BlurEvent event = new BlurEvent(form, item);
            event.setSource(source);
            source.fireEvent(event);
        }
    }

    private DynamicForm form;
    private FormItem item;

    public BlurEvent(DynamicForm form, FormItem item) {
        this.form = form;
        this.item = item;
    }

    @Override
    public final GwtEvent.Type<BlurHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(BlurHandler handler) {
        handler.onBlur(this);
    }

    public DynamicForm getForm() {
        return form;
    }

    public FormItem getItem() {
        return item;
    }
}
