package com.smartgwt.mobile.client.data;

import com.smartgwt.mobile.client.types.OperatorId;

public class AdvancedCriteria extends Criterion {

    public AdvancedCriteria() {
        markAdvancedCriteria();
    }

    public AdvancedCriteria(Criterion c) {
        super(c);
        markAdvancedCriteria();
    }

    public AdvancedCriteria(OperatorId operator) {
        super(operator);
        markAdvancedCriteria();
    }

    public AdvancedCriteria(String fieldName, OperatorId operator) {
        super(fieldName, operator);
        markAdvancedCriteria();
    }

    public AdvancedCriteria(String fieldName, OperatorId operator, Object value) {
        super(fieldName, operator, value);
        markAdvancedCriteria();
    }

    public AdvancedCriteria(OperatorId operator, Criterion... criterias) {
        super(operator, criterias);
        markAdvancedCriteria();
    }
}
