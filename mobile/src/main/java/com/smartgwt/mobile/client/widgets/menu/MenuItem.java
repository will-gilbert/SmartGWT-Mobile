package com.smartgwt.mobile.client.widgets.menu;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.client.widgets.menu.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.menu.events.HasClickHandlers;
import com.smartgwt.mobile.client.widgets.menu.events.MenuItemClickEvent;

public class MenuItem implements HasClickHandlers {

    private static final String DEFAULT_TITLE = "&nbsp;";

    private HandlerManager handlerManager = null;
    private boolean enabled = true;
    private String title = DEFAULT_TITLE;

    public MenuItem() {}

    public MenuItem(String title) {
        this.title = title == null ? DEFAULT_TITLE : title;
    }

    public final boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled == null ? true : enabled.booleanValue();
    }

    public final String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? DEFAULT_TITLE : title;
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) handlerManager.fireEvent(event);
    }

    private HandlerManager ensureHandlerManager() {
        if (handlerManager == null) handlerManager = new HandlerManager(this);
        return handlerManager;
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return ensureHandlerManager().addHandler(MenuItemClickEvent.getType(), handler);
    }
}
