package com.smartgwt.mobile.client.internal.widgets.tableview;

import com.google.gwt.user.client.ui.Image;
import com.smartgwt.mobile.client.widgets.icons.IconResources;

public class TableViewImplAndroid extends TableViewImpl {

    @Override
    public Image getDefaultSingleSelectionIcon() {
        return new Image(IconResources.INSTANCE.checkmark());
    }
}
