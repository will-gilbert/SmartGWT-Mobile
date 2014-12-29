package com.smartgwt.mobile.client.types;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.widgets.NavigationButton;

/**
 * Should a button render with a specific navigation direction or as a square action button?
 */
public enum NavigationDirection {
    /**
     * No navigation direction - renders as a square button
     */
    NONE("none"),

    /**
     * Navigate forward - renders as an arrow pointing right
     */
    FORWARD("next"),

    /**
     * Navigate backwards - renders as an arrow pointing left
     */
    BACK("back");

    NavigationDirection(String value) {}

    @SGWTInternal
    public final String _getClassName() {
        switch (this) {
            case NONE:
                return null;
            case FORWARD:
                return NavigationButton._CSS.nextButtonClass();
            case BACK:
                return NavigationButton._CSS.backButtonClass();
        }
        assert false : "NavigationDirection._getClassName() needs to handle NavigationDirection." + this;
        throw new RuntimeException();
    }
}