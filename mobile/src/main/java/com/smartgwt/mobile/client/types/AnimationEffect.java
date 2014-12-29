package com.smartgwt.mobile.client.types;

public enum AnimationEffect implements ValueEnum {

    FADE("fade"),
    SLIDE("slide");

    private final String value;

    private AnimationEffect(String value) {
        this.value = value;
    }

    @Override
    public final String getValue() {
        return value;
    }
}
