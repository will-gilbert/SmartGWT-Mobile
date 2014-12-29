package com.smartgwt.mobile.internal.gwt.dom.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.internal.gwt.dom.client.event.TransitionEndHandler;

@SGWTInternal
public class SuperElement extends Element {

    private static native JavaScriptObject createTransitionEndListener(TransitionEndHandler handler) /*-{
        return $entry(function (event) {
            handler.@com.smartgwt.mobile.internal.gwt.dom.client.event.TransitionEndHandler::onTransitionEnd(Lcom/smartgwt/mobile/internal/gwt/dom/client/event/TransitionEndEvent;)(event);
        });
    }-*/;

    protected SuperElement() {}

    public final native ClientRect getBoundingClientRect() /*-{
        return this.getBoundingClientRect();
    }-*/;

    public final native CSSStyleDeclaration getComputedStyle(String pseudoElt) /*-{
        return $wnd.getComputedStyle(this, pseudoElt);
    }-*/;

    public final native HandlerRegistration addEventListener(String type, JavaScriptObject listener, boolean useCapture) /*-{
        this.addEventListener(type, listener, useCapture);
        return @com.smartgwt.mobile.client.internal.widgets.events.NativeHandlerRegistration::new(Lcom/google/gwt/dom/client/Element;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;Z)(this, type, listener, useCapture);
    }-*/;

    public final HandlerRegistration addTransitionEndHandler(TransitionEndHandler handler) {
        return addEventListener(DOMConstants.INSTANCE.getTransitionEndEventType(), createTransitionEndListener(handler), false);
    }

    /**
     * Returns a non-live NodeList of all elements descended from the current element which match
     * the specified CSS selectors.
     */
    public final native NodeList<Element> querySelectorAll(String selectors) /*-{
        return this.querySelectorAll(selectors);
    }-*/;

    public final native void insertAdjacentHTML(InsertAdjacentHTMLPosition position, String html) /*-{
        if (this.insertAdjacentHTML) {
            if (position == null) position = @com.smartgwt.mobile.internal.gwt.dom.client.InsertAdjacentHTMLPosition::AFTER_BEGIN;
            this.insertAdjacentHTML(position.@com.smartgwt.mobile.internal.gwt.dom.client.InsertAdjacentHTMLPosition::getValue()(), html);
        } else {
            var range = this.ownerDocument.createRange();
            range.selectNode(this);
            var docFragment = range.createContextualFragment(html);
            if (position == @com.smartgwt.mobile.internal.gwt.dom.client.InsertAdjacentHTMLPosition::AFTER_END) {
                this.parentNode.insertBefore(docFragment, this.nextSibling);
            } else if (position == @com.smartgwt.mobile.internal.gwt.dom.client.InsertAdjacentHTMLPosition::BEFORE_END) {
                this.appendChild(docFragment);
            } else if (position == @com.smartgwt.mobile.internal.gwt.dom.client.InsertAdjacentHTMLPosition::BEFORE_BEGIN) {
                this.parentNode.insertBefore(docFragment, this);
            } else {
                this.insertBefore(docFragment, this.firstChild);
            }
        }
    }-*/;

    // https://developer.mozilla.org/en-US/docs/Web/API/Element.matches
    public final native boolean matches(String selectorString) /*-{
        // Check to see if Element.matches() is available. If so, use it.
        // http://caniuse.com/matchesselector
        var matchesFun = (this.matches ||
                          this.matchesSelector ||
                          this.webkitMatchesSelector ||
                          this.mozMatchesSelector ||
                          this.msMatchesSelector ||
                          this.oMatchesSelector);
        if (matchesFun != null) {
            return matchesFun.call(this, selectorString);

        // Otherwise, use querySelectorAll().
        } else {
            // Determine the context. We can't use this element as the context because querySelectorAll()
            // searches the *subtrees* of the context.
            //
            // This element's parentElement would work, but if that is not available, then use
            // the ownerDocument.
            var context = this.parentElement || this.ownerDocument;
            var matches = context.querySelectorAll(selectorString);
            if (matches != null) {
                for (var i = 0, len = matches.length; i < len; ++i) {
                    if (matches[i] === this) return true;
                }
            }
            return false;
        }
    }-*/;
}
