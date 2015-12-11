package com.smartgwt.mobile.showcase.client.widgets.tableviews;

import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.showcase.client.widgets.images.ImageResources;

public class Details extends ScrollablePanel {

    public Details(String title) {
        super(title);
        setWidth("100%");
        TableView tableView = new TableView();
        @SuppressWarnings({"serial"})
        RecordList recordList = new RecordList(
                new Record() {
                    {
                        setAttribute("_id", "1");
                        setAttribute("title", "Lotto");
                        setAttribute("icon", ImageResources.INSTANCE.ipod());
                        setAttribute("info", "Lotto Winnings");
                        setAttribute("description", "Congratulations you've won the lotto!!");
                    }
                },
                new Record() {
                    {
                        setAttribute("_id", "2");
                        setAttribute("title", "Lotto");
                        setAttribute("icon", ImageResources.INSTANCE.ipod());
                        setAttribute("info", "Re: Lotto Winnings");
                        setAttribute("description", "Would you like it in yearly installments or a lump sum?");
                    }
                });
        tableView.setTitleField("title");
        tableView.setShowNavigation(false);
        tableView.setShowIcons(true);
        tableView.setTableMode(TableMode.GROUPED);
        tableView.setData(recordList);
        addMember(tableView);
    }
}
