package com.smartgwt.mobile.client.internal.widgets;

import java.util.List;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.Criteria;
import com.smartgwt.mobile.client.data.DSCallback;
import com.smartgwt.mobile.client.data.DSRequest;
import com.smartgwt.mobile.client.data.DSResponse;
import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.events.HasDataChangedHandlers;
import com.smartgwt.mobile.client.types.DSOperationType;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.DataBoundComponent;

@SGWTInternal
public abstract class DataBoundEditorComponent<D extends List</*? extends */Record> & HasDataChangedHandlers> extends DataBoundComponent<D> {

    @Override
    public void fetchData(Criteria criteria, DSCallback callback, DSRequest requestProperties) {
        final DataSource ds = getDataSource();
        if (ds == null) {
            SC.logWarn("Ignoring call to fetchData() on a DynamicForm with no valid dataSource");
            return;
        }
        requestProperties = _buildRequest(requestProperties, DSOperationType.FETCH, callback);
        callback = new DSCallback() {
            @Override
            public void execute(DSResponse dsResponse, Object rawData, DSRequest dsRequest) {
                _fetchDataReply(dsResponse, rawData, dsRequest);
            }
        };
        ds.fetchData(criteria, callback, requestProperties);
    }

    public void _fetchDataReply(DSResponse dsResponse, Object rawData, DSRequest dsRequest) {
        final Record record = dsResponse.getRecord();
        final int status = dsResponse.getStatus();
        if (status == DSResponse.STATUS_SUCCESS ||
            status == DSResponse.STATUS_VALIDATION_ERROR)
        {
            if (record != null) editRecord(record);
        }
    }

    public abstract void editRecord(Record record);
}
