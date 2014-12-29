package com.smartgwt.mobile.client.rpc;

import java.util.HashMap;
import java.util.Map;

import com.smartgwt.mobile.SGWTInternal;

public class RPCResponse {

    public static final int STATUS_SUCCESS = 0,
            STATUS_OFFLINE = 1,
            STATUS_FAILURE = -1,
            STATUS_VALIDATION_ERROR = -4,
            STATUS_LOGIN_INCORRECT = -5,
            STATUS_MAX_LOGIN_ATTEMPTS_EXCEEDED = -6,
            STATUS_LOGIN_REQUIRED = -7,
            STATUS_LOGIN_SUCCESS = -8,
            STATUS_UPDATE_WITHOUT_PK_ERROR = -9,
            STATUS_TRANSACTION_FAILED = -10,
            STATUS_TRANSPORT_ERROR = -90,
            STATUS_UNKNOWN_HOST_ERROR = -91,
            STATUS_CONNECTION_RESET_ERROR = -92,
            STATUS_SERVER_TIMEOUT = -100;

    @SGWTInternal
    protected Map<String, Object> _attributes = new HashMap<String, Object>();

    public final Integer getTransactionNum() {
        return (Integer)_attributes.get("transactionNum");
    }

    @SGWTInternal
    public void _setTransactionNum(Integer transactionNum) {
        _attributes.put("transactionNum", transactionNum);
    }

    public final int getStatus() {
        final Integer ret = (Integer)_attributes.get("status");
        return ret == null ? STATUS_FAILURE : ret.intValue();
    }

    public void setStatus(Integer status) {
        _attributes.put("status", status);
    }

    public final Integer getHttpResponseCode() {
        return (Integer)_attributes.get("httpResponseCode");
    }

    public void setHttpResponseCode(Integer httpResponseCode) {
        _attributes.put("httpResponseCode", httpResponseCode);
    }

    public final Map<String, String> getHttpHeaders() {
        @SuppressWarnings("unchecked")
        final Map<String, String> httpHeaders = (Map<String, String>)_attributes.get("httpHeaders");
        return httpHeaders;
    }

    @SGWTInternal
    public void _setHttpHeaders(Map<String, String> httpHeaders) {
        _attributes.put("httpHeaders", httpHeaders);
    }
}
