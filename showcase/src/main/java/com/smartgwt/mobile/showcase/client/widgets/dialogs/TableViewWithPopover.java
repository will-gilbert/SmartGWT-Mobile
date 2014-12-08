package com.smartgwt.mobile.showcase.client.widgets.dialogs;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.client.core.Rectangle;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.Popover;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.grid.CellFormatter;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickHandler;

public class TableViewWithPopover extends ScrollablePanel {

    private static final String ID_PROPERTY = "id",
            FIRST_NAME_PROPERTY = "first_name",
            LAST_NAME_PROPERTY = "last_name",
            BIO_PROPERTY = "bio";

    private static Record createRecord(String id, String firstName, String lastName, String bio) {
        final Record record = new Record();
        record.setAttribute(ID_PROPERTY, id);
        record.setAttribute(FIRST_NAME_PROPERTY, firstName);
        record.setAttribute(LAST_NAME_PROPERTY, lastName);
        record.setAttribute(BIO_PROPERTY, bio);
        return record;
    }

    private TableView presidentsTable;

    private Popover popover;
    private ScrollablePanel bioPanel;

    private HandlerRegistration recordNavigationClickRegistration = null;

    public TableViewWithPopover(String title) {
        super(title);

        final RecordList data = new RecordList();
        data.add(createRecord("1", "George", "Washington", "George Washington was the first President of the United States."));
        data.add(createRecord("2", "John", "Adams", "John Adams was the second President of the United States."));
        data.add(createRecord("3", "Thomas", "Jefferson", "Thomas Jefferson was the third President of the United States."));
        data.add(createRecord("4", "James", "Madison", "James Madison was the fourth President of the United States."));
        data.add(createRecord("5", "James", "Monroe", "James Monroe was the fifth President of the United States."));
        data.add(createRecord("6", "John Quincy", "Adams", "John Quincy Adams was the sixth President of the United States."));
        data.add(createRecord("7", "Andrew", "Jackson", "Andrew Jackson was the seventh President of the United States."));
        data.add(createRecord("8", "Martin", "Van Buren", "Martin Van Buren was the eighth President of the United States."));
        data.add(createRecord("9", "William Henry", "Harrison", "William Henry Harrison was the ninth President of the United States."));
        data.add(createRecord("10", "John", "Tyler", "John Tyler was the tenth President of the United States."));

        final ListGridField fullNameField = new ListGridField("-fullName");
        fullNameField.setCellFormatter(new CellFormatter() {
            @Override
            public String format(Object value, Record record, int rowNum, int fieldNum) {
                final String firstName = record.getAttribute(FIRST_NAME_PROPERTY),
                        lastName = record.getAttribute(LAST_NAME_PROPERTY);
                return "<span style='font-weight:normal'><b>" + lastName + "</b>, " + firstName + "</span>";
            }
        });

        presidentsTable = new TableView();
        presidentsTable.setFields(fullNameField);
        presidentsTable.setTitleField(fullNameField.getName());
        presidentsTable.setTableMode(TableMode.GROUPED);
        presidentsTable.setShowNavigation(true);
        presidentsTable.setData(data);
        addMember(presidentsTable);

        bioPanel = new ScrollablePanel("Bio");
        popover = new Popover("Bio", bioPanel);

        recordNavigationClickRegistration = presidentsTable.addRecordNavigationClickHandler(new RecordNavigationClickHandler() {
            @Override
            public void onRecordNavigationClick(RecordNavigationClickEvent event) {
                final Record record = event.getRecord();
                final Rectangle clientRect = presidentsTable.getRowClientBounds(record);
                if (clientRect != null) {
                    popover.setTitle(record.getAttribute(FIRST_NAME_PROPERTY) + " " + record.getAttribute(LAST_NAME_PROPERTY));
                    bioPanel.setContents("<div style='padding:5px'>" + record.getAttribute(BIO_PROPERTY) + "</div>");
                    popover.showForArea(clientRect);
                }

                // Alternatively, using TableView.getRowTop():
                //final Integer rowTop = presidentsTable.getRowTop(record);
                //if (rowTop != null) {
                //    final int clientY = rowTop.intValue() + presidentsTable.getElement().getOffsetTop() - getScrollTop() + getClientBounds().getTop();
                //    popover.setTitle(record.getAttribute(FIRST_NAME_PROPERTY) + " " + record.getAttribute(LAST_NAME_PROPERTY));
                //    bioPanel.setContents("<div style='padding:5px'>" + record.getAttribute(BIO_PROPERTY) + "</div>");
                //    popover.showForArea(0, clientY, Window.getClientWidth(), presidentsTable.getCellHeight());
                //}
            }
        });
    }

    @Override
    public void destroy() {
        if (recordNavigationClickRegistration != null) {
            recordNavigationClickRegistration.removeHandler();
            recordNavigationClickRegistration = null;
        }
        popover.destroy();
        super.destroy();
    }
}
