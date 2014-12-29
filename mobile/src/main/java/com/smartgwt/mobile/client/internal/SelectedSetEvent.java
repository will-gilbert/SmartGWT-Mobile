package com.smartgwt.mobile.client.internal;

import com.google.gwt.event.shared.GwtEvent;

public class SelectedSetEvent extends GwtEvent<SelectedSetHandler> {

    private static Type<SelectedSetHandler> TYPE = null;

    public static Type<SelectedSetHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<SelectedSetHandler>();
        }
        return TYPE;
    }

    public static <S extends HasSelectedSetHandlers> void fire(S source, SelectionItem selectionItem, boolean state, boolean previousState) {
        if (TYPE != null) {
            final SelectedSetEvent event = new SelectedSetEvent(selectionItem, state, previousState);
            source.fireEvent(event);
        }
    }

    private SelectionItem selectionItem;
    private boolean state;
    private boolean previousState;

    private SelectedSetEvent(SelectionItem selectionItem, boolean state, boolean previousState) {
        this.selectionItem = selectionItem;
        this.state = state;
        this.previousState = previousState;
    }

    @Override
    public final Type<SelectedSetHandler> getAssociatedType() {
        return TYPE;
    }

    public final SelectionItem getSelectionItem() {
        return selectionItem;
    }

    public final boolean getState() {
        return state;
    }

    public final boolean getPreviousState() {
        return previousState;
    }

    @Override
    protected void dispatch(SelectedSetHandler handler) {
        handler._onSelectedSet(this);
    }
}
