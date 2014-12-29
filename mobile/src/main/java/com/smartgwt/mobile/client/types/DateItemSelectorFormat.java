package com.smartgwt.mobile.client.types;

public enum DateItemSelectorFormat implements ValueEnum {
    DAY_MONTH_YEAR("DMY"),
    MONTH_DAY_YEAR("MDY"),
    YEAR_MONTH_DAY("YMD"),
    DAY_MONTH("DM"),
    MONTH_DAY("MD"),
    YEAR_MONTH("YM"),
    MONTH_YEAR("MY");

    private final String value;

    private DateItemSelectorFormat(String value) {
        this.value = value;
    }

    public final String getValue() {
        return this.value;
    }
}
