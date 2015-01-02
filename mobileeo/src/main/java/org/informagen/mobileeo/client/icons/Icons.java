package org.informagen.mobileeo.client.icons;

import com.google.gwt.core.client.GWT;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;

public interface Icons extends ClientBundle {

    public static final Icons INSTANCE = GWT.create(Icons.class);


    // NB: These images MUST be in the same directory as this file; The same
    //     directory in the Classpath was not sufficient. I can't explain why this
    //     had to be but I wasted the better part of an afternoon trying to put
    //      these four file in the 'src/main/resources' directory tree.

    @Source("lernu.png")
    public ImageResource lernu();

    @Source("calendar.png")
    public ImageResource calendar();

    @Source("espdic.png")
    public ImageResource espdic();

    @Source("dictionary.png")
    public ImageResource dictionary();

    @Source("eo-glossary.png")
    public ImageResource eoGlossary();
    
   @Source("esperanta-retradio.png")
    public ImageResource esperantaRetradio();
    
   @Source("radio-verda.png")
    public ImageResource radioVerda();
    
   @Source("settings.png")
    public ImageResource settings();

    //====================================================================

/*

    @Source("com/smartgwt/mobile/client/widgets/icons/action.png")
    @ImageOptions(preventInlining = true)
    public ImageResource action();

    @Source("com/smartgwt/mobile/client/widgets/icons/add.png")
    @ImageOptions(preventInlining = true)
    public ImageResource add();

    @Source("com/smartgwt/mobile/client/widgets/icons/arrow_down.png")
    @ImageOptions(preventInlining = true)
    public ImageResource arrow_down();

    @Source("com/smartgwt/mobile/client/widgets/icons/arrow_left.png")
    @ImageOptions(preventInlining = true)
    public ImageResource arrow_left();

    @Source("com/smartgwt/mobile/client/widgets/icons/arrow_right.png")
    @ImageOptions(preventInlining = true)
    public ImageResource arrow_right();

    @Source("com/smartgwt/mobile/client/widgets/icons/arrow_up.png")
    @ImageOptions(preventInlining = true)
    public ImageResource arrow_up();

    @Source("com/smartgwt/mobile/client/widgets/icons/attachment.png")
    @ImageOptions(preventInlining = true)
    public ImageResource attachment();

    @Source("com/smartgwt/mobile/client/widgets/icons/bag.png")
    @ImageOptions(preventInlining = true)
    public ImageResource bag();

    @Source("com/smartgwt/mobile/client/widgets/icons/bookmarks.png")
    @ImageOptions(preventInlining = true)
    public ImageResource bookmarks();

    @Source("com/smartgwt/mobile/client/widgets/icons/bullseye.png")
    @ImageOptions(preventInlining = true)
    public ImageResource bullseye();

    @Source("com/smartgwt/mobile/client/widgets/icons/chart.png")
    @ImageOptions(preventInlining = true)
    public ImageResource chart();

    @Source("com/smartgwt/mobile/client/widgets/icons/chat.png")
    @ImageOptions(preventInlining = true)
    public ImageResource chat();

    @Source("com/smartgwt/mobile/client/widgets/icons/cog.png")
    @ImageOptions(preventInlining = true)
    public ImageResource cog();

    @Source("com/smartgwt/mobile/client/widgets/icons/compose.png")
    @ImageOptions(preventInlining = true)
    public ImageResource compose();

    @Source("com/smartgwt/mobile/client/widgets/icons/computer.png")
    @ImageOptions(preventInlining = true)
    public ImageResource computer();

    @Source("com/smartgwt/mobile/client/widgets/icons/contacts.png")
    @ImageOptions(preventInlining = true)
    public ImageResource contacts();

    @Source("com/smartgwt/mobile/client/widgets/icons/download.png")
    @ImageOptions(preventInlining = true)
    public ImageResource download();

    @Source("com/smartgwt/mobile/client/widgets/icons/downloads.png")
    @ImageOptions(preventInlining = true)
    public ImageResource downloads();

    @Source("com/smartgwt/mobile/client/widgets/icons/flag.png")
    @ImageOptions(preventInlining = true)
    public ImageResource flag();

    @Source("com/smartgwt/mobile/client/widgets/icons/location_pin.png")
    @ImageOptions(preventInlining = true)
    public ImageResource location_pin();

    @Source("com/smartgwt/mobile/client/widgets/icons/more.png")
    @ImageOptions(preventInlining = true)
    public ImageResource more();

    @Source("com/smartgwt/mobile/client/widgets/icons/refresh.png")
    @ImageOptions(preventInlining = true)
    public ImageResource refresh();

    @Source("com/smartgwt/mobile/client/widgets/icons/reload.png")
    @ImageOptions(preventInlining = true)
    public ImageResource reload();

    @Source("com/smartgwt/mobile/client/widgets/icons/settings.png")
    @ImageOptions(preventInlining = true)
    public ImageResource settings();

    @Source("com/smartgwt/mobile/client/widgets/icons/stop.png")
    @ImageOptions(preventInlining = true)
    public ImageResource stop();

    @Source("com/smartgwt/mobile/client/widgets/icons/top_rated.png")
    @ImageOptions(preventInlining = true)
    public ImageResource top_rated();

    */
}
