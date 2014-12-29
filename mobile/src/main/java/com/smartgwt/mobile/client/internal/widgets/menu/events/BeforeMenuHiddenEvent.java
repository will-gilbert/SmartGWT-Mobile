package com.smartgwt.mobile.client.internal.widgets.menu.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class BeforeMenuHiddenEvent extends GwtEvent<BeforeMenuHiddenHandler> {

    private static Type<BeforeMenuHiddenHandler> TYPE = null;

    public static Type<BeforeMenuHiddenHandler> getType() {
        if (TYPE == null) TYPE = new Type<BeforeMenuHiddenHandler>();
        return TYPE;
    }

    public static <S extends HasBeforeMenuHiddenHandlers> void fire(S source) {
        if (TYPE != null) {
            final BeforeMenuHiddenEvent event = new BeforeMenuHiddenEvent();
            source.fireEvent(event);
        }
    }

    @Override
    public Type<BeforeMenuHiddenHandler> getAssociatedType() {
        assert TYPE != null;
        return TYPE;
    }

    @Override
    protected void dispatch(BeforeMenuHiddenHandler handler) {
        handler._onBeforeMenuHidden(this);
    }
}
