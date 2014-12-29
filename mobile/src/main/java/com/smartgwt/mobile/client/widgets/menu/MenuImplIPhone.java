package com.smartgwt.mobile.client.widgets.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.core.Function;
import com.smartgwt.mobile.client.internal.EventUtil;
import com.smartgwt.mobile.client.internal.theme.iphone.MenuCssResourceIPhone;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.menu.events.ItemClickEvent;
import com.smartgwt.mobile.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;
import com.smartgwt.mobile.internal.gwt.dom.client.event.AnimationEndEvent;

@SGWTInternal
class MenuImplIPhone extends MenuImpl {

    private static final MenuCssResourceIPhone CSS = (MenuCssResourceIPhone)Menu._CSS;
    private static final Element CONTENT_MEASURER_ELEM = Document.get().createDivElement();
    private static final int MIN_MENU_PAGE_SIDE_MARGIN_WIDTH_PX = 13;
    static {
        CONTENT_MEASURER_ELEM.setClassName(CSS.contentMeasurerClass());
        Document.get().getBody().insertFirst(CONTENT_MEASURER_ELEM);
    }

    private static int measureContent(String title) {
        CONTENT_MEASURER_ELEM.setInnerHTML(title);
        final int ret = CONTENT_MEASURER_ELEM.getOffsetWidth();
        CONTENT_MEASURER_ELEM.setInnerText("");
        return ret;
    }

    private static enum CurrentOp {
        NONE, SHOWING, HIDING, FLIPPING_PAGE;
    };

    private static class MenuItemDatum {
        public MenuItem item;

        public Element itemElement = null;

        public int contentWidth;

        public MenuItemDatum(MenuItem item) {
            this.item = item;
        }
    }

    // When there are many options, the iPhone will subdivide the items into "pages" and add
    // left/right arrow buttons to scroll through the pages of items.
    private class Page extends Canvas {

        private static final String ITEM_DATUM_INDEX_ATTRIBUTE_NAME = "data-sc-itemDatumIndex";

        private int index;

        private Element leftArrowItemElem = null, rightArrowItemElem = null;
        private boolean rightArrowItemDisabled = false;
        private MenuItemDatum[] itemData;
        private Map<MenuItemDatum, Element> itemElements = null;

        public Page(int index) {
            this.index = index;
            final Element div = Document.get().createDivElement();
            div.setClassName(CSS.menuPageClass());
            setElement(div);
            sinkEvents(Event.ONCLICK);
        }

        @Override
        public void destroy() {
            itemElements.clear();
            for (MenuItemDatum itemDatum : itemData) {
                itemDatum.itemElement = null;
            }
            itemData = null;
            super.destroy();
        }

        public final int getIndex() {
            return index;
        }

        public final MenuItemDatum[] getItemData() {
            return itemData;
        }

        private Integer findItemIndex(Element element) {
            for (; element != null; element = element.getParentElement()) {
                if (element.equals(MenuImplIPhone.this.self.getElement())) break;
                if (element.hasAttribute(ITEM_DATUM_INDEX_ATTRIBUTE_NAME)) {
                    return Integer.valueOf(element.getAttribute(ITEM_DATUM_INDEX_ATTRIBUTE_NAME));
                }
            }
            return null;
        }

        @Override
        public void onBrowserEvent(Event event) {
            super.onBrowserEvent(event);
            final Element targetElem;
            switch (event.getTypeInt()) {
                case Event.ONCLICK:
                    if (currentOp != CurrentOp.NONE) return;

                    targetElem = EventUtil.getTargetElem(event);
                    if (targetElem != null) {
                        if (leftArrowItemElem != null && leftArrowItemElem.isOrHasChild(targetElem)) {
                            event.stopPropagation();
                            leftArrowItemElem.addClassName(CSS.selectedClass());
                            MenuImplIPhone.this.prevPage(this, new Function<Void>() {
                                @Override
                                public Void execute() {
                                    leftArrowItemElem.removeClassName(CSS.selectedClass());
                                    return null;
                                }
                            });
                        } else if (rightArrowItemElem != null && !rightArrowItemDisabled &&
                                rightArrowItemElem.isOrHasChild(targetElem))
                        {
                            event.stopPropagation();
                            rightArrowItemElem.addClassName(CSS.selectedClass());
                            MenuImplIPhone.this.nextPage(this, new Function<Void>() {
                                @Override
                                public Void execute() {
                                    rightArrowItemElem.removeClassName(CSS.selectedClass());
                                    return null;
                                }
                            });
                        } else {
                            final Integer itemDatumIndex = findItemIndex(targetElem);
                            if (itemDatumIndex != null) {
                                final MenuItemDatum itemDatum = itemData[itemDatumIndex.intValue()];
                                activeItemDatum = itemDatum;
                                if (itemDatum.item.getEnabled()) {
                                    MenuItemClickEvent._fire(itemDatum.item, MenuImplIPhone.this.target, MenuImplIPhone.this.self);
                                    final boolean cancelled = ItemClickEvent._fire(self, itemDatum.item);
                                    if (!cancelled) {
                                        final Element itemElement = itemElements.get(itemDatum);
                                        itemElement.addClassName(CSS.selectedClass());
                                        final boolean menuTipSelected = MenuImplIPhone.this.menuTipItemDatum == itemDatum;
                                        if (menuTipSelected) menuTipElem.addClassName(CSS.selectedClass());
                                        MenuImplIPhone.this.self._hide();
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        }

        public void setItemData(MenuItemDatum[] itemData, boolean showLeftArrowItem, boolean showRightArrowItem, boolean isLastPage) {
            this.itemData = itemData;
            itemElements = new HashMap<MenuItemDatum, Element>();

            final Element pageElem = getElement();

            final Document document = Document.get();
            if (showLeftArrowItem) {
                leftArrowItemElem = document.createDivElement();
                leftArrowItemElem.setClassName(CSS.menuItemClass());
                leftArrowItemElem.addClassName(CSS.firstClass());
                leftArrowItemElem.addClassName(CSS.arrowItemClass());
                leftArrowItemElem.setInnerHTML("<div class='" + CSS.contentClass() + "'><span class='" + CSS.labelClass() + "'>&#9664;</span></div>");
                pageElem.appendChild(leftArrowItemElem);
            }
            if (itemData != null) {
                for (int i = 0; i < itemData.length; ++i) {
                    final MenuItemDatum itemDatum = itemData[i];
                    final Element itemElem = document.createDivElement();
                    itemDatum.itemElement = itemElem;
                    itemElem.setAttribute(ITEM_DATUM_INDEX_ATTRIBUTE_NAME, Integer.toString(i));
                    itemElem.setClassName(CSS.menuItemClass());
                    if (!itemDatum.item.getEnabled()) itemElem.addClassName(CSS.disabledClass());
                    final Element contentElem = document.createDivElement();
                    contentElem.setClassName(CSS.contentClass());
                    itemElem.appendChild(contentElem);
                    final Element labelElem = document.createSpanElement();
                    labelElem.setClassName(CSS.labelClass());
                    labelElem.setInnerHTML(itemDatum.item.getTitle());
                    contentElem.appendChild(labelElem);
                    pageElem.appendChild(itemElem);
                    itemElements.put(itemDatum, itemElem);
                }

                if (itemData.length > 0) {
                    if (!showLeftArrowItem) itemData[0].itemElement.addClassName(CSS.firstClass());
                    if (!showRightArrowItem) itemData[itemData.length - 1].itemElement.addClassName(CSS.lastClass());
                }
            }
            if (showRightArrowItem) {
                rightArrowItemElem = document.createDivElement();
                rightArrowItemElem.setClassName(CSS.menuItemClass());
                rightArrowItemElem.addClassName(CSS.lastClass());
                rightArrowItemElem.addClassName(CSS.arrowItemClass());
                rightArrowItemElem.setInnerHTML("<div class='" + CSS.contentClass() + "'><span class='" + CSS.labelClass() + "'>&#9654;</span></div>");
                rightArrowItemDisabled = !isLastPage;
                if (rightArrowItemDisabled) rightArrowItemElem.addClassName(CSS.disabledClass());
                pageElem.appendChild(rightArrowItemElem);
            }
        }
    }

    private Canvas screenCover;
    private Menu self;
    private List<? extends MenuItem> items = null;
    private List<Page> pages = new ArrayList<Page>();
    private int firstPageWidth;

    private Element menuTipWrapperElem;
    private Element menuTipElem;

    private Canvas target = null;
    private int lastX, lastMinY;
    private CurrentOp currentOp = CurrentOp.NONE;
    private boolean destroyAfterHiding = false;
    private Page visiblePage = null;
    // The MenuItemDatum corresponding to the menu tip, which is positioned directly beneath.
    private MenuItemDatum menuTipItemDatum = null;
    private MenuItemDatum activeItemDatum = null;

    @Override
    void init(Menu self) {
        this.self = self;
        final Document document = Document.get();

        final Element screenCoverElem = document.createDivElement();
        screenCoverElem.setClassName(CSS.screenCoverClass());
        screenCover = new Canvas(screenCoverElem) {
            {
                sinkEvents(Event.ONCLICK);
            }

            @Override
            public void onBrowserEvent(Event event) {
                super.onBrowserEvent(event);
                switch (event.getTypeInt()) {
                    // If the screen cover is tapped, hide the context menu.
                    case Event.ONCLICK:
                        MenuImplIPhone.this.self._hide();
                        break;
                }
            }
        };

        self._sinkAnimationEndEvent();

        final Element element = self.getElement();
        menuTipWrapperElem = document.createDivElement();
        menuTipWrapperElem.setClassName(CSS.menuTipWrapperClass());
        element.appendChild(menuTipWrapperElem);
        menuTipElem = document.createDivElement();
        menuTipElem.setClassName(CSS.menuTipClass());
        menuTipWrapperElem.appendChild(menuTipElem);

        screenCover.addChild(self);
    }

    private void destroyPages() {
        if (visiblePage != null) {
            final Element visiblePageElem = visiblePage.getElement();
            if (visiblePageElem.getParentNode() != null) visiblePageElem.removeFromParent();
            visiblePage = null;
        }
        for (Page page : pages) {
            page.destroy();
        }
        pages.clear();
    }

    @Override
    void destroyImpl(Menu self) {
        assert this.self == self;
        if (currentOp != CurrentOp.NONE) {
            destroyAfterHiding = true;
            return;
        }
        destroyPages();
    }

    @Override
    void onBrowserEvent(Menu self, Event event) {
        super.onBrowserEvent(self, event);

        final String eventType = event.getType();
        if (DOMConstants.INSTANCE.getAnimationEndEventType().equalsIgnoreCase(eventType)) {
            final Element targetElem = EventUtil.getTargetElem(event);
            if (self.getElement().equals(targetElem)) {
                final AnimationEndEvent animationEndEvent = event.cast();
                if (CSS.menuFadeInAnimationName().equals(animationEndEvent.getAnimationName())) {
                    onShown();
                } else if (CSS.menuFadeOutAnimationName().equals(animationEndEvent.getAnimationName())) {
                    onHidden();
                }
            }
        }
    }

    @Override
    void setItems(Menu self, List<? extends MenuItem> items) {
        assert this.self == self;
        this.items = items;
    }

    private void createPages() {
        destroyPages();

        // Create enough pages to hold the menu items.
        final int clientWidth = Window.getClientWidth(),
                availableWidth = clientWidth - 2 * MIN_MENU_PAGE_SIDE_MARGIN_WIDTH_PX -
                        CSS.menuBorderRightWidthPx() - CSS.menuBorderLeftWidthPx();
        int remainingWidth = availableWidth;
        int pageIndex = 0;
        Page currentPage = new Page(pageIndex);
        final List<MenuItemDatum> pageItemData = new ArrayList<MenuItemDatum>();
        final Iterator<? extends MenuItem> it = items.iterator();
        while (it.hasNext()) {
            final MenuItem item = it.next();
            final int contentWidth = measureContent(item.getTitle());
            boolean needAnotherPage;
            if (pageIndex == 0) {
                // First page
                if (it.hasNext()) {
                    needAnotherPage = (remainingWidth - contentWidth - CSS.arrowItemWidthPx()) < 0;
                } else {
                    needAnotherPage = (remainingWidth - contentWidth) < 0;
                }
            } else {
                needAnotherPage = (remainingWidth - contentWidth - 2 * CSS.arrowItemWidthPx()) < 0;
            }
            boolean handledItem = false;
            if (needAnotherPage) {
                if (pageItemData.isEmpty()) {
                    // Special case: The first item of a page is really, really, really long.
                    // It gets a page of its own.
                    pageItemData.add(new MenuItemDatum(item));
                    handledItem = true;
                }
                currentPage.setItemData(pageItemData.toArray(new MenuItemDatum[pageItemData.size()]), pageIndex > 0, pageIndex > 0 || !handledItem || it.hasNext(), !handledItem || it.hasNext());
                pageItemData.clear();
                if (pageIndex == 0) {
                    firstPageWidth = availableWidth - remainingWidth;
                }
                pages.add(currentPage);
                remainingWidth = availableWidth;
                ++pageIndex;
                currentPage = new Page(pageIndex);
            }
            if (!handledItem) {
                pageItemData.add(new MenuItemDatum(item));
                remainingWidth -= contentWidth;
            }
        }
        assert !it.hasNext();
        if (items.isEmpty() || !pageItemData.isEmpty()) {
            currentPage.setItemData(pageItemData.toArray(new MenuItemDatum[pageItemData.size()]), pageIndex > 0, pageIndex > 0, false);
            if (pageIndex == 0) {
                firstPageWidth = availableWidth - remainingWidth;
            }
            pages.add(currentPage);
        }
    }

    private void prevPage(final Page currentPage, final Function<Void> callback) {
        assert currentOp == CurrentOp.NONE;
        currentOp = CurrentOp.FLIPPING_PAGE;

        final int currentPageIndex = currentPage.getIndex();
        assert currentPageIndex > 0;

        final Page prevPage = pages.get(currentPageIndex - 1);
        completePageFlip(currentPage, prevPage, callback);
    }

    private void nextPage(final Page currentPage, final Function<Void> callback) {
        assert currentOp == CurrentOp.NONE;
        currentOp = CurrentOp.FLIPPING_PAGE;

        final int currentPageIndex = currentPage.getIndex();
        assert currentPageIndex < pages.size() - 1;

        final Page nextPage = pages.get(currentPageIndex + 1);
        completePageFlip(currentPage, nextPage, callback);
    }

    private void completePageFlip(final Page currentPage, final Page nextPage, final Function<Void> callback) {
        nextPage.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
        self.addChild(nextPage);
        new Timer() {
            @Override
            public void run() {
                positionMenuTip(nextPage, MIN_MENU_PAGE_SIDE_MARGIN_WIDTH_PX);

                new Timer() {
                    @Override
                    public void run() {
                        self.removeChild(currentPage);
                        nextPage.getElement().getStyle().clearVisibility();
                        visiblePage = nextPage;
                        currentOp = CurrentOp.NONE;
                        if (callback != null) callback.execute();
                    }
                }.schedule(CSS.pageFlipAnimationDurationMillis());
            }
        }.schedule(1);
    }

    private void positionMenu(int x, int minY) {
        assert visiblePage != null;
        assert x == lastX && minY == lastMinY;

        final Element menuElem = self.getElement();
        menuElem.getStyle().setTop(minY - (CSS.menuOffsetHeightPx() + CSS.menuTipWrapperOffsetHeight() + 1), Style.Unit.PX);

        final Element pageElem = visiblePage.getElement();
        final int pageElemLeft;
        final int pages_size = pages.size();
        if (pages_size == 1) {
            // Special case. With only one page, try to center the menu around `x'.
            pageElemLeft = Math.max(MIN_MENU_PAGE_SIDE_MARGIN_WIDTH_PX, x - firstPageWidth / 2);
            if (Window.getClientWidth() - 2 * MIN_MENU_PAGE_SIDE_MARGIN_WIDTH_PX - CSS.menuBorderLeftWidthPx() - CSS.menuBorderRightWidthPx() < firstPageWidth) {
                pageElem.getStyle().setRight(MIN_MENU_PAGE_SIDE_MARGIN_WIDTH_PX, Style.Unit.PX);
            }
        } else {
            pageElemLeft = MIN_MENU_PAGE_SIDE_MARGIN_WIDTH_PX;
            pageElem.getStyle().setRight(MIN_MENU_PAGE_SIDE_MARGIN_WIDTH_PX, Style.Unit.PX);

            for (int i = 1; i < pages_size; ++i) {
                final Page page = pages.get(i);
                page.getElement().getStyle().setLeft(MIN_MENU_PAGE_SIDE_MARGIN_WIDTH_PX, Style.Unit.PX);
                page.getElement().getStyle().setRight(MIN_MENU_PAGE_SIDE_MARGIN_WIDTH_PX, Style.Unit.PX);
            }
        }
        pageElem.getStyle().setLeft(pageElemLeft, Style.Unit.PX);

        positionMenuTip(visiblePage, pageElemLeft);
    }

    private void positionMenuTip(Page invisiblePage, final int pageElemLeft) {
        assert "hidden".equals(invisiblePage.getElement().getStyle().getVisibility()) ||
                "hidden".equals(self.getElement().getStyle().getVisibility())
                : "`invisiblePage' must be invisible.";

        // Compute `menuTipItemDatum' and position the menu tip.
        int absoluteLeft = pageElemLeft + CSS.menuBorderLeftWidthPx();
        if (invisiblePage.getIndex() > 0) absoluteLeft += CSS.arrowItemWidthPx(); // Width of the previous page item.
        final MenuItemDatum[] pageItemData = invisiblePage.getItemData();
        int i = 0;
        for (; i < pageItemData.length && absoluteLeft < lastX; ++i) {
            final MenuItemDatum itemDatum = pageItemData[i];
            // We have to recompute the offsetWidths of the item elements because when menu
            // items are paged, their elements are flexed to fill up available space, usually
            // resulting in different offsetWidths than the values computed using the content
            // measurer element.
            itemDatum.contentWidth = itemDatum.itemElement.getOffsetWidth();
            absoluteLeft += itemDatum.contentWidth;
        }
        int menuTipElemLeft;
        if (i == pageItemData.length && i == 0) {
            // Pathological case: No menu items.
            menuTipItemDatum = null;
            menuTipElemLeft = pageElemLeft + CSS.menuBorderLeftWidthPx() - CSS.menuTipBeforePseudoelementLeftPx();
        } else if (i == 0) {
            menuTipItemDatum = pageItemData[0];
            menuTipElemLeft = Math.max(absoluteLeft - CSS.menuTipBeforePseudoelementLeftPx(), lastX);
        } else {
            menuTipItemDatum = pageItemData[i - 1];
            final int max = Math.max(absoluteLeft - menuTipItemDatum.contentWidth - CSS.menuTipBeforePseudoelementLeftPx(), lastX);
            menuTipElemLeft = Math.min(max, absoluteLeft + CSS.menuTipBeforePseudoelementLeftPx());
        }

        if (i != pageItemData.length || i != 0) { // Excluding the pathological case.
            // Make sure that the menu tip does not start under an arrow item or too close to
            // an edge of the menu page.
            // If the menu tip is too close to an edge of the menu page, then there is a noticeable
            // gap between the hypotenuse of the menu tip triangle and the rounded-off corner.
            if (i == pageItemData.length) {
                menuTipElemLeft = Math.min(menuTipElemLeft, absoluteLeft + menuTipItemDatum.itemElement.getOffsetWidth() - (pages.size() == 1 ? CSS.menuBorderRadiusPx() : CSS.arrowItemWidthPx()) + CSS.menuTipBeforePseudoelementLeftPx());
            }
            if (i == 0 || (i == 1 && menuTipItemDatum == pageItemData[0])) {
                menuTipElemLeft = Math.max(pageElemLeft + CSS.menuBorderLeftWidthPx() + (invisiblePage.getIndex() == 0 ? CSS.menuBorderRadiusPx() : CSS.arrowItemWidthPx()) - CSS.menuTipBeforePseudoelementLeftPx(), menuTipElemLeft);
            }
        }

        menuTipElem.getStyle().setLeft(menuTipElemLeft, Style.Unit.PX);
    }

    @Override
    void showAt(Menu self, Canvas target, int x, int y, int minX, int maxX, int minY, int maxY) {
        assert this.self == self;
        this.target = target;
        lastX = (minX + maxX) / 2;
        lastMinY = minY;
        final Element element = self.getElement();
        element.removeClassName(CSS.fadeOutClass());
        if (visiblePage == null) {
            assert currentOp == CurrentOp.NONE;
            createPages();
            visiblePage = pages.get(0);

            currentOp = CurrentOp.SHOWING;
            self.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
            self.addChild(visiblePage);
            RootLayoutPanel.get().add(screenCover);
            new Timer() {
                @Override
                public void run() {
                    positionMenu(lastX, lastMinY);
                    MenuImplIPhone.this.self.getElement().getStyle().clearVisibility();
                    element.addClassName(CSS.fadeInClass());
                }
            }.schedule(1);
        } else positionMenu(x, minY);
    }

    private void onShown() {
        self.getElement().removeClassName(CSS.fadeInClass());
        currentOp = CurrentOp.NONE;
    }

    private void onHidden() {
        self.getElement().removeClassName(CSS.fadeOutClass());
        currentOp = CurrentOp.NONE;

        if (activeItemDatum != null) {
            if (activeItemDatum.itemElement != null) activeItemDatum.itemElement.removeClassName(CSS.selectedClass());
            if (activeItemDatum == menuTipItemDatum) menuTipElem.removeClassName(CSS.selectedClass());
        }
        self.removeChild(visiblePage);
        visiblePage = null;
        RootLayoutPanel.get().remove(screenCover);
        if (destroyAfterHiding) destroyImpl(this.self);
        else destroyPages();
    }

    @Override
    void hide(Menu self) {
        assert this.self == self;
        if (currentOp == CurrentOp.NONE) {
            self._aboutToHide();

            assert visiblePage != null;
            final Element element = self.getElement();
            element.removeClassName(CSS.fadeInClass());
            currentOp = CurrentOp.HIDING;
            self._fireBeforeMenuHiddenEvent();
            element.addClassName(CSS.fadeOutClass());
        }
    }
}
