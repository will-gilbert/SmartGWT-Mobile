package com.smartgwt.mobile.client.data.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.DSRequest;
import com.smartgwt.mobile.client.data.DSResponse;
import com.smartgwt.mobile.client.widgets.events.Cancellable;

public class ErrorEvent extends GwtEvent<HandleErrorHandler> implements Cancellable {

    private static Type<HandleErrorHandler> TYPE = null;

    @SGWTInternal
    public static Type<HandleErrorHandler> _getType() {
        if (TYPE == null) TYPE = new Type<HandleErrorHandler>();
        return TYPE;
    }

    @SGWTInternal
    public static <S extends HasHandleErrorHandlers> boolean _fire(S source, DSRequest dsRequest, DSResponse dsResponse) {
        if (TYPE != null) {
            final ErrorEvent event = new ErrorEvent(dsRequest, dsResponse);
            source.fireEvent(event);
            return event.cancelled;
        }
        return false;
    }

    private boolean cancelled = false;
    private DSRequest dsRequest;
    private DSResponse dsResponse;

    private ErrorEvent(DSRequest dsRequest, DSResponse dsResponse) {
        this.dsRequest = dsRequest;
        this.dsResponse = dsResponse;
    }

    public final DSRequest getRequest() {
        return dsRequest;
    }

    public final DSResponse getResponse() {
        return dsResponse;
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
    public Type<HandleErrorHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(HandleErrorHandler handler) {
        handler.onHandleError(this);
    }
}
