package com.smartgwt.mobile.client.widgets.form.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.SGWTInternal;

public class FormSubmitFailedEvent extends GwtEvent<FormSubmitFailedHandler> {

    private static Type<FormSubmitFailedHandler> TYPE = null;

    public static Type<FormSubmitFailedHandler> getType() {
        if (TYPE == null) TYPE = new Type<FormSubmitFailedHandler>();
        return TYPE;
    }

    @SGWTInternal
    public static <S extends HasFormSubmitFailedHandlers> void _fire(S source) {
        if (TYPE != null) {
            final FormSubmitFailedEvent event = new FormSubmitFailedEvent();
            source.fireEvent(event);
        }
    }

    private FormSubmitFailedEvent() {}

    @Override
    public final Type<FormSubmitFailedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FormSubmitFailedHandler handler) {
        handler.onFormSubmitFailed(this);
    }
}
