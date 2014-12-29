package com.smartgwt.mobile.client.internal.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.events.Cancellable;

@SGWTInternal
public abstract class AbstractCancellableEvent<H extends EventHandler> extends GwtEvent<H> implements Cancellable {

    private boolean cancelled = false;

    protected AbstractCancellableEvent() {}

    @Override
    public final boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public abstract Type<H> getAssociatedType();

    @Override
    protected abstract void dispatch(H handler);
}
