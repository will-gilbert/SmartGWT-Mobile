package com.smartgwt.mobile.client.theme;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Shared;

@Shared
public interface ScrollablePanelCssResource extends CssResource {

    @ClassName("sc-scrollable")
    public String scrollablePanelClass();

    @ClassName("scrollablePanelContent")
    public String scrollablePanelContentClass();

    // Using WebKit terminology for the following
    // https://www.webkit.org/blog/363/styling-scrollbars/

    @ClassName("verticalScrollbarTrack")
    public String verticalScrollbarTrackClass();

    @ClassName("verticalScrollbarThumb")
    public String verticalScrollbarThumbClass();
}
