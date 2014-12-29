package com.smartgwt.mobile.client.widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.types.ImageStyle;
import com.smartgwt.mobile.client.util.SC;

public class Img extends StatefulCanvas {

    private static final RegExp DATA_URI_START_RE = RegExp.compile("^data:", "i");

    @SGWTInternal
    public static String _urlForState(String baseURL, boolean selected, boolean focused, String state, String pieceName, String customState) {
        if (baseURL == null || baseURL.isEmpty() || DATA_URI_START_RE.test(baseURL)) return baseURL;
        // short circuit to just return baseURL for the simple case
        if ((state == null || state.isEmpty()) &&
            (pieceName == null || pieceName.isEmpty()) &&
            !selected &&
            !focused &&
            (customState == null || customState.isEmpty()))
        {
            return baseURL;
        }

        // break baseURL up into name and extension
        final int period = baseURL.lastIndexOf('.');
        if (period < 0) return baseURL;
        final String name = baseURL.substring(0, period),
                extension = baseURL.substring(period);

        final StringBuilder buffer = new StringBuilder();

        buffer.append(name);

        if (selected) {
            buffer.append("_Selected");
        }
        if (focused) {
            buffer.append("_Focused");
        }
        // add state
        if (state != null && !state.isEmpty()) {
            buffer.append('_').append(state);
        }
        if (customState != null && !customState.isEmpty()) {
            buffer.append('_').append(customState);
        }
        // add pieceName
        if (pieceName != null && !pieceName.isEmpty()) {
            buffer.append('_').append(pieceName);
        }
        buffer.append(extension);
        return buffer.toString();
    }

    private transient TableSectionElement tbody;
    private transient TableRowElement tr;
    private transient TableCellElement td;
    private transient Image image;

    private ImageStyle imageType;
    private Integer imageWidth;
    private Integer imageHeight;
    private Integer width, height;
    private String src;

    private Img(Image image) {
        createElements();
        this.image = image;
        image.getElement().<ImageElement>cast().setAttribute("align", "top");
        add(image, td.<com.google.gwt.user.client.Element>cast());
        setWidth(100);
        setHeight(100);
        setImageType(ImageStyle.STRETCH);
    }

    public Img() {
        this(new Image());
    }

    public Img(String src) {
        this(new Image(src));
        this.src = src;
    }

    public Img(String src, int width, int height) {
        this(src);
        internalSetWidth(width);
        internalSetHeight(height);
    }

    public Img(ImageResource resource) {
        this(new Image(resource));
    }

    private void createElements() {
        final Document document = Document.get();
        final TableElement table = document.createTableElement();
        table.addClassName("sc-img");
        table.setBorder(0);
        table.setCellPadding(0);
        table.setCellSpacing(0);
        tbody = document.createTBodyElement();
        table.appendChild(tbody);
        tr = document.createTRElement();
        tbody.appendChild(tr);
        td = document.createTDElement();
        tr.appendChild(td);
        setElement(table);
    }

    public final Integer getWidth() {
        return width;
    }

    private void internalSetWidth(int width) {
        this.width = width;
        getElement().getStyle().setWidth(width, Style.Unit.PX);
        td.getStyle().setWidth(width, Style.Unit.PX);
    }

    public void setWidth(int width) {
        internalSetWidth(width);
    }

    public final Integer getHeight() {
        return height;
    }

    private void internalSetHeight(int height) {
        this.height = height;
        td.getStyle().setHeight(height, Style.Unit.PX);
    }

    public void setHeight(int height) {
        internalSetHeight(height);
    }

    public final String getAltText() {
        return image.getElement().<ImageElement>cast().getAlt();
    }

    public void setAltText(String altText) {
        image.getElement().<ImageElement>cast().setAlt(altText);
    }

    public final Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
        if (imageType != null && imageType != ImageStyle.NORMAL && imageType != ImageStyle.CENTER) {
            SC.warn("setImageWidth() called on an Img widget with imageType:" + imageType.getValue());
            return;
        }
        if (imageWidth == null) {
            image.setWidth("auto");
        } else {
            image.setWidth(imageWidth.intValue() + "px");
        }
    }

    public final Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
        if (imageType != null && imageType != ImageStyle.NORMAL && imageType != ImageStyle.CENTER) {
            SC.warn("setImageHeight() called on an Img widget with imageType:" + imageType.getValue());
            return;
        }
        if (imageHeight == null) {
            image.setHeight("auto");
        } else {
            image.setHeight(imageHeight.intValue() + "px");
        }
    }

    public final ImageStyle getImageType() {
        return imageType;
    }

    public void setImageType(ImageStyle imageType) {
        this.imageType = imageType;
        if (imageType == null) {
            imageType = ImageStyle.NORMAL;
        }
        switch (imageType) {
            case NORMAL:
                td.setAlign("left");
                td.setVAlign("top");
                if (imageWidth == null) {
                    image.setWidth("auto");
                } else {
                    image.setWidth(imageWidth.intValue() + "px");
                }
                if (imageHeight == null) {
                    image.setHeight("auto");
                } else {
                    image.setHeight(imageHeight.intValue() + "px");
                }
                break;
            case CENTER:
                td.setAlign("center");
                td.setVAlign("middle");
                if (imageWidth == null) {
                    image.setWidth("auto");
                } else {
                    image.setWidth(imageWidth.intValue() + "px");
                }
                if (imageHeight == null) {
                    image.setHeight("auto");
                } else {
                    image.setHeight(imageHeight.intValue() + "px");
                }
                break;
            case STRETCH:
                td.setAlign("left");
                td.setVAlign("top");
                image.setWidth("100%");
                image.setHeight("100%");
                break;
        }
    }

    public final String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
        stateChanged();
    }

    public final String _getURL() {
        return _getURL(null, getState().getValue(), Canvas._booleanValue(isSelected(), false), false);
    }
    @SGWTInternal
    public final String _getURL(String pieceName, String state, boolean selected, boolean focused) {
        return _urlForState(this.src, selected, focused, state, pieceName, null);
    }

    @Override
    protected void stateChanged() {
        super.stateChanged();
        image.setUrl(_getURL());
    }
}
