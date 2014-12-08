package com.smartgwt.mobile.showcase.client.widgets.forms;

import java.util.HashMap;
import java.util.Map;

import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.NavigationMode;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.Resettable;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickHandler;

public class Forms extends ScrollablePanel {

    private static Record createRecord(String id, String title) {
        Record record = new Record();
        record.setAttribute("_id", id);
        record.setAttribute("title", title);
        return record;
    }

    private final String[] titles = {
        "Inputs",
        "Pickers",
        "Segmented Controls",
        "Sliders",
        "Switch Items",
        "Embedded TableView",
        "Autoconfigured from DataSource"
    };

    private final Map<String, Panel> map = new HashMap<String, Panel>();

    public Forms(String title, final NavStack nav) {
        super(title);

        map.put(titles[0], new Inputs(titles[0]));
        map.put(titles[1], new Pickers(titles[1]));
        map.put(titles[2], new SegmentedControls(titles[2]));
        map.put(titles[3], new Sliders(titles[3]));
        map.put(titles[4], new SwitchItems(titles[4]));
        map.put(titles[5], new EmbeddedTableView(titles[5]));
        map.put(titles[6], new AutoconfiguredFromDataSource("Autoconfigured from DS"));

        final TableView tableView = new TableView();
        final RecordList recordList = new RecordList();
        for (int i = 0; i < titles.length; ++i) {
            recordList.add(createRecord(Integer.toString(i), titles[i]));
        }
        tableView.setTitleField("title");
        tableView.setShowNavigation(true);
        tableView.setSelectionType(SelectionStyle.SINGLE);
        tableView.setNavigationMode(NavigationMode.WHOLE_RECORD);
        tableView.setParentNavStack(nav);
        tableView.setTableMode(TableMode.GROUPED);
        tableView.addRecordNavigationClickHandler(new RecordNavigationClickHandler() {
            @Override
            public void onRecordNavigationClick(RecordNavigationClickEvent event) {
                final Record selectedRecord = event.getRecord();
                String title = selectedRecord.getAttribute("title");
                create(title, nav);
            }
        });
        tableView.setData(recordList);
        addMember(tableView);
    }

    private void create(String title, NavStack nav) {
        final Panel panel = map.get(title);
        if (panel != null) {
            if (panel instanceof Resettable) ((Resettable)panel).reset();
            nav.push(panel);
        }
    }
}
