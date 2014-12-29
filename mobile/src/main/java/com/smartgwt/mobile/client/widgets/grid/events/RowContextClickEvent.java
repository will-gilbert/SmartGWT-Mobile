package com.smartgwt.mobile.client.widgets.grid.events;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.widgets.events.Cancellable;

public class RowContextClickEvent extends GridRowColEvent<RowContextClickHandler> implements Cancellable {

    private static Type<RowContextClickHandler> TYPE = null;

    public static Type<RowContextClickHandler> getType() {
        if (TYPE == null) TYPE = new Type<RowContextClickHandler>();
        return TYPE;
    }

    // Returns whether the event was cancelled.
    @SGWTInternal
    public static <S extends HasRowContextClickHandlers> boolean _fire(S source, int colNum, Record record, int rowNum) {
        if (TYPE == null) {
            return false;
        } else {
            final RowContextClickEvent event = new RowContextClickEvent(colNum, record, rowNum);
            source.fireEvent(event);
            return event.isCancelled();
        }
    }

    private boolean cancelled = false;

    private RowContextClickEvent(int colNum, Record record, int rowNum) {
        super(colNum, record, rowNum);
    }

    @Override
    public final boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public final Type<RowContextClickHandler> getAssociatedType() {
        assert TYPE != null;
        return TYPE;
    }

    @Override
    protected void dispatch(RowContextClickHandler handler) {
        handler.onRowContextClick(this);
    }
}
