package com.smartgwt.mobile.client.internal;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperElement;

@SGWTInternal
public final class EventUtil {

    // Mobile Safari for iOS 4.3.2 devices and possibly other browsers can use a Text node for the target
    // of an event.
    public static SuperElement getElement(EventTarget target) {
        if (Element.is(target)) {
            return Element.as(target).<SuperElement>cast();
        } else if (Node.is(target)) {
            final Node n = Node.as(target);
            return n.getParentElement().<SuperElement>cast();
        }
        return null;
    }

    public static SuperElement getRelatedElement(NativeEvent event) {
        return getElement(event.getRelatedEventTarget());
    }

    public static SuperElement getTargetElem(NativeEvent event) {
        return getElement(event.getEventTarget());
    }

    private EventUtil() {}
}
