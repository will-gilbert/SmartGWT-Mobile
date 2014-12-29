package com.smartgwt.mobile.client.widgets.form.events;

import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;

public class SubmitValuesEvent extends GwtEvent<SubmitValuesHandler> {

    private static Type<SubmitValuesHandler> TYPE;

    @SGWTInternal
    public static Type<SubmitValuesHandler> _getType() {
        if (TYPE == null) TYPE = new Type<SubmitValuesHandler>();
        return TYPE;
    }

    @SGWTInternal
    public static void _fire(DynamicForm form) {
        if (TYPE != null) {
            final SubmitValuesEvent event = new SubmitValuesEvent(form.getValues());
            form.fireEvent(event);
        }
    }

    private Map<String, Object> values;

    private SubmitValuesEvent(Map<String, Object> values) {
        this.values = values;
    }

    @Override
    public final Type<SubmitValuesHandler> getAssociatedType() {
        return TYPE;
    }

    public final Map<String, Object> getValuesAsMap() {
        return values;
    }

    @Override
    protected void dispatch(SubmitValuesHandler handler) {
        handler.onSubmitValues(this);
    }
}
