package com.smartgwt.mobile.client.widgets.events;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.events.AbstractCancellableEvent;

public class ShowContextMenuEvent extends AbstractCancellableEvent<ShowContextMenuHandler> {

    private static Type<ShowContextMenuHandler> TYPE = null;

    public static Type<ShowContextMenuHandler> getType() {
        if (TYPE == null) TYPE = new Type<ShowContextMenuHandler>();
        return TYPE;
    }

    // Returns whether the event was cancelled.
    @SGWTInternal
    public static <S extends HasShowContextMenuHandlers> boolean _fire(S source) {
        if (TYPE == null) {
            return false;
        } else {
            final ShowContextMenuEvent event = new ShowContextMenuEvent();
            source.fireEvent(event);
            return event.isCancelled();
        }
    }

    private ShowContextMenuEvent() {}

    @Override
    public final Type<ShowContextMenuHandler> getAssociatedType() {
        assert TYPE != null;
        return TYPE;
    }

    @Override
    protected void dispatch(ShowContextMenuHandler handler) {
        handler.onShowContextMenu(this);
    }
}
