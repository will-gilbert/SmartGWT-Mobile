package com.smartgwt.mobile.client.internal.widgets.tableview;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.ui.Image;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class TableViewImpl {

    public Image getDefaultSingleSelectionIcon() {
        Image image = new Image("data:image/svg+xml,%3Csvg%20xmlns='http://www.w3.org/2000/svg'%20width='14'%20height='14'%20version='1.1'%3E%3Cg%3E%3Cpath%20style='fill:#324f85;fill-opacity:1;stroke:none'%20d='m%200.5,7%205,7%201.7,0%20L%2013.5,0%2010.8,0%206,10.5%203.5,7%20z'/%3E%3C/g%3E%3C/svg%3E");
        final ImageElement img = image.getElement().cast();
        img.setWidth(14);
        img.setHeight(14);
        return image;
    }
}
