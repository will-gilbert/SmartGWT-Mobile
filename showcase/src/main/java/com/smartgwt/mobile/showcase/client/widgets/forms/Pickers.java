package com.smartgwt.mobile.showcase.client.widgets.forms;

import com.google.gwt.dom.client.StyleInjector;
import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.data.OperationBinding;
import com.smartgwt.mobile.client.types.DSDataFormat;
import com.smartgwt.mobile.client.types.DSOperationType;
import com.smartgwt.mobile.client.util.LogicalDate;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.ColorPickerItem;
import com.smartgwt.mobile.client.widgets.form.fields.DateItem;
import com.smartgwt.mobile.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.mobile.client.widgets.form.fields.SelectItem;
import com.smartgwt.mobile.client.widgets.form.fields.TimeItem;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;

public class Pickers extends ScrollablePanel {

    static {
        StyleInjector.injectAtEnd(".app-scientificNameCell { color: gray; font-size: 18px; }");
    }

    private DynamicForm form;
    private DateItem dueItem;
    private TimeItem timeItem;
    private DateTimeItem datetimeItem;
    private ColorPickerItem colorItem;
    private SelectItem fruitItem;
    private SelectItem animalItem;

    public Pickers(String title) {
        super(title);

        form = new DynamicForm();

        dueItem = new DateItem("due", "Due Date", "Due Date");
        dueItem.setAllowEmptyValue(true);
        LogicalDate startDate = new LogicalDate();
        startDate = new LogicalDate(startDate.getLogicalYear(), startDate.getLogicalMonth() - 2, startDate.getLogicalDate());
        dueItem.setStartDate(startDate);
        LogicalDate endDate = new LogicalDate(startDate.getLogicalYear(), startDate.getLogicalMonth() + 12, startDate.getLogicalDate());
        dueItem.setEndDate(endDate);

        timeItem = new TimeItem("arrival", "Arrival Time", "Arrival Time");
        timeItem.setAllowEmptyValue(true);

        datetimeItem = new DateTimeItem("dt", "Datetime", "Datetime");
        datetimeItem.setAllowEmptyValue(true);

        colorItem = new ColorPickerItem("color", "Favorite Color", "Favorite Color");

        final DataSource fruitsDS = new DataSource("pickersSample-fruitsDS");
        fruitsDS.setFetchDataURL("sampleResponses/fruits");
        fruitsDS.setFields(new DataSourceField("value", "integer"), new DataSourceField("name", "text"));
        fruitItem = new SelectItem("fruit", "Favorite Fruit", "Favorite Fruit");
        fruitItem.setAllowEmptyValue(true);
        fruitItem.setOptionDataSource(fruitsDS);
        fruitItem.setValueField("value");
        fruitItem.setDisplayField("name");
        fruitItem.fetchData();

        final DataSource animalsDS = new DataSource("pickersSample-animalsDS");
        animalsDS.setFetchDataURL("sampleResponses/animals.xml");
        animalsDS.setFields(new DataSourceField("_id", "integer"), new DataSourceField("scientificName", "text"),
                new DataSourceField("commonName", "text"));
        animalsDS.setOperationBindings(new OperationBinding(DSOperationType.FETCH) {{
            setDataFormat(DSDataFormat.XML);
        }});
        animalItem = new SelectItem("animal", "Favorite Animal", "Favorite Animal");
        animalItem.setPickListBaseStyle("app-aCell");
        animalItem.setValueField("_id");
        animalItem.setDisplayField("commonName");
        animalItem.setOptionDataSource(animalsDS);
        animalItem.setPickListFields(new ListGridField("commonName"),
                new ListGridField("scientificName") {{
                    setBaseStyle("app-scientificNameCell");
                    setWidth("30%");
                }});
        animalItem.setAllowEmptyValue(true);
        animalItem.fetchData();

        form.setFields(dueItem, timeItem, datetimeItem, colorItem, fruitItem, animalItem);
        addMember(form);
    }

    @Override
    public void reset() {
        super.reset();
        form.clearValues();
    }
}
