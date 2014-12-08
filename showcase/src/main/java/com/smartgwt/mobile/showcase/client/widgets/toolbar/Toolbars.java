package com.smartgwt.mobile.showcase.client.widgets.toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smartgwt.mobile.client.core.Function;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.NavigationMode;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickHandler;

public class Toolbars extends ScrollablePanel {

    private String titles[] = {
            "Toolbar with Buttons",
            "Toolbar with Segmented Controls",
            "Toolbar with Inputs"
    };
    private List<Function<Panel>> functions = new ArrayList<Function<Panel>>();
    private Map<String, Function<Panel>> map = new HashMap<String,Function<Panel>>();   
    private static Record createRecord(String id, String title) {
        Record record = new Record();
        record.setAttribute("_id", id);
        record.setAttribute("title", title);
        return record;
    }

    private void create(String title, NavStack nav) {
        final Function<Panel> function = map.get(title);
        if (function != null) {
            final Panel panel = function.execute();
            if (panel != null) {
                nav.push(panel);
            }
        }
    }

    public Toolbars(String title, final NavStack nav) {
        super(title);
        final TableView tableView = new TableView();
        RecordList recordList = new RecordList();
        functions.add(new Function<Panel>(){public Panel execute(){return new ToolbarButtons(titles[0]);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new ToolbarSegmentedControls(titles[1]);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new ToolbarInputs(titles[2]);}});
        for(int i = 0; i < titles.length; ++i) {
            recordList.add(createRecord(new Integer(i).toString(),titles[i]));
            map.put(titles[i], functions.get(i));
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
}
