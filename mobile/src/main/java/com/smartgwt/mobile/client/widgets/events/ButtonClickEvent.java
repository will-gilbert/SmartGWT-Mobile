package com.smartgwt.mobile.client.widgets.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.client.widgets.Button;

public class ButtonClickEvent extends GwtEvent<ButtonClickHandler> {

    private static Type<ButtonClickHandler> TYPE = null;

    public static Type<ButtonClickHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<ButtonClickHandler>();
        }
        return TYPE;
    }

    public static <S extends HasButtonClickHandlers> void fire(S source, Button button, int index) {
        if (TYPE != null) {
            final ButtonClickEvent event = new ButtonClickEvent(button, index);
            source.fireEvent(event);
        }
    }

    private Button button;
    private int index;

    private ButtonClickEvent(Button button, int index) {
        this.button = button;
        this.index = index;
    }

    public final Button getButton() {
        return button;
    }

    public final int getIndex() {
        return index;
    }

    @Override
    public final Type<ButtonClickHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ButtonClickHandler handler) {
        handler.onButtonClick(this);
    }
}
