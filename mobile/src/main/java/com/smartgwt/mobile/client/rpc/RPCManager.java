package com.smartgwt.mobile.client.rpc;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.DSRequest;
import com.smartgwt.mobile.client.data.DSResponse;
import com.smartgwt.mobile.client.util.Page;
import com.smartgwt.mobile.client.util.SC;

public class RPCManager {

    private static final HandleErrorCallback DEFAULT_HANDLE_ERROR_CALLBACK = new HandleErrorCallback() {
        @Override
        public void handleError(DSResponse response, DSRequest request) {
            final int status = response.getStatus();
            final Object data = response._getData();
            if (data instanceof CharSequence || data instanceof Character) {
                SC.say("Error", data.toString());
            } else {
                SC.logWarn("RPCManager.handleError(): Request " + request.getRequestId() + " failed with status code " + response.getStatus());
            }
        }
    };

    private static int nextTransactionNum = 1;
    private static String actionURL;
    private static int defaultTimeoutMillis = 240000; // 4 minutes
    private static HandleErrorCallback handleErrorCallback = null;

    static {
        // Hard-coding the default data URL in the first place. You can override it globally,
        // per-DataSource, per-OperatiopnBinding or for each individual DSRequest
        setActionURL("[ISOMORPHIC]/RESTHandler");
    }

    @SGWTInternal
    public static native boolean _isOnLine() /*-{
        var navigator = $wnd.navigator;
        if ("onLine" in navigator) {
            return !!navigator.onLine;
        }
        return true;
    }-*/;

    public static String getActionURL() {
        return actionURL;
    }

    public static void setActionURL(String actionURL) {
        RPCManager.actionURL = Page.getURL(actionURL);
    }

    @SGWTInternal
    public static int _getDefaultTimeoutMillis() {
        assert defaultTimeoutMillis >= 1;
        return defaultTimeoutMillis;
    }

    public static void setDefaultTimeout(double defaultTimeoutMillis) {
        RPCManager.defaultTimeoutMillis = Math.max(1, (int)defaultTimeoutMillis);
    }

    @SGWTInternal
    public static int _getNextTransactionNum() {
        return nextTransactionNum++;
    }

    public static void setHandleErrorCallback(HandleErrorCallback handleErrorCallback) {
        RPCManager.handleErrorCallback = handleErrorCallback;
    }

    public static void _handleError(DSResponse response, DSRequest request) {
        (handleErrorCallback == null ? DEFAULT_HANDLE_ERROR_CALLBACK : handleErrorCallback).handleError(response, request);
    }
}
