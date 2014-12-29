package com.smartgwt.mobile.client.internal.widgets.form.fields;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.form.fields.DateTimeItem;

@SGWTInternal
public class NativeDateTimeItem extends DateTimeItem {

    public NativeDateTimeItem(String name, String title) {
        super(name, new DateTimeItemImplNative());
        super.setTitle(title);
    }
}
