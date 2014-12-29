package com.smartgwt.mobile.client.widgets.form.fields.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;

public class ChangedEvent extends GwtEvent<ChangedHandler> {

    private static GwtEvent.Type<ChangedHandler> TYPE;

    public static GwtEvent.Type<ChangedHandler> getType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<ChangedHandler>();
        }
        return TYPE;
    }

    public static <S extends HasChangedHandlers> void fire(S source, DynamicForm form, FormItem item, Object value) {
        if (TYPE != null) {
            ChangedEvent event = new ChangedEvent(form, item, value);
            source.fireEvent(event);
        }
    }

    private DynamicForm form;
    private FormItem item;
    private Object value;

    public ChangedEvent(DynamicForm form, FormItem item, Object value) {
        this.form = form;
        this.item = item;
        this.value = value;
    }

    @Override
    public final GwtEvent.Type<ChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ChangedHandler handler) {
        handler.onChanged(this);
    }

    /**
     * 
     * @return the form to which the changed form item belongs.
     */
    public DynamicForm getForm() {
        return form;
    }

    /**
     * 
     * @return the form item whose value has changed.
     */
    public FormItem getItem() {
        return item;
    }

    /**
     * 
     * @return the value of the form item after the change.
     */
    public Object getValue() {
        return value;
    }
}
