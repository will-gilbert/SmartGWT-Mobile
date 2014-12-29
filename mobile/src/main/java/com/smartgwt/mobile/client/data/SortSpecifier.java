package com.smartgwt.mobile.client.data;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.types.SortDirection;

/**
 * Class defining the details of a single sort operation.
 */
public class SortSpecifier {

    @SGWTInternal
    public static SortSpecifier[] _shallowClone(SortSpecifier[] sortSpecifiers) {
        if (sortSpecifiers == null) return null;
        final int sortSpecifiers_length = sortSpecifiers.length;
        SortSpecifier[] ret = new SortSpecifier[sortSpecifiers_length];
        System.arraycopy(sortSpecifiers, 0, ret, 0, sortSpecifiers_length);
        return ret;
    }

    private String fieldName;
    private SortDirection sortDirection;

    public SortSpecifier(String fieldName) {
        this(fieldName, SortDirection.ASCENDING);
    }

    public SortSpecifier(String fieldName, SortDirection sortDirection) {
        this.fieldName = fieldName;
        this.sortDirection = sortDirection;
    }

    /**
     * The field name to which this specifier applies.
     * @return the field name.
     */
    public final String getField() {
        return fieldName;
    }

    /**
     * The direction in which this specifier should sort.
     * @return the sort direction.
     */
    public final SortDirection getSortDirection() {
        return sortDirection;
    }

    @SGWTInternal
    public final boolean _isUp() {
        return (sortDirection == SortDirection.ASCENDING);
    }

    @Override
    public final String toString() {
        if (sortDirection == SortDirection.ASCENDING) return fieldName;
        else return "-" + fieldName;
    }
}
