package com.smartgwt.mobile.client.internal.data;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.SortSpecifier;

@SGWTInternal
public interface HasSortSpecifiers {

    public SortSpecifier[] getSort();

    public void setSort(SortSpecifier... sortSpecifiers);
}
