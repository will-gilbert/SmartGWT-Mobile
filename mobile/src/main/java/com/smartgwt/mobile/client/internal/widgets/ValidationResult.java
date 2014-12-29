package com.smartgwt.mobile.client.internal.widgets;

import java.util.ArrayList;
import java.util.Map;

import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class ValidationResult {

    private Map<String, ArrayList<String>> errors;
    private Object resultingValue;
    private Boolean stopOnError;
    private boolean valid;

    public final Map<String, ArrayList<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, ArrayList<String>> errors) {
        this.errors = errors;
    }

    public final Object getResultingValue() {
        return resultingValue;
    }

    public void setResultingValue(Object resultingValue) {
        this.resultingValue = resultingValue;
    }

    public final Boolean getStopOnError() {
        return stopOnError;
    }

    public void setStopOnError(Boolean stopOnError) {
        this.stopOnError = stopOnError;
    }

    public final boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
