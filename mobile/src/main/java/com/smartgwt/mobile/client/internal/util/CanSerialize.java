package com.smartgwt.mobile.client.internal.util;

import java.io.IOException;
import java.util.Map;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.util.JSONEncoder;

@SGWTInternal
public interface CanSerialize {

    @SGWTInternal
    public Appendable _serializeTo(Appendable a, Map<Object, String> objPaths, String objPath, String prefix, JSONEncoder settings) throws IOException;
}
