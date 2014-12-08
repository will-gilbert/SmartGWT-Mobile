package com.smartgwt.mobile.showcase.client.widgets.other;

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

public class Other extends ScrollablePanel implements RecordNavigationClickHandler {

    private static final String ID_PROPERTY = "_id";
    private static final String TITLE_PROPERTY = "title";
    private String titles[] = {
            "Img",
            "ImgButton",
            "GWT Interoperability"
    };
    private List<Function<Panel>> functions = new ArrayList<Function<Panel>>();
    private Map<String, Function<Panel>> map = new HashMap<String,Function<Panel>>();
    private NavStack navStack;
    private TableView tableView;   
    private static Record createRecord(String id, String title) {
        final Record record = new Record();
        record.setAttribute(ID_PROPERTY, id);
        record.setAttribute(TITLE_PROPERTY, title);
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

    public Other(String title, NavStack navStack) {
        super(title);
        this.navStack = navStack;
        final RecordList data = new RecordList();
        functions.add(new Function<Panel>(){public Panel execute(){return new Imgs(titles[0]);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new ImgButtons(titles[1]);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new WidgetCanvases(titles[2]);}});
        for(int i = 0; i < titles.length; ++i) {
            data.add(createRecord(new Integer(i).toString(),titles[i]));
            map.put(titles[i], functions.get(i));
        }
        tableView = new TableView();
        tableView.setTitleField(TITLE_PROPERTY);
        tableView.setTableMode(TableMode.GROUPED);
        tableView.setShowNavigation(true);
        tableView.setSelectionType(SelectionStyle.SINGLE);
        tableView.setNavigationMode(NavigationMode.WHOLE_RECORD);
        tableView.setParentNavStack(navStack);
        tableView.addRecordNavigationClickHandler(this);
        tableView.setData(data);
        addMember(tableView);
    }

    @Override
    public void onRecordNavigationClick(RecordNavigationClickEvent event) {
        final Record selectedRecord = event.getRecord();
        String title = selectedRecord.getAttribute("title");
        create(title, navStack);
    }
}
