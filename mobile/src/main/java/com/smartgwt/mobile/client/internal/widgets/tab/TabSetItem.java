package com.smartgwt.mobile.client.internal.widgets.tab;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.theme.TabSetCssResource;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.events.DropEvent;
import com.smartgwt.mobile.client.widgets.events.DropHandler;
import com.smartgwt.mobile.client.widgets.events.DropOutEvent;
import com.smartgwt.mobile.client.widgets.events.DropOutHandler;
import com.smartgwt.mobile.client.widgets.events.DropOverEvent;
import com.smartgwt.mobile.client.widgets.events.DropOverHandler;
import com.smartgwt.mobile.client.widgets.events.HasDropHandlers;
import com.smartgwt.mobile.client.widgets.events.HasDropOutHandlers;
import com.smartgwt.mobile.client.widgets.events.HasDropOverHandlers;
import com.smartgwt.mobile.client.widgets.icons.IconResources;
import com.smartgwt.mobile.client.widgets.tab.Tab;
import com.smartgwt.mobile.client.widgets.tab.TabSet;

@SGWTInternal
public class TabSetItem extends Canvas implements HasDropOverHandlers, HasDropOutHandlers, HasDropHandlers {

    private static final TabSetCssResource CSS = TabSet._CSS;

    public static final String DROP_OVER_CLASS_NAME = "drop-over";

    private Tab tab;
    private boolean selected = false;

    private Canvas badge = null;
    private Canvas image = null;
    private Canvas title = null;
    
    private HandlerRegistration clickRegistration, dropOverRegistration, dropOutRegistration,
            dropRegistration;

    public TabSetItem(Tab tab) {
        super(Document.get().createDivElement());
        if (tab == null) throw new NullPointerException("`tab' cannot be `null'.");
        this.tab = tab;

        final Element element = getElement();
        element.setClassName(CSS.tabClass());
        element.addClassName("sc-layout-box-pack-center");
    }

    private void removeHandlers() {
        if (dropRegistration != null) {
            dropRegistration.removeHandler();
            dropRegistration = null;
        }
        if (dropOutRegistration != null) {
            dropOutRegistration.removeHandler();
            dropOutRegistration = null;
        }
        if (dropOverRegistration != null) {
            dropOverRegistration.removeHandler();
            dropOverRegistration = null;
        }
        if (clickRegistration != null) {
            clickRegistration.removeHandler();
            clickRegistration = null;
        }
    }

    @Override
    public void destroy() {
        removeHandlers();
        tab = null;
        super.destroy();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (Canvas._isIOSMin6_0()) internalApplySelected(selected);
    }

    public final Tab getTab() {
        return tab;
    }

    public void setTabSet(final TabSet tabSet) {
        removeHandlers();
        if (tabSet != null) {
            if (tab == tabSet._getMoreTab()) {
                // On click, use a different code path to select the More tab because we do
                // not want a TabSelectedEvent to be fired. This would expose a reference to
                // the internal `moreTab'.
                clickRegistration = addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        tabSet._selectMoreTab();
                    }
                });
                // The More tab is not eligible for drag & drop.
            } else {
                clickRegistration = addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        tabSet.selectTab(tab);
                    }
                });
                dropOverRegistration = addDropOverHandler(new DropOverHandler() {
                    @Override
                    public void onDropOver(DropOverEvent event) {
                        getElement().addClassName(DROP_OVER_CLASS_NAME);
                        // TODO Move this to tabs.css
                        getElement().getStyle().setProperty("backgroundImage", "-webkit-gradient(radial, center center, 0, 50%  50%, 50, from(rgba(255,255,255,0.10)), to(rgba(0,0,0, 0.15)), color-stop(0%,white))");
                    }
                });
                dropOutRegistration = addDropOutHandler(new DropOutHandler() {
                    public void onDropOut(DropOutEvent event) {
                        getElement().removeClassName(DROP_OVER_CLASS_NAME);
                        getElement().getStyle().setProperty("backgroundImage", "none");
                    }
                });
                dropRegistration = addDropHandler(new DropHandler() {
                    public void onDrop(DropEvent event) {
                        tabSet._onTabDrop(TabSetItem.this, event);
                    }
                });
            }
        }
    }

    private void internalApplySelected(boolean selected) {
        if (selected) {
            _setClassName(CSS.activeTabClass(), false);
        } else {
            _removeClassName(CSS.activeTabClass());
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;

        // Work-around an issue that affects `TabSet's on iOS 6.
        // See TabSet.onLoad() for a description of the issue.
        if (Canvas._isIOSMin6_0() && !isAttached()) return;

        internalApplySelected(selected);
    }

    public void setBadge(String value) {
        if (badge != null) {
            badge.destroy();
            badge = null;
        }
        if (value != null && !(value = value.trim()).isEmpty()) {
            final DivElement divElem = Document.get().createDivElement();
            divElem.setClassName(CSS.tabBadgeWrapperClass());
            divElem.setInnerHTML("<div class='" + CSS.tabBadgeClass() + "'><p class='" + CSS.tabBadgeContentClass() + "'>" + value + "</p></div>");
            badge = new Canvas(divElem);
            addChild(0, badge);
        }
    }

    public void setIcon(Image icon, boolean prerendered) {
        if (image != null) {
            image.destroy();
            image = null;
        }
        if (icon == null) {
            getElement().removeClassName(CSS.tabHasIconClass());
            getElement().removeClassName(CSS.tabIconPrerenderedClass());
        } else {
            if (!prerendered) {
                getElement().removeClassName(CSS.tabIconPrerenderedClass());
                final ImageElement imgElem = Document.get().createImageElement();
                imgElem.setSrc(IconResources.INSTANCE.blank().getSafeUri().asString());
                imgElem.getStyle().setProperty("webkitMaskBoxImage", "url(" + icon.getUrl() + ")");
                image = new Canvas(imgElem);
                addChild(image);
            } else {
                getElement().addClassName(CSS.tabIconPrerenderedClass());
                final DivElement divElem = Document.get().createDivElement();
                divElem.setClassName(CSS.tabIconWrapperClass());
                divElem.getStyle().setBackgroundImage("url(" + icon.getUrl() + ")");
                image = new Canvas(divElem);
                addChild(image);
            }
            getElement().addClassName(CSS.tabHasIconClass());
        }
    }

    public void setTitle(String title) {
        if (this.title != null) {
            this.title.destroy();
            this.title = null;
        }
        if (title != null) {
            final SpanElement spanElem = Document.get().createSpanElement();
            spanElem.setInnerHTML(title);
            spanElem.setClassName(CSS.tabTitleClass());
            this.title = new Canvas(spanElem);
            addChild(this.title);
        }
    }

    @Override
    public HandlerRegistration addDropHandler(DropHandler handler) {
        return addHandler(handler, DropEvent.getType());
    }

    @Override
    public HandlerRegistration addDropOutHandler(DropOutHandler handler) {
        return addHandler(handler, DropOutEvent.getType());
    }

    @Override
    public HandlerRegistration addDropOverHandler(DropOverHandler handler) {
        return addHandler(handler, DropOverEvent.getType());
    }
}
