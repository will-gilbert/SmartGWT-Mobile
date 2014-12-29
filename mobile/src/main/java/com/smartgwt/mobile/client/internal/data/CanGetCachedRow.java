package com.smartgwt.mobile.client.internal.data;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.SelectionItem;

@SGWTInternal
public interface CanGetCachedRow<I extends SelectionItem> {

    @SGWTInternal
    public I _getCachedRow(int rowNum);
}
