package org.informagen.eovortaro.client.application;

import com.google.gwt.user.client.rpc.AsyncCallback;

// GWT - Development logging; Alert Window logging
import com.google.gwt.core.client.GWT;

import com.smartgwt.mobile.client.util.SC;


public abstract class Callback<T> implements AsyncCallback<T> {
    
    public void onFailure(Throwable throwable) {
        GWT.log(throwable.toString());
        SC.warn(throwable.getMessage());

    }
    
    public abstract void onSuccess(T result);
}