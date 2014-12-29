package com.smartgwt.mobile.client.types;

public enum State implements ValueEnum {

    STATE_UP(""),
    STATE_DOWN("Down"),
    STATE_DISABLED("Disabled");

    private final String value;

    private State(String value) {
        this.value = value;
    }

    @Override
    public final String getValue() {
        return value;
    }
}
