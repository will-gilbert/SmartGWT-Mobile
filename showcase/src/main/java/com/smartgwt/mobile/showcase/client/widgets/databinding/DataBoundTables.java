package com.smartgwt.mobile.showcase.client.widgets.databinding;

import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.internal.util.AnimationUtil;
import com.smartgwt.mobile.client.types.FetchMode;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.HRWidget;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.mobile.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.tableview.TableView;

public class DataBoundTables extends ScrollablePanel {
    Panel output = new Panel();

    public DataBoundTables(String title) {
        super(title);
        setWidth("100%");
        final TableView tableView = new TableView();
        tableView.setTitleField("continentName");
        tableView.setShowNavigation(false);
        tableView.setSelectionType(SelectionStyle.SINGLE);
        tableView.setShowSelectedIcon(true);
        tableView.setTableMode(TableMode.PLAIN);
        tableView.setDataFetchMode(FetchMode.BASIC);
        tableView.setDataSource(new DataSource("continentsDS"));
        tableView.getDataSource().setDataURL("sampleResponses/continents");
        tableView.fetchData();        
        output.setStyleName("sc-rounded-panel");
        output.getElement().getStyle().setProperty("textAlign", "center");
        output.getElement().getStyle().setOpacity(0.0);
        HLayout panelWrapper = new HLayout();
        panelWrapper.setWidth("auto");
        panelWrapper.setHeight("auto");
        panelWrapper.setLayoutMargin(20);
        panelWrapper.setPaddingAsLayoutMargin(true);
        panelWrapper.setMembersMargin(20);
        panelWrapper.addMember(output);

        tableView.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (event.getState()) {
                    output.setContents("Selected row is "+event.getRecord().get("continentName"));
                    AnimationUtil.fadeTransition(output, true);
                }
            }
        });
        addMember(tableView);
        addMember(new HRWidget());
        addMember(panelWrapper);
    }
}
