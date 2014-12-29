package com.smartgwt.mobile.client.types;

public enum DateDisplayFormat implements ValueEnum {

    TOSTRING("toString"),
    TOLOCALESTRING("toLocaleString"),
    TOUSSHORTDATE("toUSShortDate"),
    TOUSSHORTDATETIME("toUSShortDatetime"),
    TOEUROPEANSHORTDATE("toEuropeanShortDate"),
    TOEUROPEANSHORTDATETIME("toEuropeanShortDatetime"),
    TOJAPANSHORTDATE("toJapanShortDate"),
    TOJAPANSHORTDATETIME("toJapanShortDatetime"),
    TOSERIALIZEABLEDATE("toSerializeableDate"),
    TODATESTAMP("toDateStamp");

    private final String value;

    private DateDisplayFormat(String value) {
        this.value = value;
    }

    @Override
    public final String getValue() {
        return this.value;
    }
}
