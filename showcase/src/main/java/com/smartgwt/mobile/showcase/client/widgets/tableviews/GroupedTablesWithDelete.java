hpackage com.smartgwt.mobile.showcase.client.widgets.tableviews;

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
import com.smartgwt.mobile.showcase.client.widgets.images.ImageResources;

public class GroupedTablesWithDelete extends ScrollablePanel {

    private static final String ID_PROPERTY = "_id";

    private NavigationButton backButton;

    public GroupedTablesWithDelete(String title) {
        super(title);
        final TabSet tabset = (TabSet) RootLayoutPanel.get().getWidget(0);
        final TableView tableView = new TableView();
        final NavigationButton deleteItemsButton = new NavigationButton("Delete");
        deleteItemsButton.setButtonType(ButtonType.BORDERED_WARNING);
        deleteItemsButton.setDisabled(true);
        @SuppressWarnings({"serial"})
        final RecordList recordList = new RecordList(
                new Record() {
                    {
                        setAttribute(ID_PROPERTY, "1");
                        setAttribute("title", "People");
                        setAttribute("icon", ImageResources.INSTANCE.ipod());
                    }
                },
                new Record() {
                    {
                        setAttribute(ID_PROPERTY, "2");
                        setAttribute("title", "Places");
                        setAttribute("icon", ImageResources.INSTANCE.phone());
                    }
                });
        tableView.setTitleField("title");
        tableView.setShowNavigation(false);
        tableView.setCanRemoveRecords(true);
        tableView.setDeferRemoval(true);
        tableView.setShowIcons(true);
        tableView.setTableMode(TableMode.GROUPED);
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
        tableView.setData(recordList);
        addMember(tableView);
        setActions(new Action[]{
            new Action("Delete") {
                @Override
                public void execute(ActionContext context) {
                    final NavStack nav = (NavStack) tabset.getTab(1).getPane();
                    if (context.getControl().getTitle().equals("Delete")) {
                        tableView.showDeletableRecords();
                        context.getControl().setTitle("Done");
                        backButton = nav.getNavigationBar().getLeftButton();
                        nav.getNavigationBar().setLeftButton(deleteItemsButton);
                    } else {
                        tableView.hideDeletableRecords();
                        context.getControl().setTitle("Delete");
                        tableView.discardAllEdits();
                        nav.getNavigationBar().setLeftButton(backButton);
                    }
                }
            }
        });
        deleteItemsButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                tableView.saveAllEdits();
                tableView.showDeletableRecords();
                deleteItemsButton.setDisabled(true);
            }
        });
    }
}
