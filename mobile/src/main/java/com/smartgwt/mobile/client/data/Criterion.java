package com.smartgwt.mobile.client.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.smartgwt.mobile.client.types.OperatorId;
import com.smartgwt.mobile.client.util.EnumUtil;

public class Criterion extends Criteria {

    public Criterion() {
    }

    public Criterion(Criterion c) {
        super(c);
    }

    public Criterion(OperatorId operator) {
        internalSetOperator(operator);
    }

    public Criterion(String fieldName, OperatorId operator) {
        internalSetFieldName(fieldName);
        internalSetOperator(operator);
    }

    public Criterion(String fieldName, OperatorId operator, Object value) {
        this(fieldName, operator);
        internalSetValue(value);
    }

    public Criterion(OperatorId operator, Criterion... criterias) {
        internalSetOperator(operator);
        setAttribute("criteria", new ArrayList<Criterion>());
        for (final Criterion c : criterias) {
            internalAppendToCriterionList(c);
        }
    }

    private <T extends Comparable<T>> Criterion(String fieldName, OperatorId operator, T start, T end, Object dummy) {
        this(fieldName, operator);
        assert operator == OperatorId.IBETWEEN_INCLUSIVE ||
               operator == OperatorId.BETWEEN ||
               operator == OperatorId.BETWEEN_INCLUSIVE
               : "The operator type must be OperatorId IBETWEEN_INCLUSIVE, BETWEEN, or BETWEEN_INCLUSIVE.";
        assert start.compareTo(end) <= 0 : "`start' must be less than or equal to `end'.";
        setAttribute("start", start);
        setAttribute("end", end);
        markAdvancedCriteria();
    }

    public Criterion(String fieldName, OperatorId operator, Integer start, Integer end) {
        this(fieldName, operator, start, end, null);
    }

    public Criterion(String fieldName, OperatorId operator, Long start, Long end) {
        this(fieldName, operator, start, end, null);
    }

    public Criterion(String fieldName, OperatorId operator, Float start, Float end) {
        this(fieldName, operator, start, end, null);
    }

    public Criterion(String fieldName, OperatorId operator, Double start, Double end) {
        this(fieldName, operator, start, end, null);
    }

    public Criterion(String fieldName, OperatorId operator, String start, String end) {
        this(fieldName, operator, start, end, null);
    }

    public Criterion(String fieldName, OperatorId operator, Date start, Date end) {
        this(fieldName, operator, start, end, null);
    }

    public final String getFieldName() {
        return getAttribute("fieldName");
    }

    private void internalSetFieldName(String fieldName) {
        setAttribute("fieldName", fieldName);
    }
    public void setFieldName(String fieldName) {
        internalSetFieldName(fieldName);
    }

    public final OperatorId getOperator() {
        Object obj = getAttributeAsObject("operator");
        if (obj == null) return null;
        if (obj instanceof OperatorId) return (OperatorId)obj;
        return EnumUtil.getEnum(OperatorId.values(), obj.toString());
    }

    private void internalSetOperator(OperatorId operator) {
        setAttribute("operator", operator);
    }
    public void setOperator(OperatorId operator) {
        internalSetOperator(operator);
    }

    public final Object getValue() {
        return getAttributeAsObject("value");
    }

    public final Boolean getValueAsBoolean() {
        return getAttributeAsBoolean("value");
    }

    public final Float getValueAsFloat() {
        return getAttributeAsFloat("value");
    }

    public final Double getValueAsDouble() {
        return getAttributeAsDouble("value");
    }

    /**
     * Synonym of {@link #getValueAsInteger()}.
     */
    public final Integer getValueAsInt() {
        return getValueAsInteger();
    }

    public final Integer getValueAsInteger() {
        return getAttributeAsInt("value");
    }

    public final Long getValueAsLong() {
        return getAttributeAsLong("value");
    }

    public final Date getValueAsDate() {
        return getAttributeAsDate("value");
    }

    public final String getValueAsString() {
        return getAttribute("value");
    }

    private void internalSetValue(Object value) {
        setAttribute("value", value);
    }
    public void setValue(Object value) {
        internalSetValue(value);
    }

    public final Criterion[] getCriteria() {
        final List<Criterion> l = getAttributeAsList("criteria");
        if (l == null) return null;
        // TODO Need to unmarshall `AdvancedCriteria' instances?
        return l.toArray(new Criterion[l.size()]);
    }

    private void internalAppendToCriterionList(Criterion c) {
        c.unmarkAdvancedCriteria();
        final List<Criterion> l = getAttributeAsList("criteria");
        assert l != null : "appendToCriterionList called when no criterion list exists";
        if (l != null) {
            l.add(c);
        }
    }
    public void appendToCriterionList(Criterion c) {
        internalAppendToCriterionList(c);
    }

    public void addCriteria(Criterion c) {
        if (getOperator() == OperatorId.AND) {
            final List<Criterion> l = getAttributeAsList("criteria");
            appendToCriterionList(c);
        } else {
            final Criterion thisCopy = new Criterion(this);
            final Object oldFieldName = remove("fieldName");
            remove("value");
            remove("start");
            remove("end");
            setOperator(OperatorId.AND);
            setAttribute("criteria", new ArrayList<Criterion>());
            if (oldFieldName != null) appendToCriterionList(thisCopy);
            appendToCriterionList(c);
        }
    }

    public void addCriteria(String field, Object value) {
        addCriteria(field, OperatorId.EQUALS, value);
    }

    public void addCriteria(String field, OperatorId operator, Object value) {
        addCriteria(new Criterion(field, operator, value));
    }

    public void markAdvancedCriteria() {
        setAttribute("_constructor", "AdvancedCriteria");
    }

    public void unmarkAdvancedCriteria() {
        remove("_constructor");
    }
}
