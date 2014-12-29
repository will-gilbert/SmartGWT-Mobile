package com.smartgwt.mobile.client.widgets.grid;

import com.smartgwt.mobile.client.data.Record;

public interface GroupValueFunction {

    public Object getGroupValue(Object value, Record record, ListGridField field, String fieldName, ListGrid grid);
}
