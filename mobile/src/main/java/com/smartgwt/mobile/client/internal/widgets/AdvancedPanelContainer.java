package com.smartgwt.mobile.client.internal.widgets;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.Action;
import com.smartgwt.mobile.client.widgets.PanelContainer;

@SGWTInternal
public interface AdvancedPanelContainer extends PanelContainer {

    @SGWTInternal
    public Action[] _getUnhandledActions(Action[] actions);
}
