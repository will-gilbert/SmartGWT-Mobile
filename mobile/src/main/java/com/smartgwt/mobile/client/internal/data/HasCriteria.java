package com.smartgwt.mobile.client.internal.data;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.Criteria;

@SGWTInternal
public interface HasCriteria {

    public Criteria getCriteria();

    public void setCriteria(Criteria criteria);
}
