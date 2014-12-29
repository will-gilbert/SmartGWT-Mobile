package com.smartgwt.mobile.showcase.client.widgets.tableviews;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.Action;
import com.smartgwt.mobile.client.widgets.ActionContext;
import com.smartgwt.mobile.client.widgets.BaseButton.ButtonType;
import com.smartgwt.mobile.client.widgets.NavigationButton;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.events.DataChangedEvent;
import com.smartgwt.mobile.client.widgets.events.DataChangedHandler;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.tab.TabSet;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.mobile.showcase.client.widgets.images.ImageResources;

public class SimpleTablesWithEdit extends ScrollablePanel {

    private NavigationButton backButton = null;

    public SimpleTablesWithEdit(String title) {
        super(title);
        TabSet tabset = (TabSet) RootLayoutPanel.get().getWidget(0);
        final NavStack nav = (NavStack) tabset.getTab(1).getPane();
        final TableView tableView = new TableView();
        final ToolStrip toolbar = new ToolStrip();
        final ToolStripButton deleteItemsButton = new ToolStripButton("Delete");
        deleteItemsButton.setButtonType(ButtonType.BORDERED_WARNING);
        deleteItemsButton.setDisabled(true);
        deleteItemsButton.setStretch(true);
        final ToolStripButton moveItemsButton = new ToolStripButton("Move");
        moveItemsButton.setDisabled(true);
        moveItemsButton.setStretch(true);
        final ToolStripButton markItemsButton = new ToolStripButton("Mark");
        markItemsButton.setDisabled(true);
        markItemsButton.setStretch(true);
        toolbar.addButton(deleteItemsButton);
        toolbar.addButton(moveItemsButton);
        toolbar.addButton(markItemsButton);
        @SuppressWarnings({"serial"})
        final RecordList recordList = new RecordList(
                    new Record() {
                        {
                            setAttribute("_id", 1);
                            setAttribute("title", "People");
                            setAttribute("icon", ImageResources.INSTANCE.ipod());
                        }
                    },
                    new Record() {
                        {
                            setAttribute("_id", 2);
                            setAttribute("title", "Places");
                            setAttribute("icon", ImageResources.INSTANCE.phone());
                            setAttribute("detailCount", 32);
                        }
                    }
                );
        tableView.setTitleField("title");
        tableView.setShowNavigation(false);
        tableView.setCanRemoveRecords(true);
        tableView.setDeferRemoval(true);
        tableView.setShowIcons(true);
        tableView.setShowDetailCount(true);
        tableView.setTableMode(TableMode.PLAIN);
        tableView.addDataChangedHandler(new DataChangedHandler() {
            @Override
            public void onDataChanged(DataChangedEvent event) {
                if (tableView.hasChanges()) {
                    deleteItemsButton.setDisabled(false);
                } else {
                    deleteItemsButton.setDisabled(true);
                }
            }
        });

        setActions(new Action[]{

            new Action("Edit") {
                
                @Override
                public void execute(ActionContext context) {
                    if (context.getControl().getTitle().equals("Edit")) {
                        tableView.showDeletableRecords();
                        final ToolStripButton control = (ToolStripButton)context.getControl();
                        control.setTitle("Cancel");
                        control.setButtonType(ButtonType.BORDERED_IMPORTANT);
                        backButton = nav.getNavigationBar().getLeftButton();
                        nav.getNavigationBar().setLeftButton(null);
                        context.getPanel().addMember(toolbar);
                    } else {
                        tableView.hideDeletableRecords();
                        final ToolStripButton control = (ToolStripButton)context.getControl();
                        control.setTitle("Edit");
                        control.setButtonType(ButtonType.BORDERED);
                        tableView.discardAllEdits();
                        nav.getNavigationBar().setLeftButton(backButton);
                        context.getPanel().removeMember(toolbar);
                    }
                }
            }
        });
        deleteItemsButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                tableView.saveAllEdits();
                tableView.hideDeletableRecords();
                nav.getNavigationBar().getRightButton().setTitle("Edit");
                nav.getNavigationBar().getRightButton().setButtonType(ButtonType.BORDERED);
                nav.getNavigationBar().setLeftButton(backButton);
                SimpleTablesWithEdit.this.removeMember(toolbar);
                deleteItemsButton.setDisabled(true);
            }
        });
        tableView.setData(recordList);
        addMember(tableView);
    }
}
