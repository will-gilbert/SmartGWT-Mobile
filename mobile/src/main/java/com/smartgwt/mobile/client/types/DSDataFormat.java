package com.smartgwt.mobile.client.types;

public enum DSDataFormat implements ValueEnum {

    /**
     * DataSource expects XML responses.  This is the correct setting when consuming RSS or Atom feeds,
     * XML web services (whether SOAP, REST, XML-RPC, or a custom format), and XML static files.
     * 
     * <p>The request format is specified by the OperationBinding {@link com.smartgwt.mobile.client.data.OperationBinding#getDataProtocol
     * protocol}.
     */
    XML("xml"),

    /**
     * DataSource expects JSON responses.
     * 
     * <p>The request format is specified by the OperationBinding {@link com.smartgwt.mobile.client.data.OperationBinding#getDataProtocol
     * protocol}.
     */
    JSON("json"),

    /**
     * Smart GWT Mobile will not attempt to parse the response.  Instead, {@link com.smartgwt.mobile.client.data.DataSource#transformResponse
     * DataSource.transformResponse()} must be implemented.  <code>transformResponse()</code> will receive the <code>data</code> parameter as a
     * String, and must parse this String into an array of {@link com.smartgwt.mobile.client.data.Record Records}, which should be set as the DSResponse {@link
     * com.smartgwt.mobile.client.data.DSResponse#getData data}.
     * 
     * <p>The request format is specified by the OperationBinding {@link com.smartgwt.mobile.client.data.OperationBinding#getDataProtocol
     * protocol}.
     */
    CUSTOM("custom");

    private final String value;

    private DSDataFormat(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
