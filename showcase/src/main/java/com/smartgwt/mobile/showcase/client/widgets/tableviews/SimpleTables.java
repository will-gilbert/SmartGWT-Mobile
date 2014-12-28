package com.smartgwt.mobile.showcase.client.widgets.tableviews;

import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.showcase.client.widgets.images.ImageResources;

public class SimpleTables extends ScrollablePanel {

    public SimpleTables(String title) {
        super(title);

        this.setWidth("100%");

        TableView tableView = new TableView();
        tableView.setTitleField("title");
        tableView.setShowNavigation(false);
        tableView.setShowIcons(true);
        tableView.setTableMode(TableMode.PLAIN);

        @SuppressWarnings({"serial"})
        RecordList recordList = new RecordList(
            new Record[] {

                new Record() {
                    {
                        setAttribute("_id", "1");
                        setAttribute("title", "People");
                        setAttribute("icon", ImageResources.INSTANCE.ipod());
                    }
                },

                new Record() {
                    {
                        setAttribute("_id", "2");
                        setAttribute("title", "Places");
                        setAttribute("icon", ImageResources.INSTANCE.phone());
                    }
                }

            });


        tableView.setData(recordList);
        
        addMember(tableView);
    }
}
