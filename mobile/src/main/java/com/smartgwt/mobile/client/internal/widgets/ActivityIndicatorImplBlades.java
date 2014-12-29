package com.smartgwt.mobile.client.internal.widgets;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.theme.ios.ActivityIndicatorCssResourceIOS;
import com.smartgwt.mobile.client.widgets.ActivityIndicator;

@SGWTInternal
public class ActivityIndicatorImplBlades extends ActivityIndicatorImpl {

    private static final ActivityIndicatorCssResourceIOS CSS = (ActivityIndicatorCssResourceIOS)ActivityIndicator._CSS;

    public ActivityIndicatorImplBlades() {
        final Element element = getElement();
        final Document document = Document.get();

        DivElement bladeDiv;

        bladeDiv = document.createDivElement();
        bladeDiv.setClassName(CSS.blade1Class());
        element.appendChild(bladeDiv);

        bladeDiv = document.createDivElement();
        bladeDiv.setClassName(CSS.blade2Class());
        element.appendChild(bladeDiv);

        bladeDiv = document.createDivElement();
        bladeDiv.setClassName(CSS.blade3Class());
        element.appendChild(bladeDiv);

        bladeDiv = document.createDivElement();
        bladeDiv.setClassName(CSS.blade4Class());
        element.appendChild(bladeDiv);

        bladeDiv = document.createDivElement();
        bladeDiv.setClassName(CSS.blade5Class());
        element.appendChild(bladeDiv);

        bladeDiv = document.createDivElement();
        bladeDiv.setClassName(CSS.blade6Class());
        element.appendChild(bladeDiv);

        bladeDiv = document.createDivElement();
        bladeDiv.setClassName(CSS.blade7Class());
        element.appendChild(bladeDiv);

        bladeDiv = document.createDivElement();
        bladeDiv.setClassName(CSS.blade8Class());
        element.appendChild(bladeDiv);
    }
}
