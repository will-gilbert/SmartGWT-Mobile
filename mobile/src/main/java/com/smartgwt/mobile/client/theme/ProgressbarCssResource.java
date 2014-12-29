package com.smartgwt.mobile.client.theme;

import com.google.gwt.resources.client.CssResource;
import com.smartgwt.mobile.SGWTInternal;

public interface ProgressbarCssResource extends CssResource {

    @ClassName("sc-progressbar")
    public String progressbarClass();

    @ClassName("progressbarBar")
    @SGWTInternal
    public String _progressbarBarClass();
}
