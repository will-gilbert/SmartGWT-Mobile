package com.smartgwt.mobile.client.internal.test;

import java.util.List;

import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public interface AutoTestLocatable {

    @SGWTInternal
    public Object _getAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration);

    @SGWTInternal
    public AutoTestLocatable _getChildFromLocatorSubstring(String substring, int index, List<String> locatorArray, GetAttributeConfiguration configuration);

    @SGWTInternal
    public Object _getInnerAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration);
}
