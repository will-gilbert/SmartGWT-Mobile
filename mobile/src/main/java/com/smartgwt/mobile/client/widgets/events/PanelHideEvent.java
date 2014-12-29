package com.smartgwt.mobile.client.widgets.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.smartgwt.mobile.client.widgets.Panel;

public class PanelHideEvent extends GwtEvent<PanelHideHandler> implements Cancellable {
    private boolean cancel = false;
    private static GwtEvent.Type<PanelHideHandler> TYPE;
    private Panel panel;
    
    public PanelHideEvent(Panel panel) {
        this.panel = panel;
    }
    
    public static <S extends HasPanelHideHandlers & HasHandlers> void fire(S source, Panel panel) {
        if (TYPE != null) {
            PanelHideEvent event = new PanelHideEvent(panel);
            source.fireEvent(event);
        }
    }
    
    public static GwtEvent.Type<PanelHideHandler> getType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<PanelHideHandler>();
        }
        return TYPE;
    }
    
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PanelHideHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PanelHideHandler handler) {
        handler.onPanelHidden(this);
	}
	
	public Panel getPanel() {
		return panel;
	}
	
    /**
     * false to prevent this event from bubbling to this widget's parent, true or undefined to bubble.
     */
    public void cancel() {
        cancel = true;
    }

    /**
     * @return true if cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

}
