package com.smartgwt.mobile.client.types;

public enum SortDirection implements ValueEnum {

    /**
     * Sort in ascending order (eg: A-Z, larger items later in the list)
     */
    ASCENDING("ascending"),

    /**
     * Sort in descending order (eg: Z-A, larger items earlier in the list)
     */
    DESCENDING("descending");

    public final String value;

    private SortDirection(String value) {
        this.value = value;
    }

    @Override
    public final String getValue() {
        return value;
    }
}
