package com.smartgwt.mobile.client.widgets.grid;

import java.util.LinkedHashMap;

import com.google.gwt.regexp.shared.RegExp;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.internal.widgets.form.fields.CanCopyFieldConfiguration;
import com.smartgwt.mobile.client.widgets.Canvas;

public class ListGridField implements CanCopyFieldConfiguration {
    private static final RegExp VALID_WIDTH_RE = RegExp.compile("^(?:\\d+(?:\\.\\d+)?%?|\\*)$");

    private String baseStyle;
    private CellFormatter cellFormatter;
    private Boolean escapeHTML;
    private GroupTitleRenderer groupTitleRenderer;
    private GroupValueFunction groupValueFunction;
    private String name;
    private LinkedHashMap<?, String> valueMap;
    private Object width;

    public ListGridField(String name) {
        this.name = name;
    }

    public final String getBaseStyle() {
        return baseStyle;
    }

    public void setBaseStyle(String baseStyle) {
        this.baseStyle = baseStyle;
    }

    public void setCellFormatter(CellFormatter cellFormatter) {
        this.cellFormatter = cellFormatter;
    }

    public final Boolean getEscapeHTML() {
        return escapeHTML;
    }

    @SGWTInternal
    public final boolean _getEscapeHTML() {
        return Canvas._booleanValue(escapeHTML, false);
    }

    public void setEscapeHTML(Boolean escapeHTML) {
        this.escapeHTML = escapeHTML;
    }

    public final GroupTitleRenderer getGroupTitleRenderer() {
        return groupTitleRenderer;
    }

    public void setGroupTitleRenderer(GroupTitleRenderer groupTitleRenderer) {
        this.groupTitleRenderer = groupTitleRenderer;
    }

    public final GroupValueFunction getGroupValueFunction() {
        return groupValueFunction;
    }

    public void setGroupValueFunction(GroupValueFunction groupValueFunction) {
        this.groupValueFunction = groupValueFunction;
    }

    public final String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final LinkedHashMap<?, String> getValueMap() {
        return valueMap;
    }

    public void setValueMap(LinkedHashMap<?, String> valueMap) {
        this.valueMap = valueMap;
    }

    @SGWTInternal
    public final Object _getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setWidth(String width) {
        assert width == null || VALID_WIDTH_RE.test(width) : "The format of `width' is \"*\" or a number or a percentage.";
        this.width = width;
    }

    @Override
    public void _copyFieldConfiguration(DataSourceField field) {
        if (valueMap == null) valueMap = field.getValueMap();
    }

    @SGWTInternal
    public String _formatCellValue(Object value, Record record, int rowNum, int fieldNum, ListGrid grid) {
        if (cellFormatter != null) return cellFormatter.format(value, record, rowNum, fieldNum);

        final String fieldName = grid.getFieldName(fieldNum);
        if (fieldName == null) return null;
        if (valueMap != null && valueMap.containsKey(value)) {
            return valueMap.get(value);
        } else {
            return record.getAttribute(fieldName);
        }
    }
}
