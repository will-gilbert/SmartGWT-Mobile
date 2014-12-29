package com.smartgwt.mobile.client.internal.widgets.form.fields;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.form.fields.DateItem;

@SGWTInternal
public class NativeDateItem extends DateItem {

    public NativeDateItem(String name, String title) {
        super(name, new DateItemImplNative());
        super.setTitle(title);
    }
}
