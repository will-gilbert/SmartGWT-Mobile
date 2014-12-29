package com.smartgwt.mobile.client.internal.widgets.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ValuesSelectedEvent extends GwtEvent<ValuesSelectedHandler> {

    private static Type<ValuesSelectedHandler> TYPE = null;

    public static Type<ValuesSelectedHandler> getType() {
        if (TYPE == null) TYPE = new Type<ValuesSelectedHandler>();
        return TYPE;
    }

    public static <S extends HasValuesSelectedHandlers & HasHandlers> void fire(S source, Object[] values) {
        if (TYPE != null) {
            final ValuesSelectedEvent event = new ValuesSelectedEvent(values);
            source.fireEvent(event);
        }
    }

    private Object[] values;

    private ValuesSelectedEvent(Object[] values) {
        this.values = values;
    }

    public final Object[] getValues() {
        return values;
    }

    @Override
    public final Type<ValuesSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ValuesSelectedHandler handler) {
        handler._onValuesSelected(this);
    }
}
