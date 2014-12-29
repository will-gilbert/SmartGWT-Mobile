package com.smartgwt.mobile.client.internal.util;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.util.IsCallback;

@SGWTInternal
public interface UsernameAndPasswordCallback extends IsCallback {

    public void execute(String username, String password);
}
