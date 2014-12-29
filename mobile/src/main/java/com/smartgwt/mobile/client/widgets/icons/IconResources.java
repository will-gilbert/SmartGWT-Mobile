/*
 * SmartGWT Mobile
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * SmartGWT Mobile is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT Mobile is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package com.smartgwt.mobile.client.widgets.icons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * <code>ClientBundle</code> for various icon resources commonly used in mobile apps.
 */
public interface IconResources extends ClientBundle {

    public static IconResources INSTANCE = GWT.create(IconResources.class);

    @Source("action.png")
    ImageResource action();

    @Source("activity_indicator.png")
    ImageResource activity_indicator();

    @Source("add.png")
    ImageResource add();

    @Source("arrow_down.png")
    ImageResource arrow_down();

    @Source("arrow_left.png")
    ImageResource arrow_left();

    @Source("arrow_right.png")
    ImageResource arrow_right();

    @Source("arrow_up.png")
    ImageResource arrow_up();

    @Source("attachment.png")
    ImageResource attachment();

    @Source("bag.png")
    ImageResource bag();

    @Source("bar_graph.png")
    ImageResource bar_graph();

    @Source("bookmarks.png")
    ImageResource bookmarks();

    @Source("blank.gif")
    ImageResource blank();

    @Source("bullseye.png")
    ImageResource bullseye();

    @Source("cabinet.png")
    ImageResource cabinet();

    @Source("calendar.png")
    ImageResource calendar();

    @Source("chart.png")
    ImageResource chart();

    @Source("chat.png")
    ImageResource chat();

    @Source("chat_more.png")
    ImageResource chat_more();

    @Source("cog.png")
    ImageResource cog();

    @Source("compose.png")
    ImageResource compose();

    @Source("computer.png")
    ImageResource computer();

    @Source("contacts.png")
    ImageResource contacts();

    @Source("download.png")
    ImageResource download();

    @Source("downloads.png")
    ImageResource downloads();

    @Source("eye.png")
    ImageResource eye();

    @Source("files.png")
    ImageResource files();

    @Source("film.png")
    ImageResource film();

    @Source("flag.png")
    ImageResource flag();

    @Source("gift.png")
    ImageResource gift();

    @Source("heart.png")
    ImageResource heart();

    @Source("history.png")
    ImageResource history();

    @Source("home.png")
    ImageResource home();

    @Source("id_card.png")
    ImageResource id_card();

    @Source("info.png")
    ImageResource info();

    @Source("light_bulb.png")
    ImageResource light_bulb();

    @Source("lightning_bolt.png")
    ImageResource lightning_bolt();

    @Source("location_pin.png")
    ImageResource location_pin();

    @Source("more.png")
    ImageResource more();

    @Source("refresh.png")
    ImageResource refresh();

    @Source("reload.png")
    ImageResource reload();

    @Source("search.png")
    ImageResource search();

    @Source("settings.png")
    ImageResource settings();

    @Source("stop.png")
    ImageResource stop();

    @Source("top_rated.png")
    ImageResource top_rated();

    @Source("disclosure_arrow.png")
    @ImageResource.ImageOptions(flipRtl = true)
    ImageResource disclosure_arrow();

    @Source("disclosure_button.png")
    @ImageResource.ImageOptions(flipRtl = true)
    ImageResource disclosure_button();

    @Source("checkmark.png")
    ImageResource checkmark();

    @Source("clear.png")
    ImageResource clear();

}
