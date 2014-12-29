package org.informagen.eovortaro.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class UpdateHeaderEvent extends GwtEvent<HandlerFor.UpdateHeaderEvent> {

    public static Type<HandlerFor.UpdateHeaderEvent> TYPE = new Type<HandlerFor.UpdateHeaderEvent>();

    // The event can carry properties and methods
    
    public String title = null;
    public Boolean isHomeBtnVisible = null;

    public UpdateHeaderEvent() {}


    public UpdateHeaderEvent(String title, boolean isHomeBtnVisible) {
        this.title = title;
        this.isHomeBtnVisible = isHomeBtnVisible ? Boolean.TRUE : Boolean.FALSE;
    }

    // Chainable setter
    public UpdateHeaderEvent title(String title) {
        this.title = title;
        return this;
    }
    
    public UpdateHeaderEvent isHomeBtnVisible(boolean isHomeBtnVisible) {
        this.isHomeBtnVisible = isHomeBtnVisible ? Boolean.TRUE : Boolean.FALSE;
        return this;
    }


    // GwtEvent abstract methods ==============================================================
    @Override
    public Type<HandlerFor.UpdateHeaderEvent> getAssociatedType() { return TYPE;}

    @Override
    protected void dispatch(HandlerFor.UpdateHeaderEvent handler) { handler.process(this); }
}
