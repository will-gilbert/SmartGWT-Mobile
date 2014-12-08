package com.smartgwt.mobile.showcase.client.widgets.databinding;

import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.data.SortSpecifier;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.tableview.TableView;

public class PagedDataBinding extends ScrollablePanel {

    public PagedDataBinding(String title) {
        super(title);

        final DataSource countryDS = new DataSource();
        countryDS.setMetaDataPrefix("");
        final TableView tableView = new TableView();

        tableView.setWidth("100%");
        tableView.setTitleField("countryName");
        tableView.setShowNavigation(false);
        tableView.setSelectionType(SelectionStyle.SINGLE);
        tableView.setShowSelectedIcon(true);
        tableView.setTableMode(TableMode.GROUPED);
        tableView.setDataPageSize(5);

        DataSourceField codeField = new DataSourceField("countryCode", "Country Code");
        codeField.setPrimaryKey(true);
        DataSourceField nameField = new DataSourceField("countryName", "Country Name");  
        DataSourceField populationfield = new DataSourceField("population", "Population");
        DataSourceField areaField = new DataSourceField("area", "Total Area");  
        DataSourceField governmentField = new DataSourceField("government", "Government");

        countryDS.setFields(codeField, nameField, populationfield, areaField, governmentField);
        countryDS.setDataURL("countriesDataProvider");

        tableView.setDataSource(countryDS);
        tableView.setSort(new SortSpecifier("countryCode"));
        tableView.fetchData();
        addMember(tableView);
    }
}
 