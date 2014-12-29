package com.smartgwt.mobile.client.internal.widgets.form.fields;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.form.fields.TimeItem;

@SGWTInternal
public class NativeTimeItem extends TimeItem {

    public NativeTimeItem(String name, String title) {
        super(name, new TimeItemImplNative());
        super.setTitle(title);
    }
}
