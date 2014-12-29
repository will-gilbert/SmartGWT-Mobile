package com.smartgwt.mobile.client.widgets.form;

import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;

public interface FormItemValueFormatter {
    public String formatValue(Object value, Record record, DynamicForm form, FormItem item);
}
