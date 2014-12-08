package com.smartgwt.mobile.showcase.client.widgets.tableviews;

import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.tableview.RecordFormatter;
import com.smartgwt.mobile.client.widgets.tableview.TableView;

public class CustomRecordFormatting extends ScrollablePanel {

    public static final String ID_PROPERTY = "_id",
            CONTENT_PROPERTY = "content";

    private static Record createRecord(String id, String content) {
        final Record record = new Record();
        record.setAttribute(ID_PROPERTY, id);
        record.setAttribute(CONTENT_PROPERTY, content);
        return record;
    }

    public CustomRecordFormatting(String title) {
        super(title);

        TableView tableView = new TableView();
        RecordList data = new RecordList(
                createRecord("1", "<span style='color:#800080;font-size:15pt;font-style:italic;font-weight:bold'>Purple</span>"),
                createRecord("2", "<span style='color:#FF4500;font-size:15pt;font-weight:bold;text-decoration:underline'>Orange</span>"),
                createRecord("3", "<span style='color:#FF0000;font-size:15pt;font-weight:bold'>Red</span>"));
        tableView.setData(data);
        tableView.setRecordFormatter(new RecordFormatter() {

            @Override
            public String format(Record record) {
                return record.getAttribute(CONTENT_PROPERTY);
            }
        });
        addMember(tableView);
    }
}
