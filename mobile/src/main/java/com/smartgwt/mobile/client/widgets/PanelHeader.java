package com.smartgwt.mobile.client.widgets;

import com.google.gwt.resources.client.ImageResource;
import com.smartgwt.mobile.client.types.PanelHeaderPosition;
import com.smartgwt.mobile.client.widgets.layout.HLayout;

public class PanelHeader extends HLayout {
	protected PanelHeaderPosition headerPosition = PanelHeaderPosition.TOP;
	protected ImageResource icon;
	protected String iconStyle;
	protected boolean showPanelHeader = true;
	protected String title;
	
	public PanelHeader(String title) {
		this.title = title;
	}
	
	public PanelHeader(String title, ImageResource icon) {
		this(title);
		this.icon = icon;
	}

	public PanelHeaderPosition getHeaderPosition() {
		return headerPosition;
	}

	public void setHeaderPosition(PanelHeaderPosition headerPosition) {
		this.headerPosition = headerPosition;
	}

	public ImageResource getIcon() {
		return icon;
	}

	public void setIcon(ImageResource icon) {
		this.icon = icon;
	}

	public String getIconStyle() {
		return iconStyle;
	}

	public void setIconStyle(String iconStyle) {
		this.iconStyle = iconStyle;
	}

	public boolean isShowPanelHeader() {
		return showPanelHeader;
	}

	public void setShowPanelHeader(boolean showPanelHeader) {
		this.showPanelHeader = showPanelHeader;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


}
