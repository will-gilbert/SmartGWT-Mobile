package com.smartgwt.mobile.client.widgets.events;

import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ValuesSelectedEvent extends GwtEvent<ValuesSelectedHandler> {

    /**
     * Handler type.
     */
    private static GwtEvent.Type<ValuesSelectedHandler> TYPE;
    private Map<Integer,String> values;

    public ValuesSelectedEvent(Map<Integer,String> values) {
        this.values = values;
    }

    /**
     * Fires a open event on all registered handlers in the handler manager.If no
     * such handlers exist, this method will do nothing.
     *
     * @param <S>    The event source
     * @param source the source of the handlers
     */
    public static <S extends HasValuesSelectedHandlers & HasHandlers> void fire(S source, Map<Integer,String> values) {
        if (TYPE != null) {
            ValuesSelectedEvent event = new ValuesSelectedEvent(values);
            source.fireEvent(event);
        }
    }

    /**
     * Gets the type associated with this event.
     *
     * @return returns the handler type
     */
    public static GwtEvent.Type<ValuesSelectedHandler> getType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<ValuesSelectedHandler>();
        }
        return TYPE;
    }


    @Override
    protected void dispatch(ValuesSelectedHandler handler) {
        handler.onValuesSelected(this);
    }

    // Because of type erasure, our static type is
    // wild carded, yet the "real" type should use our I param.

    @Override
    public final GwtEvent.Type<ValuesSelectedHandler> getAssociatedType() {
        return TYPE;
    }


    /**
     * the new value
     *
     * @return the new value
     */
    public Map<Integer,String> getValues() {
        return values;
    }


}
