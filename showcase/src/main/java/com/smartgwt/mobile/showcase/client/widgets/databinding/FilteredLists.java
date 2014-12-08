package com.smartgwt.mobile.showcase.client.widgets.databinding;

import java.util.LinkedHashMap;

import com.smartgwt.mobile.client.data.Criteria;
import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.data.SortSpecifier;
import com.smartgwt.mobile.client.types.FetchMode;
import com.smartgwt.mobile.client.types.SortDirection;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.HRWidget;
import com.smartgwt.mobile.client.widgets.Header2;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.ButtonItem;
import com.smartgwt.mobile.client.widgets.form.fields.SwitchItem;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.mobile.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.mobile.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.mobile.client.widgets.tableview.TableView;

public class FilteredLists extends ScrollablePanel {

    private LinkedHashMap<String, String> typesMap;

    private DynamicForm form;
    private ButtonItem typeItem;
    private SwitchItem onlyPoisonousItem;

    private TableView tableView;

    private void fetchData() {
        Criteria criteria = new Criteria();
        if(onlyPoisonousItem.isChecked()) {
            if(!"all".equals(typeItem.getValue())) {
                criteria = form.getValuesAsCriteria();
            } else {
                criteria.addCriteria(onlyPoisonousItem.getName(), onlyPoisonousItem.getValue());
            }
            tableView.fetchData(criteria);
        } else if(!"all".equals(typeItem.getValue())){
            String name = typeItem.getName();
            Object value = typeItem.getValue();
            criteria.addCriteria(name, value);
            tableView.fetchData(criteria);
        } else {
            tableView.fetchData(criteria);
        }
    }

    public FilteredLists(String title) {
        super(title);
        final Header2 header2 = new Header2("Select Types of Mushrooms");
        final Header2 results = new Header2("Results");
        typesMap = new LinkedHashMap<String, String>() {{
            put("all", "All");
            put("saprotroph", "Saprotroph");
            put("parasitic", "Parasitic");
            put("mycorrhizal", "Mycorrhizal");
        }};
        form = new DynamicForm();
        tableView = new TableView();
        typeItem = new ButtonItem("info", "Type");
        typeItem.setValue("all");
        typeItem.setValueMap(typesMap);
        onlyPoisonousItem = new SwitchItem("poisonous", "Only poisonous types");
        typeItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                fetchData();
            }
        });       
        onlyPoisonousItem.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                fetchData();
            }
        });
        form.setFields(typeItem, onlyPoisonousItem);

        tableView.setTitleField("title");
        tableView.setShowNavigation(false);
        tableView.setTableMode(TableMode.GROUPED);
        tableView.setDataFetchMode(FetchMode.BASIC);

        tableView.setSort(new SortSpecifier("info", SortDirection.DESCENDING), new SortSpecifier("title"));
        tableView.setDataSource(new DataSource("fungiDS"));
        tableView.getDataSource().setDataURL("sampleResponses/fungi");
        tableView.fetchData();
        addMember(header2);
        addMember(form);
        addMember(new HRWidget());
        addMember(results);
        addMember(tableView);
    }
}
