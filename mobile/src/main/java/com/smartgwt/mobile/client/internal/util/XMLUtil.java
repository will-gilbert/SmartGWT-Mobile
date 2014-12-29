package com.smartgwt.mobile.client.internal.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.xml.client.Attr;
import com.google.gwt.xml.client.CDATASection;
import com.google.gwt.xml.client.CharacterData;
import com.google.gwt.xml.client.Comment;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.Text;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public final class XMLUtil {

    public static String escapeCDATA(String cdata) {
        if (cdata == null) return "";
        return cdata.replace("]]>", "]]]><![CDATA[]>");
    }

    public static String escapeElementContent(String xmlContent) {
        return escapeElementContent(xmlContent, true);
    }

    public static String escapeElementContent(String xmlContent, boolean escapeEntities) {
        if (xmlContent == null) return "";
        String ret = xmlContent;
        if (escapeEntities) ret = ret.replace("&", "&amp;");
        return ret.replace("<", "&lt;"); // There's no need to escape '>'.
    }

    public static String escapeAttribute(String attribute) {
        return escapeAttribute(attribute, true);
    }

    public static String escapeAttribute(String attribute, boolean escapeEntities) {
        if (attribute == null) return "";
        String ret = attribute;
        if (escapeEntities) ret = ret.replace("&", "&amp;");
        return ret.replace("\"", "&#34;").replace("'", "&#39;");
    }

    /**
     * Implements an accessor for the <a href="http://www.w3.org/TR/2004/REC-DOM-Level-3-Core-20040407/core.html#Node3-textContent"><code>textContent</code> DOM Level 3 attribute</a>
     * of the given element.
     * 
     * @param el the target node.
     * @return the <code>textContent</code> attribute value of <code>n</code>.
     */
    // Related: http://code.google.com/p/google-web-toolkit/issues/detail?id=719
    public static String getTextContent(Element el) {
        if (el == null) return null;
        StringBuilder sb = new StringBuilder();
        appendTextContent(sb, el);
        return sb.toString();
    }

    public static void appendTextContent(StringBuilder out, Element el) {
        if (el == null) return;
        final NodeList children = el.getChildNodes();
        final int children_length = children.getLength();
        for (int childIndex = 0; childIndex < children_length; ++childIndex) {
            final Node child = children.item(childIndex);
            if (child instanceof Text) out.append(child.getNodeValue());
            else if (child instanceof Element) appendTextContent(out, (Element)child);
        }
    }

    /**
     * Removes all children (if any) of <code>n</code>.
     * 
     * @param n the node to modify.
     */
    public static void removeAllChildren(Node n) {
        setChildren(n, null);
    }

    /**
     * Removes all children (if any) of <code>n</code> and then appends <code>newSoleChild</code>.
     * 
     * @param n the node to modify.
     * @param newSoleChild the node that will be the sole child node of <code>n</code>.
     */
    public static void setChildren(Node n, Node newSoleChild) {
        if (n == null) return;
        for (Node child = n.getFirstChild(); child != null; child = n.getFirstChild()) {
            n.removeChild(child);
        }
        if (newSoleChild != null) {
            assert n != null;
            n.appendChild(newSoleChild);
        }
    }

    /**
     * Utility function for controlled iteration of nested children of <code>el</code>.
     * 
     * <p>This function approximates the utility of E4X' <code>for each (var child in el[.nestedTagName]*)</code>
     * syntax. For example, to convert <code>for each (var child in el.subelement.subsubelement.subsubsubelement)</code>,
     * one can use <code>forEachElementIn(el, visitor, "subelement", "subsubelement", "subsubsubelement");</code>.
     */
    public static <R> R forEachElementIn(Element el, final Visitor<R, Element> visitor, String... nestingTagNames) {
        if (el == null || visitor == null || nestingTagNames == null || nestingTagNames.length == 0) return null;

        R result = null;
        String[] nestingTagNamesSlice = null;

        if (nestingTagNames[0] != null) {
            final NodeList children = el.getChildNodes();
            final int children_length = children.getLength();
            for (int childIndex = 0; childIndex < children_length; ++childIndex) {
                final Node child = children.item(childIndex);
                if (! (child instanceof Element)) continue;
                final Element childEl = (Element)child;
                if (nestingTagNames[0].equals(childEl.getTagName())) {
                    if (nestingTagNames.length == 1) result = visitor.visit(childEl);
                    else {
                        if (nestingTagNamesSlice == null) {
                            nestingTagNamesSlice = new String[nestingTagNames.length - 1];
                            System.arraycopy(nestingTagNames, 1, nestingTagNamesSlice, 0, nestingTagNamesSlice.length);
                        }
                        result = forEachElementIn(childEl, visitor, nestingTagNamesSlice);
                    }
                    if (result != null) return result;
                }
            }
        }
        return null;
    }

    /**
     * Makes a static list of child elements of <code>el</code> having tag name <code>tagName</code>.
     * 
     * @param el the element.
     * @param tagName child element tag name.
     * @return an array of child elements of <code>el</code> having the given tag name.
     */
    public static Element[] getElementsByTagNameStatic(Element el, String tagName) {
        if (el == null) return null;

        List<Element> childEls = new ArrayList<Element>();
        if (tagName != null) {
            NodeList children = el.getChildNodes();
            final int children_length = children.getLength();
            for (int childIndex = 0; childIndex < children_length; ++childIndex) {
                final Node child = children.item(childIndex);
                if (! (child instanceof Element)) continue;
                final Element childEl = (Element)child;
                if (tagName.equals(childEl.getTagName())) {
                    childEls.add(childEl);
                }
            }
        }

        return childEls.toArray(new Element[childEls.size()]);
    }

    /**
     * Converts <code>el</code> to the text of an XML document representing it.
     * 
     * <p>This is for debugging purposes, and may not preserve all element features.
     * Namespaces, for example, are not supported.
     * 
     * @param el the element to generate an XML text representation for.
     * @return XML text.
     */
    public static String toXML(Element el) {
        StringBuilder sb = new StringBuilder();
        try {
            appendXML(sb, el);
        } catch (IOException ex) { assert false; }
        String ret = sb.toString();
        if (!ret.isEmpty()) ret = ret.substring(1);
        return ret;
    }

    public static <A extends Appendable> A appendXML(A out, Element el) throws IOException {
        return appendXML(out, el, "");
    }

    public static <A extends Appendable> A appendXML(A out, Element el, final String indent) throws IOException {
        if (el == null) return out;
        out.append('\n').append(indent).append('<').append(el.getTagName());
        if (el.hasAttributes()) {
            final NamedNodeMap attrs = el.getAttributes();
            final int attrs_length = attrs.getLength();
            for (int i = 0; i < attrs_length; ++i) {
                final Attr attr = (Attr)attrs.item(i);
                if (attr.getSpecified()) {
                    out.append(" ").append(attr.getName()).append("=\"").append(escapeAttribute(attr.getValue())).append('"');
                }
            }
        }
        if (!el.hasChildNodes()) {
            out.append("/>");
            return out;
        }
        out.append(">");
        final NodeList children = el.getChildNodes();
        final int children_length = children.getLength();
        final String innerIndent = "  " + indent;
        for (int childIndex = 0; childIndex < children_length; ++childIndex) {
            final Node child = children.item(childIndex);
            if (child instanceof Comment) out.append("<!--").append(child.getNodeValue()).append("-->");
            else if (child instanceof CDATASection) out.append("<![CDATA[").append(escapeCDATA(child.getNodeValue())).append("]]>");
            else if (child instanceof CharacterData) out.append(escapeElementContent(child.getNodeValue()));
            else if (child instanceof Element) appendXML(out, (Element)child, innerIndent).append('\n').append(indent);
        }
        out.append("</").append(el.getTagName()).append(">");
        return out;
    }

    private XMLUtil() {}
}
