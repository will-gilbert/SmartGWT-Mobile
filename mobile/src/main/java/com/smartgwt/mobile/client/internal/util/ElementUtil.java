package com.smartgwt.mobile.client.internal.util;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public final class ElementUtil {

    public static native Element getActiveElement() /*-{
        return $doc.activeElement;
    }-*/;

    public static boolean hasClassName(Element element, String className) {
        if (className == null || className.isEmpty()) return false;
        final String str = element.getClassName();
        int pos = str.indexOf(className);
        while (pos != -1) {
            if (pos == 0 || str.charAt(pos - 1) == ' ') {
                pos += className.length();
                if (pos == str.length() || str.charAt(pos) == ' ') {
                    return true;
                }
            } else pos += className.length();
            if (pos >= str.length()) break;
            pos = str.indexOf(className, pos);
        }
        return false;
    }

    public static Element getChildElementHavingClass(Element parentElement, String className) {
        if (parentElement != null) {
            final NodeList<Node> childNodes = parentElement.getChildNodes();
            final int childNodes_length = childNodes.getLength();
            for (int i = 0; i < childNodes_length; ++i) {
                final Node n = childNodes.getItem(i);
                if (n.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                final Element childElement = Element.as(n);
                if (hasClassName(childElement, className)) {
                    return childElement;
                }
            }
        }
        return null;
    }

    public static void removeAllChildren(Node n) {
        if (n == null) return;
        for (Node child = n.getFirstChild(); child != null; child = n.getFirstChild()) {
            n.removeChild(child);
        }
    }

    /**
     * Calculates the width of the margin box if the width of the content box were 0.
     * 
     * @param element the element.
     * @return the minimum width of the margin box in pixels.
     */
    public static native float getMinMarginBoxWidth(Element element) /*-{
        var computedStyle = $wnd.getComputedStyle(element, null);

        var widthValue = computedStyle.getPropertyCSSValue("width");
        var width;
        if (widthValue == null) {
            width = 0;
        } else {
            if (widthValue.primitiveType == $wnd.CSSPrimitiveValue.CSS_IDENT || widthValue.cssText == "auto") {
                // When `element` is a child (or child of a child, etc.) of a parent that is
                // display:none, the width might not have been calculated.
                return;
            }
            width = widthValue == null ? 0 : widthValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
        }

        var ret = element.offsetWidth - width;

        var boxSizing;
        if ("webkitBoxSizing" in computedStyle) boxSizing = computedStyle.webkitBoxSizing;
        else if ("mozBoxSizing" in computedStyle) boxSizing = computedStyle.mozBoxSizing;
        else if ("boxSizing" in computedStyle) boxSizing = computedStyle.boxSizing;
        else boxSizing = "content-box";

        if (boxSizing == "padding-box" || boxSizing == "border-box") {
            // Add back the width of right and left padding.
            var paddingValue = computedStyle.getPropertyCSSValue("padding-right");
            if (paddingValue != null) ret += paddingValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
            paddingValue = computedStyle.getPropertyCSSValue("padding-left");
            if (paddingValue != null) ret += paddingValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
        }
        if (boxSizing == "border-box") {
            // Add back the widths of the right and left borders.
            var borderWidthValue = computedStyle.getPropertyCSSValue("border-right-width");
            if (borderWidthValue != null) ret += borderWidthValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
            borderWidthValue = computedStyle.getPropertyCSSValue("border-left-width");
            if (borderWidthValue != null) ret += borderWidthValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
        }

        // Add the widths of the right and left margins.
        var marginValue = computedStyle.getPropertyCSSValue("margin-right");
        if (marginValue != null) ret += marginValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
        marginValue = computedStyle.getPropertyCSSValue("margin-left");
        if (marginValue != null) ret += marginValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
        return ret;
    }-*/;

    /**
     * Sets the width of the border box of <code>element</code>.
     * 
     * @param element the element.
     * @param width the new border box width in pixels.
     */
    public static native void setBorderBoxWidth(Element element, float width) /*-{
        var computedStyle = $wnd.getComputedStyle(element, null);

        var boxSizing;
        if ("webkitBoxSizing" in computedStyle) boxSizing = computedStyle.webkitBoxSizing;
        else if ("mozBoxSizing" in computedStyle) boxSizing = computedStyle.mozBoxSizing;
        else if ("boxSizing" in computedStyle) boxSizing = computedStyle.boxSizing;
        else boxSizing = "content-box";

        if (boxSizing != "padding-box" && boxSizing != "border-box") {
            // Subtract the width of right and left padding.
            var paddingValue = computedStyle.getPropertyCSSValue("padding-right");
            if (paddingValue != null) width -= paddingValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
            paddingValue = computedStyle.getPropertyCSSValue("padding-left");
            if (paddingValue != null) width -= paddingValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
        }
        if (boxSizing != "border-box") {
            // Subtract the widths of the right and left borders.
            var borderWidthValue = computedStyle.getPropertyCSSValue("border-right-width");
            if (borderWidthValue != null) width -= borderWidthValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
            borderWidthValue = computedStyle.getPropertyCSSValue("border-left-width");
            if (borderWidthValue != null) width -= borderWidthValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
        }

        element.style.width = width + "px";
    }-*/;

    public static native float getHMarginWidth(Element element) /*-{
        var ret = 0;
        var computedStyle = $wnd.getComputedStyle(element, null);
        var marginValue = computedStyle.getPropertyCSSValue("margin-right");
        if (marginValue != null) ret += marginValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
        marginValue = computedStyle.getPropertyCSSValue("margin-left");
        if (marginValue != null) ret += marginValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
        return ret;
    }-*/;

    /**
     * Calculates the width of the element including border, padding, and optionally margin.
     * 
     * @param element the element.
     * @param includeMargin whether to include the left and right margin widths in the calculation.
     * @return the outer width in pixels.
     * @see <a href="http://api.jquery.com/outerWidth/"><code>.outerWidth()</code> - jQuery API</a>
     */
    public static float getOuterWidth(Element element, boolean includeMargin) {
        return includeMargin ? element.getOffsetWidth() + getHMarginWidth(element) : element.getOffsetWidth();
    }

    public static native float getVMarginWidth(Element element) /*-{
        var ret = 0;
        var computedStyle = $wnd.getComputedStyle(element, null);
        var marginValue = computedStyle.getPropertyCSSValue("margin-top");
        if (marginValue != null) ret += marginValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
        marginValue = computedStyle.getPropertyCSSValue("margin-bottom");
        if (marginValue != null) ret += marginValue.getFloatValue($wnd.CSSPrimitiveValue.CSS_PX);
        return ret;
    }-*/;

    public static float getOuterHeight(Element element, boolean includeMargin) {
        return includeMargin ? element.getOffsetHeight() + getVMarginWidth(element) : element.getOffsetHeight();
    }

    private ElementUtil() {}
}
