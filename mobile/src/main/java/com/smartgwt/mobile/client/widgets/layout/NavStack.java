package com.smartgwt.mobile.client.widgets.layout;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.core.Function;
import com.smartgwt.mobile.client.internal.EventHandler;
import com.smartgwt.mobile.client.internal.EventUtil;
import com.smartgwt.mobile.client.internal.test.AutoTestLocatable;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.util.AnimationUtil;
import com.smartgwt.mobile.client.internal.util.AnimationUtil.Direction;
import com.smartgwt.mobile.client.internal.widgets.AdvancedPanelContainer;
import com.smartgwt.mobile.client.internal.widgets.events.BeforePanelShownEvent;
import com.smartgwt.mobile.client.internal.widgets.events.BeforePanelShownHandler;
import com.smartgwt.mobile.client.internal.widgets.events.HasBeforePanelShownHandlers;
import com.smartgwt.mobile.client.types.PanelHeaderPosition;
import com.smartgwt.mobile.client.widgets.Action;
import com.smartgwt.mobile.client.widgets.ActionContext;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.ContainerFeatures;
import com.smartgwt.mobile.client.widgets.Header1;
import com.smartgwt.mobile.client.widgets.NavigationButton;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.events.HasPanelHideHandlers;
import com.smartgwt.mobile.client.widgets.events.HasPanelShowHandlers;
import com.smartgwt.mobile.client.widgets.events.HasPanelSkipHandlers;
import com.smartgwt.mobile.client.widgets.events.PanelHideEvent;
import com.smartgwt.mobile.client.widgets.events.PanelHideHandler;
import com.smartgwt.mobile.client.widgets.events.PanelShowEvent;
import com.smartgwt.mobile.client.widgets.events.PanelShowHandler;
import com.smartgwt.mobile.client.widgets.events.PanelSkipEvent;
import com.smartgwt.mobile.client.widgets.events.PanelSkipHandler;
import com.smartgwt.mobile.client.widgets.tab.TabSet;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperElement;

public class NavStack extends Panel implements AdvancedPanelContainer, HasBeforePanelShownHandlers,
HasPanelShowHandlers, HasPanelHideHandlers, HasPanelSkipHandlers, ValueChangeHandler<String> {

    private boolean animating = false;
    private ScrollableLayout layout;
    private final int maxIconControls = 3;
    protected NavigationBar navigationBar = new NavigationBar(this);
    private Function<Void> resetAnimatingFlag = null;
    protected Stack<Panel> stack = new Stack<Panel>();
    private transient HandlerRegistration valueChangeRegistration;

    {
        layout  = new ScrollableLayout(44, 0);
        getElement().addClassName(NavigationBar._CSS.navStackClass());
    }

    public NavStack() {
        init();
    }

    public NavStack(String title) {
        super(title);
        init();
    }

    public NavStack(String title, ImageResource icon) {
        super(title,icon);
        init();
    }

    public NavStack(Panel panel) {
        super(panel.getTitle(),panel.getIcon());
        init();
        push(panel);
    }

    private void init() {
        if (Canvas._getFixNavigationBarPositionDuringKeyboardFocus()) {
            _sinkFocusInEvent();
            _sinkFocusOutEvent();
        }
    }

    @Override
    public Object _getAttributeFromSplitLocator(List<String> locatorArray, GetAttributeConfiguration configuration) {
        switch (configuration.getAttribute()) {
            case IS_CLICKABLE:
                if (animating) return false;
                break;
            default:
                break;
        }
        return super._getAttributeFromSplitLocator(locatorArray, configuration);
    }

    @Override
    public AutoTestLocatable _getChildFromLocatorSubstring(String substring, int index, List<String> locatorArray, GetAttributeConfiguration configuration) {
        if (substring.equals("navigationBar")) return navigationBar;
        if (substring.equals("top")) return top();
        return super._getChildFromLocatorSubstring(substring, index, locatorArray, configuration);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (_HISTORY_ENABLED) {
            valueChangeRegistration = History.addValueChangeHandler(this);
        }

        Widget parentWidget = getParent();
        if (parentWidget != null && parentWidget.getParent() instanceof TabSet) {
            layout.setBottom(0);
        } else {
            layout.setBottom(0);
        }
    }

    @Override
    public void onUnload() {
        if (_HISTORY_ENABLED) {
            if (valueChangeRegistration != null) {
                valueChangeRegistration.removeHandler();
                valueChangeRegistration = null;
            }
        } else {
            assert valueChangeRegistration == null;
        }
        super.onUnload();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            navigationBar._layOutMembers();
        }
    }

	public final NavigationBar getNavigationBar() {
		return navigationBar;
	}

    @SGWTInternal
    public final boolean _isAnimating() {
        return animating;
    }

    private void setAnimatingFlag() {
        resetAnimatingFlag = new Function<Void>() {
            @Override
            public Void execute() {
                if (resetAnimatingFlag == this) animating = false;
                return null;
            }
        };
        animating = true;
    }

    protected void pushHistory() {
        if (!_HISTORY_ENABLED) return;
        String historyToken = History.getToken();
        if(stack.size()>1) {
            String newHistory = buildHistory(stack.size());
            if(!historyToken.equals(newHistory)) {
                if(newHistory.length() > historyToken.length()) {
                    History.newItem(newHistory);
                }
            }
        }
    }

    protected void popHistory() {
        if (!_HISTORY_ENABLED) return;
        String history = buildHistory(stack.size());
        History.newItem(history);
    }
    
    protected String buildHistory(int depth) {
        String currentHistory = "";
        if(stack.size() >= depth) {
            for(int i = 0; i < depth; ++i) {
                currentHistory += stack.get(i).getTitle();
                if(i < depth-1) {
                    currentHistory += "/";
                }
            }
        }
        return currentHistory;
    }

	public void push(Panel panel) {
	    push(panel, Direction.LEFT);
	}

	protected void push(final Panel panel, Direction direction) {
        if (panel._getShowPanelHeader() && direction == Direction.UP) {
            final Panel oldPanel = stack.peek();
            setAnimatingFlag();
            AnimationUtil.slideTransition(oldPanel, panel, layout, direction, new Function<Void>() {
                @Override
                public Void execute() {
                    panel._containerChanged(NavStack.this);
                    BeforePanelShownEvent.fire(NavStack.this, panel);
                    executeActions(panel);
                    return null;
                }
            }, new Function<Void>() {
                @Override
                public Void execute() {
                    resetAnimatingFlag.execute();
                    PanelShowEvent.fire(NavStack.this, panel);
                    PanelHideEvent.fire(NavStack.this, oldPanel);
                    return null;
                }
            });
            ((ScrollableLayout)layout).setTop(0);
            stack.push(panel);
        } else {
    		panel.setHeaderPosition(PanelHeaderPosition.BOTTOM);
    		if(stack.size() > 0) {
    			// animate the new panel into place
                final Panel oldPanel = stack.peek();
    			// note: slideTransition automatically adds new member and removes old
                setAnimatingFlag();
                AnimationUtil.slideTransition(oldPanel, panel, layout, direction, new Function<Void>() {
                    @Override
                    public Void execute() {
                        navigationBar._push(panel);

                        panel._containerChanged(NavStack.this);
                        BeforePanelShownEvent.fire(NavStack.this, panel);
                        executeActions(panel);
                        return null;
                    }
                }, new Function<Void>() {
                    @Override
                    public Void execute() {
                        resetAnimatingFlag.execute();
                        PanelShowEvent.fire(NavStack.this, panel);
                        PanelHideEvent.fire(NavStack.this, oldPanel);
                        return null;
                    }
                });
    		} else {
    			this.addMember(navigationBar);
    			layout.addMember(panel);
    			this.addMember(layout);
    	        navigationBar.setTitleLabel(new Header1(panel.getTitle()));
                panel._containerChanged(NavStack.this);
                BeforePanelShownEvent.fire(this, panel);
                executeActions(panel);
                PanelShowEvent.fire(this, panel);
    		}
    		stack.push(panel);
            pushHistory();
        }
	}

    private void executeActions(final Panel panel) {
        Action[] actions = panel.getActions();
        if (actions != null) {
            final Set<Action> unhandledActions;
            {
                unhandledActions = new HashSet<Action>();
                final Action[] a = _getUnhandledActions(actions);
                if (a != null) unhandledActions.addAll(Arrays.asList(a));
            }

            final NavigationButton[] buttons = new NavigationButton[actions.length - unhandledActions.size()];
            int i = 0;
            for (final Action action : actions) {
                if (unhandledActions.contains(action)) continue;

                final NavigationButton button = buttons[i++] = new NavigationButton(action.getTitle());
                button.setIcon(action.getIcon(), true);
                button.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        action.execute(new ActionContext() {
                            @Override
                            public Panel getPanel() {
                                return panel;
                            }

                            @Override
                            public Canvas getControl() {
                                return button;
                            }
                        });
                    }
                });
            }

            navigationBar.setRightButtons(buttons);
        }
    }

    public Panel pop() {
        return pop(Direction.RIGHT);
    }

    public Panel pop(Direction direction) {
        final Panel topPanel = stack.peek();

        final Panel priorPanel = stack.pop();
        final Panel newPanel = stack.peek();
        assert priorPanel == topPanel;

        final Function<Void> afterCallback = new Function<Void>() {
            @Override
            public Void execute() {
                resetAnimatingFlag.execute();
                PanelShowEvent.fire(NavStack.this, newPanel);
                PanelHideEvent.fire(NavStack.this, priorPanel);
                return null;
            }
        };

        if (topPanel._getShowPanelHeader() && direction == Direction.DOWN) { 
            ((ScrollableLayout)layout).setTop(47);
            setAnimatingFlag();
            AnimationUtil.slideTransition(priorPanel, newPanel, layout, direction, new Function<Void>() {
                @Override
                public Void execute() {
                    BeforePanelShownEvent.fire(NavStack.this, newPanel);
                    return null;
                }
            }, afterCallback);
        } else {
            popHistory();
            // note: slideTransition automatically adds new member and removes old
            setAnimatingFlag();
            AnimationUtil.slideTransition(priorPanel, newPanel, layout, direction, new Function<Void>() {
                @Override
                public Void execute() {
                    navigationBar._pop();
                    BeforePanelShownEvent.fire(NavStack.this, newPanel);
                    return null;
                }
            }, afterCallback);
        }
        return topPanel;
    }

    /**
     * Pops off panels until <code>panel</code> is in view.  <code>panel</code> is guaranteed to
     * be visible after this method completes, even if it means that the stack of panels in this
     * <code>NavStack</code> is emptied and then <code>panel</code> is pushed onto the stack.
     * 
     * <p>For each panel that is skipped over (that is, it was not the visible panel, but it also
     * will not end up being the visible panel after this method completes), a
     * {@link com.smartgwt.mobile.client.widgets.events.PanelSkipEvent} is fired.  After
     * all the {@link com.smartgwt.mobile.client.widgets.events.PanelSkipEvent}s are fired,
     * there will be a {@link com.smartgwt.mobile.client.widgets.events.PanelShowEvent} fired
     * for the new panel in view and a {@link com.smartgwt.mobile.client.widgets.events.PanelHideEvent}
     * fired for the previously visible panel.
     * 
     * @param panel the panel that should be visible after the operation completes.
     * @return the previously-visible panel, or <code>null</code> if there was no previously
     * visible panel or the currently visible panel is <code>panel</code>.
     */
    public Panel popTo(Panel panel) {
        return popTo(panel, Direction.RIGHT);
    }

    public Panel popTo(final Panel panel, Direction direction) {
        if (! stack.isEmpty()) {
            final Panel topPanel = stack.peek();
            if (topPanel == panel) return null; // Nothing needs to be done because the navstack is already at `panel`.

            Panel priorPanel = null, newPanel = null;
            do {
                priorPanel = stack.pop();
                if (stack.isEmpty()) {
                    newPanel = null;
                } else {
                    newPanel = stack.peek();
                }

                if (priorPanel != topPanel) {
                    navigationBar._pop();
                    PanelSkipEvent panelSkippedEvent = new PanelSkipEvent(priorPanel);
                    fireEvent(panelSkippedEvent);
                }
            } while (! stack.isEmpty() && newPanel != panel);

            if (! stack.isEmpty()) {
                // We've found the panel.  Apply the transition from `topPanel` to `newPanel`, which equals `panel`.
                assert newPanel == panel;
                priorPanel = topPanel;
                final Panel finalNewPanel = newPanel, finalPriorPanel = priorPanel;
                // note: slideTransition automatically adds new member and removes old
                setAnimatingFlag();
                AnimationUtil.slideTransition(priorPanel, newPanel, layout, direction, new Function<Void>() {
                    @Override
                    public Void execute() {
                        navigationBar._pop();
                        BeforePanelShownEvent.fire(NavStack.this, finalNewPanel);
                        return null;
                    }
                }, new Function<Void>() {
                    @Override
                    public Void execute() {
                        resetAnimatingFlag.execute();
                        PanelShowEvent.fire(NavStack.this, finalNewPanel);
                        PanelHideEvent.fire(NavStack.this, finalPriorPanel);
                        return null;
                    }
                });
            } else {
                layout.removeMember(priorPanel);
                if (direction == null) push(panel);
                else push(panel, direction.getOppositeDirection());
                PanelHideEvent panelHiddenEvent = new PanelHideEvent(topPanel);
                fireEvent(panelHiddenEvent);
            }
            return topPanel;
        } else {
            assert stack.isEmpty();
            if (direction == null) push(panel);
            else push(panel, direction.getOppositeDirection());
            return null;
        }
    }

	public Panel get(int i) {
		return stack.get(i);
	}

    public void clear() {
        if (animating) throw new IllegalStateException("The NavStack cannot be cleared while a transition animation is in progress.");
        if (!stack.isEmpty()) {
            removeMember(layout);
            final Panel topPanel = top();
            layout.removeMember(topPanel);
            removeMember(navigationBar);

            PanelHideEvent.fire(this, topPanel);

            while (!stack.isEmpty()) {
                PanelSkipEvent.fire(this, stack.pop());
            }

            navigationBar._clear();
        }
    }

	public Panel top() {
		return stack.peek();
	}

	public int size() {
		return stack.size();
	}

	public void setSinglePanel(Panel panel) {
		clear();
        push(panel);
	}

    @Override
    public ContainerFeatures getContainerFeatures() {
        return new ContainerFeatures(this, false, true, true, true, maxIconControls);
    }

    @Override
    public Action[] _getUnhandledActions(Action[] actions) {
        if (actions == null || actions.length == 0) return null;
        if (!getContainerFeatures().isShowsPrimaryAction() ||
            actions.length > maxIconControls)
        {
            return actions;
        }

        final boolean allIconOnly;
        {
            boolean b = true;
            for (final Action action : actions) {
                if (action.getIcon() == null ||
                    (action.getTitle() != null && !action.getTitle().isEmpty()))
                {
                    b = false;
                    break;
                }
            }
            allIconOnly = b;
        }

        if (!allIconOnly && actions.length > 1) {
            // Only the first action is handled.
            Action[] unhandledActions = new Action[actions.length - 1];
            System.arraycopy(actions, 1, unhandledActions, 0, unhandledActions.length);
            return unhandledActions;
        }

        return null;
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        if (Canvas._getFixNavigationBarPositionDuringKeyboardFocus()) {
            final String eventType = event.getType();
            if ("focusin".equals(eventType)) {
                final SuperElement targetElem = EventUtil.getTargetElem(event);
                if (EventHandler.couldShowSoftKeyboard(targetElem)) {
                    navigationBar._fixPosition();
                }
            } else if ("focusout".equals(eventType)) {
                final SuperElement targetElem = EventUtil.getTargetElem(event);
                if (EventHandler.couldShowSoftKeyboard(targetElem)) {
                    navigationBar._scheduleUnfixPosition();
                }
            }
        }
    }

    @Override
    public HandlerRegistration _addBeforePanelShownHandler(BeforePanelShownHandler handler) {
        return addHandler(handler, BeforePanelShownEvent.getType());
    }

	@Override
	public HandlerRegistration addPanelHideHandler(PanelHideHandler handler) {
        return this.addHandler(handler, PanelHideEvent.getType());
	}

	@Override
	public HandlerRegistration addPanelShowHandler(PanelShowHandler handler) {
        return this.addHandler(handler, PanelShowEvent.getType());
	}

    @Override
    public HandlerRegistration addPanelSkipHandler(PanelSkipHandler handler) {
        return addHandler(handler, PanelSkipEvent.getType());
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String value = event.getValue();
        if(stack.size() > 0) {
            int size = stack.size();
            String backOne = buildHistory(size-1);
            if(value != null && value.length() > 0 && value.equals(backOne)) {
                pop();
            }
        }
    }
}
