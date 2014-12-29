package com.smartgwt.mobile.client.widgets;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.types.AttributeType;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.PanelHeaderPosition;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.Layout;
import com.smartgwt.mobile.client.widgets.layout.SegmentedControl;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;

public class Panel extends Layout {

    private Action[] actions;
    private PanelContainer container;
	protected PanelHeader header;
    private Boolean showPanelHeader;

    public native int getWheelDelta(Event event) /*-{
		return event.wheelDelta;
	}-*/;

	public Panel() {
        this.header = new PanelHeader(null);
	}

	public Panel(String title) {
		this.header = new PanelHeader(title);
	}

	public Panel(String title, ImageResource icon) {
		this.header = new PanelHeader(title, icon);
	}

    public final Action[] getActions() {
        return actions;
	}

    public void setActions(Action... actions) {
        this.actions = actions;
    }

    public final PanelContainer getPanelContainer() {
        return container;
    }

    @SGWTInternal
    public final PanelHeader _getHeader() {
        return header;
    }

	public PanelHeaderPosition getHeaderPosition() {
		return header.getHeaderPosition();
	}

	public void setHeaderPosition(PanelHeaderPosition headerPosition) {
		this.header.setHeaderPosition(headerPosition);
	}

	public String getIconStyle() {
		return this.header.getIconStyle();
	}

	public void setIconStyle(String iconStyle) {
		this.header.setIconStyle(iconStyle);
	}

    @Override
    public Object _getInnerAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration) {
        if (configuration.getAttribute() == AttributeType.VALUE) {
            if (locatorArray.size() == 1) {
                final String valueName = locatorArray.get(0);
                if ("title".equals(valueName)) return getTitle();
            }
        }
        return super._getInnerAttributeFromSplitLocator(locatorArray, configuration);
    }

    public final Boolean getShowPanelHeader() {
		return showPanelHeader;
	}

    @SGWTInternal
    public final boolean _getShowPanelHeader() {
        return _booleanValue(showPanelHeader, false);
    }

    public void setShowPanelHeader(Boolean showPanelHeader) {
		this.showPanelHeader = showPanelHeader;
	}

	public String getTitle() {
		return this.header.getTitle();
	}

	public void setTitle(String title) {
		this.header.setTitle(title);
	}
	
	public ImageResource getIcon() {
		return this.header.getIcon();
	}

	public void setIcon(ImageResource icon) {
		this.header.setIcon(icon);
	}

    @SGWTInternal
    public void _containerChanged(PanelContainer container) {
		this.container = container;
        final ContainerFeatures features = container.getContainerFeatures();
        assert features.getMaxIconControls() > 0;
        final Action[] unhandledActions = features._getUnhandledActions(actions);
        final boolean willAddTitle = !features.isShowsTitle(),
                willAddSC = (unhandledActions != null);
        if (_getShowPanelHeader() || willAddTitle || willAddSC) {
            final ToolStrip toolbar = new ToolStrip();

            if (willAddTitle) {
                final Header1 title = new Header1(header.getTitle());
                toolbar.addMember(title);
            } else {
                toolbar.setAlign(Alignment.RIGHT);
            }

            if (willAddSC) {
                final SegmentedControl sc = new SegmentedControl();
                sc.setInheritTint(true);
                if (!willAddTitle) toolbar.addSpacer();
                assert unhandledActions != null;
                final Segment[] segments = new Segment[unhandledActions.length];
                for (int i = 0; i < unhandledActions.length; ++i) {
                    final Action action = unhandledActions[i];
                    final Segment segment = segments[i] = new Segment(action.getTitle());
                    segment.setIcon(action.getIcon(), true);
                    segment.addClickHandler(new ClickHandler() {
	                    @Override
	                    public void onClick(ClickEvent event) {
	                        action.execute(new ActionContext() {
	                            @Override
	                            public Panel getPanel() {
	                                return Panel.this;
	                            }
	                            @Override
	                            public Canvas getControl() {
                                    return segment;
	                            }                           
	                        });
	                    }            
	                });
	            }
                sc.setSegments(segments);
                toolbar.addSegmentedControl(sc);
	        }

            addMember(toolbar, 0);
		}
	}
}
