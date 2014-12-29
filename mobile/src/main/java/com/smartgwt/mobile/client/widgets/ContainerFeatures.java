package com.smartgwt.mobile.client.widgets;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.widgets.AdvancedPanelContainer;

public class ContainerFeatures {

    private PanelContainer panelContainer;
	private int maxIconControls;
	private boolean showsIcon;
	private boolean showsPrimaryAction;
	private boolean showsPrimaryActionTitle;
	private boolean showsTitle;

	public ContainerFeatures(PanelContainer panelContainer, boolean showsIcon, boolean showsPrimaryAction, boolean showsPrimaryActionTitle, boolean showsTitle, int maxIconControls) {
        if (maxIconControls < 0) throw new IllegalArgumentException("`maxIconControls' must be non-negative.");
        this.panelContainer = panelContainer;
		this.showsIcon = showsIcon;
		this.showsPrimaryAction = showsPrimaryAction;
		this.showsPrimaryActionTitle = showsPrimaryActionTitle;
		this.showsTitle = showsTitle;
		this.maxIconControls = maxIconControls;
	}
	public int getMaxIconControls() {
		return maxIconControls;
	}
	public boolean isShowsIcon() {
		return showsIcon;
	}
	public boolean isShowsPrimaryAction() {
		return showsPrimaryAction;
	}
	public boolean isShowsPrimaryActionTitle() {
		return showsPrimaryActionTitle;
	}
	public boolean isShowsTitle() {
		return showsTitle;
	}

    @SGWTInternal
    public Action[] _getUnhandledActions(Action[] actions) {
        if (panelContainer instanceof AdvancedPanelContainer) return ((AdvancedPanelContainer)panelContainer)._getUnhandledActions(actions);
        if (isShowsPrimaryAction() && actions != null && actions.length <= maxIconControls) return null; // All actions are handled.
        return actions; // No actions are handled.
    }
}
