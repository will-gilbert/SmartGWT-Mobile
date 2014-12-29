package com.smartgwt.mobile.client.widgets.menu.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.menu.Menu;
import com.smartgwt.mobile.client.widgets.menu.MenuItem;

public class MenuItemClickEvent extends GwtEvent<ClickHandler> {

    private static Type<ClickHandler> TYPE = null;

    public static Type<ClickHandler> getType() {
        if (TYPE == null) TYPE = new Type<ClickHandler>();
        return TYPE;
    }

    @SGWTInternal
    public static void _fire(MenuItem source, Canvas target, Menu menu) {
        if (TYPE != null) {
            final MenuItemClickEvent event = new MenuItemClickEvent(target, menu);
            source.fireEvent(event);
        }
    }

    private Canvas target;
    private Menu menu;

    private MenuItemClickEvent(Canvas target, Menu menu) {
        this.target = target;
        this.menu = menu;
    }

    public final Canvas getTarget() {
        return target;
    }

    public final Menu getMenu() {
        return menu;
    }

    public final MenuItem getItem() {
        final Object source = getSource();
        assert source instanceof MenuItem;
        return (MenuItem)source;
    }

    @Override
    public final Type<ClickHandler> getAssociatedType() {
        assert TYPE != null;
        return TYPE;
    }

    @Override
    protected void dispatch(ClickHandler handler) {
        handler.onClick(this);
    }
}
