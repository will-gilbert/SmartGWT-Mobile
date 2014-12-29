package com.smartgwt.mobile.internal.gwt.dom.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.core.Rectangle;

// http://www.w3.org/TR/cssom-view/#clientrect
@SGWTInternal
public class ClientRect extends JavaScriptObject {

    protected ClientRect() {}

    public final native double getTop() /*-{
        return this.top;
    }-*/;

    public final native double getRight() /*-{
        return this.right;
    }-*/;

    public final native double getBottom() /*-{
        return this.bottom;
    }-*/;

    public final native double getLeft() /*-{
        return this.left;
    }-*/;

    public final native double getWidth() /*-{
        return this.width;
    }-*/;

    public final native double getHeight() /*-{
        return this.height;
    }-*/;

    public final native ClientRect getBorderRect(int borderWidth) /*-{
        var doubleBorderWidth = 2 * borderWidth;
        return {
            top: this.top - borderWidth,
            left: this.left - borderWidth,
            bottom: this.bottom + borderWidth,
            right: this.right + borderWidth,
            width: this.width + doubleBorderWidth,
            height: this.height + doubleBorderWidth
        };
    }-*/;

    public final Rectangle asRectangle() {
        return new Rectangle((int)getLeft(), (int)getTop(), (int)Math.ceil(getWidth()), (int)Math.ceil(getHeight()));
    }

    /**
     * Does this <code>ClientRect</code> fully contain <code>otherRect</code>? This method returns
     * true if this <code>ClientRect</code> is the same rectangle (in other words, this method
     * does not require proper inclusion).
     */
    public final boolean contains(ClientRect otherRect) {
        return (this.getTop() <= otherRect.getTop() &&
                this.getLeft() <= otherRect.getLeft() &&
                otherRect.getBottom() <= this.getBottom() &&
                otherRect.getRight() <= this.getRight());
    }
}
