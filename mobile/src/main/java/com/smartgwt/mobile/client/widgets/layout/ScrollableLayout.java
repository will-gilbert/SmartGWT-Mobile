package com.smartgwt.mobile.client.widgets.layout;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;

/**
 * A {@link com.smartgwt.mobile.client.widgets.layout.Layout} that is primarily designed for
 * holding a {@link com.smartgwt.mobile.client.widgets.ScrollablePanel}. It is designed to be
 * able to absolutely position the <code>ScrollablePanel</code>'s {@link com.google.gwt.user.client.ui.UIObject#getElement() element}
 * a given number of pixels from the top and bottom of the parent element.
 * 
 * <p>Unlike a <code>ScrollablePanel</code>, a <code>ScrollableLayout</code> does not
 * contain code for scrolling its content.
 * 
 * @see com.smartgwt.mobile.client.widgets.ScrollablePanel
 */
public class ScrollableLayout extends VLayout {

    private int top, bottom;

    /**
     * 
     * @param top the number of pixels from the top of the parent element's content box
     * to position the top of the <code>ScrollableLayout</code> element.
     * @param bottom the number of pixels from the bottom of the parent element's content box
     * to position the bottom of the <code>ScrollableLayout</code> element.
     */
    public ScrollableLayout(int top, int bottom) {
        super();
        this.top = top;
        this.bottom = bottom;

        final Element element = getElement();
        final Style elementStyle = element.getStyle();
        elementStyle.setPosition(Style.Position.ABSOLUTE);
        elementStyle.setOverflow(Style.Overflow.HIDDEN);
        elementStyle.setTop(top, Style.Unit.PX);
        elementStyle.setRight(0, Style.Unit.PX);
        elementStyle.setBottom(bottom, Style.Unit.PX);
        elementStyle.setLeft(0, Style.Unit.PX);
        onLoad();
	}

	public void setTop(int top) {
        this.top = top;
        getElement().getStyle().setTop(top, Style.Unit.PX);
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
        getElement().getStyle().setBottom(bottom, Style.Unit.PX);
    }

    @Override
    public void onLoad() {
        getElement().getStyle().setTop(top, Style.Unit.PX);
        getElement().getStyle().setBottom(bottom, Style.Unit.PX);
    }
}
