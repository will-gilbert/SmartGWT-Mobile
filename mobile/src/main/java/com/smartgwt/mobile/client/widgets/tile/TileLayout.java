package com.smartgwt.mobile.client.widgets.tile;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.events.HasClickHandlers;
import com.smartgwt.mobile.client.widgets.events.HasPanelHideHandlers;
import com.smartgwt.mobile.client.widgets.events.HasPanelShowHandlers;
import com.smartgwt.mobile.client.widgets.events.PanelHideEvent;
import com.smartgwt.mobile.client.widgets.events.PanelHideHandler;
import com.smartgwt.mobile.client.widgets.events.PanelShowEvent;
import com.smartgwt.mobile.client.widgets.events.PanelShowHandler;
import com.smartgwt.mobile.client.widgets.icons.IconResources;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.layout.Layout;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;

public class TileLayout extends Panel implements HasPanelShowHandlers, HasPanelHideHandlers  {
     public class Tile extends Canvas implements HasClickHandlers {
        private String title;
        private Image icon;
        private ImageElement imageElement;
        private Element span;
 
        /**
         * Constructor that sets the tile title and icon.
         *
         * @param title the tile title
         * @param icon the tile icon
         */
        public Tile(String title, String icon) {
            super();
            this.title = title;
            this.icon = icon == null ? null : new Image(icon);
            this.createTile();
        }

        /**
         * Constructor that sets the tile title and icon.
         *
         * @param title the tile title
         * @param iconResource the tile icon as an ImageResource
         */
        public Tile(String title, ImageResource iconResource) {
            this(title, iconResource == null ? null : new Image(iconResource));
        }

        /**
         * Constructor that sets the tile title and icon.
         *
         * @param title the tile title
         * @param icon the tile icon
         */
        public Tile(String title, Image icon) {
            super();
            this.title = title;
            this.icon = icon;
            this.createTile();
        }
        
        @SuppressWarnings("deprecation")
        private void createTile() {
            setElement(Document.get().createDivElement());
            this._setClassName("sc-tabbar-tab", false);
            if (icon != null) {
                imageElement = Document.get().createImageElement();
                imageElement.setSrc(IconResources.INSTANCE.blank().getURL());
                imageElement.setHeight(30);
                imageElement.setWidth(30); 
                imageElement.setAttribute("border","0");
                imageElement.getStyle().setProperty("webkitMaskBoxImage", "url(" + icon.getUrl() + ")");
                this.addChild(new Canvas(imageElement)); 
            } else this._setClassName("sc-tabbar-tab-noicon", false);
            if (title != null) {
                span = DOM.createSpan();
                span.setInnerText(title);
                span.setClassName("sc-tab-span");
                this.addChild(new Canvas(span));
            }
            DOM.setEventListener(getElement(), this);
            sinkEvents(Event.ONCLICK | Event.GESTUREEVENTS | Event.TOUCHEVENTS | Event.MOUSEEVENTS);
        }
        
        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public void onBrowserEvent(Event event) {
            Element target = event.getEventTarget().cast();
            if (target != null && getElement().isOrHasChild(target)) {
                int type = event.getTypeInt();
                switch (type) {
                    case Event.ONMOUSEDOWN:
                    case Event.ONTOUCHSTART:
                        onStart(event);
                        break;
                }        
            }
        }
        public void onStart(Event event) {
                event.preventDefault();
                totalX = totalY = 0;
                lastY = (event.getTouches() != null && event.getTouches().length() > 0) ? event.getTouches().get(0).getClientY(): event.getClientY();
                lastX = (event.getTouches() != null && event.getTouches().length() > 0) ? event.getTouches().get(0).getClientX(): event.getClientX();
                //createDraggableTile(title,icon);
                draggable = new Tile(title,icon);
                draggable.getElement().getStyle().setPosition(Position.ABSOLUTE);
                draggable.getElement().getStyle().setZIndex(10000);
                draggable.getElement().getStyle().setLeft(lastX+getElement().getAbsoluteLeft(), Unit.PX);
                draggable.getElement().getStyle().setTop(lastY+getElement().getAbsoluteTop(), Unit.PX);
                draggable.getElement().getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), "scale(1.3,1.3)");
                TileLayout.this.container.addMember(draggable);
                //((TabSet)TileLayout.this.container).setDraggable(draggable);

        }     
        
        /**
         * Set the enabled state of the button.
         *
         * @param onoff true to enable, false to disable. Default is true
         */
        @Override
        public void setDisabled(boolean onoff) {
            if (onoff) {
                disable();
            } else {
                enable();
            }
        }

    }
       
    int tilesPerLine = 4;
    String tileSize = "25%";
    List<HLayout> rows = new ArrayList<HLayout>();
    Tile draggable;
    Layout container;
    boolean active = false;
    int lastY = 0, totalY = 0, lastX = 0, totalX = 0;

    
    public TileLayout(String title, Layout container) {
        super(title);
        _setClassName("sc-layout-tile", false);
        setShowPanelHeader(true);
        this.container = container;
    }
       
    public void replaceTile(Tile oldTile, String title, Image icon) {
        for(HLayout row: rows) {
            Canvas tiles[] = row.getMembers();
            for(int i = 0; i < tiles.length; ++i) {
                if(tiles[i].getTitle().equals(oldTile.getTitle())) {
                    row.removeMember(tiles[i]);
                    row.addMember(new Tile(title, icon), i);
                    return;
                }
            }
        }
    }
    
    /*
    @SuppressWarnings("deprecation")
    private void createDraggableTile(String title, Image icon) {
        draggable = new Canvas(Document.get().createDivElement());
        draggable.setTitle(title);
        draggable.setClassName("sc-tabbar-tab");
        draggable.getElement().getStyle().setPosition(Position.ABSOLUTE);
        draggable.getElement().getStyle().setZIndex(10000);

        if (icon != null) {
            ImageElement imageElement = Document.get().createImageElement();
            imageElement.setSrc(IconResources.INSTANCE.blank().getURL());
            imageElement.setHeight(30);
            imageElement.setWidth(30); 
            imageElement.setAttribute("border","0");
            CssUtil.setStyleWK(imageElement.getStyle(), "-webkit-mask-box-image", "url(" + icon.getUrl() + ")");   
            draggable.addChild(new Canvas(imageElement)); 
        } else this.setClassName("sc-tabbar-tab-noicon");
        if (title != null) {
            Element span = DOM.createSpan();
            span.setInnerText(title);
            span.setClassName("sc-tab-span");
            draggable.addChild(new Canvas(span));
        }
    }
    */
    
    public void addTile(String title, ImageResource icon) {
        if(rows.isEmpty()) {
            HLayout hlayout = new HLayout();
            final Tile tile = new Tile(title, icon);
            tile.setWidth(tileSize);
            hlayout.addMember(tile);
            rows.add(hlayout);  
            addMember(hlayout);
        } else {
            HLayout hlayout = rows.get(rows.size()-1);
            if(hlayout.getMembers().length < tilesPerLine) {
                Tile tile = new Tile(title, icon);
                tile.setWidth(tileSize);
                hlayout.addMember(tile);
            } else {
                hlayout = new HLayout();
                Tile tile = new Tile(title, icon);
                tile.setWidth(tileSize);
                hlayout.addMember(tile);
                rows.add(hlayout);   
                addMember(hlayout);
            }           
        }
    }
    
    @Override
    public void onBrowserEvent(Event event) {
        Element target = event.getEventTarget().cast();
        if (target != null && getElement().isOrHasChild(target)) {
            int type = event.getTypeInt();
            switch (type) {
                case Event.ONMOUSEDOWN:
                case Event.ONTOUCHSTART:
                    onStart(event);
                    break;
                case Event.ONMOUSEMOVE:
                case Event.ONTOUCHMOVE:
                    onMove(event);
                    break;
                case Event.ONMOUSEUP:
                case Event.ONTOUCHEND:
                case Event.ONTOUCHCANCEL:
                    onEnd(event);
                    break;
            }        
        }
    }
    public void onStart(Event event) {
        if(!active) {
            event.preventDefault();
            event.stopPropagation();
            active = true;
            super.onBrowserEvent(event);
        }
    }
    public void onMove(Event event) {
        if(active) {
            event.preventDefault();
            event.stopPropagation();
            int Y = (event.getTouches() != null && event.getTouches().length() > 0) ? event.getTouches().get(0).getClientY(): event.getClientY();
            int deltaY = Y - lastY;
            lastY = Y;
            totalY += deltaY;
            int X = (event.getTouches() != null && event.getTouches().length() > 0) ? event.getTouches().get(0).getClientX(): event.getClientX();
            int deltaX = X - lastX;
            lastX = X;
            totalX += deltaX;
            if(draggable != null) {
                draggable.getElement().getStyle().setProperty("webkitTransform","translate("+totalX+"px,"+totalY+"px) scale(1.3,1.3)");
            }
        }
    }
    public void onEnd(Event event) {
        if(active) {
            event.preventDefault();
            event.stopPropagation();
            active = false;
            if(draggable != null) {
                draggable.getElement().getStyle().setProperty("webkitTransform","translate(0,0)");
                new Timer() {
                    public void run() {
                        TileLayout.this.container.removeMember(draggable);
                        draggable = null;
                        //((TabSet)TileLayout.this.container).setDraggable(draggable);
                    }
                }.schedule(50);
            }
            super.onBrowserEvent(event);
        }
    }

    @Override
    public HandlerRegistration addPanelHideHandler(PanelHideHandler handler) {
        return this.addHandler(handler, PanelHideEvent.getType());
    }

    @Override
    public HandlerRegistration addPanelShowHandler(PanelShowHandler handler) {
        return this.addHandler(handler, PanelShowEvent.getType());
    }

}
