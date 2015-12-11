package com.smartgwt.mobile.showcase.client.widgets.tableviews;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.NavigationMode;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.Action;
import com.smartgwt.mobile.client.widgets.ActionContext;
import com.smartgwt.mobile.client.widgets.BaseButton.ButtonType;
import com.smartgwt.mobile.client.widgets.NavigationButton;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.tab.TabSet;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.client.widgets.tableview.events.DetailsSelectedEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.DetailsSelectedHandler;

public class GroupedTablesWithReorder extends ScrollablePanel {

    private static final String ID_PROPERTY = "_id";

    private NavigationButton reorderButton = null, doneButton = new NavigationButton("Done");

    public GroupedTablesWithReorder(String title) {
        super(title);
        setWidth("100%");
        TabSet tabset = (TabSet) RootLayoutPanel.get().getWidget(0);
        final NavStack nav = (NavStack) tabset.getTab(1).getPane();
        final TableView tableView = new TableView();
        @SuppressWarnings({"serial"})
        final RecordList recordList = new RecordList(
                    new Record() {
                        {
                            setAttribute(ID_PROPERTY, "1");
                            setAttribute("title", "Jalapeño");
                            setAttribute("details", true);
                            setAttribute("scovilleHeatUnits", 8000);
                        }
                    },
                    new Record() {
                        {
                            setAttribute(ID_PROPERTY, "2");
                            setAttribute("title", "Serrano");
                            setAttribute("details", true);
                            setAttribute("scovilleHeatUnits", 25000);
                        }
                    },
                    new Record() {
                        {
                            setAttribute(ID_PROPERTY, "3");
                            setAttribute("title", "Cayenne");
                            setAttribute("details", true);
                            setAttribute("scovilleHeatUnits", 60000);
                        }
                    },
                    new Record() {
                        {
                            setAttribute(ID_PROPERTY, "4");
                            setAttribute("title", "Chile de árbol");
                            setAttribute("details", true);
                            setAttribute("scovilleHeatUnits", 65000);
                        }
                    },
                    new Record() {
                        {
                            setAttribute(ID_PROPERTY, "5");
                            setAttribute("title", "Scotch Bonnet");
                            setAttribute("details", true);
                            setAttribute("scovilleHeatUnits", 350000);
                        }
                    },
                    new Record() {
                        {
                            setAttribute(ID_PROPERTY, "6");
                            setAttribute("title", "Habanero");
                            setAttribute("details", true);
                            setAttribute("scovilleHeatUnits", 570000);
                        }
                    }
                );
        tableView.setTitleField("title");
        tableView.setShowNavigation(true);
        tableView.setNavigationMode(NavigationMode.NAVICON_ONLY);
        tableView.setCanReorderRecords(true);
        tableView.setTableMode(TableMode.GROUPED);
        tableView.setData(recordList);
        tableView.addDetailsSelectedHandler(new DetailsSelectedHandler() {
            class SpiceInfo extends Panel {
                public SpiceInfo(String contents) {
                    super("SpiceInfo");
                    this.setContents(contents);
                    this.setStyleName("sc-rounded-panel");
                    this.setStyleName("sc-stretch");
                    this.setMargin(10);
                }
            }
            @Override
            public void onDetailsSelected(DetailsSelectedEvent event) {
                Record selectedRecord = event.getRecord();
                if (selectedRecord != null) {
                    nav.push(new SpiceInfo(selectedRecord.getAttribute("title") + " - " + selectedRecord.getAttribute("scovilleHeatUnits") + " Scoville"));
                }
            }
        });
        addMember(tableView);
        setActions(new Action[]{
            new Action("Reorder") {
                @Override
                public void execute(ActionContext context) {
                    reorderButton = nav.getNavigationBar().getRightButton();
                    nav.getNavigationBar().setRightButton(doneButton);
                    tableView.showMoveableRecords();
                }
            }
        });
        doneButton.setButtonType(ButtonType.BORDERED_IMPORTANT);
        doneButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                nav.getNavigationBar().setRightButton(reorderButton);
                tableView.hideMoveableRecords();
            }
        });
    }
}
