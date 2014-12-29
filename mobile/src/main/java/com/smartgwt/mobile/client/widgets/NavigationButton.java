package com.smartgwt.mobile.client.widgets;

import com.smartgwt.mobile.client.internal.widgets.NavigationButtons;
import com.smartgwt.mobile.client.internal.widgets.layout.NavigationBarItem;
import com.smartgwt.mobile.client.types.NavigationDirection;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;

public class NavigationButton extends ToolStripButton implements NavigationBarItem {

    private NavigationDirection direction = NavigationDirection.NONE;

    public NavigationButton(String title) {
        super(title);
    }

    public NavigationButton(String title, NavigationDirection direction) {
        this(title);
        setDirection(direction);
    }

    public NavigationButton(NavigationDirection direction) {
        super();
        setDirection(direction);
    }

    public NavigationDirection getDirection() {
        return direction;
    }

    public void setDirection(NavigationDirection direction) {
        if (direction == null) direction = NavigationDirection.NONE;
        if (getParent() instanceof NavigationButtons && direction != NavigationDirection.NONE) {
            throw new IllegalArgumentException("Only NavigationDirection NONE is allowed when multiple buttons are in use.");
        }
        if (this.direction != direction) {
            if (this.direction != NavigationDirection.NONE) getElement().removeClassName(this.direction._getClassName());
            this.direction = direction;

            if (this.direction != NavigationDirection.NONE) {
                getElement().addClassName(this.direction._getClassName());
                if (tintColor != null) {
                    setTintColor(tintColor);
                }
            }
            _fireContentChangedEvent();
        }
    }
}
