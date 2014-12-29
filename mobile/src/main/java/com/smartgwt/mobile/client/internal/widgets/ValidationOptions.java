package com.smartgwt.mobile.client.internal.widgets;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.types.ValidationMode;

@SGWTInternal
public class ValidationOptions {

    private Boolean changing;
    private Boolean dontValidatorNullValue;
    private ValidationMode serverValidationMode;
    private Boolean typeValidationsOnly;
    private String unknownErrorMessage;

    public final Boolean getChanging() {
        return changing;
    }

    public void setChanging(Boolean changing) {
        this.changing = changing;
    }

    public final Boolean getDontValidatorNullValue() {
        return dontValidatorNullValue;
    }

    public void setDontValidatorNullValue(Boolean dontValidatorNullValue) {
        this.dontValidatorNullValue = dontValidatorNullValue;
    }

    public final ValidationMode getServerValidationMode() {
        return serverValidationMode;
    }

    public void setServerValidationMode(ValidationMode serverValidationMode) {
        this.serverValidationMode = serverValidationMode;
    }

    public final Boolean getTypeValidationsOnly() {
        return typeValidationsOnly;
    }

    public void setTypeValidationsOnly(Boolean typeValidationsOnly) {
        this.typeValidationsOnly = typeValidationsOnly;
    }

    public final String getUnknownErrorMessage() {
        return unknownErrorMessage;
    }

    public void setUnknownErrorMessage(String unknownErrorMessage) {
        this.unknownErrorMessage = unknownErrorMessage;
    }
}
