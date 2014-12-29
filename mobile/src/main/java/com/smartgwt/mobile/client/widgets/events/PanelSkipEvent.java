package com.smartgwt.mobile.client.widgets.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.client.widgets.Panel;

public class PanelSkipEvent extends GwtEvent<PanelSkipHandler> {

    private static GwtEvent.Type<PanelSkipHandler> TYPE;

    public static GwtEvent.Type<PanelSkipHandler> getType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<PanelSkipHandler>();
        }
        return TYPE;
    }

    public static <S extends HasPanelSkipHandlers> void fire(S source, Panel panel) {
        if (TYPE != null) {
            PanelSkipEvent event = new PanelSkipEvent(panel);
            source.fireEvent(event);
        }
    }

    private Panel panel;

    public PanelSkipEvent(Panel panel) {
        this.panel = panel;
    }

    @Override
    public final GwtEvent.Type<PanelSkipHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PanelSkipHandler handler) {
        handler.onPanelSkipped(this);
    }

    public Panel getPanel() {
        return panel;
    }
}
