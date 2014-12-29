package com.smartgwt.mobile.client.internal.widgets.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class PickerHiddenEvent extends GwtEvent<PickerHiddenHandler> {

    private static Type<PickerHiddenHandler> TYPE = null;

    public static Type<PickerHiddenHandler> getType() {
        if (TYPE == null) TYPE = new Type<PickerHiddenHandler>();
        return TYPE;
    }

    public static <S extends HasPickerHiddenHandlers> void fire(S source) {
        if (TYPE != null) {
            final PickerHiddenEvent event = new PickerHiddenEvent();
            source.fireEvent(event);
        }
    }

    private PickerHiddenEvent() {}

    @Override
    public Type<PickerHiddenHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PickerHiddenHandler handler) {
        handler._onPickerHidden(this);
    }
}
