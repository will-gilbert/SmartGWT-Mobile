package com.smartgwt.mobile.client.widgets.menu.events;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.events.AbstractCancellableEvent;
import com.smartgwt.mobile.client.widgets.menu.MenuItem;

public class ItemClickEvent extends AbstractCancellableEvent<ItemClickHandler> {

    private static Type<ItemClickHandler> TYPE = null;

    public static Type<ItemClickHandler> getType() {
        if (TYPE == null) TYPE = new Type<ItemClickHandler>();
        return TYPE;
    }

    // Returns whether the event was cancelled.
    @SGWTInternal
    public static <S extends HasItemClickHandlers> boolean _fire(S source, MenuItem item) {
        if (TYPE == null) {
            return false;
        } else {
            final ItemClickEvent event = new ItemClickEvent(item);
            source.fireEvent(event);
            return event.isCancelled();
        }
    }

    private MenuItem item;

    private ItemClickEvent(MenuItem item) {
        this.item = item;
    }

    public final MenuItem getItem() {
        return item;
    }

    @Override
    public final Type<ItemClickHandler> getAssociatedType() {
        assert TYPE != null;
        return TYPE;
    }

    @Override
    protected void dispatch(ItemClickHandler handler) {
        handler.onItemClick(this);
    }
}
