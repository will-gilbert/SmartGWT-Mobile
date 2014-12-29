package com.smartgwt.mobile.client.widgets.form.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;

public class ItemChangedEvent extends GwtEvent<ItemChangedHandler> {

    public static final Type<ItemChangedHandler> TYPE = new Type<ItemChangedHandler>();

    public static <S extends HasItemChangedHandlers> void fire(S source, DynamicForm form, FormItem formItem, Object newValue) {
        ItemChangedEvent event = new ItemChangedEvent(form, formItem, newValue);
        event.setSource(source);
        source.fireEvent(event);
    }

    private DynamicForm form;
    private FormItem formItem;
    private Object newValue;

    public ItemChangedEvent(FormItem formItem, Object newValue) {
        this.formItem = formItem;
        this.newValue = newValue;
    }

    public ItemChangedEvent(DynamicForm form, FormItem formItem, Object newValue) {
        this.form = form;
        this.formItem = formItem;
        this.newValue = newValue;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ItemChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ItemChangedHandler handler) {
        handler.onItemChanged(this);
    }

    public final DynamicForm getForm() {
        return form;
    }

    public final FormItem getFormItem() {
        return formItem;
    }

    public final Object getNewValue() {
        return newValue;
    }
}
