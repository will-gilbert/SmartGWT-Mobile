package com.smartgwt.mobile.client.internal.data;

import java.util.Date;

import com.google.gwt.i18n.shared.TimeZone;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public interface CanFormatDateTime {

    public String format(Date date, TimeZone timeZone);
}
