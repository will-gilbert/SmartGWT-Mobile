package com.smartgwt.mobile.client.widgets.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.smartgwt.mobile.client.widgets.Panel;

public class PanelShowEvent extends GwtEvent<PanelShowHandler> implements Cancellable {
    private boolean cancel = false;
    private static GwtEvent.Type<PanelShowHandler> TYPE;
    private Panel panel;
    
    public PanelShowEvent(Panel panel) {
        this.panel = panel;
    }
    
    public static <S extends HasPanelShowHandlers & HasHandlers> void fire(S source, Panel panel) {
        if (TYPE != null) {
            PanelShowEvent event = new PanelShowEvent(panel);
            source.fireEvent(event);
        }
    }
    
    public static GwtEvent.Type<PanelShowHandler> getType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<PanelShowHandler>();
        }
        return TYPE;
    }
    
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PanelShowHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PanelShowHandler handler) {
        handler.onPanelShown(this);
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
