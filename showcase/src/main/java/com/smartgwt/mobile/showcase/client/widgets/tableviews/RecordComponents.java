package com.smartgwt.mobile.showcase.client.widgets.tableviews;

import com.google.gwt.dom.client.Style;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.NavigationMode;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.Label;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.form.fields.SwitchItem;
import com.smartgwt.mobile.client.widgets.tableview.TableView;

public class RecordComponents extends ScrollablePanel {

    public static final String SERVER_PROPERTY = "name",
            SERVER_IPV4_PROPERTY = "ipv4Address",
            SERVER_BACKUPS_ARE_ENABLED_PROPERTY = "enableBackups";

    private static class MyTableView extends TableView {

        private static class RecordComponent extends Panel {

            private Record serverRecord;

            private SwitchItem enableBackupsSwitch;
            private Label nameLabel, ipLabel;

            public RecordComponent(Record serverRecord) {
                setStyleName("sc-record-title");
                Style style = getElement().getStyle();
                style.setOverflow(Style.Overflow.VISIBLE);
                style.setWidth(100, Style.Unit.PCT);

                this.serverRecord = serverRecord;

                enableBackupsSwitch = new SwitchItem("enabled");
                style = enableBackupsSwitch.getElement().getStyle();
                style.setFloat(Style.Float.RIGHT);
                Boolean backupsAreEnabled = serverRecord.getAttributeAsBoolean(SERVER_BACKUPS_ARE_ENABLED_PROPERTY);
                enableBackupsSwitch.setChecked(backupsAreEnabled == null ? false : backupsAreEnabled.booleanValue());
                addMember(enableBackupsSwitch);

                nameLabel = new Label(serverRecord.getAttribute(SERVER_PROPERTY));
                nameLabel.getElement().setClassName("app-info-label");
                addMember(nameLabel);
                ipLabel = new Label("<code>" + serverRecord.getAttribute(SERVER_IPV4_PROPERTY) + "</code>");
                ipLabel.getElement().setClassName("app-info-label");
                style = ipLabel.getElement().getStyle();
                style.setDisplay(Style.Display.BLOCK);
                style.setFontSize(12, Style.Unit.PX);
                addMember(ipLabel);
            }
        }

        public MyTableView() {
        }

        @Override
        public final boolean getShowRecordComponents() {
            return true;
        }

        @Override
        protected Canvas createRecordComponent(Record serverRecord) {
            return new RecordComponent(serverRecord);
        }
    }

    private static Record createServerRecord(String name, String ipv4Address, boolean backupsAreEnabled) {
        Record record = new Record();
        record.setAttribute(SERVER_PROPERTY, name);
        record.setAttribute(SERVER_IPV4_PROPERTY, ipv4Address);
        record.setAttribute(SERVER_BACKUPS_ARE_ENABLED_PROPERTY, backupsAreEnabled);
        return record;
    }

    public RecordComponents(String title) {
        super(title);

        RecordList serverRecords = new RecordList(
            createServerRecord("dallas", "192.13.4.92", true),
            createServerRecord("houston", "192.13.4.90", false),
            createServerRecord("sanfran", "192.99.0.200", false),
            createServerRecord("boston", "192.82.54.203", true),
            createServerRecord("baltimore", "192.50.21.7", true),
            createServerRecord("seattle", "192.60.0.46", false),
            createServerRecord("nyc1", "192.3.90.5", false),
            createServerRecord("nyc2", "192.3.90.81", true)
        );
        TableView tableView = new MyTableView();
        tableView.setData(serverRecords);
        tableView.setNavigationMode(NavigationMode.NAVICON_ONLY);
        addMember(tableView);
    }
}
