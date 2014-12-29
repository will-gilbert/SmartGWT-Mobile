package com.smartgwt.mobile.client.widgets.menu;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.widgets.menu.events.BeforeMenuHiddenEvent;
import com.smartgwt.mobile.client.internal.widgets.menu.events.BeforeMenuHiddenHandler;
import com.smartgwt.mobile.client.internal.widgets.menu.events.HasBeforeMenuHiddenHandlers;
import com.smartgwt.mobile.client.theme.MenuCssResource;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.menu.events.HasItemClickHandlers;
import com.smartgwt.mobile.client.widgets.menu.events.ItemClickEvent;
import com.smartgwt.mobile.client.widgets.menu.events.ItemClickHandler;

public class Menu extends Canvas implements HasItemClickHandlers, HasBeforeMenuHiddenHandlers {

    @SGWTInternal
    public static final MenuCssResource _CSS = ThemeResources.INSTANCE.menuCSS();

    private final MenuImpl impl = GWT.create(MenuImpl.class);

    private List<MenuItem> items = new ArrayList<MenuItem>();

    private transient HandlerRegistration valueChangeRegistration;
    private transient String origHistoryItem;
    private transient String newHistoryItem;

    public Menu() {
        setElement(impl.createElement());
        impl.init(this);
    }

    @Override
    public void destroy() {
        impl.destroyImpl(this);
        super.destroy();
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        impl.onBrowserEvent(this, event);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (_HISTORY_ENABLED) {
            valueChangeRegistration = History.addValueChangeHandler(new ValueChangeHandler<String>() {

                @Override
                public void onValueChange(ValueChangeEvent<String> event) {
                    assert _HISTORY_ENABLED;
                    if (origHistoryItem != null &&
                        origHistoryItem.equals(event.getValue()))
                    {
                        origHistoryItem = null;
                        newHistoryItem = null;
                        _hide();
                    }
                }
            });
        }
    }

    @Override
    public void onUnload() {
        if (_HISTORY_ENABLED) {
            if (valueChangeRegistration != null) {
                valueChangeRegistration.removeHandler();
                valueChangeRegistration = null;
            }
            origHistoryItem = null;
            newHistoryItem = null;
        } else {
            assert valueChangeRegistration == null;
        }
        super.onUnload();
    }

    public void setData(MenuItem... items) {
        this.items.clear();
        if (items != null) {
            final int items_length = items.length;
            for (int i = 0; i < items_length; ++i) {
                if (items[i] == null) throw new NullPointerException("`items[" + i + "]' cannot be null.");
                this.items.add(items[i]);
            }
        }
        impl.setItems(this, this.items);
    }

    public final MenuItem[] getItems() {
        return items.toArray(new MenuItem[items.size()]);
    }

    public final void setItems(MenuItem... items) {
        setData(items);
    }

    // Shows the menu for the given (`x', `y') coordinate, but possibly taking into account
    // a region of interest, defined by (`minX', `minY') to (`maxX', `maxY'), that should
    // remain visible even with the context menu showing on screen.
    @SGWTInternal
    public void _showAt(Canvas target, int x, int y, int minX, int maxX, int minY, int maxY) {
        assert minX <= x && x <= maxX;
        assert minY <= y && y <= maxY;

        if (_HISTORY_ENABLED) {
            origHistoryItem = History.getToken();
            newHistoryItem = origHistoryItem + ";menu";
            History.newItem(newHistoryItem, false);
        }

        impl.showAt(this, target, x, y, minX, maxX, minY, maxY);
    }

    @SGWTInternal
    public void _hide() {
        impl.hide(this);
    }

    @SGWTInternal
    public void _aboutToHide() {
        if (_HISTORY_ENABLED &&
            newHistoryItem != null &&
            newHistoryItem.equals(History.getToken()))
        {
            final String origHistoryItem = this.origHistoryItem;
            this.origHistoryItem = null;
            this.newHistoryItem = null;
            History.back();
        }
    }

    @SGWTInternal
    public void _fireBeforeMenuHiddenEvent() {
        BeforeMenuHiddenEvent.fire(this);
    }

    @Override
    public HandlerRegistration addItemClickHandler(ItemClickHandler handler) {
        return addHandler(handler, ItemClickEvent.getType());
    }

    @Override
    @SGWTInternal
    public HandlerRegistration _addBeforeMenuHiddenHandler(BeforeMenuHiddenHandler handler) {
        return addHandler(handler, BeforeMenuHiddenEvent.getType());
    }
}
