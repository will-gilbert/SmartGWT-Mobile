package com.smartgwt.mobile.showcase.client.widgets.menus;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickHandler;

public class Menus extends ScrollablePanel {

    private static final String ID_PROPERTY = "_id",
            NAME_PROPERTY = "name";

    private static Record createMenuSampleRecord(int id, String name) {
        final Record record = new Record();
        record.setAttribute(ID_PROPERTY, Integer.valueOf(id));
        record.setAttribute(NAME_PROPERTY, name);
        return record;
    }

    private TableView menuSamplesTable;
    private HandlerRegistration recordNavigationClickRegistration;

    public Menus(String title, final NavStack navStack) {
        super(title);

        final RecordList data = new RecordList() {{
            add(createMenuSampleRecord(1, "Basic Context Menu"));
        }};

        menuSamplesTable = new TableView();
        menuSamplesTable.setDefaultPrimaryKeyFieldName(ID_PROPERTY);
        menuSamplesTable.setTitleField(NAME_PROPERTY);
        menuSamplesTable.setTableMode(TableMode.GROUPED);
        menuSamplesTable.setSelectionType(SelectionStyle.SINGLE);
        menuSamplesTable.setParentNavStack(navStack);
        menuSamplesTable.setData(data);
        
        recordNavigationClickRegistration = menuSamplesTable.addRecordNavigationClickHandler(new RecordNavigationClickHandler() {
            @Override
            public void onRecordNavigationClick(RecordNavigationClickEvent event) {
                final Record record = event.getRecord();
                final Integer id = record.getAttributeAsInt(ID_PROPERTY);
                if (id == 1) {
                    navStack.push(new BasicContextMenu());
                }
            }
        });
        addMember(menuSamplesTable);
    }

    @Override
    public void destroy() {
        if (recordNavigationClickRegistration != null) {
            recordNavigationClickRegistration.removeHandler();
            recordNavigationClickRegistration = null;
        }
        super.destroy();
    }
}
