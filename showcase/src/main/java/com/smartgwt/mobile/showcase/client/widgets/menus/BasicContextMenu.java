package com.smartgwt.mobile.showcase.client.widgets.menus;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.client.types.ImageStyle;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.Img;
import com.smartgwt.mobile.client.widgets.Label;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.menu.Menu;
import com.smartgwt.mobile.client.widgets.menu.MenuItem;
import com.smartgwt.mobile.client.widgets.menu.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.menu.events.ItemClickEvent;
import com.smartgwt.mobile.client.widgets.menu.events.ItemClickHandler;
import com.smartgwt.mobile.client.widgets.menu.events.MenuItemClickEvent;

public class BasicContextMenu extends Panel {

    private static final int TRANSLATE_DELTA_PX = 40,
            SCALE_DELTA_PX = 20,
            START_IMG_SIZE_PX = 30,
            NUM_SIZES = 3;

    static {
        StyleInjector.injectAtEnd(".app-basicContextMenuSample-img {" +
                "    -webkit-transition: top 200ms ease-in-out, left 200ms ease-in-out;" +
                "    -moz-transition: top 200ms ease-in-out, left 200ms ease-in-out;" +
                "    transition: top 200ms ease-in-out, left 200ms ease-in-out;" +
                "}" +
                ".app-basicContextMenuSample-img td {" +
                "    -webkit-transition: width 200ms ease-in-out, height 200ms ease-in-out;" +
                "    -moz-transition: width 200ms ease-in-out, height 200ms ease-in-out;" +
                "    transition: width 200ms ease-in-out, height 200ms ease-in-out;" +
                "}", true);
    }

    private Img img;
    private int imgLeftPx = 40, imgTopPx = 80, imgSizePx = START_IMG_SIZE_PX;

    private Menu menu;
    private MenuItem leftItem, rightItem, upItem, downItem, enlargeItem, shrinkItem, nevermindItem;
    private HandlerRegistration menuItemClickRegistration, nevermindItemClickRegistration;

    public BasicContextMenu() {
        super("Context Menu");

        getElement().getStyle().setBackgroundColor("#f7f7f7");
        getElement().getStyle().setWidth(100, Style.Unit.PCT);
        getElement().getStyle().setHeight(100, Style.Unit.PCT);

        addMember(new Label("Tap &amp; hold the image to reveal the context menu."));

        // We will use a contextual menu to manipulate this image
        img = new Img("./sampleImages/yinyang.gif");
        img.getElement().addClassName("app-basicContextMenuSample-img");
        img.setWidth(imgSizePx);
        img.setHeight(imgSizePx);
        img.setImageType(ImageStyle.STRETCH);
        img.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        img.getElement().getStyle().setTop(imgTopPx, Style.Unit.PX);
        img.getElement().getStyle().setLeft(imgLeftPx, Style.Unit.PX);
        addMember(img);

        // Build a contextual menu
        menu = new Menu();
        leftItem = new MenuItem("Left");
        rightItem = new MenuItem("Right");
        upItem = new MenuItem("Up");
        downItem = new MenuItem("Down");
        enlargeItem = new MenuItem("Enlarge");
        shrinkItem = new MenuItem("Shrink");
        shrinkItem.setEnabled(false);

        // A "Nevermind" item to cancel displaying the context menu is not necessary because
        // if the user taps anywhere off of the menu, the menu will automatically be dismissed.
        // This sample only uses a "Nevermind" item to demonstrate menu ClickHandlers.

        nevermindItem = new MenuItem("Nevermind");
        nevermindItemClickRegistration = nevermindItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                SC.logWarn("'Nevermind' tapped.");
            }
        });

        menu.setItems(leftItem, rightItem, upItem, downItem, enlargeItem, shrinkItem, nevermindItem);

        menuItemClickRegistration = menu.addItemClickHandler(new ItemClickHandler() {
            @Override
            public void onItemClick(ItemClickEvent event) {

                final MenuItem item = event.getItem();

                if (item == leftItem) {
                    imgLeftPx -= TRANSLATE_DELTA_PX;
                    img.getElement().getStyle().setLeft(imgLeftPx, Style.Unit.PX);
                    enableDisableItems();
                } else if (item == rightItem) {
                    imgLeftPx += TRANSLATE_DELTA_PX;
                    img.getElement().getStyle().setLeft(imgLeftPx, Style.Unit.PX);
                    enableDisableItems();
                } else if (item == upItem) {
                    imgTopPx -= TRANSLATE_DELTA_PX;
                    img.getElement().getStyle().setTop(imgTopPx, Style.Unit.PX);
                    enableDisableItems();
                } else if (item == downItem) {
                    imgTopPx += TRANSLATE_DELTA_PX;
                    img.getElement().getStyle().setTop(imgTopPx, Style.Unit.PX);
                    enableDisableItems();
                } else if (item == enlargeItem) {
                    imgSizePx += SCALE_DELTA_PX;
                    img.setWidth(imgSizePx);
                    img.setHeight(imgSizePx);
                    enableDisableItems();
                } else if (item == shrinkItem) {
                    imgSizePx -= SCALE_DELTA_PX;
                    img.setWidth(imgSizePx);
                    img.setHeight(imgSizePx);
                    enableDisableItems();
                }
            }
        });

        img.setContextMenu(menu);
    }

    @Override
    public void destroy() {

        if (menuItemClickRegistration != null) {
            menuItemClickRegistration.removeHandler();
            menuItemClickRegistration = null;
        }

        if (nevermindItemClickRegistration != null) {
            nevermindItemClickRegistration.removeHandler();
            nevermindItemClickRegistration = null;
        }

        img.setContextMenu(null);
        menu.destroy();
        super.destroy();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        enableDisableItems();
    }

    // Enables or disables `leftItem', `rightItem', `upItem', `downItem', `enlargeItem', and
    //   `shrinkItem' based on whether the action would place any part of the image off-screen.
    //   The item is enabled if the action would not place any part of the image off-screen;
    //   disabled otherwise.

    private void enableDisableItems() {
        final int offsetWidth = getElement().getOffsetWidth(),
                offsetHeight = getElement().getOffsetHeight();

        leftItem.setEnabled(imgLeftPx >= TRANSLATE_DELTA_PX);
        rightItem.setEnabled(imgLeftPx + TRANSLATE_DELTA_PX + imgSizePx <= offsetWidth);
        upItem.setEnabled(imgTopPx >= TRANSLATE_DELTA_PX);
        downItem.setEnabled(imgTopPx + TRANSLATE_DELTA_PX + imgSizePx <= offsetHeight);

        enlargeItem.setEnabled((imgLeftPx + imgSizePx + SCALE_DELTA_PX <= offsetWidth) &&
                (imgTopPx + imgSizePx + SCALE_DELTA_PX <= offsetHeight) &&
                (imgSizePx < START_IMG_SIZE_PX + NUM_SIZES * SCALE_DELTA_PX));

        shrinkItem.setEnabled(imgSizePx > START_IMG_SIZE_PX);
    }
}
