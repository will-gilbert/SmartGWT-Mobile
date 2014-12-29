package com.smartgwt.mobile.client.widgets.form;

import java.util.Collection;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.Canvas;

@SGWTInternal
abstract class DynamicFormImpl {

    abstract void destroyImpl(DynamicForm self);

    abstract void addFields(DynamicForm self, Collection<Canvas> fields);

    abstract void removeField(DynamicForm self, Canvas field);

    abstract void clearErrors(DynamicForm self);

    abstract void showError(DynamicForm self, boolean showError, Canvas field, Object errorObj);
}
