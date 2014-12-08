package com.smartgwt.mobile.showcase.client.widgets.forms;

import java.util.List;

import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.grid.CellFormatter;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;
import com.smartgwt.mobile.client.widgets.tableview.TableView;

public class EmbeddedTableView extends ScrollablePanel {

    private static final String SALABLE_ITEM_ID_PROPERTY = "_id",
            SALABLE_ITEM_DESCRIPTION_PROPERTY = "description",
            SALABLE_ITEM_UNITS_PROPERTY = "units";
    private static Record createSalableItemRecord(int salableItemID, String description) {
        final Record record = new Record();
        record.setAttribute(SALABLE_ITEM_ID_PROPERTY, salableItemID);
        record.setAttribute(SALABLE_ITEM_DESCRIPTION_PROPERTY, description);
        return record;
    }
    private static Record createSalableItemRecord(int salableItemID, String description, String units) {
        final Record record = createSalableItemRecord(salableItemID, description);
        record.setAttribute(SALABLE_ITEM_UNITS_PROPERTY, units);
        return record;
    }

    private static final String ORDER_LINE_ITEM_ID_PROPERTY = "_id",
            ORDER_LINE_ITEM_SALABLE_ITEM_RECORD_PROPERTY = "salableItemRecord",
            ORDER_LINE_ITEM_QUANTITY_PROPERTY = "quantity";
    private static Record createOrderLineItemRecord(long orderLineItemID, Record salableItemRecord, double quantity) {
        final Record record = new Record();
        record.setAttribute(ORDER_LINE_ITEM_ID_PROPERTY, orderLineItemID);
        record.setAttribute(ORDER_LINE_ITEM_SALABLE_ITEM_RECORD_PROPERTY, salableItemRecord);
        record.setAttribute(ORDER_LINE_ITEM_QUANTITY_PROPERTY, quantity);
        return record;
    }

    private static final ListGridField SALABLE_ITEM_DESCRIPTION_FIELD = new ListGridField("-salableItemDescription"),
            FORMATTED_QUANTITY_FIELD = new ListGridField("-quantity");
    static {
        SALABLE_ITEM_DESCRIPTION_FIELD.setCellFormatter(new CellFormatter() {
            @Override
            public String format(Object value, Record orderLineItemRecord, int rowNum, int fieldNum) {
                final Record salableItemRecord = orderLineItemRecord.getAttributeAsRecord(ORDER_LINE_ITEM_SALABLE_ITEM_RECORD_PROPERTY);
                return salableItemRecord == null ? null : salableItemRecord.getAttribute(SALABLE_ITEM_DESCRIPTION_PROPERTY);
            }
        });
        FORMATTED_QUANTITY_FIELD.setCellFormatter(new CellFormatter() {
            @Override
            public String format(Object value, Record orderLineItemRecord, int rowNum, int fieldNum) {
                final Record salableItemRecord = orderLineItemRecord.getAttributeAsRecord(ORDER_LINE_ITEM_SALABLE_ITEM_RECORD_PROPERTY);
                final Double quantity = orderLineItemRecord.getAttributeAsDouble(ORDER_LINE_ITEM_QUANTITY_PROPERTY);
                if (salableItemRecord == null) return quantity == null ? null : Double.toString(quantity.doubleValue());
                final String units = salableItemRecord.getAttribute(SALABLE_ITEM_UNITS_PROPERTY);
                return quantity == null ? null : (Double.toString(quantity.doubleValue()) + (units == null ? "" : " " + units));
            }
        });
    }

    private static final String ORDER_ID_PROPERTY = "_id",
            ORDER_LINE_ITEM_RECORDS_PROPERTY = "orderLineItems";
    private static Record createOrderRecord(int id, List<? extends Record> orderLineItems) {
        final Record record = new Record();
        record.setAttribute(ORDER_ID_PROPERTY, id);
        record.setAttribute(ORDER_LINE_ITEM_RECORDS_PROPERTY, orderLineItems);
        return record;
    }

    public EmbeddedTableView(String title) {
        super(title);

        final Record gallon87OctaneGasoline = createSalableItemRecord(10087, "Gasoline - 87 oct.", "Gal"),
                gallon89OctaneGasoline = createSalableItemRecord(10089, "Gasoline - 89 oct.", "Gal"),
                gallon92OctaneGasoline = createSalableItemRecord(10092, "Gasoline - 92 oct.", "Gal"),
                gallonDiesel = createSalableItemRecord(20001, "Diesel", "Gal"),
                regularCarWash = createSalableItemRecord(831, "Car wash"),
                theWorksCarWash = createSalableItemRecord(835, "'The Works' car wash"),
                _32OzBottledWater = createSalableItemRecord(93, "32oz. Bottled water"),
                banana = createSalableItemRecord(143, "Banana");

        final RecordList order1006193Data = new RecordList() {{
            add(createOrderLineItemRecord(2406008137301906L, gallon89OctaneGasoline, 34.1));
            add(createOrderLineItemRecord(2406008137301920L, regularCarWash, 1));
            add(createOrderLineItemRecord(2406008137301022L, _32OzBottledWater, 2));
            add(createOrderLineItemRecord(2406008137301023L, banana, 1));
        }};

        final Record orderRecord = createOrderRecord(1006193, order1006193Data);

        final TableView tableView = new TableView();
        tableView.setMainRecord(orderRecord);
        tableView.setSubrecordsProperty(ORDER_LINE_ITEM_RECORDS_PROPERTY);
        tableView.setTitleField(SALABLE_ITEM_DESCRIPTION_FIELD.getName());
        tableView.setShowDetailCount(true);
        tableView.setDetailCountProperty(FORMATTED_QUANTITY_FIELD.getName());
        tableView.setFields(SALABLE_ITEM_DESCRIPTION_FIELD, FORMATTED_QUANTITY_FIELD);

        final DynamicForm form = new DynamicForm();
        form.setFields(tableView);
        addMember(form);
    }
}
