package com.smartgwt.mobile.showcase.client.widgets.tableviews;

import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.FetchMode;
import com.smartgwt.mobile.client.types.NavigationMode;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.mobile.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.mobile.client.widgets.tableview.TableView;

public class GroupedTables extends ScrollablePanel {

    private static Record createRecord(int id, String name, String capitalCity) {
        Record record = new Record();
        record.setAttribute("_id", id);
        record.setAttribute("title", name);
        record.setAttribute("capitalCity", capitalCity);
        return record;
    }

    public GroupedTables(String title) {
        super(title);
        this.setWidth("100%");
        final TableView tableView = new TableView();

        RecordList recordList = new RecordList(
                createRecord(1, "Alabama", "Montgomery"),
                createRecord(2, "Alaska", "Juneau"),
                createRecord(3, "Arizona", "Phoenix"),
                createRecord(4, "Arkansas", "Little Rock"),
                createRecord(5, "California", "Sacramento"),
                createRecord(6, "Colorado", "Denver"),
                createRecord(7, "Connecticut", "Hartford"),
                createRecord(8, "Delaware", "Dover"),
                createRecord(9, "Florida", "Tallahassee"),
                createRecord(10, "Georgia", "Atlanta"),
                createRecord(11, "Hawaii", "Honolulu"),
                createRecord(12, "Idaho", "Boise"),
                createRecord(13, "Illinois", "Springfield"),
                createRecord(14, "Indiana", "Indianapolis"),
                createRecord(15, "Iowa", "Des Moines"),
                createRecord(16, "Kansas", "Topeka"),
                createRecord(17, "Kentucky", "Frankfort"),
                createRecord(18, "Louisiana", "Baton Rouge"),
                createRecord(19, "Maine", "Augusta"),
                createRecord(20, "Maryland", "Annapolis"),
                createRecord(21, "Massachusetts", "Boston"),
                createRecord(22, "Michigan", "Lansing"),
                createRecord(23, "Minnesota", "St. Paul"),
                createRecord(24, "Mississippi", "Jackson"),
                createRecord(25, "Missouri", "Jefferson City"),
                createRecord(26, "Montana", "Helena"),
                createRecord(27, "Nebraska", "Lincoln"),
                createRecord(28, "Nevada", "Carson City"),
                createRecord(29, "New Hampshire", "Concord"),
                createRecord(30, "New Jersey", "Trenton"),
                createRecord(31, "New Mexico", "Santa Fe"),
                createRecord(32, "New York", "Albany"),
                createRecord(33, "North Carolina", "Raleigh"),
                createRecord(34, "North Dakota", "Bismarck"),
                createRecord(35, "Ohio", "Columbus"),
                createRecord(36, "Oklahoma", "Oklahoma City"),
                createRecord(37, "Oregon", "Salem"),
                createRecord(38, "Pennsylvania", "Harrisburg"),
                createRecord(39, "Rhode Island", "Providence"),
                createRecord(40, "South Carolina", "Columbia"),
                createRecord(41, "South Dakota", "Pierre"),
                createRecord(42, "Tennessee", "Nashville"),
                createRecord(43, "Texas", "Austin"),
                createRecord(44, "Utah", "Salt Lake City"),
                createRecord(45, "Vermont", "Montpelier"),
                createRecord(46, "Virginia", "Richmond"),
                createRecord(47, "Washington", "Olympia"),
                createRecord(48, "West Virginia", "Charleston"),
                createRecord(49, "Wisconsin", "Madison"),
                createRecord(50, "Wyoming", "Cheyenne"));
        tableView.setTitleField("title");
        tableView.setShowNavigation(false);
        tableView.setSelectionType(SelectionStyle.SINGLE);
        tableView.setShowSelectedIcon(true);
        tableView.setNavigationMode(NavigationMode.WHOLE_RECORD);
        tableView.setTableMode(TableMode.GROUPED);
        tableView.setDataFetchMode(FetchMode.BASIC);
        tableView.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (event.getState()) {
                    final Record selectedRecord = event.getRecord();
                    final String stateName = selectedRecord.getAttribute("title");
                    String possessiveAdjective = stateName + (stateName.endsWith("s") ? "'" : "'s");
                    SC.say(possessiveAdjective + " capital is " + selectedRecord.getAttribute("capitalCity") + ".");
                }
            }
        });
        tableView.setData(recordList);
        addMember(tableView);
    }
}
