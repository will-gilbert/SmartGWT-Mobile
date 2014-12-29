package com.smartgwt.mobile.client.widgets.form;

import com.smartgwt.mobile.client.widgets.form.fields.FormItem;

public interface FormItemValueParser {

    public Object parseValue(String value, DynamicForm form, FormItem item);
}
