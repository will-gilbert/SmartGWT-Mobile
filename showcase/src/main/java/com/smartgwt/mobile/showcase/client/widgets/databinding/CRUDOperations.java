/**
 * 
 */
package com.smartgwt.mobile.showcase.client.widgets.databinding;

import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.FetchMode;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.Action;
import com.smartgwt.mobile.client.widgets.ActionContext;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.layout.VLayout;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.showcase.client.resources.AppResources;

public class CRUDOperations extends ScrollablePanel {

    private static final DataSourceField CODE_FIELD = new DataSourceField("countryCode", "Country Code"),
            NAME_FIELD = new DataSourceField("countryName", "Country Name"),
            POPULATION_FIELD = new DataSourceField("population", "Population"),
            AREA_FIELD = new DataSourceField("area", "Total Area"),
            INDEPENDENCE_FIELD = new DataSourceField("independence"),
            GOVERNMENT_FIELD = new DataSourceField("government", "Government");

    static {
        CODE_FIELD.setPrimaryKey(true);
        POPULATION_FIELD.setType("int");
        AREA_FIELD.setType("float");
        INDEPENDENCE_FIELD.setType("date");
    }

	public CRUDOperations(String title) {
		super(title);
		setWidth("100%");
		VLayout layout = new VLayout();
        layout.setWidth("100%");
        layout.setAlign(Alignment.CENTER);

        final DataSource countryDS = new DataSource("CRUD_countries");
        final TableView tableView = new TableView();
        tableView.setAlign(Alignment.CENTER);

        setActions(new Action(AppResources.INSTANCE.add()) {
            @Override
            public void execute(ActionContext context) {
                tableView.addData(new Record());

                context.getControl().disable();
            }
        }, new Action(AppResources.INSTANCE.refresh()) {
            @Override
            public void execute(ActionContext context) {
                tableView.updateData(new Record());

                context.getControl().disable();
            }
        }, new Action(AppResources.INSTANCE.stop()) {
            @Override
            public void execute(ActionContext context) {
                tableView.removeData(new Record());

                context.getControl().disable();
            }
        });

        tableView.setTitleField(NAME_FIELD.getName());
        tableView.setShowNavigation(false);
        tableView.setSelectionType(SelectionStyle.SINGLE);
        tableView.setTableMode(TableMode.GROUPED);
        tableView.setDataFetchMode(FetchMode.BASIC);

        countryDS.setFields(CODE_FIELD, NAME_FIELD, POPULATION_FIELD, AREA_FIELD, INDEPENDENCE_FIELD, GOVERNMENT_FIELD);
        countryDS.setFetchDataURL("data/dataIntegration/json/country_fetch.js");
        countryDS.setAddDataURL("data/dataIntegration/json/country_add.js");
        countryDS.setUpdateDataURL("data/dataIntegration/json/country_update.js");
        countryDS.setRemoveDataURL("data/dataIntegration/json/country_remove.js");

        tableView.setDataSource(countryDS);
        tableView.fetchData();

        layout.addMember(tableView);
        addMember(layout);
    }
}
