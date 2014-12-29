package com.smartgwt.mobile.client.internal;

import java.util.List;

import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.events.HasDataChangedHandlers;

public class RecordSelection extends Selection<Record> {

    public <D extends List</*? extends */Record> & HasDataChangedHandlers> RecordSelection(D data) {
        super(data);
    }

    @Override
    protected Record[] _createSelectionItemArray(int length) {
        return new Record[length];
    }
}
