package com.smartgwt.mobile.client.internal.widgets.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.Panel;

@SGWTInternal
public class BeforePanelShownEvent extends GwtEvent<BeforePanelShownHandler> {

    private static Type<BeforePanelShownHandler> TYPE = null;

    public static Type<BeforePanelShownHandler> getType() {
        if (TYPE == null) TYPE = new Type<BeforePanelShownHandler>();
        return TYPE;
    }

    public static <S extends HasBeforePanelShownHandlers> void fire(S source, Panel panel) {
        if (TYPE != null) {
            final BeforePanelShownEvent event = new BeforePanelShownEvent(panel);
            source.fireEvent(event);
        }
    }

    private Panel panel;

    private BeforePanelShownEvent(Panel panel) {
        this.panel = panel;
    }

    public final Panel getPanel() {
        return panel;
    }

    @Override
    public Type<BeforePanelShownHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(BeforePanelShownHandler handler) {
        handler._onBeforePanelShown(this);
    }
}
