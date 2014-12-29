package com.smartgwt.mobile.internal.gwt.dom.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleElement;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public final class DOMImplementationCSS {

    private static final DOMImplementationCSS INSTANCE = new DOMImplementationCSS();

    // `styleElem` must be added to the document before this function will work.
    private static native CSSStyleSheet getSheet(StyleElement styleElem) /*-{
        return styleElem.sheet;
    }-*/;

    public static DOMImplementationCSS get() {
        return INSTANCE;
    }

    private DOMImplementationCSS() {}

    public CSSStyleSheet createCSSStyleSheet() {
        return createCSSStyleSheet(null, null);
    }

    public CSSStyleSheet createCSSStyleSheet(String title, String media) {
        final Document document = Document.get();
        final StyleElement styleElem = document.createStyleElement();
        if (title != null) {
            styleElem.setTitle(title);
        }
        if (media != null) {
            styleElem.setMedia(media);
        }
        document.getDocumentElement().appendChild(styleElem);
        return getSheet(styleElem);
    }
}
