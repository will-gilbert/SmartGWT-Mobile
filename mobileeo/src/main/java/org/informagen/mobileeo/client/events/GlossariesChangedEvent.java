package org.informagen.mobileeo.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class GlossariesChangedEvent extends GwtEvent<HandlerFor.GlossariesChangedEvent> {

    public static Type<HandlerFor.GlossariesChangedEvent> TYPE = new Type<HandlerFor.GlossariesChangedEvent>();

    // The event can carry properties and methods
    public String from;
    public String to;

    // Stuff revelent product number into this event
    public GlossariesChangedEvent(String from, String to) {
        this.from = from;
        this.to = to;
    }

    // GwtEvent abstract methods --------------------------------------------------------------
    @Override
    public Type<HandlerFor.GlossariesChangedEvent> getAssociatedType() { return TYPE; }

    @Override
    protected void dispatch(HandlerFor.GlossariesChangedEvent handler) { handler.process(this); }
}
