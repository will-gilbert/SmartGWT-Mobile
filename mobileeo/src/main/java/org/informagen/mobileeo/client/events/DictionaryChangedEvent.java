package org.informagen.mobileeo.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class DictionaryChangedEvent extends GwtEvent<HandlerFor.DictionaryChangedEvent> {

    public static Type<HandlerFor.DictionaryChangedEvent> TYPE = new Type<HandlerFor.DictionaryChangedEvent>();

    // The event can carry properties and methods
    private final String dictionary;

    // Stuff revelent product number into this event
    public DictionaryChangedEvent(String dictionary) {
        this.dictionary = dictionary;
    }

    // Define a method(s) to retrieve the stuffed data
    public String getDictionary() { 
        return dictionary; 
    }


    // GwtEvent abstract methods --------------------------------------------------------------
    @Override
    public Type<HandlerFor.DictionaryChangedEvent> getAssociatedType() { return TYPE; }

    @Override
    protected void dispatch(HandlerFor.DictionaryChangedEvent handler) { handler.process(this); }
}
