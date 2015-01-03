package org.informagen.mobileeo.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class VisitWebPageEvent extends GwtEvent<HandlerFor.VisitWebPageEvent> {

    public static Type<HandlerFor.VisitWebPageEvent> TYPE = new Type<HandlerFor.VisitWebPageEvent>();

    // The event can carry properties and methods
    private final String url;
    
    public VisitWebPageEvent(String url) {
        this.url = url;
    }
    

    // Define a method(s) to retrieve the stuffed data
    public String getURL() { 
        return url; 
    }
    
    // GwtEvent abstract methods --------------------------------------------------------------
    @Override
    public Type<HandlerFor.VisitWebPageEvent> getAssociatedType() { return TYPE; }

    @Override
    protected void dispatch(HandlerFor.VisitWebPageEvent handler) { handler.process(this); }
}
