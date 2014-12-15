package com.smartgwt.mobile.showcase.client.widgets;

// GWT
import com.google.gwt.user.client.History;

// SmartGWT Mobile - Core
import com.smartgwt.mobile.client.core.Function;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.NavigationMode;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.TableMode;

// SmartGWT Mobile - Widgets
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.icons.IconResources;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickHandler;

// Showcase Application
import com.smartgwt.mobile.showcase.client.widgets.badges.Badges;
import com.smartgwt.mobile.showcase.client.widgets.buttons.Buttons;
import com.smartgwt.mobile.showcase.client.widgets.databinding.DataBinding;
import com.smartgwt.mobile.showcase.client.widgets.dialogs.Dialogs;
import com.smartgwt.mobile.showcase.client.widgets.forms.Forms;
import com.smartgwt.mobile.showcase.client.widgets.indicators.Indicators;
import com.smartgwt.mobile.showcase.client.widgets.layouts.Layouts;
import com.smartgwt.mobile.showcase.client.widgets.menus.Menus;
import com.smartgwt.mobile.showcase.client.widgets.other.Other;
import com.smartgwt.mobile.showcase.client.widgets.tableviews.TableViews;
import com.smartgwt.mobile.showcase.client.widgets.toolbar.Toolbars;

// Java - Collections
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Widgets extends NavStack {

    private final String titles[] = {
            "Buttons",
            "Badges",
            "Data Binding",
            "Windows & Dialogs",
            "Forms",
            "Layouts",
            "Progress Indicators",
            "TableViews",
            "Toolbars",
            "Menus",
            "Other"
    };
    private final int counts[] = {
       2, 
       1, 
       5, 
       6, 
       7, 
       2, 
       2, 
       11, 
       3, 
       1, 
       3
    };


    private final List<Function<Panel>> functions = new ArrayList<Function<Panel>>();
    private final Map<String, Function<Panel>> map = new HashMap<String,Function<Panel>>();
    private final ScrollablePanel widgets = new ScrollablePanel("Widgets", IconResources.INSTANCE.files());


    public Widgets() {

        super("Widgets", IconResources.INSTANCE.files());

        // List of panels to invoke when clicked
        functions.add(new Function<Panel>(){public Panel execute(){return new Buttons(titles[0],Widgets.this);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new Badges(titles[1]);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new DataBinding(titles[2],Widgets.this);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new Dialogs(titles[3],Widgets.this);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new Forms(titles[4],Widgets.this);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new Layouts(titles[5],Widgets.this);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new Indicators(titles[6],Widgets.this);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new TableViews(titles[7],Widgets.this);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new Toolbars(titles[8],Widgets.this);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new Menus(titles[9],Widgets.this);}});
        functions.add(new Function<Panel>(){public Panel execute(){return new Other(titles[10],Widgets.this);}});
        

        // Build the list of rows (records) to be display by the 'tableView'
        RecordList recordList = new RecordList();
        for(int i = 0; i < titles.length; ++i) {
            recordList.add(createWigetPanelRecord(i, titles[i], counts[i]));
            map.put(titles[i], functions.get(i));
        }
        
        final TableView tableView = new TableView();
        tableView.setTitleField("title");
        tableView.setShowNavigation(true);
        tableView.setSelectionType(SelectionStyle.SINGLE);
        tableView.setShowDetailCount(true);
        tableView.setNavigationMode(NavigationMode.WHOLE_RECORD);
        tableView.setParentNavStack(this);
        tableView.setTableMode(TableMode.GROUPED);
        

        // TableView row click handler
        tableView.addRecordNavigationClickHandler(new RecordNavigationClickHandler() {
            @Override
            public void onRecordNavigationClick(RecordNavigationClickEvent event) {
                if (tableView != event.getTableView()) 
                    return;

                final Record selectedRecord = event.getRecord();
                String title = selectedRecord.getAttribute("title");

                // Create this 'showcase' widget panel and display;
                //  Improvement: Use DI to instance then reuse.
                createPanelAndDisplay(title);
            }
        });
        
        tableView.setData(recordList);
        widgets.addMember(tableView);

        // Push onto the Navigation stack
        push(widgets);
    }


    //  C L A S S   M E T H O D S  =====================================================================================

    private void createPanelAndDisplay(String title) {

        // Get the instance method; Poor man's DI; Great for demos, bad for teaching
        Function<Panel> function = map.get(title);
        
        // Create the 'Showcase' panel
        Panel panel = function.execute();

        // Push onto the Navigation stack
        push(panel);
    }

    protected void parseHistory() {

        String historyToken = History.getToken();
        String currentHistory = buildHistory(stack.size());

        if(historyToken != null && historyToken.length() > 0 && !historyToken.equals(currentHistory)) {
            String path = split(historyToken,"/",1);
            if(path != null && path.length() > 0) {
                createPanelAndDisplay(path);
            }
        }
    }


    // S T A T I C   M E T H O D S   ===================================================================================

    private static Record createWigetPanelRecord(int id, String title, int detailCount) {
        Record record = new Record();
        record.setAttribute("_id", id);
        record.setAttribute("title", title);
        record.setAttribute("detailCount", detailCount);
        return record;
    }

}
