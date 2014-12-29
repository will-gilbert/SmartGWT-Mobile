package com.smartgwt.mobile.client.widgets.grid;

import com.smartgwt.mobile.client.data.Record;

public interface CellFormatter {

    public String format(Object value, Record record, int rowNum, int fieldNum);
}
