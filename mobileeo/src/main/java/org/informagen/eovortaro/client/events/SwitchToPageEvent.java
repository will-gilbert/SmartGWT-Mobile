package org.informagen.eovortaro.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class SwitchToPageEvent extends GwtEvent<HandlerFor.SwitchToPageEvent> {

    public static Type<HandlerFor.SwitchToPageEvent> TYPE = new Type<HandlerFor.SwitchToPageEvent>();

    // The event can carry properties and methods
    private final String pageName;
    private final GwtEvent event;
    
    public SwitchToPageEvent(String pageName) {
        this(pageName, null);
    }
    
    public SwitchToPageEvent(String pageName, GwtEvent event) {
        this.pageName = pageName;
        this.event = event;
    }

    // Define a method(s) to retrieve the stuffed data
    public String getPageName() { 
        return pageName; 
    }
    
    public GwtEvent getEvent() { 
        return event; 
    }

    // GwtEvent abstract methods --------------------------------------------------------------
    @Override
    public Type<HandlerFor.SwitchToPageEvent> getAssociatedType() { return TYPE; }

    @Override
    protected void dispatch(HandlerFor.SwitchToPageEvent handler) { handler.process(this); }
}
