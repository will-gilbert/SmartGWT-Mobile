package com.smartgwt.mobile.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.HasScrollHandlers;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.core.Rectangle;
import com.smartgwt.mobile.client.theme.ScrollablePanelCssResource;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperElement;

/**
 * A widget that can scroll its content.
 * 
 * <p>A <code>ScrollablePanel</code> must be added to a {@link com.smartgwt.mobile.client.widgets.layout.Layout}
 * that will absolutely position the <code>ScrollablePanel</code> {@link com.google.gwt.user.client.ui.UIObject#getElement() element}.
 */
public class ScrollablePanel extends Panel implements Resettable, HasScrollHandlers {

    @SGWTInternal
    public static final ScrollablePanelCssResource _CSS = ThemeResources.INSTANCE.scrollableCSS();

    /**
     * <code>sc-scrollable</code>, the CSS class name that is added to the <code>ScrollablePanel</code>'s
     * {@link com.google.gwt.user.client.ui.UIObject#getElement() element}.
     */
    public static final String COMPONENT_CLASS_NAME = _CSS.scrollablePanelClass();

    private transient ScrollablePanelImpl impl = GWT.create(ScrollablePanelImpl.class);

    private transient DivElement innerDiv;

    private void init() {
        getElement().addClassName(COMPONENT_CLASS_NAME);
        innerDiv = Document.get().createDivElement();
        innerDiv.setClassName(_CSS.scrollablePanelContentClass());
        getElement().appendChild(innerDiv);
        impl.init(this);
    }

	public ScrollablePanel(String title) {
		super(title);
		init();
	}

	public ScrollablePanel(String title, ImageResource icon) {
		super(title, icon);
		init();
	}

    @Override
    public void destroy() {
        impl.destroy(this);
        super.destroy();
    }

    @SGWTInternal
    public com.google.gwt.user.client.Element _getInnerElement() {
        return innerDiv.cast();
    }

    public final Rectangle getClientBounds() {
        return getElement().<SuperElement>cast().getBoundingClientRect().asRectangle();
    }

    public final int getScrollTop() {
        return impl.getScrollTop(this);
    }

    @Override
    protected void add(Widget child, Element container) {
        super.add(child, container);
    }

    @Override
    public void reset() {
        impl.reset(this);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        impl.onLoad(this);
    }

    @Override
    protected void onUnload() {
        impl.onUnload(this);
        super.onUnload();
    }

    final void superOnBrowserEvent(Event event) {
        super.onBrowserEvent(event);
    }

	@Override
	public void onBrowserEvent(Event event) {
        impl.onBrowserEvent(this, event);
	}

	@Override
	public HandlerRegistration addScrollHandler(ScrollHandler handler) {
        return addHandler(handler, com.google.gwt.event.dom.client.ScrollEvent.getType());
	}
}
