package com.smartgwt.mobile.showcase.client.widgets.tableviews;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.NavigationMode;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.mobile.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.mobile.client.widgets.grid.events.SelectionUpdatedEvent;
import com.smartgwt.mobile.client.widgets.grid.events.SelectionUpdatedHandler;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickHandler;

public class MultipleSelection extends ScrollablePanel implements RecordNavigationClickHandler,
SelectionChangedHandler, SelectionUpdatedHandler {

    public static final String ID_PROPERTY = "_id",
            TITLE_PROPERTY = "title",
            NOTE_PROPERTY = "note";

    public static String getToDoListItemTitle(Record record) {
        return record == null ? null : record.getAttribute(TITLE_PROPERTY);
    }
    public static Record createToDoListItemRecord(int id, String title, String note) {
        final Record record = new Record();
        record.setAttribute(ID_PROPERTY, id);
        record.setAttribute(TITLE_PROPERTY, title);
        record.setAttribute(NOTE_PROPERTY, note);
        return record;
    }

    private class ToDoListItemDetailsPanel extends ScrollablePanel {

        private Panel panel;

        public ToDoListItemDetailsPanel(Record record) {
            super(getToDoListItemTitle(record));

            panel = new Panel();
            panel.setContents(record.getAttribute(NOTE_PROPERTY));
            panel.setStyleName("sc-rounded-panel");
            panel.setStyleName("sc-stretch");
            panel.setMargin(10);
            addMember(panel);
        }
    }

    private NavStack navStack;

    private Panel introduction;
    private DivElement div;
    private TableView tableView;

    public MultipleSelection(String title, NavStack navStack) {
        super(title);
        this.navStack = navStack;

        introduction = new Panel();
        introduction.setContents("<p>Multiple selection is enabled for a <code>TableView</code> by setting the selectionType to <code>SelectionStyle.MULTIPLE</code>.");
        introduction.setStyleName("sc-rounded-panel");
        introduction.setStyleName("sc-stretch");
        introduction.setMargin(10);
        div = Document.get().createDivElement();
        introduction.getElement().appendChild(div);
        addMember(introduction);

        RecordList data = new RecordList(
                createToDoListItemRecord(837, "Check email", "I'm expecting an email from Dave"),
                createToDoListItemRecord(772, "Upgrade Fedora server", "Make sure to check that the cron scripts are compatible"),
                createToDoListItemRecord(798, "Set up new workstation", "Sally requested Windows with MS Office Word, Excel, PPT"),
                createToDoListItemRecord(840, "Install Xcode", "Now installed through the App Store"));

        tableView = new TableView();
        tableView.setParentNavStack(navStack);
        tableView.setTitleField(TITLE_PROPERTY);
        tableView.setSelectionType(SelectionStyle.MULTIPLE);
        tableView.setShowSelectedIcon(true);
        tableView.setShowNavigation(false);
        tableView.setNavigationMode(NavigationMode.WHOLE_RECORD);
        tableView.setTableMode(TableMode.GROUPED);
        tableView.setData(data);
        tableView.addRecordNavigationClickHandler(this);
        tableView.addSelectionChangedHandler(this);
        tableView.addSelectionUpdatedHandler(this);
        addMember(tableView);
    }

    @Override
    public void onRecordNavigationClick(RecordNavigationClickEvent event) {
        navStack.push(this.new ToDoListItemDetailsPanel(event.getRecord()));
    }

    @Override
    public void onSelectionChanged(SelectionEvent event) {
        if (event.getSource() == tableView) {
            final Record record = event.getRecord();
            final String title = getToDoListItemTitle(record);
            div.setInnerHTML("<b>'" + title + "' was " + (event.getState() ? "selected" : "deselected") + ".</b>");
        }
    }

    @Override
    public void onSelectionUpdated(SelectionUpdatedEvent event) {
        if (event.getSource() == tableView) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Selected records: ");
            final Record[] selectedRecords = tableView.getSelectedRecords();
            int i = 0;
            for (; i < selectedRecords.length; ++i) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(getToDoListItemTitle(selectedRecords[i]));
            }
            if (i == 0) {
                sb.append("(none)");
            }
            SC.logWarn(sb.toString());
        }
    }
}
