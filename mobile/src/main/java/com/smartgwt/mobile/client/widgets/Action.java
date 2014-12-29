package com.smartgwt.mobile.client.widgets;

import com.google.gwt.resources.client.ImageResource;

public abstract class Action {

    private ImageResource icon;
    private int iconSize;
    private String title;
    private String tooltip;

    public Action(String title) {
        this.title = title;
    }

    public Action(ImageResource icon) {
        this.icon = icon;
    }

    public Action(String title, ImageResource icon) {
        this(title);
        this.icon = icon;
    }

    public Action(String title, ImageResource icon, int iconSize) {
        this(title, icon);
        this.iconSize = iconSize;
    }

    public Action(String title, ImageResource icon, int iconSize, String tooltip) {
        this(title, icon, iconSize);
        this.tooltip = tooltip;
    }

    public final ImageResource getIcon() {
        return icon;
    }

    public final int getIconSize() {
        return iconSize;
    }

    public final String getTitle() {
        return title;
    }

    public final String getTooltip() {
        return tooltip;
    }

    public abstract void execute(ActionContext context);
}
