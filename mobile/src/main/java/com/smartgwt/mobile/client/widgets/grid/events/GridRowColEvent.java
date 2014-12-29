package com.smartgwt.mobile.client.widgets.grid.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.client.data.Record;

public abstract class GridRowColEvent<H extends EventHandler> extends GwtEvent<H> {

    private int colNum;
    private Record record;
    private int rowNum;

    protected GridRowColEvent(int colNum, Record record, int rowNum) {
        this.colNum = colNum;
        this.record = record;
        this.rowNum = rowNum;
    }

    public final Record getRecord() {
        return record;
    }

    public final int getRowNum() {
        return rowNum;
    }
}
