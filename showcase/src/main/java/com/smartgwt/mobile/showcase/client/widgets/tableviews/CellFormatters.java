package com.smartgwt.mobile.showcase.client.widgets.tableviews;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.grid.CellFormatter;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;
import com.smartgwt.mobile.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.mobile.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.mobile.client.widgets.menu.Menu;
import com.smartgwt.mobile.client.widgets.menu.MenuItem;
import com.smartgwt.mobile.client.widgets.menu.events.ItemClickEvent;
import com.smartgwt.mobile.client.widgets.menu.events.ItemClickHandler;
import com.smartgwt.mobile.client.widgets.tableview.TableView;

public class CellFormatters extends ScrollablePanel {

    private static final String ID_PROPERTY = "id",
            FIRST_NAME_PROPERTY = "first_name",
            LAST_NAME_PROPERTY = "last_name",
            WIKIPEDIA_ARTICLE_TITLE_PROPERTY = "wikipedia_article_title";

    private static Record createRecord(String id, String firstName, String lastName) {
        final Record record = new Record();
        record.setAttribute(ID_PROPERTY, id);
        record.setAttribute(FIRST_NAME_PROPERTY, firstName);
        record.setAttribute(LAST_NAME_PROPERTY, lastName);
        return record;
    }

    private static Record createRecord(String id, String firstName, String lastName, String wikipediaArticleTitle) {
        final Record record = createRecord(id, firstName, lastName);
        record.setAttribute(WIKIPEDIA_ARTICLE_TITLE_PROPERTY, wikipediaArticleTitle);
        return record;
    }

    private TableView presidentsTable;
    private Menu presidentsTableContextMenu = null;
    private MenuItem openWikipediaItem = null;

    private HandlerRegistration rowContextClickRegistration = null,
            itemClickRegistration = null;

    private Record contextClickedRecord = null;

    public CellFormatters(String title) {
        super(title);

        final RecordList data = new RecordList();
        data.add(createRecord("1", "George", "Washington"));
        data.add(createRecord("2", "John", "Adams"));
        data.add(createRecord("3", "Thomas", "Jefferson"));
        data.add(createRecord("4", "James", "Madison"));
        data.add(createRecord("5", "James", "Monroe"));
        data.add(createRecord("6", "John Quincy", "Adams"));
        data.add(createRecord("7", "Andrew", "Jackson"));
        data.add(createRecord("8", "Martin", "Van Buren"));
        data.add(createRecord("9", "William Henry", "Harrison"));
        data.add(createRecord("10", "John", "Tyler"));

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
        presidentsTable.setData(data);

        if (!Canvas.isUIWebView() && !Canvas.isStandAlone()) {
            // Only enable the "Open Wikipedia" feature if Showcase is not running in a UIWebView
            // or as a Web Clip (standalone mode).
            // `window.open' does not work very well in those scenarios because the user cannot
            // return to the Showcase app for lack of a back button.
            presidentsTableContextMenu = new Menu();
            openWikipediaItem = new MenuItem("Open Wikipedia");
            presidentsTableContextMenu.setItems(openWikipediaItem);
            presidentsTable.setContextMenu(presidentsTableContextMenu);
            rowContextClickRegistration = presidentsTable.addRowContextClickHandler(new RowContextClickHandler() {
                @Override
                public void onRowContextClick(RowContextClickEvent event) {
                    contextClickedRecord = event.getRecord();
                }
            });
            itemClickRegistration = presidentsTableContextMenu.addItemClickHandler(new ItemClickHandler() {

                @Override
                public void onItemClick(ItemClickEvent event) {
                    if (contextClickedRecord == null) {
                        event.cancel();
                    } else if (event.getItem() == openWikipediaItem) {
                        String wikipediaArticleTitle = contextClickedRecord.getAttribute(WIKIPEDIA_ARTICLE_TITLE_PROPERTY);
                        if (wikipediaArticleTitle == null) {
                            wikipediaArticleTitle = contextClickedRecord.getAttribute(FIRST_NAME_PROPERTY) + " " +
                                    contextClickedRecord.getAttribute(LAST_NAME_PROPERTY);
                        }
                        wikipediaArticleTitle = wikipediaArticleTitle.replace(' ', '_');
                        Window.open("https://en.wikipedia.org/wiki/" + URL.encodePathSegment(wikipediaArticleTitle), "wikipediaWindow", "location=0,status=0");
                    }
                }
            });
        }
        addMember(presidentsTable);
    }

    @Override
    public void destroy() {
        if (itemClickRegistration != null) {
            itemClickRegistration.removeHandler();
            itemClickRegistration = null;
        }
        if (rowContextClickRegistration != null) {
            rowContextClickRegistration.removeHandler();
            rowContextClickRegistration = null;
        }
        if (presidentsTableContextMenu != null) {
            presidentsTable.setContextMenu(null);
            presidentsTableContextMenu.destroy();
        }
        super.destroy();
    }
}
