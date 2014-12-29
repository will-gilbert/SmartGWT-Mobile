package com.smartgwt.mobile.client.internal.theme;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Shared;
import com.smartgwt.mobile.SGWTInternal;

@Shared
@SGWTInternal
public interface PopupCssResource extends CssResource {

    // Note: These style names are prefixed with an underscore because a non-internal
    // `CssResource' may extend this interface.

    @ClassName("_popupContainer")
    @SGWTInternal
    public String _popupContainerClass();

    @ClassName("_popupModalMask")
    @SGWTInternal
    public String _popupModalMaskClass();

    @ClassName("_showing")
    @SGWTInternal
    public String _showingClass();

    @ClassName("_hiding")
    @SGWTInternal
    public String _hidingClass();
}
