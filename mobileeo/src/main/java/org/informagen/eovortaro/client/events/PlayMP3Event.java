package org.informagen.eovortaro.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class PlayMP3Event extends GwtEvent<HandlerFor.PlayMP3Event> {

    public static Type<HandlerFor.PlayMP3Event> TYPE = new Type<HandlerFor.PlayMP3Event>();

    // The event can carry properties and methods
    public String title;
    public String heading;
    public String imgURL;
    public String htmlContent;
    public String mp3URL;


    // Stuff revelent product number into this event
    public PlayMP3Event(String mp3URL) {
       this(null, null, null, null,mp3URL);
    }

    public PlayMP3Event(String heading, String title, String imgURL, String htmlContent, String mp3URL) {
        this.heading = heading;
        this.title = title;
        this.imgURL = imgURL;
        this.htmlContent = htmlContent;
        this.mp3URL = mp3URL;
    }


    // GwtEvent abstract methods --------------------------------------------------------------
    @Override
    public Type<HandlerFor.PlayMP3Event> getAssociatedType() { return TYPE; }

    @Override
    protected void dispatch(HandlerFor.PlayMP3Event handler) { handler.process(this); }
}
