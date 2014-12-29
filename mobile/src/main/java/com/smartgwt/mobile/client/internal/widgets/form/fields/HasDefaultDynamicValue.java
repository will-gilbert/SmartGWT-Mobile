package com.smartgwt.mobile.client.internal.widgets.form.fields;

import java.util.Map;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.form.DataManager;
import com.smartgwt.mobile.client.widgets.form.fields.HasDataValue;

@SGWTInternal
public interface HasDefaultDynamicValue {

    @SGWTInternal
    public Object _getDefaultDynamicValue(HasDataValue item, DataManager dataManager, Map<String, ?> values);
}
