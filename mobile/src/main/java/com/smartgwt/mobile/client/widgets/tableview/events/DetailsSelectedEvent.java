package com.smartgwt.mobile.client.widgets.tableview.events;

import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.mobile.client.data.Record;

public class DetailsSelectedEvent extends GwtEvent<DetailsSelectedHandler> {

    private static final Type<DetailsSelectedHandler> TYPE = new Type<DetailsSelectedHandler>();

    private Record record;

    public static Type<DetailsSelectedHandler> getType() {
        return TYPE;
    }

    public DetailsSelectedEvent(Record record) {
        this.setRecord(record);
    }


    public Record getRecord() {
        return this.record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    @Override
    public Type<DetailsSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DetailsSelectedHandler handler) {
        handler.onDetailsSelected(this);
    }
}
