package com.smartgwt.mobile.client.types;

public enum DSProtocol implements ValueEnum {
    GETPARAMS("getParams"),

    POSTPARAMS("postParams"),

    //POSTXML("postXML"),

    /**
     * {@link com.smartgwt.mobile.client.data.DSRequest#getData DSRequest.data} is assumed
     * to be a String set up by {@link com.smartgwt.mobile.client.data.DataSource#transformRequest
     * DataSource.transformRequest()} and is POSTed as the HTTP request body.
     */
    POSTMESSAGE("postMessage"),

    //CLIENTCUSTOM("clientCustom")
    ;

    private final String value;

    private DSProtocol(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
