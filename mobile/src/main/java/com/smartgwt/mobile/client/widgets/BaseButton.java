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

package com.smartgwt.mobile.client.widgets;

import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.types.AttributeType;
import com.smartgwt.mobile.client.internal.util.ElementUtil;
import com.smartgwt.mobile.client.theme.ButtonsCssResource;
import com.smartgwt.mobile.client.theme.ThemeResources;
import com.smartgwt.mobile.client.types.IconAlign;
import com.smartgwt.mobile.client.types.State;
import com.smartgwt.mobile.client.widgets.icons.IconResources;

/**
 * Base abstract class for button types.
 */
@SGWTInternal
public abstract class BaseButton extends StatefulCanvas {

    @SGWTInternal
    public static final ButtonsCssResource _CSS = ThemeResources.INSTANCE.buttonsCSS();

    public static enum ButtonType {

        BORDERED("bordered"),
        BORDERED_IMPORTANT("bordered important"),
        BORDERED_WARNING("bordered warning"),
        PLAIN("plain-icon"),

        /**
         * A rounded rectangle style button.
         */
        ROUNDED_RECTANGLE("rounded-rectangle"),

        /**
         * A rounded rectangle style button. Used to denote buttons that are more "important"
         */
        ROUNDED_RECTANGLE_IMPORTANT("rounded-rectangle important"),

        /**
         * A default "action" button.
         */
        ACTION_DEFAULT("action default"),

        /**
         * Action button used for "cancel" events.
         */
        ACTION_CANCEL("action cancel"),

        /**
         * Action button used for "delete" events.
         */
        ACTION_DELETE("action delete"),

        /**
         * Action button used for "important" events.
         */
        ACTION_IMPORTANT("action important"),

        /**
         * A "green" action button.
         */
        ACTION_GREEN("action green");

        private ButtonType(String value) {}

        @SGWTInternal
        public final String _getClassNames() {
            switch (this) {
                case BORDERED:
                    return _CSS.borderedButtonClass();
                case BORDERED_IMPORTANT:
                    return _CSS.borderedButtonClass() + " " + _CSS.importantButtonClass();
                case BORDERED_WARNING:
                    return _CSS.borderedButtonClass() + " " + _CSS.warningButtonClass();
                case PLAIN:
                    return _CSS.plainIconButtonClass();
                case ROUNDED_RECTANGLE:
                    return _CSS.roundedRectangleButtonClass();
                case ROUNDED_RECTANGLE_IMPORTANT:
                    return _CSS.roundedRectangleButtonClass() + " " + _CSS.importantButtonClass();
                case ACTION_DEFAULT:
                    return _CSS.actionButtonClass() + " " + _CSS.defaultButtonClass();
                case ACTION_CANCEL:
                    return _CSS.actionButtonClass() + " " + _CSS.cancelButtonClass();
                case ACTION_DELETE:
                    return _CSS.actionButtonClass() + " " + _CSS.deleteButtonClass();
                case ACTION_IMPORTANT:
                    return _CSS.actionButtonClass() + " " + _CSS.importantButtonClass();
                case ACTION_GREEN:
                    return _CSS.actionButtonClass() + " " + _CSS.greenButtonClass();
            }
            assert false : "ButtonType._getClassNames() needs to handle ButtonType." + this;
            throw new RuntimeException();
        }
    }

    private transient DivElement contentElem;
    private transient Image iconImage;
    private transient HandlerRegistration iconLoadRegistration;
    private transient SpanElement labelElem;

    private ImageResource icon;
    private boolean masked;

    /**
     * The alignment of the icon with respect to its title
     */
    private IconAlign iconAlign = IconAlign.LEFT;

    /**
     * The color of the icon can be set programmatically. The result will be the icon being displayed
     * in solid "iconColor"
     */
    private String iconColor;

    /**
     * Buttons support being tinted dynamically.
     */
    protected String tintColor;

    private boolean stretch = false;

    /**
     * The default constructor that sets up the basic button structure. A button should have a
     * {@link #setTitle(String) title} and / or an {@link #setIcon(com.google.gwt.user.client.ui.Image, boolean) icon}
     * to have a meaningful appearance.
     */
    public BaseButton() {
        createElements();
        super.setShowDown(true);
    }

    /**
     * BaseButton constructor.
     *
     * @param title the title label of the button
     */
    public BaseButton(String title) {
        this();
        setTitle(title);
    }

    public BaseButton(BaseButton button) {
        createElements();
        super.setShowDown(true);

        setTitle(button.getTitle(), false);
        setIcon(button.icon, button.masked, false);
        setIconAlign(button.getIconAlign());
        setIconColor(button.getIconColor());
        setTintColor(button.getTintColor());
        setStretch(button.stretch);
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

    private void createElements() {
        DivElement buttonElem = Document.get().createDivElement();
        buttonElem.addClassName(_CSS.buttonClass());
        buttonElem.addClassName(_CSS.iconOnlyButtonClass());
        contentElem = Document.get().createDivElement();
        contentElem.addClassName(_CSS.buttonContentClass());
        labelElem = Document.get().createSpanElement();
        labelElem.addClassName(_CSS.buttonLabelClass());
        contentElem.appendChild(labelElem);
        buttonElem.appendChild(contentElem);
        setElement(buttonElem);
    }

    @Override
    public void destroy() {
        if (iconLoadRegistration != null) {
            iconLoadRegistration.removeHandler();
        }
    }

    /**
     * Sets the title of the button.
     * 
     * @deprecated Use {@link #setTitle(String)} instead.
     */
    @Override
    @Deprecated
    public void setContents(String html) {
        // Note: The reason why this method does not set the innerHTML of `contentElem` is
        // that it is impossible to get the "actual" (unclipped) width of the contents in
        // _getContentWidth().  This is because `contentElem` is display:block.
        // display:inline-block would work, but then text-overflow:ellipsis on the label
        // element no longer works.  Also, overflow:hidden would have to be added to
        // getElement(), but this can change the appearance of the button considerably
        // (for example, NavigationButton places the arrows of "back" and "next" buttons
        // outside of the border box and these are clipped by overflow:hidden on getElement()).
        setTitle(html);
    }

    @SGWTInternal
    public final float _getContentWidth() {
        float ret = labelElem.getOffsetWidth();
        if (icon != null && iconImage.isVisible()) {
            ret += ElementUtil.getOuterWidth(iconImage.getElement(), true);
        }
        return ret;
    }

    @SGWTInternal
    public final SpanElement _getLabelElement() {
        return labelElem;
    }

    @Override
    public String _getStateName() {
        // FIXME Consolidate the CSS selectors in buttons.gwt.css.
        return _CSS.buttonClass();
    }

    /**
     * Return the title label.
     *
     * @return the title label
     */
    public final String getTitle() {
        return labelElem.getInnerHTML();
    }

    /**
     * Sets the title label of the button.
     *
     * @param title the button title
     */
    public void setTitle(String title) {
        setTitle(title, true);
    }

    private void setTitle(String title, boolean fireContentChangedEvent) {
        labelElem.setInnerHTML(title);
        if (title == null || title.isEmpty()) {
            getElement().addClassName(_CSS.iconOnlyButtonClass());
        } else {
            getElement().removeClassName(_CSS.iconOnlyButtonClass());
        }
        if (fireContentChangedEvent) _fireContentChangedEvent();
    }

    /**
     * Return the button icon, or null if not set.
     *
     * @return the button icon
     */
    public final ImageResource getIcon() {
        return icon;
    }

    /**
     * Set the button icon from an {@link com.google.gwt.resources.client.ImageResource}.
     * <p>
     * <b>Note:</b> When using masking, the <code>ImageResource</code> must be a separate resource
     * at runtime. If using GWT {@link com.google.gwt.resources.client.ClientBundle}s, it is
     * sufficient to set the <code>preventInlining</code> image option to <code>true</code>.
     *
     * @param icon         the icon image resource.
     * @param mask         true to mask the button. Masking is typically applied to black-only icons causing its final
     *                     appearance to be white
     */
    public void setIcon(ImageResource icon, boolean mask) {
        setIcon(icon, mask, true);
    }

    /**
     * Set the button icon from an {@link com.google.gwt.resources.client.ImageResource}.
     * <p>
     * <b>Note:</b> When using masking, the <code>ImageResource</code> must be a separate resource
     * at runtime. If using GWT {@link com.google.gwt.resources.client.ClientBundle}s, it is
     * sufficient to set the <code>preventInlining</code> image option to <code>true</code>.
     *
     * @param icon         the icon image resource.
     * @param mask         true to mask the button. Masking is typically applied to black-only icons causing its final
     *                     appearance to be white
     * @param iconAlign    the alignment of the icon
     */
    public void setIcon(ImageResource icon, boolean mask, IconAlign iconAlign) {
        setIcon(icon, mask, true);
        setIconAlign(iconAlign);
    }

    private void setIcon(final ImageResource icon, boolean mask, boolean fireContentChangedEvent) {
        assert !mask || icon == null || (icon.getTop() == 0 && icon.getLeft() == 0)
                : "When using masking, the ImageResource must be a separate resource at runtime.";
        if (iconLoadRegistration != null) {
            iconLoadRegistration.removeHandler();
        }
        if (iconImage != null) {
            remove(iconImage);
            iconImage = null;
        }
        this.icon = icon;
        this.masked = mask;

        if (icon != null) {
            iconImage = new Image(icon);
            getElement().addClassName(_CSS.buttonHasIconClass());
            if (icon.getWidth() > 0) {
                doSetIcon(false);
            } else {
                iconImage.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
                iconLoadRegistration = iconImage.addLoadHandler(new com.google.gwt.event.dom.client.LoadHandler() {
                    @Override
                    public void onLoad(com.google.gwt.event.dom.client.LoadEvent event) {
                        iconLoadRegistration.removeHandler();
                        doSetIcon(true);
                    }
                });
            }
            if (iconAlign != IconAlign.RIGHT) {
                insert(iconImage, contentElem.<com.google.gwt.user.client.Element>cast(), 0, true);
            } else {
                add(iconImage, contentElem.<com.google.gwt.user.client.Element>cast());
            }
        } else {
            getElement().removeClassName(_CSS.buttonHasIconClass());
        }
        if (fireContentChangedEvent) _fireContentChangedEvent();
    }

    private void doSetIcon(boolean fireContentChangedEvent) {
        assert icon != null && iconImage != null;
        final Element img = iconImage.getElement();
        final Style imgStyle = img.getStyle();
        if (masked) {
            iconImage.setResource(IconResources.INSTANCE.blank());
            if ("IMG".equals(img.getTagName())) {
                final ImageElement iconImageImgElem = ImageElement.as(img);
                iconImageImgElem.removeAttribute("width");
                iconImageImgElem.removeAttribute("height");
            }
            imgStyle.clearWidth();
            imgStyle.clearHeight();
            img.setClassName(_CSS.maskedButtonIconClass());
            imgStyle.clearBackgroundColor();
            imgStyle.clearBackgroundImage();
            // Note: Mobile WebKit automatically scales down the mask image to fit the element
            // to which the mask image is applied.
            imgStyle.setProperty("WebkitMaskBoxImage", "url(" + icon.getSafeUri().asString() + ")");
        } else {
            if ("IMG".equals(img.getTagName())) {
                final ImageElement iconImageImgElem = ImageElement.as(img);
                iconImageImgElem.removeAttribute("width");
                iconImageImgElem.removeAttribute("height");
            }
            imgStyle.clearWidth();
            imgStyle.clearHeight();
            imgStyle.clearProperty("MozBackgroundSize");
            imgStyle.clearProperty("OBackgroundSize");
            imgStyle.clearProperty("WebkitBackgroundSize");
            imgStyle.clearProperty("backgroundSize");
            img.removeClassName(_CSS.maskedButtonIconClass());
        }

        setIconAlign(iconAlign);
        if (iconColor != null) {
            setIconColor(iconColor);
        }

        imgStyle.clearVisibility();
        if (fireContentChangedEvent) _fireContentChangedEvent();
    }

    /**
     * Return the icon alignment.
     *
     * @return the icon alignment. Defaults to left
     */
    public IconAlign getIconAlign() {
        return iconAlign;
    }

    /**
     * Set the icon alignment. Default is left.
     *
     * @param iconAlign the icon alignment
     */
    public void setIconAlign(IconAlign iconAlign) {
        if (this.iconAlign != iconAlign) {
            if (this.iconAlign != null && this.iconAlign != IconAlign.LEFT) {
                getElement().removeClassName(iconAlign._getClassName());
            }
            if (icon != null) {
                if (this.iconAlign == IconAlign.RIGHT) {
                    remove(iconImage);
                    insert(iconImage, contentElem.<com.google.gwt.user.client.Element>cast(), 0, true);
                } else if (iconAlign == IconAlign.RIGHT) {
                    remove(iconImage);
                    add(iconImage, contentElem.<com.google.gwt.user.client.Element>cast());
                }
            }
            this.iconAlign = iconAlign;
            if (icon != null && iconAlign != null && iconAlign != IconAlign.LEFT) {
                getElement().addClassName(iconAlign._getClassName());
            }
        }
    }

    /**
     * Return the tint color. Default is null and the theme's default color is used.
     *
     * @return the tint color
     */
    public String getTintColor() {
        return tintColor;
    }

    /**
     * Set the button tintColor. Can pass in the color name, the hex value, or the rgb / rgba value as a String.
     * <br><br>
     * <b>Note:</b> Passing a rgba string with a value for opacity allows buttons to have an opacity / translucency.
     *
     * @param tintColor the tint color
     */
    public void setTintColor(String tintColor) {
        this.tintColor = tintColor;
        if (tintColor != null) {
            getElement().addClassName(_CSS.customTintedButtonClass());
            getElement().getStyle().setBackgroundColor(tintColor);
        } else {
            getElement().removeClassName(_CSS.customTintedButtonClass());
            getElement().getStyle().clearBackgroundColor();
        }
    }

    /**
     * Return the icon color.
     *
     * @return the icon color. Defaults to null
     */
    public final String getIconColor() {
        return iconColor;
    }

    /**
     * Set the icon color. This overrides the default color of the icon an applies a solid color to the icon
     * based on the icon color provided. An example use of this is to set the icon color of a "star" icon to yellow
     * to indicate it has been marked.
     *
     * @param iconColor the icon color. Defaults to null
     */
    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
        if (this.icon != null) {
            final Element imgElement = iconImage.getElement();
            imgElement.getStyle().setBackgroundColor(iconColor);
        }
    }

      
    /**
     * Should the button stretch in width?
     *
     * @return true for stretch, false for auto-width based on contents
     */
    public final boolean isStretch() {
        return stretch;
    }

    /**
     * Should the button stretch in width?
     *
     * @param stretch true for stretch, false for auto-width based on contents
     */
    public void setStretch(boolean stretch) {
        this.stretch = stretch;
        if (stretch) {
            getElement().addClassName(_CSS.stretchButtonClass());
        } else {
            getElement().removeClassName(_CSS.stretchButtonClass());
        }
    }

    @Override
    protected void stateChanged() {
        super.stateChanged();
        if (getState() == State.STATE_DOWN || Canvas._booleanValue(isSelected(), false)) {
            getElement().addClassName(_CSS.touchedButtonClass());
        } else {
            getElement().removeClassName(_CSS.touchedButtonClass());
        }
    }
}
