package com.smartgwt.mobile.showcase.client.widgets.tableviews;

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

public class TableViews extends ScrollablePanel {

    private String titles[] = {
            "Simple Tables",
            "Simple Tables with Edit",
            "Grouped Tables",
            "Grouped Tables with Delete",
            "Grouped Tables with Reorder",
            "Subtitle Tables",
            "Detail Tables",
            "Custom Record Formatting",
            "Record Components",
            "Multiple Selection",
            "Cell Formatters"
    };
    private List<Function<Panel>> functions = new ArrayList<Function<Panel>>();
    private Map<String, Function<Panel>> map = new HashMap<String,Function<Panel>>(); 

    final TableView tableView = new TableView();

    public TableViews(String title, final NavStack nav) {
        super(title);

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

        RecordList recordList = new RecordList();

        functions.add(new Function<Panel>(){public Panel execute(){return new SimpleTables(titles[0]);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new SimpleTablesWithEdit(titles[1]);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new GroupedTables(titles[2]);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new GroupedTablesWithDelete(titles[3]);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new GroupedTablesWithReorder(titles[4]);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new Subtitles(titles[5]);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new Details(titles[6]);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new CustomRecordFormatting(titles[7]);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new RecordComponents(titles[8]);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new MultipleSelection(titles[9],nav);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new CellFormatters(titles[10]);}});

        for(int i = 0; i < titles.length; ++i) {
            recordList.add(createRecord(new Integer(i).toString(),titles[i]));
            map.put(titles[i], functions.get(i));
        }


        tableView.setData(recordList);
        addMember(tableView);
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

    // Static method ===================================================================================================

    private static Record createRecord(String id, String title) {
        Record record = new Record();
        record.setAttribute("_id", id);
        record.setAttribute("title", title);
        return record;
    }


}
