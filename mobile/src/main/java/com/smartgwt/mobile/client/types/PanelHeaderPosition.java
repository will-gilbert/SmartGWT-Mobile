package com.smartgwt.mobile.client.types;

public enum PanelHeaderPosition implements ValueEnum {
    TOP("top"),
    BOTTOM("bottom");
    
	private String value;

    PanelHeaderPosition(String value) {
        this.value = value;
    }
    
	@Override
	public String getValue() {
		return value;
	}

}
