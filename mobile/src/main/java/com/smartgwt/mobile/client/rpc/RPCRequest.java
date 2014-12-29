package com.smartgwt.mobile.client.rpc;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.http.client.RequestBuilder;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.Canvas;

public class RPCRequest {

    @SGWTInternal
    protected Map<String, Object> _attributes = new HashMap<String, Object>();

    @SGWTInternal
    public final Map<String, Object> _getAttributes() {
        return _attributes;
    }

    @SGWTInternal
    public final Integer _getTransactionNum() {
        return (Integer)_attributes.get("transactionNum");
    }

    @SGWTInternal
    public void _setTransactionNum(Integer transactionNum) {
        _attributes.put("transactionNum", transactionNum);
    }

    public final String getRequestId() {
        return (String)_attributes.get("requestId");
    }

    public void setRequestId(String requestId) {
        _attributes.put("requestId", requestId);
    }

    public final String getHttpMethod() {
        return (String)_attributes.get("httpMethod");
    }

    public void setHttpMethod(String httpMethod) {
        _attributes.put("httpMethod", httpMethod);
    }

    /**
     * @deprecated Deprecated in favor of {@link #setHttpMethod(String)}.
     */
    @Deprecated
    public final void setHttpMethod(RequestBuilder.Method httpMethod) {
        setHttpMethod(httpMethod.toString());
    }

    public final String getContentType() {
        return (String)_attributes.get("contentType");
    }

    public void setContentType(String contentType) {
        _attributes.put("contentType", contentType);
    }

    public final Boolean getShowPrompt() {
        return (Boolean)_attributes.get("showPrompt");
    }

    public void setShowPrompt(Boolean showPrompt) {
        _attributes.put("showPrompt", showPrompt);
    }

    public final Boolean getWillHandleError() {
        return (Boolean)_attributes.get("willHandleError");
    }

    @SGWTInternal
    public final boolean _getWillHandleError() {
        return Canvas._booleanValue(getWillHandleError(), false);
    }

    public void setWillHandleError(Boolean willHandleError) {
        _attributes.put("willHandleError", willHandleError);
    }

    public final Integer getTimeout() {
        return (Integer)_attributes.get("timeout");
    }

    public void setTimeout(Integer timeout) {
        _attributes.put("timeout", timeout);
    }

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        _attributes.put("httpHeaders", httpHeaders);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getHttpHeaders() {
        return (Map<String, String>) _attributes.get("httpHeaders");
    } 
}
