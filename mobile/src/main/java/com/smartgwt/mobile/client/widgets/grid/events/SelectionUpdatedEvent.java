package com.smartgwt.mobile.client.widgets.grid.events;

import com.google.gwt.event.shared.GwtEvent;

public class SelectionUpdatedEvent extends GwtEvent<SelectionUpdatedHandler> {

    private static Type<SelectionUpdatedHandler> TYPE = null;

    public static Type<SelectionUpdatedHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<SelectionUpdatedHandler>();
        }
        return TYPE;
    }

    public static <S extends HasSelectionUpdatedHandlers> void fire(S source) {
        if (TYPE != null) {
            final SelectionUpdatedEvent event = new SelectionUpdatedEvent();
            source.fireEvent(event);
        }
    }

    @Override
    public Type<SelectionUpdatedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SelectionUpdatedHandler handler) {
        handler.onSelectionUpdated(this);
    }
}
